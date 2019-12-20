package de.swprojekt.speeddating.service.showuser;

import java.util.List;

import de.swprojekt.speeddating.model.User;
/*
 * Interface gibt zu implementierende Methoden fuer ShowUserImpl vor
 * In Views wird nur Interface eingebunden!
 */
public interface IShowUserService {
	public User showUser(int user_id);
	public List<User> showStudierendeUserInEvent(int event_id);
	public List<User> showUnternehmenUserInEvent(int event_id);
}
