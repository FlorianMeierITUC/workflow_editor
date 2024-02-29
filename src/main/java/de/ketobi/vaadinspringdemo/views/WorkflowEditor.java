package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "workfloweditor")
@PageTitle("Workflow editor")
public class WorkflowEditor extends VerticalLayout {

    public WorkflowEditor(){
        add(new H3("Workflow editor"));
        add(new Paragraph("Edit a workflow and its workflow nodes."));

    }
}
