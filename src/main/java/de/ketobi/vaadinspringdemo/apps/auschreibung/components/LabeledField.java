package de.ketobi.vaadinspringdemo.apps.auschreibung.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasPlaceholder;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LabeledField extends VerticalLayout {

    public LabeledField(String label, Component field) {
        this(label, field, null);
    }

    public LabeledField(String label, Component field, String placeholder) {
        setSpacing(false);
        setPadding(false);
        setMargin(false);
        setWidthFull();
        
        if (field instanceof HasSize) {
            ((HasSize) field).setWidthFull(); // Ensures the input takes full width inside the column
        }
        H3 fieldLabel = new H3(label);
        fieldLabel.getStyle()
                .set("margin", "2rem 0 0.25em 0")
                .set("font-size", "1rem");

        if (placeholder != null && field instanceof HasPlaceholder) {
            ((HasPlaceholder) field).setPlaceholder(placeholder);
        }

        // Styling the field
        // field.getElement().getStyle()
        //         .set("background-color", "#e3f2fd") // light blue
        //         // .set("border-radius", "1px")
        //         // .set("padding", "0.5rem")
        //         // .set("min-height", "3rem") 
        //         .set("font-size", "1rem");

        if (field instanceof HasStyle) {
            ((HasStyle) field).addClassName("form-field");
        }

        add(fieldLabel, field);
    }
}
