package de.swprojekt.speeddating.service.pdf;

import java.io.FileNotFoundException;
import java.util.List;

import de.swprojekt.speeddating.model.User;

public interface IMatchingAsPDFService {
	public String pdfErstellen(List<User> users, int event_id, String eventname, String password) throws FileNotFoundException;
}
