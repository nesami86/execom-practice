package tests;

import hello.WebController;
import hello.beans.Friend;
import hello.beans.Tweet;
import hello.beans.TwitterData;
import hello.beans.User;
import hello.utils.TwitterDataRepository;
import hello.utils.UserRepository;
import hello.utils.Validations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.UserOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class, WebControllerTest.TestConfig.class})
public class WebControllerTest {

	@Configuration
	static class TestConfig {
				
		@Bean
		public WebController webControllerInstance(){
			return new WebController();
		}
	}
		
	@Autowired
	private WebController webControllerInstance; 
	
	@Autowired
	private Twitter twitter;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Validations validations;
	
	@Autowired
	private User user;
	
	@Autowired
	private BindingResult bindingResult;
	
	@Autowired
	private Model model;
	
	@Autowired
	private UserOperations userOperations;
	
	@Autowired
	private TwitterData twitterData;
	
	@Autowired
	private TwitterDataRepository twitterDataRepository;
	
	@Autowired
	private Tweet tweet;
	
	@Autowired
	private Friend friend;
		
	@Before
    public void setUp() {
		
		reset(twitter, userRepository, validations, user, bindingResult, model);
		
        User user = new User();
        user.setUsername("nesa");
        Authentication auth = new UsernamePasswordAuthenticationToken(user,null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
	
	@Test
	public void homeTest() {
		
		assertEquals("index", webControllerInstance.home(model));
	}
	
	@Test
	public void helloTest1() {
		
		when(twitter.isAuthorized()).thenReturn(true);
		when(twitter.userOperations()).thenReturn(userOperations);
		when(userOperations.getProfileId()).thenReturn((long) 1);
		when(twitterDataRepository.findByTwitterId(twitter.userOperations().getProfileId())).thenReturn(twitterData);
		when(twitterData.getTwitterName()).thenReturn("nesa");
				
		webControllerInstance.hello(model);
	}
	
	@Test
	public void helloTest2() {
		
		when(twitter.isAuthorized()).thenReturn(false);
		assertEquals("index", webControllerInstance.hello(model));
	}
	
	@Test
	public void helloTest3() {
		
		when(twitter.isAuthorized()).thenReturn(false);
		assertEquals("index", webControllerInstance.hello(model));
	}
	
	@Test
	public void helloTest4() {
		
		when(userRepository.findByUsername("nesa")).thenReturn(user);
		when(twitter.isAuthorized()).thenReturn(true);	
		when(twitter.userOperations()).thenReturn(userOperations);
		when(userOperations.getProfileId()).thenReturn((long) 1);
		doNothing().when(validations).synchronizeTwitter("nesa", twitter);
		
		assertEquals("synced", webControllerInstance.hello(model));
	}
	
	@Test
	public void helloTest5() {
		
		when(twitter.isAuthorized()).thenReturn(true);
		when(twitter.userOperations()).thenReturn(userOperations);
		when(userOperations.getProfileId()).thenReturn((long) 1);
		
		assertEquals("twitterProfile", webControllerInstance.hello(model));
	}
		
	@Test
	public void showRegisterFormTest() {
		
		assertEquals("registerForm", webControllerInstance.showRegisterForm(user));
	}
	
	@Test
	public void profileTest() {
			
		when(twitter.isAuthorized()).thenReturn(true);	
		webControllerInstance.profile(model);
	}
			
	@Test
	public void loginTest() {
		
		assertEquals("login", webControllerInstance.login());
	}
	
	@Test
	public void registerTest1() {
		
		when(validations.checkRegistration(user)).thenReturn("OK");
		assertEquals("registrationComplete", webControllerInstance.register(user, bindingResult, model));
	}
	
	@Test
	public void registerTest2() {
		
		when(bindingResult.hasErrors()).thenReturn(true);
		assertEquals("registerForm", webControllerInstance.register(user, bindingResult, model));
	}
	
	@Test
	public void registerTest3() {
		
		when(bindingResult.hasErrors()).thenReturn(false);
		when(validations.checkRegistration(user)).thenReturn("NOT_OK");
		assertEquals("registerForm", webControllerInstance.register(user, bindingResult, model));
	}
		
	@Test
	public void logoutTest1() {
		
		assertEquals("redirect:/logout", webControllerInstance.logout());
	}
}