package de.swprojekt.speeddating.service.pdf;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import de.swprojekt.speeddating.model.EventMatching;
import de.swprojekt.speeddating.model.User;

public interface IMatchingAsPDFService {
	public String pdfErstellen(List<User> users, int event_id, String eventname, String password) throws FileNotFoundException;
	public String pdfMatchingErgebnisseErstellen(ArrayList<EventMatching> arrayList, int event_id, String eventname, String password) throws FileNotFoundException;
	public String pdfEventorganisatorenZugaengeErstellen(List<User> users, String password) throws FileNotFoundException;
	

}
