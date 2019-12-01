package de.swprojekt.speeddating.service.showunternehmen;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
