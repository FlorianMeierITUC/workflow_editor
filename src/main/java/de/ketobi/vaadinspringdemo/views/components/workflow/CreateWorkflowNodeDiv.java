package de.ketobi.vaadinspringdemo.views.components.workflow;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.Div;
import de.ketobi.vaadinspringdemo.entities.Workflow;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.entities.WorkflowNodeTypes;
import de.ketobi.vaadinspringdemo.repositories.WorkflowNodeRepository;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CreateWorkflowNodeDiv extends Div{
    private Workflow workFlow;
    private WorkflowNodeRepository wfNodeRepository;
    private TextField title = new TextField("Title *");
    private Select<WorkflowNodeTypes> type = new Select<>();
    private TextField executorClass = new TextField("Class");
    private TextField responsible = new TextField("Responsible");
    private Select<WorkflowNode> predecessor = new Select<>();
    private Select<WorkflowNode> successor = new Select<>();

    public CreateWorkflowNodeDiv(Workflow wf, WorkflowNodeRepository wfNodeRepository){
        this.workFlow = wf;
        this.wfNodeRepository = wfNodeRepository;

        executorClass.setVisible(false);
        responsible.setVisible(false);
        predecessor.setVisible(false);
        successor.setVisible(false);

        predecessor.setLabel("Predecessor node");
        predecessor.setItems(wfNodeRepository.findByIdWorkflow(workFlow.getId()));
        predecessor.setItemLabelGenerator(node -> node == null ? "" : node.getTitle() + " (" + node.getType() + ")");
        predecessor.setEmptySelectionAllowed(true);

        successor.setLabel("Successor node");
        successor.setItems(wfNodeRepository.findByIdWorkflow(workFlow.getId()));
        successor.setItemLabelGenerator(node -> node == null ? "" : node.getTitle() + " (" + node.getType() + ")");
        successor.setEmptySelectionAllowed(true);

        type.setLabel("Type *");
        type.setItems(Arrays.stream(WorkflowNodeTypes.values())
                .filter(e -> e != WorkflowNodeTypes.START)
                .collect(Collectors.toList()));
        type.addValueChangeListener(event -> {
            WorkflowNodeTypes selectedType = event.getValue();
            switch (selectedType) {
                case END:
                    executorClass.setVisible(false);
                    responsible.setVisible(false);
                    predecessor.setVisible(true);
                    successor.setVisible(false);
                    break;
                case OR:
                case AND:
                case UNION:
                    executorClass.setVisible(false);
                    responsible.setVisible(false);
                    predecessor.setVisible(true);
                    successor.setVisible(true);
                    break;
                case USER_DECISION:
                case USER_ACTION:
                    executorClass.setVisible(true);
                    responsible.setVisible(true);
                    predecessor.setVisible(true);
                    successor.setVisible(true);
                    break;
                case BATCH_DECISION:
                case BATCH_ACTION:
                    executorClass.setVisible(true);
                    responsible.setVisible(false);
                    predecessor.setVisible(true);
                    successor.setVisible(true);
                    break;
                default:
                    executorClass.setVisible(false);
                    responsible.setVisible(false);
                    predecessor.setVisible(false);
                    successor.setVisible(false);
                    break;
            }
        });
        add(title);
        add(type);
        add(executorClass);
        add(responsible);
        add(predecessor);
        add(successor);
        add(new SaveNodeButton());
    }

    class SaveNodeButton extends Button {
        public SaveNodeButton(){
            setText("+ Create node");
            addClickListener(e -> {
                WorkflowNode node = new WorkflowNode();
                node.setIdWorkflow(workFlow.getId());
                node.setTitle(title.getValue());
                node.setType(type.getValue());
                node.setExecutorClass(executorClass.getValue());
                node.setResponsible(responsible.getValue());
                ArrayList<ObjectId> predecessors = new ArrayList<>();
                predecessors.add(predecessor.getValue().getId());
                node.setPredecessorNodes(predecessors);
                ArrayList<ObjectId> successors = new ArrayList<>();
                successors.add(successor.getValue().getId());
                node.setSuccessorNodes(successors);
                wfNodeRepository.save(node);
                Notification notification = Notification
                        .show("Workflow node created!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            });
        }
    }
}