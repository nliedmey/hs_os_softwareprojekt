package de.swprojekt.speeddating.service.unternehmen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.model.User;
import de.swprojekt.speeddating.repository.IUnternehmenRepository;
import de.swprojekt.speeddating.repository.IUserRepository;

@Service
public class UnternehmenImpl implements IUnternehmenService {

	@Autowired
	IUnternehmenRepository iUnternehmenRepository;
	
	@Autowired
	private IUserRepository iUserRepository;
	
	@Override
	public Unternehmen speicherUnternehmen(Unternehmen einUnternehmenDAO) {
		Unternehmen einUnternehmen = new Unternehmen();
		einUnternehmen.setUnternehmensname(einUnternehmenDAO.getUnternehmensname());
		einUnternehmen.setAnsprechpartner(einUnternehmenDAO.getAnsprechpartner());
		einUnternehmen.setKontaktmail(einUnternehmenDAO.getKontaktmail());
		return iUnternehmenRepository.save(einUnternehmen);
		
	}

	@Override
	public void deleteUnternehmen(Unternehmen einUnternehmenDAO) {
		Unternehmen einUnternehmen = new Unternehmen();
		
		einUnternehmen.setUnternehmen_id(einUnternehmenDAO.getUnternehmen_id());
		einUnternehmen.setUnternehmensname(einUnternehmenDAO.getUnternehmensname());
		
		String username = einUnternehmen.getUnternehmensname() + "*" + einUnternehmen.getUnternehmen_id();
		User gefundenerUser=iUserRepository.findByUsername(username);
		if (gefundenerUser!= null) {
			iUserRepository.delete(gefundenerUser);
		}

		iUnternehmenRepository.deleteById(einUnternehmen.getUnternehmen_id());
		
	}

	@Override
	public void changeUnternehmen(Unternehmen einUnternehmenDAO) {
		iUnternehmenRepository.save(einUnternehmenDAO);
		
	}

}
