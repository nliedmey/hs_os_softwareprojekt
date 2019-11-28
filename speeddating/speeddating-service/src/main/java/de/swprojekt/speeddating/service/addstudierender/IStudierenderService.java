package de.swprojekt.speeddating.service.addstudierender;

import de.swprojekt.speeddating.model.Studierender;

public interface IStudierenderService {
	public void speicherStudierenden(Studierender einStudierender);
	public void deleteStudierenden(Studierender einStudierender);
	public void changeStudierenden(Studierender einStudierender);
}
