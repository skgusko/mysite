package mysite.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import mysite.vo.UserVo;

public class UserDetailsImpl extends UserVo implements UserDetails {

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() { //email
		return getEmail();
	}
	
	@Override
	public String getPassword() { 
		return super.getPassword();
	}

}
