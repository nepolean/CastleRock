package com.real.proj.forum.service;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.real.proj.controller.exception.DBException;
import com.real.proj.controller.exception.DBException.Operation;
import com.real.proj.controller.exception.EntityNotFoundException;
import com.real.proj.controller.exception.SecurityPermissionException;
import com.real.proj.forum.model.Forum;
import com.real.proj.forum.model.Message;
import com.real.proj.forum.model.MessageType;
import com.real.proj.forum.model.SubscriptionRequest;
import com.real.proj.message.SimpleMessage;
import com.real.proj.user.model.User;
import com.real.proj.user.service.UserService;

@Service
public class ForumService implements IForumService {

  private static final Logger logger = LogManager.getLogger(ForumService.class);
  @Autowired
  private ForumRepository forumRepository;
  private UserService userService;

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Forum addUserToForum(String forumId, String loggedInUser, String targetUser) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("addSubscriber " + targetUser + ", forum " + forumId);
    }
    Forum f = this.getForum(forumId);
    assertForumNotClosed(f);
    assertOwnership(f, loggedInUser);
    User subscriber = this.getUser(targetUser);
    try {
      f.addSubscriber(subscriber);
      f = (Forum) this.forumRepository.save(f);
    } catch (Exception ex) {
      if (logger.isErrorEnabled())
        logger.error("Error while adding subscriber " + subscriber.getEmail(), ex);
      throw new DBException("Subscriber", DBException.Operation.adding);
    }
    try {
      this.userService.subscribe(subscriber.getEmail(), f.getId());
    } catch (Exception ex) {
      if (logger.isErrorEnabled())
        logger.error("Error while adding subscription to the user");
      // TODO shouldn't we add the subscription to the user subscriber list?
    }
    return f;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.real.proj.forum.service.INotificationService#createForum(java.lang.
   * String, java.lang.String)
   */
  @Override
  public Forum createForum(String subject, String userName) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("Create new forum with subject " + subject);
    }

    if (userName != null && subject != null) {
      User user = this.getUser(userName);
      Forum f = new Forum(subject);
      f.setOwner(user);
      f.addSubscriber(user);

      try {
        f = (Forum) this.forumRepository.save(f);
      } catch (Exception ex) {
        if (logger.isErrorEnabled())
          logger.error("Error while creating the forum", ex);
        throw new DBException("Forum", Operation.creating);
      }
      try {
        user = this.userService.subscribe(userName, f.getId());
        if (logger.isInfoEnabled())
          logger.info("added subscription to the user" + user);
      } catch (Exception ex) {
        ex.printStackTrace();
        // TODO handle this case
      }
      return f;
    } else {
      if (logger.isErrorEnabled())
        logger.error("Arguments are empty " + subject + " , " + userName);
      throw new IllegalArgumentException("Arguments are empty");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.real.proj.forum.service.IForumService#subscribeMe(java.lang.String,
   * java.lang.String)
   */
  public SimpleMessage subscribeMe(String forumId, String loggedInUser) throws Exception {
    Forum f = this.getForum(forumId);
    User user = this.getUser(loggedInUser);
    this.assertForumNotClosed(f);
    String message = "";
    if (f.isAutoSubscriptionEnabled()) {
      f.addSubscriber(user);
      message = "Your subscription has been accepted";
    } else {
      SubscriptionRequest request = new SubscriptionRequest(user);
      f.addSubscriptionRequest(request);
      message = "A subscription request has been created. You will be notified once apporved";
    }
    try {
      f = this.forumRepository.save(f);
    } catch (Exception ex) {
      if (logger.isErrorEnabled())
        logger.error("Error while updating forum", ex);
      throw new DBException("Subscriber", DBException.Operation.creating);
    }
    try {
      NotificationHelper.notifyUser(f.getOwner());
    } catch (Exception ex) {
      if (logger.isErrorEnabled())
        logger.error("Error while notifying the user ", ex);
    }

    return new SimpleMessage(message);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.real.proj.forum.service.INotificationService#getForum(java.lang.String)
   */
  @Override
  public Forum getRequestedForum(String forumId, String requestedBy) throws Exception {
    Forum f = this.getForum(forumId);
    this.assertAuthorized(requestedBy, f);
    return f;
  }

  private Forum getForum(String forumId) throws Exception {
    Forum f;
    try {
      f = (Forum) this.forumRepository.findOne(forumId);
    } catch (Exception ex) {
      if (logger.isErrorEnabled()) {
        logger.debug("Error getting forum " + forumId, ex);
      }
      throw new DBException("Forum", Operation.retrieving);
    }

    if (f == null) {
      if (logger.isErrorEnabled())
        logger.error("Forum with id " + forumId + " is not found");
      throw new EntityNotFoundException(forumId, "NOT_FOUND", "Forum");
    }

    return f;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.real.proj.forum.service.INotificationService#getForums(java.lang.
   * String)
   */
  @Override
  public List<Forum> getForums(String userName) {
    List<Forum> myForums = this.forumRepository.findBySubscribers_Email(userName);
    return myForums;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.real.proj.forum.service.INotificationService#getMessagesForForum(java.
   * lang.String, int, int)
   */
  @Override
  public Forum getMessagesForForum(String forumId, int start, int count) throws Exception {
    Forum f = (Forum) this.forumRepository.findOne(forumId);
    // List messages = MessageService.getMessages(f, start, count);
    f.getMessages();
    return f;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.real.proj.forum.service.INotificationService#postMessage(java.lang.
   * String, java.lang.String, java.lang.String)
   */
  @Override
  public Forum postMessage(String message, String forumId, String userId) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("forumId : " + forumId + " , Messasge " + message);
    }

    if (forumId != null && message != null) {
      Forum f = this.getForum(forumId);
      assertForumNotClosed(f);
      assertAuthorized(userId, f);
      User author = userService.getUser(userId);
      Message post = new Message(MessageType.TEXT);
      post.setMessage(message);
      post.setAuthor(author);
      try {
        f.postMessage(post);
        f = (Forum) this.forumRepository.save(f);
        return f;
      } catch (Exception ex) {
        logger.error("Error while posting the message", ex);
        throw new DBException("Post", Operation.creating);
      }
    } else {
      throw new IllegalArgumentException("Invalid arguments");
    }
  }

  public void setForumRepository(ForumRepository forumRepository) {
    this.forumRepository = forumRepository;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.real.proj.forum.service.INotificationService#subscribeUser(java.lang.
   * String, java.lang.String)
   * 
   * @Override public Forum subscribeUser(String forumId, String loggedInUser)
   * throws Exception { if (logger.isDebugEnabled()) {
   * logger.debug("subscribeUser ( " + forumId + " , " + loggedInUser + " )"); }
   * 
   * Forum f = this.getForum(forumId); assertForumNotClosed(f); User user =
   * this.userService.getUser(loggedInUser); if
   * (!user.getSubscriptions().contains(forumId)) { f.addSubscriber(user); try {
   * this.forumRepository.save(f); } catch (Exception arg5) { if
   * (logger.isErrorEnabled()) { logger.error("Error while saving the user",
   * arg5); } throw new DBException("Error while saving the user"); }
   * 
   * user.addSubscription(f.getId());
   * 
   * try { this.userService.save(user); } catch (Exception ex) { if
   * (logger.isErrorEnabled()) { logger.error("Error while saving the user",
   * ex); } } }
   * 
   * return f; }
   */

  @Override
  public void closeForum(String forumId, String loggedInUser) throws Exception {
    Forum f = this.getForum(forumId);
    this.assertOwnership(f, loggedInUser);
    f.setClosed(true);
    try {
      forumRepository.save(f);
    } catch (Exception ex) {
      if (logger.isErrorEnabled()) {
        logger.error("Error while closing the forum with id " + forumId, ex);
      }
      throw new DBException("Forum", DBException.Operation.updating);
    }

  }

  @Override
  public SimpleMessage unsubscribeMe(String forumId, String userId) throws Exception {
    Forum f = getForum(forumId);
    if (f.getOwner().getEmail().equals(userId))
      throw new Exception("Owner cannot unsubsribe himself");
    f.getSubscribers().remove(userId);
    forumRepository.save(f);
    return new SimpleMessage("You are unsubscribed from the forum");
  }

  @Override
  public SimpleMessage removeUserFromForum(String forumId, String subscriberId, String ownerId) throws Exception {
    Forum f = this.getForum(forumId);
    User owner = this.getUser(ownerId);
    User subscriber = this.getUser(subscriberId);
    if (!f.getOwner().getEmail().equals(ownerId)) {
      throw new SecurityPermissionException();
    }
    f.getSubscribers().remove(subscriber);
    return new SimpleMessage("The user is removed from the forum");
  }

  private User getUser(String userName) throws Exception {
    User user = null;
    try {
      user = this.userService.getUser(userName);
    } catch (Exception ex) {
      if (logger.isErrorEnabled())
        logger.error("Error while retrieving user " + userName, ex);
      throw new DBException("User", Operation.retrieving);
    }
    if (user == null) {
      if (logger.isErrorEnabled()) {
        logger.error("User not found in db " + userName);
      }
      throw new EntityNotFoundException(userName, "Entity NotFound", "User");
    }
    return user;
  }

  private void assertForumNotClosed(Forum f) {
    if (f.isClosed()) {
      if (logger.isWarnEnabled())
        logger.warn("trying to post a message to closed forum ");
      throw new IllegalStateException("User cannot post a message to a closed forum");
    }
  }

  private void assertOwnership(Forum f, String loggedInUser) throws Exception {
    if (!f.getOwner().getEmail().equals(loggedInUser)) {
      if (logger.isErrorEnabled())
        logger.error("User, " + loggedInUser + " is not authorized");
      throw new SecurityPermissionException();
    }
  }

  private void assertAuthorized(String loggedInUser, Forum f) throws SecurityPermissionException {
    if (!this.userBelongsToForum(f, loggedInUser)) {
      if (logger.isErrorEnabled())
        logger.error("User does not have permission to post to this forum " + loggedInUser);
      throw new SecurityPermissionException();

    }
  }

  private boolean userBelongsToForum(Forum f, String user) {
    Iterator<User> subscribers = f.getSubscribers().iterator();
    while (subscribers.hasNext()) {
      User usr = (User) subscribers.next();
      if (usr.getEmail().equals(user)) {
        return true;
      }
    }

    return false;
  }

}