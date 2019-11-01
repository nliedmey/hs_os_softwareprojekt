package de.swprojekt.speeddating.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.User;
import de.swprojekt.speeddating.repository.IUserRepository;

@Service
public class UserServiceImpl implements UserDetailsService{

	@Autowired
	private IUserRepository iUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=iUserRepository.findByUsername(username); //hier User aus Model
		return new CustomUserDetails(user.getUsername(), user.getPassword(), true, true, true, true, user.getAuthorities());
	}

}
