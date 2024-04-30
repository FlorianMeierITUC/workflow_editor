package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.entities.Workflow;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.repositories.WorkflowNodeRepository;
import de.ketobi.vaadinspringdemo.repositories.WorkflowRepository;
import de.ketobi.vaadinspringdemo.views.components.workflow.CreateWorkflowNodeDiv;
import de.ketobi.vaadinspringdemo.views.components.workflow.viewer.Canvas;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Route(value = "workfloweditor", layout = MainLayout.class)
@PageTitle("Workflow editor")
public class WorkflowEditor extends VerticalLayout implements HasUrlParameter<String> {
    private Workflow workFlow;
    private String idWorkflow;
    private WorkflowRepository wfRepository;
    private WorkflowNodeRepository wfNodeRepository;
    private Div nodeDiv = new Div();
    private Div treeDiv = new Div();

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
            nodeDiv.add(new Paragraph(node.getTitle() + " - " + node.getType()));
        }
        nodeDiv.add(new Html("<HR>"));
        nodeDiv.add(new CreateWorkflowNodeDiv(workFlow, wfNodeRepository));
    }

    private void drawWorkflow(){
        treeDiv.removeAll();
        List<WorkflowNode> nodes = wfNodeRepository.findByIdWorkflow(workFlow.getId());
        Canvas workflowView = new Canvas(nodes);
        treeDiv.add(workflowView);
    }
}
