package de.ketobi.vaadinspringdemo.main.workflows;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.*;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.components.CreateWorkflowNodeDiv;
import de.ketobi.vaadinspringdemo.main.workflows.components.ScheduleWorkflowsDialog;
import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowService;
import de.ketobi.vaadinspringdemo.main.workflows.viewer.WorkflowView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route(value = "workfloweditor", layout = MainLayout.class)
@PageTitle("Workflow editor")
public class WorkflowEditor extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {
    private final WorkflowService wfService;
    private final WorkflowNodeService wfNodeService;
    private final UserService userService;
    private Workflow workFlow;
    private String idWorkflow;
    private Div nodeDiv = new Div();
    private Div treeDiv = new Div();
    private Select<WorkflowNode> editNodeSelect = new Select<>();

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        }
    }
    @Autowired
    public WorkflowEditor(WorkflowService wfService, WorkflowNodeService wfNodeService, UserService userService){
        this.wfService = wfService;
        this.wfNodeService = wfNodeService;
        this.userService = userService;
        add(new H3("Workflow editor"));
        add(new Paragraph("Edit a workflow and its workflow nodes."));
        add(nodeDiv);
        add(treeDiv);
    }
    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.idWorkflow = parameter;
        this.workFlow = wfService.getById(idWorkflow);
        fillNodeDiv();
        drawWorkflow();
    }

    private void fillNodeDiv(){
        nodeDiv.add(new Paragraph("Active: "+workFlow.isActive()));
        nodeDiv.add(new Paragraph("Name: "+workFlow.getName()));
        nodeDiv.add(new Paragraph("Description: "+workFlow.getDescription()));
        nodeDiv.add(new Paragraph("Select here if the workflow is scheduled or event driven: "));
        //TODO if a workflow is not scheduled anymore the schedule must be deleted!
        nodeDiv.add(new Button("Set Scheduled", e -> {
            ScheduleWorkflowsDialog dialog = new ScheduleWorkflowsDialog();
            dialog.open();
        }));
        HorizontalLayout editNodes = new HorizontalLayout();
        editNodes.add(new Paragraph("Edit nodes: "));
        editNodeSelect.setItems(wfNodeService.getAllWithoutStartAndEnd(workFlow.getId()));
        editNodeSelect.setEmptySelectionAllowed(true);
        editNodes.add(editNodeSelect);
        editNodes.add(new EditNodeButton());
        nodeDiv.add(editNodes);
        nodeDiv.add(new Html("<HR>"));
        nodeDiv.add(new CreateWorkflowNodeDiv(workFlow, wfNodeService, userService, this::drawWorkflow));
    }

    public void drawWorkflow(){
        treeDiv.removeAll();
        List<WorkflowNode> nodes = wfNodeService.getAll(workFlow.getId());
        treeDiv.add(new WorkflowView(nodes));
    }

    private class EditNodeButton extends Button {
        EditNodeButton(){
            setText("Edit");
            addClickListener(clickEvent -> {
                WorkflowNode node = editNodeSelect.getValue();
                if(node!=null){
                    Dialog editNodeDialog = new Dialog();
                    VerticalLayout editNodeLayout = new VerticalLayout();
                    editNodeLayout.add(new Paragraph("Edit node: "+node.getTitle()));
                    editNodeLayout.add(new Paragraph("ID: "+node.getId()));
                    editNodeLayout.add(new Paragraph("Type: "+node.getType()));
                    Button closeButton = new Button(new Icon("lumo", "cross"),
                            (e) -> editNodeDialog.close());
                    closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    editNodeDialog.getHeader().add(closeButton);

                    Select<User> responsible = new Select<>();
                    responsible.setLabel("Responsible");
                    ArrayList<User> users = new ArrayList<>();
                    users.add(UserService.getEntityCreator());
                    users.addAll(userService.getAll());
                    responsible.setItems(users);
                    responsible.setItemLabelGenerator(User::getName);
                    if(node.getResponsible()!=null) {
                        if (node.getResponsible().equals(UserService.getEntityCreator().getId())) {
                            responsible.setValue(UserService.getEntityCreator());
                        } else {
                            responsible.setValue(userService.getUserById(node.getResponsible()));
                        }
                    }
                    Button saveButton = new Button("Save", e -> {
                        if(responsible.getValue()!=null) {
                            node.setResponsible(responsible.getValue().getId());
                        }
                        wfNodeService.save(node);
                        editNodeDialog.close();
                    });
                    if(node.getType().equals(WorkflowNodeTypes.USER_ACTION) || node.getType().equals(WorkflowNodeTypes.USER_DECISION)) {
                        editNodeLayout.add(responsible);
                    }
                    editNodeDialog.add(editNodeLayout);
                    editNodeDialog.getFooter().add(saveButton);
                    editNodeDialog.open();
                }
            });
        }
    }
}
