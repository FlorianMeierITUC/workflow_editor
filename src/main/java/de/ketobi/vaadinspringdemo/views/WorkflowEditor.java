package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.svg.Svg;
import com.vaadin.flow.component.svg.elements.Circle;
import com.vaadin.flow.component.svg.elements.Rect;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import de.ketobi.vaadinspringdemo.entities.Workflow;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.repositories.WorkflowNodeRepository;
import de.ketobi.vaadinspringdemo.repositories.WorkflowRepository;
import de.ketobi.vaadinspringdemo.views.components.workflow.CreateWorkflowNodeDiv;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@Route(value = "workfloweditor", layout = MainLayout.class)
@PageTitle("Workflow editor")
public class WorkflowEditor extends VerticalLayout implements HasUrlParameter<String> {
    private Workflow workFlow;
    private String idWorkflow;
    private WorkflowRepository wfRepository;
    private WorkflowNodeRepository wfNodeRepository;
    private Div nodeDiv = new Div();
    private Div treeDiv = new Div();
    private TextField title = new TextField("Title");
    private TextField type = new TextField("Type");
    private TextField executorClass = new TextField("Class");
    private TextField responsible = new TextField("Responsible");
    private TextField predecessor = new TextField("Predecessor node", "START");
    private TextField successor = new TextField("Successor node", "END");

    @Autowired
    public WorkflowEditor(WorkflowRepository wfRepository, WorkflowNodeRepository wfNodeRepository){
        this.wfRepository = wfRepository;
        this.wfNodeRepository = wfNodeRepository;
        add(new H3("Workflow editor"));
        add(new Paragraph("Edit a workflow and its workflow nodes."));
        add(nodeDiv);
        add(treeDiv);
    }
    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.idWorkflow = parameter;
        this.workFlow = wfRepository.findById(idWorkflow).get();
        fillNodeDiv();
        drawWorkflow();
    }

    private void fillNodeDiv(){
        nodeDiv.add(new Paragraph("Active: "+workFlow.isActive()));
        nodeDiv.add(new Paragraph("Name: "+workFlow.getName()));
        nodeDiv.add(new Paragraph("Description: "+workFlow.getDescription()));
        nodeDiv.add(new Paragraph("Select here if the workflow is scheduled or event driven: "+workFlow.getName()));
        nodeDiv.add(new Paragraph("Nodes:"));
        for (WorkflowNode node : wfNodeRepository.findByIdWorkflow(workFlow.getId())){
            nodeDiv.add(new Paragraph(node.getTitle()));
        }
        nodeDiv.add(new Html("<HR>"));
        nodeDiv.add(new CreateWorkflowNodeDiv(workFlow, wfNodeRepository));
    }

    private void drawWorkflow(){
        treeDiv.removeAll();
        Svg draw = new Svg();
        Rect rect = new Rect("rect", 100, 100);
        Circle circle = new Circle("circle", 50);

        rect.move(75, 0);
        rect.size(150, 150);

        circle.center(150, 75);
        circle.setRadius(75);
        circle.setFillColor("#396");

        draw.add(rect);
        draw.add(circle);
        treeDiv.add(draw);
    }

}
