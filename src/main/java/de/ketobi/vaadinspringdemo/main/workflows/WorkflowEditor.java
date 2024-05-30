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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.repositories.UserRepository;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.components.CreateWorkflowNodeDiv;
import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowNodeRepository;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowRepository;
import de.ketobi.vaadinspringdemo.main.workflows.viewer.WorkflowView;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "workfloweditor", layout = MainLayout.class)
@PageTitle("Workflow editor")
public class WorkflowEditor extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {
    private Workflow workFlow;
    private String idWorkflow;
    private WorkflowRepository wfRepository;
    private WorkflowNodeRepository wfNodeRepository;
    private UserRepository userRepository;
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
    public WorkflowEditor(WorkflowRepository wfRepository, WorkflowNodeRepository wfNodeRepository, UserRepository userRepository){
        this.wfRepository = wfRepository;
        this.wfNodeRepository = wfNodeRepository;
        this.userRepository = userRepository;
        add(new H3("Workflow editor"));
        add(new Paragraph("Edit a workflow and its workflow nodes."));
        add(nodeDiv);
        add(treeDiv);
    }
    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.idWorkflow = parameter;
        this.workFlow = wfRepository.findById(new ObjectId(idWorkflow)).get();
        fillNodeDiv();
        drawWorkflow();
    }

    private void fillNodeDiv(){
        nodeDiv.add(new Paragraph("Active: "+workFlow.isActive()));
        nodeDiv.add(new Paragraph("Name: "+workFlow.getName()));
        nodeDiv.add(new Paragraph("Description: "+workFlow.getDescription()));
        nodeDiv.add(new Paragraph("Select here if the workflow is scheduled or event driven: "+workFlow.getName()));
        HorizontalLayout editNodes = new HorizontalLayout();
        editNodes.add(new Paragraph("Edit nodes: "));
        //find all nodes of this workflow except the START and END nodes
        editNodeSelect.setItems(wfNodeRepository.findByIdWorkflow(workFlow.getId()).stream().filter(node -> !node.getType().equals(WorkflowNodeTypes.START) && !node.getType().equals(WorkflowNodeTypes.END)).collect(Collectors.toList()));
        editNodeSelect.setEmptySelectionAllowed(true);
        editNodes.add(editNodeSelect);
        editNodes.add(new EditNodeButton());
        nodeDiv.add(editNodes);
        nodeDiv.add(new Html("<HR>"));
        nodeDiv.add(new CreateWorkflowNodeDiv(workFlow, wfNodeRepository, userRepository, this::drawWorkflow));
    }

    public void drawWorkflow(){
        treeDiv.removeAll();
        List<WorkflowNode> nodes = wfNodeRepository.findByIdWorkflow(workFlow.getId());
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
                    responsible.setItems(userRepository.findAll());
                    responsible.setItemLabelGenerator(User::getName);
                    if(node.getResponsible()!=null) {
                        responsible.setValue(userRepository.findById(node.getResponsible()).orElseThrow());
                    }
                    TextField executorClass = new TextField("Component");
                    executorClass.setValue(node.getExecutorClass());
                    Button saveButton = new Button("Save", e -> {
                        node.setExecutorClass(executorClass.getValue());
                        if(responsible.getValue()!=null) {
                            node.setResponsible(responsible.getValue().getId());
                        }
                        wfNodeRepository.save(node);
                        editNodeDialog.close();
                    });
                    if(node.getType().equals(WorkflowNodeTypes.BATCH_ACTION) || node.getType().equals(WorkflowNodeTypes.BATCH_DECISION)) {
                        editNodeLayout.add(executorClass);
                    }
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
