package de.swprojekt.speeddating.service.addevent;

import de.swprojekt.speeddating.model.Event;

public interface IAddEventService {
	public Event speicherEvent(Event einEvent);
	public void addeEventInEventorga(int event_id, int user_id);
}
