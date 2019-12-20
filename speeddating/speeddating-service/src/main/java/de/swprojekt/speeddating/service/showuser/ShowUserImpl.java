package de.swprojekt.speeddating.service.showuser;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.User;
import de.swprojekt.speeddating.repository.IUserRepository;
/*
 * Implementierung des IShowUserService-Interfaces
 * Eingebunden in Views wird hingegen nur das Interface! 
 * (hierfuer muss die Implementierung als Service deklariert sein!)
 */
@Service
public class ShowUserImpl implements IShowUserService {

	@Autowired	//Repository wird autowired
	IUserRepository iUserRepository;	//Zugriff auf User-Entities in DB

//	@Override
//	public Studierender showStudierenden(int stud_id) {
//		Optional<Studierender> gefundenerStudierender;
//		try {
//			gefundenerStudierender = iStudierenderRepository.findById(stud_id);
//			return gefundenerStudierender.get();
//		} catch (NoSuchElementException e) {
//			System.out.println("Kein Studierender zu ID vorhanden!");
//			return null;
//		}
//	}

	@Override
	public User showUser(int user_id) {
		Optional<User> gefundenerUser;
		try {
			gefundenerUser = iUserRepository.findById(user_id);
			return gefundenerUser.get();
		} catch (NoSuchElementException e) {
			System.out.println("Kein User zu ID vorhanden!");
			return null;
		}
	}
	
	@Override
	public List<User> showStudierendeUserInEvent(int event_id) {
		List<Integer> gefundeneStudierendeUserIDZuEvent=iUserRepository.findStudierendeUserByEventId(event_id);
		List<User> gefundeneStudierendeUserZuEvent=new ArrayList<User>();
		for(int studierendeUserId:gefundeneStudierendeUserIDZuEvent)
		{
			gefundeneStudierendeUserZuEvent.add(iUserRepository.findById(studierendeUserId).get());
		}
		return gefundeneStudierendeUserZuEvent;
	}
	
	@Override
	public List<User> showUnternehmenUserInEvent(int event_id) {
		List<Integer> gefundeneUnternehmenUserIDZuEvent=iUserRepository.findUnternehmenUserByEventId(event_id);
		List<User> gefundeneUnternehmenUserZuEvent=new ArrayList<User>();
		for(int unternehmenUserId:gefundeneUnternehmenUserIDZuEvent)
		{
			gefundeneUnternehmenUserZuEvent.add(iUserRepository.findById(unternehmenUserId).get());
		}
		return gefundeneUnternehmenUserZuEvent;
	}
	
	

}
