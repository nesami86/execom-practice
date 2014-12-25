package hello;

import hello.beans.TwitterData;
import hello.beans.User;
import hello.utils.TwitterDataRepository;
import hello.utils.UserDetailsServiceImpl;
import hello.utils.UserRepository;
import hello.utils.Validations;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Main application controller
 */
@Controller
public class WebController extends WebMvcConfigurerAdapter {
	
	@Autowired
	private Twitter twitter;
	
	@Autowired
	private ConnectionRepository connectionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TwitterDataRepository twitterDataRepository;
	
	@Autowired
	private Validations validations;
	
	@Autowired
    private UserDetailsServiceImpl userDetailsService;
		
	@RequestMapping("/home")
    public String home(Model model) {
    	
		model.addAttribute("hasLoggedIn", 0);
        return "index";
    }
	
	/**
	 * Responds to all "default" calls ("/"), initialization call, call from twitter api, etc.
	 * 
	 * @param model
	 * @return adequate page with optional parameters
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String hello(Model model) {
		
		User u = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (u == null && twitter.isAuthorized()) {
			
			u = userRepository.findByTwitterId(twitter.userOperations().getProfileId());
		}
				
		if (!twitter.isAuthorized() && u == null) {

			model.addAttribute("hasLoggedIn", 0);
			return "index";
			
		} else if (!twitter.isAuthorized() && u != null) {
			
			model.addAttribute("hasLoggedIn", 1);
			return "index";
			
		} else if (u != null && u.getTwitterId() == 0 && twitter.isAuthorized()) {	
			
			validations.synchronizeTwitter(u.getUsername(), twitter);
			return "synced";
					
		} else {
						
			validations.saveTwitterData(twitter);
			TwitterData twitterData = twitterDataRepository.findByTwitterId(twitter.userOperations().getProfileId());
			
			model.addAttribute("twitterProfile", twitterData.getTwitterName());
		    model.addAttribute("friends", twitterData.getFriends());
		    model.addAttribute("tweets", twitterData.getTweets());
		    model.addAttribute("hasProfile", 0);
		    
		    if (u != null && u.getUsername() != null) {
		    	    	
		    	model.addAttribute("hasProfile", 1);
		    }
		    		  
		    return  "twitterProfile";
		} 
	}	
	
    @RequestMapping("/registerForm")
    public String showRegisterForm(User user) {
    	
        return "registerForm";
    }
    
    @RequestMapping("/profile")
    public String profile(Model model) {
    	
    	boolean connectedWithTwitter = false;
    	    	
    	if (twitter.isAuthorized()) {
    		
    		connectedWithTwitter = true;
    	}
    	
    	SecurityContext securityContext = SecurityContextHolder.getContext();
    	Authentication authentication = securityContext.getAuthentication();
    	String name = authentication.getName();
    	
    	model.addAttribute("user", userRepository.findByUsername(name));
    	model.addAttribute("isConnectedWithTwitter", connectedWithTwitter);  	   	
    	   	
    	return "profile";
    }   
    
    @RequestMapping("/login")
    public String login() {
    	    	
        return "login";
    }
    
    @RequestMapping("/register")
    public String register(@Valid User user, BindingResult bindingResult, Model model) {
      
    	if (bindingResult.hasErrors()) {
    		
    		return "registerForm";
    		
    	} else if (!validations.checkRegistration(user).equals("OK")) {
            
    		model.addAttribute("error", "Username already taken!");
    		return "registerForm";
        
    	} else {
    		
    		return "registrationComplete";
    	}
    }
    
    @RequestMapping("/signout")
    public String logout() {
    	
    	if (connectionRepository.findConnections(Twitter.class).size() > 0) {
    		
			connectionRepository.removeConnection(connectionRepository.findPrimaryConnection(Twitter.class).getKey());
		}
    	
    	return "redirect:/logout";
    }
}