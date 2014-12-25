package hello.utils;

import hello.beans.Friend;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * Declares interfaces implemented by hibernate
 */
@Component
public interface FriendsRepository extends CrudRepository<Friend, Long>{

	Friend findByName(String name);
}