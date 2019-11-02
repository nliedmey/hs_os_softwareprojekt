package de.swprojekt.speeddating.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.User;
import de.swprojekt.speeddating.repository.IUserRepository;

@Service
public class RegisterUserServiceImpl implements IRegisterUserService {

	@Autowired
	private IUserRepository iUserRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public void save(String username, String password) {
		User user=new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		iUserRepository.save(user);	
	}
	
}
