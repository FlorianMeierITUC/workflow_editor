package de.ketobi.vaadinspringdemo.views.components.workflow;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

import static de.ketobi.vaadinspringdemo.entities.WorkflowNodeTypes.*;

public class CreateWorkflowNodeDiv extends Div{
    private Workflow workFlow;
    private WorkflowNodeRepository wfNodeRepository;
    private TextField title = new TextField("Title *");
    private Select<WorkflowNodeTypes> type = new Select<>();
    private HorizontalLayout nodeDetailsInput = new HorizontalLayout();
    private TextField executorClass = new TextField("Class");
    private TextField responsible = new TextField("Responsible");
    private Select<WorkflowNode> predecessor = new Select<>();
    private Select<WorkflowNode> successor = new Select<>();
    private Runnable drawWorkflow;

    public CreateWorkflowNodeDiv(Workflow wf, WorkflowNodeRepository wfNodeRepository, Runnable drawWorkflow){
        this.workFlow = wf;
        this.wfNodeRepository = wfNodeRepository;
        this.drawWorkflow = drawWorkflow;

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
                .filter(e -> e != START)
                .collect(Collectors.toList()));
        type.addValueChangeListener(event -> {
            WorkflowNodeTypes selectedType = event.getValue();
            nodeDetailsInput.removeAll();
            nodeDetailsInput.add(createNodeDetailsInput(selectedType));
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
        add(nodeDetailsInput);
        add(executorClass);
        add(responsible);
        add(predecessor);
        add(successor);
        add(new SaveNodeButton());
    }

    private Div createNodeDetailsInput(WorkflowNodeTypes type){
        Div div = new Div();
        switch (type){
            case END:
            case OR:
            case AND:
            case UNION:
            case USER_DECISION:
                div.add(executorClass);
                div.add(responsible);
                break;
            case USER_ACTION:
                div.add(executorClass);
                div.add(responsible);
                break;
            case BATCH_DECISION:
                div.add(executorClass);
                break;
            case BATCH_ACTION:
                div.add(executorClass);
                break;
            default:
                break;
        }
        return div;
    }

    class SaveNodeButton extends Button {
        public SaveNodeButton(){
            setText("+ Create node");
            addClickListener(e -> {
                if(title.getValue().isEmpty()){
                    Notification notification = Notification
                            .show("Title is required!");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }
                if(type.getValue() == null){
                    Notification notification = Notification
                            .show("Type is required!");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }
                WorkflowNode node = new WorkflowNode();
                node.setIdWorkflow(workFlow.getId());
                node.setTitle(title.getValue());
                node.setType(type.getValue());
                node.setExecutorClass(executorClass.getValue());
                node.setResponsible(responsible.getValue());

                //can be null! Rework the whole way of entering and saving nodes
                ArrayList<ObjectId> predecessors = new ArrayList<>();
                if(predecessor.getValue() != null){
                    predecessors.add(predecessor.getValue().getId());
                }
                node.setPredecessorNodes(predecessors);

                ArrayList<ObjectId> successors = new ArrayList<>();
                if(successor.getValue() != null){
                    successors.add(successor.getValue().getId());
                }
                node.setSuccessorNodes(successors);

                wfNodeRepository.save(node);
                //set the created node as the successor of the predecessor nodes (if any)
                //predecessors are not allowed to have another successor (except OR and AND nodes)
                //END nodes are not allowed to have a successor
                if(predecessor.getValue() != null){
                    WorkflowNode predecessorNode = predecessor.getValue();
                    switch (node.getType()){
                        case START:
                        case UNION:
                        case USER_ACTION:
                        case BATCH_ACTION:
                            //remove other successors from the predecessor node and set the new node as the only successor
                            predecessorNode.getSuccessorNodes().clear();
                            predecessorNode.getSuccessorNodes().add(node.getId());
                            break;
                        case OR:
                        case AND:
                            //Add the new node as a successor to the predecessor nodes
                            predecessorNode.getSuccessorNodes().add(node.getId());
                            break;
                        case USER_DECISION:
                        case BATCH_DECISION:
                            //TODO set the new node either as the success or failure node of the predecessor node
                            break;
                        default:
                            break;
                    }
                    wfNodeRepository.save(predecessorNode);
                }
                //set the created node as the predecessor of the successor nodes (if any)
                if(successor.getValue() != null){
                    WorkflowNode successorNode = successor.getValue();
                    switch (node.getType()){
                        case END:
                        case USER_ACTION:
                        case BATCH_ACTION:
                        case OR:
                        case AND:
                        case USER_DECISION:
                        case BATCH_DECISION:
                            //set the new node as the only predecessor of the successor node
                            successorNode.getPredecessorNodes().clear();
                            successorNode.getPredecessorNodes().add(node.getId());
                            break;
                        case UNION:
                            //add the new node to the predecessors of the successor node
                            successorNode.getPredecessorNodes().add(node.getId());
                            break;
                        default:
                            break;
                    }
                    wfNodeRepository.save(successorNode);
                }
                Notification notification = Notification
                        .show("Workflow node created!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                drawWorkflow.run();
            });
        }
    }
}