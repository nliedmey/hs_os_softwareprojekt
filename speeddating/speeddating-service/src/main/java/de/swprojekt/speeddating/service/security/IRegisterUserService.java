package de.swprojekt.speeddating.service.security;

public interface IRegisterUserService {
	public void save(String username, String password, String role, int refEntityId);
	public boolean changePassword(String username, String oldPassword, String newPassword);
}
