package com.real.proj.forum.integration.test;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.real.proj.forum.model.Forum;
import com.real.proj.forum.model.User;
import com.real.proj.forum.service.IForumService;
import com.real.proj.forum.service.UserRepository;
import com.real.proj.forum.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ComponentScan(basePackages = { "com.real.proj.controller.exception", "com.real.proj.controller.exception.handler",
    "com.real.proj.forum.controller", "com.real.proj.forum.service" })
@EnableMongoRepositories
@EnableAutoConfiguration
public class ForumControllerTest {

  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private IForumService forumService;
  @Autowired
  private TestRestTemplate restClient;
  @LocalServerPort
  private int port;
  private URL base;

  private String default_user = "user";
  private String default_pwd = "user";
  private Forum forum;

  @Before
  public void setup() throws Exception {
    base = new URL("http://localhost:" + port);
    createDummyUser();
    createDummyForum();
    userService.getUser("user");
  }

  private void createDummyUser() {
    User u = new User();
    u.setEmail(default_user);
    userRepository.save(u);
  }

  private void createDummyForum() throws Exception {
    forum = forumService.createForum("TestForum", default_user);
  }

  @Test
  public void createUserForumSuccessfully() throws Exception {
    // given(forumService.createForum("Foo", "user")).willReturn(new
    // Forum("Foo"));
    RequestEntity request = buildWebRequest("/forum/create", "POST", "TestForum");
    ResponseEntity<Forum> response = restClient.withBasicAuth(default_user, default_pwd).exchange(request, Forum.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void getUserForumsSuccessfully() throws Exception {
    String uri = "/forum";
    RequestEntity request = buildWebRequest(uri, "GET", null);
    ResponseEntity<Forum> response = restClient.withBasicAuth(default_user, default_pwd).exchange(request, Forum.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  private RequestEntity buildWebRequest(String uri, String httpMethod, String content) throws URISyntaxException {
    URI url = new URI(base.toString() + uri);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    RequestEntity request = null;
    if ("POST".equals(httpMethod))
      request = RequestEntity.post(url).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
          .body(content);
    else
      request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
    return request;
  }

}
