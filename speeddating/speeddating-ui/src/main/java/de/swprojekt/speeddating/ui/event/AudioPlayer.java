package de.swprojekt.speeddating.ui.event;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
/*
 * Zusaetzliche selbst erstellte Vaadin-Komponente zur Audioausgabe des Signaltons
 */
@Tag("audio") //Klasse Audio wird genutzt
public class AudioPlayer  extends Component {
	
    public AudioPlayer(){

    }
    @SuppressWarnings("deprecation")
	public void play()
    {
    	System.out.println("Ton muesste kommen");
   	getElement().callFunction("play"); //Ton abspielen
    }
    
    public  void setSource(String path){
    	getElement().setProperty("src",path); //Quelle des Tons setzen
    }
}