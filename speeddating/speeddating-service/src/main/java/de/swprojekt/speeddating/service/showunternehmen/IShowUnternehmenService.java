package de.swprojekt.speeddating.service.showunternehmen;

import java.util.List;


import de.swprojekt.speeddating.model.Unternehmen;

public interface IShowUnternehmenService {

	public List<Unternehmen> showUnternehmen();
	public Unternehmen showEinUnternehmen(int unternehmen_id);
	
}
