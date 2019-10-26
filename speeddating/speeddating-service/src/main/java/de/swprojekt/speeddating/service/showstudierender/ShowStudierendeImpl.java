package de.swprojekt.speeddating.service.showstudierender;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swprojekt.speeddating.model.Studierender;
import de.swprojekt.speeddating.repository.IStudierenderRepository;

@Service
public class ShowStudierendeImpl implements IShowStudierendeService {

	@Autowired
	IStudierenderRepository iStudierenderRepository;
	
	@Override
	public List<Studierender> showStudierende() {
		return iStudierenderRepository.findAll();
	}

}
