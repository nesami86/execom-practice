package hello;

import hello.beans.User;
import hello.utils.TwitterDataRepository;
import hello.utils.UserDetailsServiceImpl;
import hello.utils.UserRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
	public @ResponseBody String clientLogin(@RequestParam("function") String callBack, @RequestParam("username") String username, @RequestParam("password") String password) throws JsonProcessingException {
		
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
    public @ResponseBody String clientLogout(@RequestParam("function") String callBack) throws JsonProcessingException {
    	
		SecurityContextHolder.clearContext();
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "OK");
		
		return objectMapper.writeValueAsString(new JSONPObject(callBack, map));
    }
	
	@RequestMapping("/getMyTweets")
    public @ResponseBody String getMyTwitts(@RequestParam("function") String callBack) throws JsonProcessingException {
    	
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", twitterDataRepository.findByTwitterId(userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getTwitterId()).getTweets());
		
		return objectMapper.writeValueAsString(new JSONPObject(callBack, map));
    }
    
	@RequestMapping("/getMyTwitterFriends")
    public @ResponseBody String getMyTwitterFriends(@RequestParam("function") String callBack) throws JsonProcessingException {
    	
    	ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", twitterDataRepository.findByTwitterId(userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getTwitterId()).getFriends());	
   
		return objectMapper.writeValueAsString(new JSONPObject(callBack, map));
    }
    
    @RequestMapping("/getMyTwitterProfile")
    public @ResponseBody String getMyTwitterProfile(@RequestParam("function") String callBack) throws JsonProcessingException {
    	
    	ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", twitterDataRepository.findByTwitterId(userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getTwitterId()));	
   
		return objectMapper.writeValueAsString(new JSONPObject(callBack, map));
	}
}