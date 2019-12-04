package de.swprojekt.speeddating.service.showunternehmen;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.repository.IUnternehmenRepository;

@Service
public class ShowUnternehmenImpl implements IShowUnternehmenService {

	@Autowired
	IUnternehmenRepository iUnternehmenRepository;
	
	@Override
	public List<Unternehmen> showUnternehmen() {
		return iUnternehmenRepository.findAll();
	}

	@Override
	public Unternehmen showEinUnternehmen(int unternehmen_id) {
		Optional<Unternehmen> gefundenesUnternehmen;
		try {
			gefundenesUnternehmen = iUnternehmenRepository.findById(unternehmen_id);
			return gefundenesUnternehmen.get();
		} catch (NoSuchElementException e) {
			System.out.println("Kein Studierender zu ID vorhanden!");
			return null;
		}
	}

}
