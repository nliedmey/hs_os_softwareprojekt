package de.swprojekt.speeddating.ui;


import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
/*
 * Listener legt Verhalten bei nicht-vorhandenen Rechten fuer Viewzugriff fest,
 * Dies ist vor allem fuer die Pruefung bei Aufruf von RouterLinks wichtig
 * Ergaenzt das Standardverhalten von SpringSecurity, welches die Rechte bei Vaadin-RouterLinks nicht erneut prueft
 * Quelle: https://vaadin.com/tutorials/securing-your-app-with-spring-security/fine-grained-access-control
 */

@Component 
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener { //Spring registriert Listener global bei allen UI-Instanzen

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
        final UI ui = uiEvent.getUI();
        ui.addBeforeEnterListener(this::beforeEnter); //Vor dem Betreten von View wird beforeEnter-Methode ausgefuehrt 
        });
    }

    private void beforeEnter(BeforeEnterEvent event) {
    	System.out.println("beforeEnter betreten");
        if(!SecurityUtils.isAccessGranted(event.getNavigationTarget())) { //wenn Zugriffsrechte fuer Ziel nicht gewaehrt
            if(SecurityUtils.isUserLoggedIn()) { //wenn keine Rechte, aber eingeloggt 
                event.rerouteTo(ZugangVerwehrt.class); //View verbergen durch Vorgeben von NotFound (Sicherheitsaspekt) 
            } else {
                event.rerouteTo(Login.class); //wenn nicht eingeloggt und Recht aktuell nicht vorhanden, dann Rueckleitung zu Loginview
            }
        }
    }
}
