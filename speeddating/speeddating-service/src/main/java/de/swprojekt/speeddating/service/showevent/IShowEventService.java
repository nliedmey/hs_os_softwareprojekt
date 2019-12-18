package de.swprojekt.speeddating.service.showevent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.EventMatching;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
/*
 * Interface gibt zu implementierende Methoden fuer ShowEventImpl vor
 * In Views wird nur Interface eingebunden!
 */
public interface IShowEventService {
	public List<Event> showEvents();	//alle Events
	public Event showEvent(int event_id); //ein Event
	public List<Integer> showEventsOfUser(int user_id);
	public ArrayList<EventMatching> generateMatchingResultSet(Event aEvent);
}
