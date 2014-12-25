package hello.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import hello.beans.User;

/**
 * Gets users from database
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private Assembler assembler;

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User userEntity = userRepository.findByUsername(username);
		
		if (userEntity == null) {
			
			throw new UsernameNotFoundException("User not found!");
		}
			
		return assembler.buildUserFromUserEntity(userEntity);
	}
}