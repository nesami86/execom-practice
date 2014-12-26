package tests;

import hello.RestfulController;
import hello.beans.Tweet;
import hello.beans.TwitterData;
import hello.beans.User;
import hello.utils.TwitterDataRepository;
import hello.utils.UserDetailsServiceImpl;
import hello.utils.UserRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfig.class, RestfulControllerTest.TestConfig.class })
public class RestfulControllerTest {

	@Configuration
	@EnableWebMvc
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
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private UserDetails userDetails;

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

		user.setUsername("nesa");
		user.setTwitterId(1);
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null);
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		when(userRepository.findByUsername(anyString())).thenReturn(user);
		when(user.getTwitterId()).thenReturn((long) 1);
		when(twitterDataRepository.findByTwitterId(1)).thenReturn(twitterData);
	}

	@Test
	public void testClientLogin1() throws Exception {
		
//		this.mockMvc.perform(get("/clientLogin?function=function1&username=nesa&password=nesa")).andExpect(status().isOk());
		
		when(userRepository.findByUsernameAndPassword("nesa", "nesa")).thenReturn(user);
		when(userDetailsService.loadUserByUsername("nesa")).thenReturn(userDetails);
		assertEquals("f1({\"data\":\"OK_null\"})", restfulController.clientLogin("f1", "nesa", "nesa"));
	}
	
	@Test
	public void testClientLogin2() throws Exception {
		
		when(userRepository.findByUsernameAndPassword("nesa", "nesa")).thenReturn(null);
		assertEquals("f1({\"data\":\"Wrong username and/or password!\"})", restfulController.clientLogin("f1", "nesa", "nesa"));
	}
	
	@Test
	public void testClientLogout() throws Exception {
		
		this.mockMvc.perform(get("/clientLogout?function=f1")).andExpect(status().isOk());
	}	
	
	@Test
	public void testGetMyTwitts() throws Exception {

		List<Tweet> tweets = new ArrayList<Tweet>();
		Tweet t = new Tweet();
		t.setText("Test tweet");
		tweets.add(t);

		when(twitterData.getTweets()).thenReturn(tweets);

		this.mockMvc.perform(get("/getMyTweets"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].text").value("Test tweet"));
	}
	
	@Test
	public void testGetMyTwitterFriends() throws Exception {
		
		this.mockMvc.perform(get("/getMyTwitterFriends")).andExpect(status().isOk());
	}
	
	@Ignore
	@Test
	public void testGetMyTwitterProfile() throws Exception {
		
		this.mockMvc.perform(get("/getMyTwitterProfile")).andExpect(status().isOk());
	}

	@Test
	public void testTest2() throws Exception {

		this.mockMvc.perform(get("/test2"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("Nenad"))
			.andExpect(jsonPath("$.surname").value("Milovanov"));
	}
}