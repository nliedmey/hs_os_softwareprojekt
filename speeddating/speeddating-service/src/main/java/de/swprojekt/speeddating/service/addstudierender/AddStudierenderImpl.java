package de.swprojekt.speeddating.service.addstudierender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.repository.IStudierenderRepository;

@Service
public class AddStudierenderImpl implements IAddStudierenderService {

	@Autowired
	IStudierenderRepository iStudierenderRepository;
	
	@Override
	public void speicherStudierenden(Studierender einStudierenderDAO) {
		Studierender einStudierender=new Studierender();
		einStudierender.setVorname(einStudierenderDAO.getVorname());
		einStudierender.setNachname(einStudierenderDAO.getNachname());
		einStudierender.setHauptfach(einStudierenderDAO.getHauptfach());
		iStudierenderRepository.save(einStudierender);
	}

}
