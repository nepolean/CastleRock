package com.real.proj.forum.perf.test;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.subsede.forum.model.Forum;
import com.subsede.forum.service.ForumRepository;
import com.subsede.forum.service.IForumService;
import com.subsede.user.model.user.User;
import com.subsede.user.repository.user.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ForumPerformanceTest {

  private static final int SIZE = 1000;
  @Autowired
  IForumService forumService;
  @Autowired
  com.subsede.user.services.user.UserService userSerivce;
  @Autowired
  UserRepository userRepository;
  @Autowired
  ForumRepository forumRepository;

  @Before
  public void setup() {
    this.userRepository.deleteAll();
    forumRepository.deleteAll();
  }

  @Test
  public void create1000Users() {

    long start = System.currentTimeMillis();
    for (int i = 0; i < SIZE; i++) {
      User u = new User();
      u.setEmail("User" + i);
      this.userRepository.save(u);
    }
    long end = System.currentTimeMillis();

    System.out.println("Total time : " + (end - start) / 1000);
  }

  @Test
  public void create1000Forums() throws Exception {
    this.create1000Users();
    List<User> users = this.userRepository.findAll();
    long start = System.currentTimeMillis();
    for (User user : users) {
      for (int i = 0; i < SIZE / 10; i++)
        forumService.createForum("TestForum" + i, user.getEmail());
    }
    long end = System.currentTimeMillis();

    System.out.println("Total time : " + (end - start) / 1000);

  }

  @Test
  public void create1000Messages() throws Exception {
    this.create1000Forums();
    List<Forum> forums = this.forumRepository.findAll();
    for (Forum f : forums) {
      for (int i = 10; i < SIZE / 10; i++)
        forumService.postMessage("Hello " + i, f.getId(), f.getOwner().getEmail());
    }
  }

  @After
  public void cleanUp() {
    this.userRepository.deleteAll();
    forumRepository.deleteAll();
  }
}
