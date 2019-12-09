package de.swprojekt.speeddating.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
/*
 * Simple Standardansicht mit autogewireter Bean aus MessageBean
 */
@Route
public class MainView extends VerticalLayout {
	
	@Autowired MessageBean bean;
    public MainView() {
        Button button = new Button("Klick mich :p",
                e -> Notification.show(bean.getMessage()));
        add(button);
    }
}
