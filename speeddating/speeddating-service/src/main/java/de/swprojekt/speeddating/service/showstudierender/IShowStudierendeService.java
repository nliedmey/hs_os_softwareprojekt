package de.swprojekt.speeddating.service.showstudierender;

import java.util.List;

import de.swprojekt.speeddating.model.Studierender;
/*
 * Interface gibt zu implementierende Methoden fuer ShowStudierendeImpl vor
 * In Views wird nur Interface eingebunden!
 */
public interface IShowStudierendeService {
	public List<Studierender> showStudierende();
	public Studierender showStudierenden(int stud_id);
}
