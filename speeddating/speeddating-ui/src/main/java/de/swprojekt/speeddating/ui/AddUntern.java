package de.swprojekt.speeddating.ui;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

import de.swprojekt.speeddating.model.Unternehmen;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;
import de.swprojekt.speeddating.service.unternehmen.IUnternehmenService;

/*
 * View zum Anlegen von neuen Unternehmen
 */
@Route(value = "ui/untern/add", layout = MainLayout.class)
public class AddUntern extends VerticalLayout {

	@Autowired
	public AddUntern(IUnternehmenService iUnternehmenService, IShowUnternehmenService iShowUnternehmenService) {
		
		//Deklaration
		Binder<Unternehmen> binder;
		
		//Erzeugen der Input Felder
		TextField textfieldUnternehmensname = new TextField("Unternehmensname:");
		TextField textfieldAnsprechpartner = new TextField("Ansprechpartner:");
		TextField textfieldKontaktmail = new TextField("Kontakt-EMail:");
		
		//Button hinzufuegen
		Button buttonHinzufuegen = new Button("Unternehmen anlegen");
		buttonHinzufuegen.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		Button buttonAbbrechen = new Button("Abbrechen");
		buttonAbbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);
		
		//Notification
		Notification notificationSavesuccess = new Notification();
		notificationSavesuccess.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		Label labelSavesuccess = new Label("Unternehmen erfolgreich hinzugefuegt!");
		notificationSavesuccess.add(labelSavesuccess);
		
		VerticalLayout h1 = new VerticalLayout();
		h1.add(textfieldUnternehmensname);
		h1.add(textfieldAnsprechpartner);
		h1.add(textfieldKontaktmail);
		
		add(h1, buttonHinzufuegen, buttonAbbrechen);
		
		binder = new Binder<>(Unternehmen.class);
		
		binder.forField(textfieldUnternehmensname).asRequired("Unternehmensname darf nicht leer sein").bind("unternehmensname");
		binder.forField(textfieldAnsprechpartner).asRequired("Ansprechpartner darf nicht leer sein").bind("Ansprechpartner");
		binder.forField(textfieldKontaktmail).asRequired("Kontaktmail darf nicht leer sein").bind("kontaktmail");
		
		Unternehmen einUnternehmen = new Unternehmen();
		
		buttonHinzufuegen.addClickListener(event -> {
			try {
				binder.writeBean(einUnternehmen);
				iUnternehmenService.speicherUnternehmen(einUnternehmen);
				notificationSavesuccess.open();
				
				buttonHinzufuegen.getUI().ifPresent(ui->ui.navigate("maincontent"));
			} catch(ValidationException | com.vaadin.flow.data.binder.ValidationException e) {
				e.printStackTrace();
			}
		});
		
		buttonAbbrechen.addClickListener(event -> {
			SecurityContextHolder.clearContext();	//Spring-Security-Session leeren
			getUI().get().getSession().close();		//Vaadin Session leeren
			buttonHinzufuegen.getUI().ifPresent(ui->ui.navigate("maincontent"));
		});
	}
}
