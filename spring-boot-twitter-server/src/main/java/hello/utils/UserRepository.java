package hello.utils;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import hello.beans.User;

/**
 * Declares interfaces implemented by hibernate
 */
@Component
public interface UserRepository extends CrudRepository<User, Long>{

	User findByUsername(String username);
	
	User findByUsernameAndPassword(String username, String password);
	
	User findByTwitterId(long twitterId);
}