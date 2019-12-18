package de.swprojekt.speeddating.service.showevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Event;
import de.swprojekt.speeddating.model.EventMatching;
import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.repository.IEventRepository;
import de.swprojekt.speeddating.repository.IUnternehmenRepository;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;

/*
 * Implementierung des IShowEventService-Interfaces
 * Eingebunden in Views wird hingegen nur das Interface! 
 * (hierfuer muss die Implementierung als Service deklariert sein!)
 */
@Service
public class ShowEventImpl implements IShowEventService {

	@Autowired // Repository wird autowired
	IEventRepository iEventRepository; // Zugriff auf Event-Entities in DB´
	@Autowired
	IShowStudierendeService iShowStudierendeService;
	@Autowired
	IShowUnternehmenService iShowUnternehmenService;

	@Override
	public List<Event> showEvents() {
		return iEventRepository.findAll(); // gibt Liste
	}

	@Override
	public Event showEvent(int event_id) {
		Optional<Event> gefundenesEvent;
		try {
			gefundenesEvent = iEventRepository.findById(event_id);
			return gefundenesEvent.get();
		} catch (NoSuchElementException e) {
			System.out.println("Kein Event zu ID vorhanden!");
			return null;
		}
	}

	@Override
	public List<Integer> showEventsOfUser(int user_id) {
		return iEventRepository.findByUserId(user_id);
	}

	@Override
	public ArrayList<EventMatching> generateMatchingResultSet(Event aEvent) {

		Map<Studierender, Unternehmen> matchingResultMap = new HashMap<>();
		Set<Integer> studentKontaktWuensche = new HashSet<>();
		Set<Integer> unternehmenKontaktWuensche = new HashSet<>();
		ArrayList<EventMatching> eventMatchingList = new ArrayList<EventMatching>();
		
		for (Integer student_id : aEvent.getTeilnehmendeStudierende()) {
			Studierender aStudent = iShowStudierendeService.showStudierenden(student_id);
			studentKontaktWuensche.clear();
			if (aStudent != null) {
				studentKontaktWuensche = aStudent.getStudentKontaktwuensche();//
				if (!(studentKontaktWuensche.isEmpty())) {
					for (Integer studentenWunsch : studentKontaktWuensche) {
						for (Integer unternehmen_id : aEvent.getTeilnehmendeUnternehmen()) {
							unternehmenKontaktWuensche.clear();
							Unternehmen aUnternehmen = iShowUnternehmenService.showEinUnternehmen(unternehmen_id);
							if (aUnternehmen.getUnternehmen_id() == studentenWunsch) {
								unternehmenKontaktWuensche = aUnternehmen.getUnternehmenKontaktwuensche();
								for (Integer unternehmenWunsch : unternehmenKontaktWuensche) {
									if (aStudent.getStudent_id() == unternehmenWunsch) {

//										matchingResultMap.put(aStudent, aUnternehmen);
								
									    EventMatching aNewEventMatching = new EventMatching(aStudent.getStringFullNameOfStudent(), aUnternehmen.getUnternehmensname());
										eventMatchingList.add(aNewEventMatching);
										
										
//									System.out.println("Jawohl, das ist ein Matching per For -> Student"
//											+ aStudent.getStudent_id() + " und das Unternehmen"
//											+ aUnternehmen.getUnternehmen_id() + "gehoeren zusammen");				
									}
								}
							}
						}
					}
				}
			} else {
				// Student hat keine Angaben gemacht,
				// daher machen wir beim naechsten weiter um Zeit zu sparen
				continue;
			}
		}
		return eventMatchingList;

	}

}
