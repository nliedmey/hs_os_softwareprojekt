package de.swprojekt.speeddating.ui;

import java.time.LocalTime;
import org.springframework.stereotype.Service;
/*
 * Nur zum Testen von Autowire, keine Relevanz fuer Programm
 */
@Service
public class MessageBean {

    public String getMessage() {
        return "Button was clicked ummm genau" + LocalTime.now();
    }
}
