package tests;

import java.util.ArrayList;
import java.util.List;

import hello.RestfulController;
import hello.beans.Tweet;
import hello.beans.TwitterData;
import hello.beans.User;
import hello.utils.TwitterDataRepository;
import hello.utils.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfig.class, RestfulControllerTest.TestConfig.class })
public class RestfulControllerTest {

	@Configuration
	static class TestConfig {

		@Bean
		public RestfulController restfulController() {
			return new RestfulController();
		}
	}

	@Autowired
	private RestfulController restfulController;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private User user;

	@Autowired
	private TwitterDataRepository twitterDataRepository;

	@Autowired
	private TwitterData twitterData;

	@Autowired
	private Tweet tweet;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

		User user = new User();
		user.setUsername("nesa");
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
	public void testGetMyTwitts() throws Exception {

		List<Tweet> tweets = new ArrayList<Tweet>();
		tweets.add(tweet);

		when(userRepository.findByUsername("nesa")).thenReturn(user);
		when(user.getTwitterId()).thenReturn((long) 1);
		when(twitterDataRepository.findByTwitterId(1)).thenReturn(twitterData);
		when(twitterData.getTweets()).thenReturn(tweets);

		this.mockMvc.perform(get("/getMyTweets").accept(MediaType.APPLICATION_JSON));
	}

	@Test
	public void testTest() throws Exception {

		this.mockMvc.perform(get("/test2").accept(MediaType.APPLICATION_JSON));
	}

	@Test
	public void testTest2() throws Exception {

		this.mockMvc.perform(get("/test2").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.name").value("nesa"));
	}
}