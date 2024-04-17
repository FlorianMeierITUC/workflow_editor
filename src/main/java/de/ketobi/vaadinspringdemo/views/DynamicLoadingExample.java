package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("dynamic")
public class DynamicLoadingExample extends VerticalLayout {
    public Div dynamicContent;
    public DynamicLoadingExample() {
        dynamicContent = new Div();
        Button loadContentButton = new Button("Load Content");
        loadContentButton.addClickListener(event -> loadDynamicContent());
        add(loadContentButton, dynamicContent);
    }

    public void loadDynamicContent() {
        try {
            Thread.sleep(2000); // Simulate delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dynamicContent.setText("This is dynamic content");
    }
}
