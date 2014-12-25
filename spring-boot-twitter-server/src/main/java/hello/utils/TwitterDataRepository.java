package hello.utils;

import hello.beans.TwitterData;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * Declares interfaces implemented by hibernate
 */
@Component
public interface TwitterDataRepository extends CrudRepository<TwitterData, Long>{

	TwitterData findByTwitterId(long twitterId);
}