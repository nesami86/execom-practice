package hello;

import hello.beans.Tweet;
import hello.beans.TwitterData;
import hello.beans.User;
import hello.utils.TwitterDataRepository;
import hello.utils.UserDetailsServiceImpl;
import hello.utils.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 * Responds from a client application calls
 */
@Controller
public class RestfulController extends WebMvcConfigurerAdapter {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TwitterDataRepository twitterDataRepository;
	
	@Autowired
    private UserDetailsServiceImpl userDetailsService;
		
	
	/**
	 * Authenticates user
	 * 
	 * @param callBack
	 * @param username
	 * @param password
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping("/clientLogin")
	public String clientLogin(@RequestParam("function") String callBack, @RequestParam("username") String username, @RequestParam("password") String password) throws JsonProcessingException {
		
		User user = userRepository.findByUsernameAndPassword(username, password);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (user != null) {
						
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
					
			map.put("data", "OK_" + user.getName());	
		
		} else {
			
			map.put("data", "Wrong username and/or password!");	
		}
	   
		return objectMapper.writeValueAsString(new JSONPObject(callBack, map));
	}
	
	@RequestMapping("/clientLogout")
    public String clientLogout(@RequestParam("function") String callBack) throws JsonProcessingException {
    	
		SecurityContextHolder.clearContext();
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "OK");
		
		return objectMapper.writeValueAsString(new JSONPObject(callBack, map));
    }
	
	@RequestMapping("/getMyTweets")
    public List<Tweet> getMyTwitts() {
    	
		SecurityContext securityContext = SecurityContextHolder.getContext();
    	Authentication authentication = securityContext.getAuthentication();
    	String username = authentication.getName();
    	User user = userRepository.findByUsername(username);
    	long twitterId = user.getTwitterId();
    	TwitterData twitterData = twitterDataRepository.findByTwitterId(twitterId);
    	List<Tweet> tweets = twitterData.getTweets();
		
//		Tweet tweet = new Tweet();
//		tweet.setText("First Tweet");
//		List<Tweet> tweets = new ArrayList<Tweet>();
//		tweets.add(tweet);
		
		return tweets;
    }
    
    @RequestMapping("/getMyTwitterFriends")
    public Object getMyTwitterFriends() {
    	
		return twitterDataRepository.findByTwitterId(userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getTwitterId()).getFriends();	
    }
    
    @RequestMapping("/getMyTwitterProfile")
    public Object getMyTwitterProfile() {
    	
		return twitterDataRepository.findByTwitterId(userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getTwitterId());	
	}
    
    @RequestMapping("/test")
    public String test() {
    	    	
    	return "Working";
    }
    
    @RequestMapping("/test2")
    public @ResponseBody User test2() {
    	
    	User user = new User();
    	user.setUsername("nesa");
    	user.setEmail("nesami86@yahoo.com");
    	user.setId(1);
    	user.setPassword("nesa");
    	user.setSurname("milovanov");
    	user.setName("nesa");
    	user.setTwitterId(1);
    	user.setTwitterName("nesa");
    	
    	return user;
    }
}