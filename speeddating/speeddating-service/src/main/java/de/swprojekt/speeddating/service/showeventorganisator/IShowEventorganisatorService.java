package de.swprojekt.speeddating.service.showeventorganisator;

import java.util.List;

import de.swprojekt.speeddating.model.Eventorganisator;
/*
 * Interface gibt zu implementierende Methoden fuer ShowEventorganisatorImpl vor
 * In Views wird nur Interface eingebunden!
 */
public interface IShowEventorganisatorService {
	public List<Eventorganisator> showEventorganisatoren();	//alle Eventorganisatoren
	public Eventorganisator showEventorganisator(int eventorganisator_id); //ein Eventorganisator
}
