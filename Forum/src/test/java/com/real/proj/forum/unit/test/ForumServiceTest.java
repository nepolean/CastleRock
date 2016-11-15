package com.real.proj.forum.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.real.proj.forum.model.Forum;
import com.real.proj.forum.model.User;
import com.real.proj.forum.service.ForumRepository;
import com.real.proj.forum.service.IForumService;
import com.real.proj.forum.service.UserRepository;
import com.real.proj.message.SimpleMessage;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ForumServiceTest {

  @Autowired
  IForumService forumService;
  @Autowired
  UserRepository userRepository;
  @Autowired
  ForumRepository forumRepository;

  private String default_user = "user";
  private String default_pwd = "user";

  private List<User> users = new ArrayList<User>();

  @Before
  public void setup() throws MalformedURLException {
    // base = new URL("http://localhost:" + port);
    userRepository.deleteAll();
    forumRepository.deleteAll();
    createDummyUsers();
  }

  private void createDummyUsers() {

    User u = new User();
    u.setEmail("user");
    users.add(userRepository.save(u));

    User u1 = new User();
    u1.setEmail("user1@email.com");
    users.add(userRepository.save(u1));

    User u2 = new User();
    u2.setEmail("user2@email.com");
    users.add(userRepository.save(u2));

    User u3 = new User();
    u3.setEmail("user3@email.com");
    users.add(userRepository.save(u3));

  }

  @Test
  public void testCreateForum() throws Exception {
    assertNotNull(forumService);
    Forum f = forumService.createForum("TestForum", default_user);
    assertNotNull(f);
    assertEquals("TestForum", f.getSubject());
    assert (f.getSubscribers().size() == 1);
    assertEquals("user", f.getOwner().getEmail());
  }

  @Test
  public void testGetForums() throws Exception {
    assertNotNull(forumService);
    forumService.createForum("TestForum", default_user);
    forumService.createForum("TestForum", this.users.get(1).getEmail());
    List<Forum> forums = forumService.getForums(default_user);
    assertNotNull(forums);
    assert (forums.size() == 1);
  }

  @Test
  public void testPostMessage() throws Exception {
    assertNotNull(forumService);
    forumService.createForum("TestForum", default_user);
    List<Forum> forums = forumService.getForums(default_user);
    assertNotNull(forums);
    assertTrue(forums.iterator().hasNext());
    Forum first = forums.iterator().next();
    Forum withMessage = forumService.postMessage("Hello", first.getId(), default_user);
    assertNotNull(withMessage);
    assert (withMessage.getMessages().size() == 1);
  }

  @Test
  public void testAddSubscriber() throws Exception {
    assertNotNull(forumService);
    Forum f = forumService.createForum("TestForum", default_user);
    Forum updatedForum = forumService.addUserToForum(f.getId(), default_user, this.users.get(1).getEmail());
    assertNotNull(updatedForum);
    assertEquals(updatedForum.getSubscribers().size(), 2);
  }

  @Test
  public void onlyOwnerCanSubscribe() throws Exception {
    assertNotNull(forumService);
    Forum f = forumService.createForum("TestForum", default_user);
    try {
      forumService.addUserToForum(f.getId(), this.users.get(1).getEmail(), this.users.get(2).getEmail());
      fail("only a owner can subscriber other users");
    } catch (Exception ex) {
    }
  }

  @Test
  public void automaticallySubscribeIfIndicated() throws Exception {
    assertNotNull(forumService);
    Forum f = forumService.createForum("TestForum", default_user);
    f.setAutoSubscriptionEnabled(true);
    f = forumRepository.save(f);
    SimpleMessage result = forumService.subscribeMe(f.getId(), this.users.get(1).getEmail());
    assert (result.getMessage().matches("Your subscription has been accepted"));
  }

  @Test
  public void shouldCreateASubscriptionRequest() throws Exception {
    assertNotNull(forumService);
    Forum f = forumService.createForum("TestForum", default_user);
    f.setAutoSubscriptionEnabled(false);
    f = forumRepository.save(f);
    SimpleMessage result = forumService.subscribeMe(f.getId(), this.users.get(1).getEmail());
    assert (result.getMessage().startsWith("A subscription request has been created."));
  }

  @Test
  public void testCloseForum() throws Exception {
    assertNotNull(forumService);
    Forum f = forumService.createForum("TestForum", default_user);
    forumService.closeForum(f.getId(), default_user);
    f = forumService.getRequestedForum(f.getId(), default_user);
    assert (f.isClosed() == true);
  }

  @Test
  public void cannotPostToClosedForum() throws Exception {
    Forum f = forumService.createForum("TestForum", default_user);
    forumService.closeForum(f.getId(), default_user);
    f = forumService.getRequestedForum(f.getId(), default_user);
    try {
      forumService.postMessage("Some Message", f.getId(), default_user);
      fail("should not allow to post to a closed forum");
    } catch (Exception ex) {

    }

  }

  @Test
  public void cannotSubscribeToClosedForum() throws Exception {
    Forum f = forumService.createForum("TestForum", default_user);
    forumService.closeForum(f.getId(), default_user);
    f = forumService.getRequestedForum(f.getId(), default_user);
    try {
      forumService.subscribeMe(f.getId(), default_user);
      fail("should not allow to subscribe to a closed forum");
    } catch (Exception ex) {

    }

  }

  @Test
  public void unsubscribeMeTest() throws Exception {
    Forum f = forumService.createForum("TestForum", default_user);
    forumService.addUserToForum(f.getId(), default_user, users.get(1).getEmail());
    SimpleMessage result = forumService.unsubscribeMe(f.getId(), users.get(1).getEmail());
  }

  @Test
  public void ownerCannotUnsubscribeHimself() throws Exception {
    Forum f = forumService.createForum("TestForum", default_user);
    try {
      SimpleMessage result = forumService.unsubscribeMe(f.getId(), default_user);
      fail("owner cannot unsubscribe himself");
    } catch (Exception ex) {

    }
  }

  @Test
  public void unsubscribeUser() throws Exception {
    Forum f = forumService.createForum("TestForuM", default_user);
    forumService.addUserToForum(f.getId(), default_user, this.users.get(1).getEmail());
    forumService.removeUserFromForum(f.getId(), this.users.get(1).getEmail(), default_user);
  }

  @Test
  public void onlyUserCanUnsubscribe() throws Exception {
    Forum f = forumService.createForum("TestForum", default_user);
    forumService.addUserToForum(f.getId(), default_user, this.users.get(1).getEmail());
    try {
      forumService.removeUserFromForum(f.getId(), this.users.get(1).getEmail(), this.users.get(2).getEmail());
      fail("Ownly owner can unsubscribe a user");
    } catch (Exception ex) {

    }
  }

  @After
  public void cleanUp() {
    userRepository.deleteAll();
    forumRepository.deleteAll();
  }

}
