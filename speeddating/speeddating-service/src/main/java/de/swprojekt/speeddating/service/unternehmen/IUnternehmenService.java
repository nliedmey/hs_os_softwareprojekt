package de.swprojekt.speeddating.service.unternehmen;

import de.swprojekt.speeddating.model.Unternehmen;

public interface IUnternehmenService {

	public Unternehmen speicherUnternehmen(Unternehmen einUnternehmen);
	public void deleteUnternehmen(Unternehmen einUnternehmen);
	public void changeUnternehmen(Unternehmen einUnternehmen);
	
}
