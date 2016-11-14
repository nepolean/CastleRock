package com.real.proj.forum.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.real.proj.forum.model.Forum;
import com.real.proj.forum.model.User;
import com.real.proj.forum.service.ForumRepository;
import com.real.proj.forum.service.IForumService;
import com.real.proj.forum.service.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ComponentScan(basePackages = { "com.real.proj.controller.exception",
    "com.real.proj.controller.exception.handler",
    "com.real.proj.forum.controller", "com.real.proj.forum.service" })
@EnableMongoRepositories
@EnableAutoConfiguration
public class ForumControllerTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ForumRepository forumRepository;

  @Autowired
  private IForumService forumService;

  @Autowired
  private TestRestTemplate restClient;
  @LocalServerPort
  private int port;
  private URL base;

  private String defaultUser = "user";
  private String defaultPwd = "user";

  @Before
  public void setup() throws Exception {
    base = new URL("http://localhost:" + port);
    this.cleanUp();
    createDummyUser(defaultUser);
  }

  @Test
  public void createUserForumSuccessfully() throws Exception {
    TestRestTemplate restClient = this.withAuthentication();
    URI uriToPost = new URI(base.toString() + "/forum/create");
    ResponseEntity<Forum> response = restClient.postForEntity(uriToPost,
            "TestForum", Forum.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Forum myForum = response.getBody();
    assertNotNull(myForum);
    assertEquals("TestForum", myForum.getSubject());
  }

  @Test
  public void getUserForumsSuccessfully() throws Exception {
    createTestForum(defaultUser);
    URI uriToProbe = new URI(this.base.toString() + "/forum");
    TestRestTemplate restClient = withAuthentication();
    ResponseEntity<Forum[]> response = restClient.getForEntity(uriToProbe,
            Forum[].class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Forum[] userForums = response.getBody();
    assertNotNull(userForums);
    assertEquals(1, userForums.length);
  }

  @Test
  public void postMessageSuccessfully() throws Exception {
    Forum f = this.createTestForum(defaultUser);
    URI uriToPost = new URI(
            this.base.toString() + "/forum/" + f.getId() + "/post");
    TestRestTemplate restClient = withAuthentication();
    ResponseEntity<String> response = restClient.postForEntity(uriToPost,
            "Hello Test!", String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void subscribeAUserToForum() throws Exception {
    User someUser = this.createDummyUser("User1");
    Forum myForum = this.createTestForum(defaultUser);
    URI uriToPost = new URI(this.base.toString() + "/forum/" + myForum.getId()
            + "/subscribe/" + someUser.getEmail());
    TestRestTemplate restClient = withAuthentication();
    ResponseEntity<String> response = restClient.postForEntity(uriToPost, null,
            String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String output = response.getBody();
    System.out.println(output);
  }

  @After
  public void cleanUp() {
    userRepository.deleteAll();
    forumRepository.deleteAll();
  }

  private User createDummyUser(String userId) {
    User someUser = new User();
    someUser.setEmail(userId);
    someUser = userRepository.save(someUser);
    return someUser;
  }

  private Forum createTestForum(String owner) throws Exception {
    return this.forumService.createForum("TestForum", owner);
  }

  private TestRestTemplate withAuthentication() {
    return restClient.withBasicAuth(defaultUser, defaultPwd);
  }

}
