package de.swprojekt.speeddating.service.showevent;

import java.util.List;

import de.swprojekt.speeddating.model.Event;
/*
 * Interface gibt zu implementierende Methoden fuer ShowEventImpl vor
 * In Views wird nur Interface eingebunden!
 */
public interface IShowEventService {
	public List<Event> showEvents();
	public Event showEvent(int event_id); //ein Stud
}
