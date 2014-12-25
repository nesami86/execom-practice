package hello.utils;

import hello.beans.User;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Translates users from beans to spring security users
 */
@Component
public class Assembler {

	@Transactional(readOnly = true)
	org.springframework.security.core.userdetails.User buildUserFromUserEntity(User userEntity) {

		String username = userEntity.getUsername();
		String password = userEntity.getPassword();

		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		// for (SecurityRoleEntity role : userEntity.getRoles()) {
		// 		authorities.add(new GrantedAuthorityImpl(role.getRoleName()));
		// }
		authorities.add(new SimpleGrantedAuthority("user"));

		org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username, password, authorities);
		
		return user;
	}
}