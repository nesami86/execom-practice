package tests;

import hello.beans.Friend;
import hello.beans.Tweet;
import hello.beans.TwitterData;
import hello.beans.User;
import hello.utils.Assembler;
import hello.utils.FriendsRepository;
import hello.utils.TwitterDataRepository;
import hello.utils.UserDetailsServiceImpl;
import hello.utils.UserRepository;
import hello.utils.Validations;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.UserOperations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Configuration
public class TestConfig {
		
	@Bean
	public ConnectionRepository connectionRepository() {
		return Mockito.mock(ConnectionRepository.class);			
	}
	
	@Bean
	public TwitterDataRepository twitterDataRepository() {
		return Mockito.mock(TwitterDataRepository.class);			
	}
	
	@Bean
	public UserDetailsServiceImpl userDetailsServiceImpl() {
		return Mockito.mock(UserDetailsServiceImpl.class);			
	}
	
	@Bean
	public FriendsRepository friendsRepository() {
		return Mockito.mock(FriendsRepository.class);			
	}
	
	@Bean
	public Assembler assembler() {
		return Mockito.mock(Assembler.class);			
	}
		
	
	@Bean
	public Twitter twitter() {
		return Mockito.mock(Twitter.class);			
	}
		
	@Bean
	public UserRepository userRepository() {
		return Mockito.mock(UserRepository.class);		
	}
				
	@Bean
	public Validations validations() {
		return Mockito.mock(Validations.class);			
	}
		
	@Bean
	public User user() {
		return Mockito.mock(User.class);			
	}
	
	@Bean
	public BindingResult bindingResult() {
		return Mockito.mock(BindingResult.class);			
	}
	
	@Bean
	public Model model() {
		return Mockito.mock(Model.class);			
	}
	
	@Bean
	public UserOperations userOperations() {
		return Mockito.mock(UserOperations.class);			
	}
	
	@Bean
	public TwitterData twitterData() {
		return Mockito.mock(TwitterData.class);
	}
	
	@Bean
	public Friend friend() {
		return Mockito.mock(Friend.class);
	}
	
	@Bean
	public Tweet tweet() {
		return Mockito.mock(Tweet.class);
	}
}