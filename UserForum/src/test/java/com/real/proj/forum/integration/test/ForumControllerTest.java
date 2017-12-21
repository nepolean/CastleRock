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
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.real.proj.unit.test.BaseTest;
import com.subsede.forum.model.Forum;
import com.subsede.forum.service.ForumRepository;
import com.subsede.forum.service.ForumService;
import com.subsede.forum.service.IForumService;
import com.subsede.util.message.SimpleMessage;
import com.subsede.util.user.model.User;
import com.subsede.util.user.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { ForumService.class,
    UserService.class })
@ActiveProfiles("test")
@EnableMongoRepositories("com.subsede.forum.service")
@EnableAutoConfiguration
public class ForumControllerTest extends BaseTest {

  // @Autowired
  // public UserRepository userRepository;
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
    TestRestTemplate restClient = this.withAuthentication(defaultUser, defaultPwd);
    URI uriToPost = new URI(base.toString() + "/forum/create");
    ResponseEntity<Forum> response = restClient.postForEntity(uriToPost, "TestForum", Forum.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Forum myForum = response.getBody();
    assertNotNull(myForum);
    assertEquals("TestForum", myForum.getSubject());
  }

  @Test
  public void getUserForumsSuccessfully() throws Exception {
    createTestForum(defaultUser);
    URI uriToProbe = new URI(this.base.toString() + "/forum");
    TestRestTemplate restClient = withAuthentication(defaultUser, defaultPwd);
    ResponseEntity<Forum[]> response = restClient.getForEntity(uriToProbe, Forum[].class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Forum[] userForums = response.getBody();
    assertNotNull(userForums);
    assertEquals(1, userForums.length);
  }

  @Test
  public void postMessageSuccessfully() throws Exception {
    Forum f = this.createTestForum(defaultUser);
    URI uriToPost = new URI(this.base.toString() + "/forum/" + f.getId() + "/post");
    TestRestTemplate restClient = withAuthentication(defaultUser, defaultPwd);
    ResponseEntity<String> response = restClient.postForEntity(uriToPost, "Hello Test!", String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void subscribeAUserToForum() throws Exception {
    User someUser = this.createDummyUser("User1");
    Forum myForum = this.createTestForum(defaultUser);
    URI uriToPost = new URI(this.base.toString() + "/forum/" + myForum.getId() + "/subscribe/" + someUser.getEmail());
    TestRestTemplate restClient = withAuthentication(defaultUser, defaultPwd);
    ResponseEntity<String> response = restClient.postForEntity(uriToPost, null, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String output = response.getBody();
  }

  @Test
  public void unsubscribeLoggedInUser() throws Exception {
    User someOtherUser = this.createDummyUser("User1");
    Forum myForum = this.createTestForum(defaultUser);
    myForum.addSubscriber(someOtherUser);
    forumRepository.save(myForum);
    URI uriToPost = new URI(this.base.toString() + "/forum/" + myForum.getId() + "/unsubscribe");
    TestRestTemplate restClient = withAuthentication("User1", "");
    // TODO: Uncomment these after security integration is done
    // ResponseEntity<SimpleMessage> response =
    // restClient.postForEntity(uriToPost, someOtherUser.getEmail(),
    // SimpleMessage.class);
    // assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void unsubscribeOtherUser() throws Exception {
    User someOtherUser = this.createDummyUser("User1");
    Forum myForum = this.createTestForum(defaultUser);
    myForum.addSubscriber(someOtherUser);
    forumRepository.save(myForum);
    URI uriToPost = new URI(
        this.base.toString() + "/forum/" + myForum.getId() + "/unsubscribe/" + someOtherUser.getEmail());
    TestRestTemplate restClient = withAuthentication(defaultUser, defaultPwd);
    ResponseEntity<SimpleMessage> response = restClient.postForEntity(uriToPost, null, SimpleMessage.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @After
  public void cleanUp() {
    userRepository.deleteAll();
    forumRepository.deleteAll();
  }

  private Forum createTestForum(String owner) throws Exception {
    return this.forumService.createForum("TestForum", owner);
  }

  private TestRestTemplate withAuthentication(String user, String pwd) {
    return restClient.withBasicAuth(user, pwd);
  }

}
