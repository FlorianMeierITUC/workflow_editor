package de.ketobi.vaadinspringdemo.main.workflows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.components.CreateWorkflowNodeDiv;
import de.ketobi.vaadinspringdemo.main.workflows.components.ScheduleWorkflowsDialog;
import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes;
import de.ketobi.vaadinspringdemo.main.workflows.services.BeanLister;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowScheduleService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowService;
import de.ketobi.vaadinspringdemo.main.workflows.viewer.WorkflowView;

@Route(value = "workfloweditor", layout = MainLayout.class)
@PageTitle("Workflow editor")
public class WorkflowEditor extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {
    private final WorkflowService wfService;
    private final WorkflowNodeService wfNodeService;
    private final UserService userService;
    private final WorkflowScheduleService workflowScheduleService;
    private Workflow workFlow;
    private String idWorkflow;
    private Div nodeDiv = new Div();
    private Div treeDiv = new Div();
    private Select<WorkflowNode> editNodeSelect = new Select<>();
    private Paragraph scheduleInfo = new Paragraph();

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (UserService.getCurrentUser() == null) {
            event.forwardTo(Login.class);
        }
    }

    @Autowired
    public WorkflowEditor(WorkflowService wfService, WorkflowNodeService wfNodeService, UserService userService,
            WorkflowScheduleService workflowScheduleService) {
        this.wfService = wfService;
        this.wfNodeService = wfNodeService;
        this.userService = userService;
        this.workflowScheduleService = workflowScheduleService;
        add(new H3("Workflow editor"));
        add(new H4("Edit a workflow and its workflow nodes."));
        add(nodeDiv);
        add(treeDiv);

        Button validateWorkflowButton = new Button("Validate Workflow", event -> validateWorkflow());
        add(validateWorkflowButton);
    }

    private void validateWorkflow() {
        boolean nodesValid = false;
        try {
            List<WorkflowNode> nodes = wfNodeService.getAll(workFlow.getId());
            List<WorkflowNode> batchNodes = wfNodeService.getAllBatchNodes(workFlow.getId());

            nodesValid = validateNodes(nodes) && allBatchNodesImplemented(batchNodes);

            // boolean allAndNodesEndInUnionNode = validateAndNodeEndsInUnionNode(nodes);
            // System.out.println(allAndNodesEndInUnionNode);

        } catch (Exception e) {
            System.out.println("Exception");
        }

        // nofify user
        if (nodesValid) {
            Notification.show("Workflow is valid");
        } else {
            Notification.show("Workflow is not valid");
        }

        // System.out.println("Batch nodes" + batchNodes);

    }

    private boolean allBatchNodesImplemented(List<WorkflowNode> batchNodes) {

        String[] beanNames = new BeanLister().listAllBeans();
        List<String> beanNamesList = Arrays.asList(beanNames);

        for (WorkflowNode batchNode : batchNodes) {
            System.out.println("Batch node: " + batchNode.getId().toString());
            if (!beanNamesList.contains(batchNode.getId().toString())) {
                Notification.show("Batch action not found: " + batchNode.getTitle());
            }
        }
        return true;
    }

    private boolean checkSuccessorForUnionNode(ObjectId node) {
        WorkflowNode successorNode = wfNodeService.getById(node);
        if (successorNode.getType().equals(WorkflowNodeTypes.UNION)) {
            return true;
        }

        for (ObjectId successor : successorNode.getSuccessorNodes()) {
            if (checkSuccessorForUnionNode(successor)) {
                return true;
            }
        }
        return false;
    }

    public boolean validateAndNodeEndsInUnionNode(WorkflowNode startNode) {

        for (ObjectId successor : startNode.getSuccessorNodes()) {
            if (!checkSuccessorForUnionNode(successor)) {
                return false;
            }
        }
        return true;

    }

    private boolean validateNodes(List<WorkflowNode> nodes) {

        for (int i = 0; i < nodes.size(); i++) {
            WorkflowNode node = nodes.get(i);
            if (!node.validateNode()) {
                return false;
            }

            if (node.getType() == WorkflowNodeTypes.AND) {
                if (!validateAndNodeEndsInUnionNode(node)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.idWorkflow = parameter;
        this.workFlow = wfService.getById(idWorkflow);
        fillNodeDiv();
        drawWorkflow();
    }

    private void fillNodeDiv() {
        nodeDiv.add(new HtmlComponent("br"));
        nodeDiv.add(new H5("Workflow details"));
        nodeDiv.add(new Paragraph("ID: " + workFlow.getId()));
        nodeDiv.add(new Paragraph("Active: " + workFlow.isActive()));
        nodeDiv.add(new Paragraph("Name: " + workFlow.getName()));
        nodeDiv.add(new Paragraph("Description: " + workFlow.getDescription()));
        nodeDiv.add(new HtmlComponent("br"));
        nodeDiv.add(new Hr());
        nodeDiv.add(new HtmlComponent("br"));
        nodeDiv.add(new H5("Workflow scheduling"));
        nodeDiv.add(new Paragraph("Select here if the workflow is scheduled or event driven: "));
        nodeDiv.add(scheduleInfo);
        // TODO add a button div to update after a schedule was created and switch from
        // "Set schedule" to "Edit schedule"
        nodeDiv.add(new Button("Edit schedule", e -> {
            ScheduleWorkflowsDialog dialog = new ScheduleWorkflowsDialog(workFlow.getId(), workflowScheduleService,
                    this::updateScheduleInfo);
            dialog.open();
        }));
        if (workflowScheduleService.workflowIsScheduled(workFlow.getId())) {
            scheduleInfo
                    .setText("Scheduled: " + workflowScheduleService.get(workFlow.getId()).getPattern().toString());
            nodeDiv.add(new Button("Remove Schedule", e -> {
                workflowScheduleService.delete(workFlow.getId());
                scheduleInfo.setText("Event driven: This workflow will be started by a user.");
            }));
        } else {
            scheduleInfo.setText("Event driven: This workflow will be started by a user.");
        }
        nodeDiv.add(new HtmlComponent("br"));
        nodeDiv.add(new HtmlComponent("br"));
        nodeDiv.add(new Hr());
        nodeDiv.add(new HtmlComponent("br"));
        nodeDiv.add(new H5("Workflow nodes configuration"));
        HorizontalLayout editNodes = new HorizontalLayout();
        editNodes.add(new Paragraph("Edit nodes: "));
        editNodeSelect.setItems(wfNodeService.getAllWithoutStartAndEnd(workFlow.getId()));
        editNodeSelect.setEmptySelectionAllowed(true);
        editNodes.add(editNodeSelect);
        editNodes.add(new EditNodeButton());
        nodeDiv.add(editNodes);
        nodeDiv.add(new CreateWorkflowNodeDiv(workFlow, wfNodeService, userService, this::drawWorkflow,
                this::refreshEditNodeSelectItems));
    }

    public void drawWorkflow() {
        treeDiv.removeAll();
        List<WorkflowNode> nodes = wfNodeService.getAll(workFlow.getId());
        treeDiv.add(new WorkflowView(nodes));
    }

    private class EditNodeButton extends Button {
        EditNodeButton() {
            setText("Edit");
            addClickListener(clickEvent -> {
                WorkflowNode node = editNodeSelect.getValue();
                if (node != null) {
                    Dialog editNodeDialog = new Dialog();
                    VerticalLayout editNodeLayout = new VerticalLayout();
                    editNodeLayout.add(new Paragraph("Edit node: " + node.getTitle()));
                    editNodeLayout.add(new Paragraph("ID: " + node.getId()));
                    editNodeLayout.add(new Paragraph("Type: " + node.getType()));
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
                    if (node.getResponsible() != null) {
                        if (node.getResponsible().equals(UserService.getEntityCreator().getId())) {
                            responsible.setValue(UserService.getEntityCreator());
                        } else {
                            responsible.setValue(userService.getUserById(node.getResponsible()));
                        }
                    }
                    Button saveButton = new Button("Save", e -> {
                        if (responsible.getValue() != null) {
                            node.setResponsible(responsible.getValue().getId());
                        }
                        wfNodeService.save(node);
                        editNodeDialog.close();
                        refreshEditNodeSelectItems();

                    });
                    if (node.getType().equals(WorkflowNodeTypes.USER_ACTION)
                            || node.getType().equals(WorkflowNodeTypes.USER_DECISION)) {
                        editNodeLayout.add(responsible);
                    }
                    editNodeDialog.add(editNodeLayout);
                    editNodeDialog.getFooter().add(saveButton);
                    editNodeDialog.open();
                }
            });
        }
    }

    public void updateScheduleInfo() {
        scheduleInfo.setText("Scheduled: " + workflowScheduleService.get(workFlow.getId()).getPattern().toString());
    }

    public void refreshEditNodeSelectItems() {
        editNodeSelect.setItems(wfNodeService.getAllWithoutStartAndEnd(workFlow.getId()));
    }
}
