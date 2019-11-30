package de.swprojekt.speeddating.service.unternehmen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.repository.IUnternehmenRepository;

@Service
public class UnternehmenImpl implements IUnternehmenService {

	@Autowired
	IUnternehmenRepository iUnternehmenRepository;
	
	@Override
	public void speicherUnternehmen(Unternehmen einUnternehmenDAO) {
		Unternehmen einUnternehmen = new Unternehmen();
		einUnternehmen.setUnternehmensname(einUnternehmenDAO.getUnternehmensname());
		einUnternehmen.setAnsprechpartner(einUnternehmenDAO.getAnsprechpartner());
		einUnternehmen.setKontaktmail(einUnternehmenDAO.getKontaktmail());
		iUnternehmenRepository.save(einUnternehmen);
		
	}

	@Override
	public void deleteUnternehmen(Unternehmen einUnternehmenDAO) {
		Unternehmen einUnternehmen = new Unternehmen();
		einUnternehmen.setUnternehmen_id(einUnternehmenDAO.getUnternehmen_id());
		iUnternehmenRepository.deleteById(einUnternehmen.getUnternehmen_id());
		
	}

	@Override
	public void changeUnternehmen(Unternehmen einUnternehmenDAO) {
		iUnternehmenRepository.save(einUnternehmenDAO);
		
	}

}
