package de.swprojekt.speeddating.ui;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

@Route("start")
public class MainLayout extends VerticalLayout implements RouterLayout {	//stellt ParentLayout dar (implementiert RouterLayout)
	
	Div header=new Div();	
	Div childWrapper=new Div();	//in dieses Div werden Inhalte aus ChildLayouts eingefuegt (z.B. MainLayoutWithContent)
	Div footer=new Div();
    public MainLayout() {
    	setSizeFull();
    	header.setWidth("100%");
    	Button headerButton=new Button("Header");
    	headerButton.setWidth("100%");
    	header.add(headerButton);
    	
    	childWrapper.setWidth("100%");
    	
    	footer.setWidth("100%");
    	Button footerButton=new Button("Footer");
    	footerButton.setWidth("100%");
    	
    	footer.add(footerButton);
        add(header,childWrapper,footer);
    }
    @Override
	public void showRouterLayoutContent(HasElement content) {	//Methode legt fest, in welches Element Inhalte aus abgeleiteten Layouts eingefuegt werden
		System.out.println("zu childwrapper: "+childWrapper.getElement()+" wird jetzt hinzugefuegt: "+content.getElement());
		childWrapper.getElement().appendChild(content.getElement());	//In ChildWrapper wird was eingefuegt, header und footer bleiben unveraendert
	}
}
