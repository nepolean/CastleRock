package com.real.proj.forum.service;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.real.proj.controller.exception.DBException;
import com.real.proj.controller.exception.EntityNotFoundException;
import com.real.proj.forum.model.Forum;
import com.real.proj.forum.model.Message;
import com.real.proj.forum.model.MessageType;
import com.real.proj.forum.model.User;

@Service
public class ForumService {
  private static final Logger logger = LogManager.getLogger(ForumService.class);
  @Autowired
  private ForumRepository forumRepository;
  @Autowired
  private UserServiceImpl userService;

  public void setForumRepository(ForumRepository forumRepository) {
    this.forumRepository = forumRepository;
  }

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
        throw new DBException();
      }

      try {
        user = this.userService.subscribe(userName, f.getId());
        if (logger.isInfoEnabled())
          logger.info("added subscription to the user" + user);
        logger.info(user.getSubscriptions());
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

  private User getUser(String userName) throws Exception {
    User user = this.userService.getUser(userName);
    if (user == null) {
      if (logger.isErrorEnabled()) {
        logger.error("User not found in db " + userName);
      }
      throw new EntityNotFoundException(userName, "Entity NotFound", "User");
    }
    return user;
  }

  public Forum addSubscriber(String forumId, User subscriber) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("addSubscriber " + subscriber.getEmail() + ", forum " + forumId);
    }

    Forum f = this.getForum(forumId);

    try {
      f.addSubscriber(subscriber);
      f = (Forum) this.forumRepository.save(f);
    } catch (Exception ex) {
      if (logger.isErrorEnabled())
        logger.error("Error while adding subscriber", ex);
      throw new DBException("Error while adding subscriber");
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

  public Forum postMessage(String message, String forumId, String userId) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("forumId : " + forumId + " , Messasge " + message);
    }

    if (forumId != null && message != null) {
      Forum f = this.getForum(forumId);
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
        throw new DBException("Error while posting the message");
      }
    } else {
      throw new IllegalArgumentException("Invalid arguments");
    }
  }

  public Forum getForum(String forumId) throws Exception {
    Forum f;
    try {
      f = (Forum) this.forumRepository.findOne(forumId);
    } catch (Exception ex) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error getting forum", ex);
      }
      throw new DBException("Error while getting forum details");
    }

    if (f == null) {
      logger.error("Forum with id " + forumId + " is not found");
      throw new EntityNotFoundException(forumId, "NOT_FOUND", "Forum");
    } else {
      return f;
    }
  }

  public Iterable<Forum> getForums(String userName) throws Exception {
    User loggedUser = this.getUser(userName);
    List subscriptions = loggedUser.getSubscriptions();
    Iterable forums = this.forumRepository.findAll(subscriptions);
    return forums;
  }

  public Forum getMessagesForForum(String forumId, int start, int count) throws Exception {
    Forum f = (Forum) this.forumRepository.findOne(forumId);
    // List messages = MessageService.getMessages(f, start, count);
    f.getMessages();
    return f;
  }

  public Forum subscribeUser(String forumId, String loggedInUser) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("subscribeUser ( " + forumId + " , " + loggedInUser + " )");
    }

    Forum f = (Forum) this.forumRepository.findOne(forumId);
    if (f == null) {
      logger.error("The forum with id " + forumId + " does not exst");
      throw new EntityNotFoundException(forumId, "NOT_FOUND", "Forum");
    } else {
      User user = this.userService.getUser(loggedInUser);
      if (user == null) {
        logger.error("The user " + loggedInUser + " is not found");
        throw new EntityNotFoundException(loggedInUser, "NOT_FOUND", "User");
      } else {
        if (!user.getSubscriptions().contains(forumId)) {
          f.addSubscriber(user);

          try {
            this.forumRepository.save(f);
          } catch (Exception arg5) {
            if (logger.isDebugEnabled()) {
              logger.error("Error while saving the user", arg5);
            }

            throw new DBException("Error while saving the user");
          }

          user.addSubscription(f.getId());

          try {
            this.userService.save(user);
          } catch (Exception arg6) {
            if (logger.isDebugEnabled()) {
              logger.error("Error while saving the user", arg6);
            }
          }
        }

        return f;
      }
    }
  }

  public Forum addSubscriber(String forumId, String userId) throws Exception {
    User user = this.userService.getUser(userId);
    return this.addSubscriber(forumId, user);
  }
}