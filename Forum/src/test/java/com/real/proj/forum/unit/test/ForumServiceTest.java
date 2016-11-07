package com.real.proj.forum.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import com.real.proj.forum.service.ForumService;
import com.real.proj.forum.service.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ForumServiceTest {

  @Autowired
  ForumService forumService;
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
    u1.setEmail("user2@email.com");
    users.add(userRepository.save(u2));

    User u3 = new User();
    u1.setEmail("user3@email.com");
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
    Iterable<Forum> forums = forumService.getForums(default_user);
    assertNotNull(forums);
    assertTrue(forums.iterator().hasNext());
  }

  @Test
  public void testPostMessage() throws Exception {
    assertNotNull(forumService);
    forumService.createForum("TestForum", default_user);
    Iterable<Forum> forums = forumService.getForums(default_user);
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
    Forum updatedForum = forumService.addSubscriber(f.getId(), this.users.get(1).getEmail());
    assertNotNull(updatedForum);
    assertEquals(updatedForum.getSubscribers().size(), 2);
  }

  @After
  public void cleanUp() {
    userRepository.deleteAll();
    forumRepository.deleteAll();
  }

}
