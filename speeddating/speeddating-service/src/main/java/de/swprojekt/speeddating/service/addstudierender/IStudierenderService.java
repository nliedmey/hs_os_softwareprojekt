package de.swprojekt.speeddating.service.addstudierender;

import java.util.Optional;

import de.swprojekt.speeddating.model.Studierender;

public interface IStudierenderService {
	public Studierender saveStudierenden(Studierender einStudierender);
	public void changeStudierenden(Studierender einStudierender);
	public void deleteStudierenden(Studierender einStudierender);

}
