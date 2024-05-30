package de.ketobi.vaadinspringdemo.main.workflows.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.Div;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes;
import de.ketobi.vaadinspringdemo.main.user.repositories.UserRepository;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowNodeRepository;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes.*;

public class CreateWorkflowNodeDiv extends Div{
    private Workflow workFlow;
    private WorkflowNodeRepository wfNodeRepository;
    private UserRepository userRepository;
    private TextField title = new TextField("Title *");
    private Select<WorkflowNodeTypes> type = new Select<>();
    private HorizontalLayout nodeDetailsInput = new HorizontalLayout();
    private TextField executorClass = new TextField("Class");
    private Select<User> responsible = new Select<>();
    private Select<WorkflowNode> predecessor = new Select<>();
    private Select<WorkflowNode> successor = new Select<>();
    private Select<WorkflowNode> successor_success = new Select<>();
    private Select<WorkflowNode> successor_failure = new Select<>();
    private MultiSelectComboBox<WorkflowNode> multiplePredecessors = new MultiSelectComboBox<>("Predecessor nodes");
    private MultiSelectComboBox<WorkflowNode> multipleSuccessors = new MultiSelectComboBox<>("Successor nodes");
    private Runnable drawWorkflow;
    private Map<WorkflowNode, Boolean> nodeParentSuccessRelation = new ConcurrentHashMap<>();
    private boolean removeRelationToEndNode = false;

    public CreateWorkflowNodeDiv(Workflow wf, WorkflowNodeRepository wfNodeRepository, UserRepository userRepository, Runnable drawWorkflow){
        this.workFlow = wf;
        this.wfNodeRepository = wfNodeRepository;
        this.userRepository = userRepository;
        this.drawWorkflow = drawWorkflow;

        responsible.setLabel("Responsible");
        responsible.setItems(userRepository.findAll());
        responsible.setItemLabelGenerator(User::getName);

        List<WorkflowNode> predecessorNodes = wfNodeRepository.findByIdWorkflow(workFlow.getId()).stream().filter(n -> n.getType()!= END).collect(Collectors.toList());
        List<WorkflowNode> successorNodes = wfNodeRepository.findByIdWorkflow(workFlow.getId()).stream().filter(n -> n.getType()!= START).collect(Collectors.toList());


        predecessor.setLabel("Predecessor node");
        predecessor.setItems(predecessorNodes);
        predecessor.setItemLabelGenerator(node -> node == null ? "" : node.getTitle() + " (" + node.getType() + ")");
        predecessor.setValue(wfNodeRepository.findByIdWorkflowAndType(workFlow.getId(), START));
        //add a value change listener that asks the user if they want to add the node as a success or failure node if the predecessor ia a decision node
        predecessor.addValueChangeListener(event -> {
            WorkflowNode selectedNode = event.getValue();
            if(selectedNode != null && (selectedNode.getType() == WorkflowNodeTypes.USER_DECISION || selectedNode.getType() == WorkflowNodeTypes.BATCH_DECISION)){
                Dialog dialog = new Dialog();
                Button closeButton = new Button(new Icon("lumo", "cross"),
                        (e) -> dialog.close());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                dialog.getHeader().add(closeButton);
                dialog.add(new Paragraph("Do you want to add this node as a success or failure node?"));

                Button successButton = new Button("Success", e -> {
                    if(nodeParentSuccessRelation.containsKey(selectedNode)){
                        nodeParentSuccessRelation.replace(selectedNode, true);
                    } else {
                        nodeParentSuccessRelation.put(selectedNode, true);
                    }dialog.close();
                });
                Button failureButton = new Button("Failure", e -> {
                    if(nodeParentSuccessRelation.containsKey(selectedNode)){
                        nodeParentSuccessRelation.replace(selectedNode, false);
                    } else {
                        nodeParentSuccessRelation.put(selectedNode, false);
                    }dialog.close();
                });
                dialog.getFooter().add(successButton);
                dialog.getFooter().add(failureButton);

                dialog.open();
            } else if (selectedNode != null && selectedNode.getType() == WorkflowNodeTypes.AND){
                Dialog dialog = new Dialog();
                Button closeButton = new Button(new Icon("lumo", "cross"),
                        (e) -> dialog.close());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                dialog.getHeader().add(closeButton);
                dialog.add(new Paragraph("Do you want to remove the connection to the End Node?"));

                Button yesButton = new Button("Yes", e -> {
                    removeRelationToEndNode = true;
                    dialog.close();
                });
                Button noButton = new Button("No", e -> {
                    dialog.close();
                });
                dialog.getFooter().add(yesButton);
                dialog.getFooter().add(noButton);

                dialog.open();
            }
        });

        multiplePredecessors.setItems(predecessorNodes);
        multiplePredecessors.setItemLabelGenerator(node -> node == null ? "" : node.getTitle() + " (" + node.getType() + ")");
        ArrayList<WorkflowNode> nodes = new ArrayList<>();
        nodes.add(wfNodeRepository.findByIdWorkflowAndType(workFlow.getId(), START));
        multiplePredecessors.setValue(nodes);
        //add a value change listener that asks the user if they want to add the node as a success or failure node if the predecessor ia a decision node
        multiplePredecessors.addValueChangeListener(event -> {
            Set<WorkflowNode> selectedNodes = event.getValue();
            WorkflowNode selectedNode = selectedNodes.stream().filter(node -> !nodeParentSuccessRelation.containsKey(node)).findFirst().orElse(null);
            if(selectedNode != null && (selectedNode.getType() == WorkflowNodeTypes.USER_DECISION || selectedNode.getType() == WorkflowNodeTypes.BATCH_DECISION)){
                Dialog dialog = new Dialog();
                Button closeButton = new Button(new Icon("lumo", "cross"),
                        (e) -> dialog.close());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                dialog.getHeader().add(closeButton);
                dialog.add(new Paragraph("Do you want to add this node as a success or failure node?"));

                Button successButton = new Button("Success", e -> {
                    if(nodeParentSuccessRelation.containsKey(selectedNode)){
                        nodeParentSuccessRelation.replace(selectedNode, true);
                    } else {
                        nodeParentSuccessRelation.put(selectedNode, true);
                    }
                    dialog.close();
                });
                Button failureButton = new Button("Failure", e -> {
                    if(nodeParentSuccessRelation.containsKey(selectedNode)){
                        nodeParentSuccessRelation.replace(selectedNode, false);
                    } else {
                        nodeParentSuccessRelation.put(selectedNode, false);
                    }dialog.close();
                });
                dialog.getFooter().add(successButton);
                dialog.getFooter().add(failureButton);

                dialog.open();
            }
        });

        successor.setLabel("Successor node");
        successor.setItems(successorNodes);
        successor.setItemLabelGenerator(node -> node == null ? "" : node.getTitle() + " (" + node.getType() + ")");
        successor.setValue(wfNodeRepository.findByIdWorkflowAndType(workFlow.getId(), END));

        successor_success.setLabel("Successor success node");
        successor_success.setItems(successorNodes);
        successor_success.setItemLabelGenerator(node -> node == null ? "" : node.getTitle() + " (" + node.getType() + ")");
        successor_success.setValue(wfNodeRepository.findByIdWorkflowAndType(workFlow.getId(), END));

        successor_failure.setLabel("Successor failure node");
        successor_failure.setItems(successorNodes);
        successor_failure.setItemLabelGenerator(node -> node == null ? "" : node.getTitle() + " (" + node.getType() + ")");
        successor_failure.setValue(wfNodeRepository.findByIdWorkflowAndType(workFlow.getId(), END));

        multipleSuccessors.setItems(successorNodes);
        multipleSuccessors.setItemLabelGenerator(node -> node == null ? "" : node.getTitle() + " (" + node.getType() + ")");
        ArrayList<WorkflowNode> nodes2 = new ArrayList<>();
        nodes2.add(wfNodeRepository.findByIdWorkflowAndType(workFlow.getId(), END));
        multipleSuccessors.setValue(nodes2);

        type.setLabel("Type *");

        //START and END nodes are automatically generated and can't be added manually
        type.setItems(Arrays.stream(WorkflowNodeTypes.values())
                .filter(e -> e != START && e != END)
                .collect(Collectors.toList()));
        type.addValueChangeListener(event -> {
            WorkflowNodeTypes selectedType = event.getValue();
            nodeDetailsInput.removeAll();
            if(selectedType != null) nodeDetailsInput.add(createNodeDetailsInput(selectedType));
        });
        add(title);
        add(type);
        add(nodeDetailsInput);
        add(new SaveNodeButton());
    }

    private Div createNodeDetailsInput(WorkflowNodeTypes type){
        Div div = new Div();
        switch (type){
            case AND:
                div.add(predecessor);
                div.add(multipleSuccessors);
                break;
            case UNION:
                div.add(multiplePredecessors);
                div.add(successor);
                break;
            case USER_DECISION:
                div.add(predecessor);
                div.add(successor_success);
                div.add(successor_failure);
                div.add(executorClass);
                div.add(responsible);
                break;
            case USER_ACTION:
                div.add(predecessor);
                div.add(successor);
                div.add(executorClass);
                div.add(responsible);
                break;
            case BATCH_DECISION:
                div.add(predecessor);
                div.add(successor_success);
                div.add(successor_failure);
                div.add(executorClass);
                break;
            case BATCH_ACTION:
                div.add(predecessor);
                div.add(successor);
                div.add(executorClass);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
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
                if(responsible.getValue() != null) node.setResponsible(responsible.getValue().getId());

                ArrayList<ObjectId> predecessors = new ArrayList<>();
                ArrayList<ObjectId> successors = new ArrayList<>();

                switch (node.getType()){
                    case AND:
                        if(predecessor.getValue() != null){
                            predecessors.add(predecessor.getValue().getId());
                        }
                        for (WorkflowNode successorNode : multipleSuccessors.getSelectedItems()) {
                            successors.add(successorNode.getId());
                        }
                        break;
                    case UNION:
                        for (WorkflowNode predecessorNode : multiplePredecessors.getSelectedItems()) {
                            predecessors.add(predecessorNode.getId());
                        }
                        if(successor.getValue() != null){
                            successors.add(successor.getValue().getId());
                        }
                        break;
                    case USER_DECISION:
                    case BATCH_DECISION:
                        if(predecessor.getValue() != null){
                            predecessors.add(predecessor.getValue().getId());
                        }
                        if(successor_success.getValue() != null){
                            successors.add(successor_success.getValue().getId());
                            node.setSuccessorNode_success(successor_success.getValue().getId());
                        }
                        if(successor_failure.getValue() != null){
                            successors.add(successor_failure.getValue().getId());
                            node.setSuccessorNode_failure(successor_failure.getValue().getId());
                        }
                        break;
                    case USER_ACTION:
                    case BATCH_ACTION:
                        if(predecessor.getValue() != null){
                            predecessors.add(predecessor.getValue().getId());
                        }
                        if(successor.getValue() != null){
                            successors.add(successor.getValue().getId());
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + node.getType());
                }

                node.setPredecessorNodes(predecessors);
                node.setSuccessorNodes(successors);

                wfNodeRepository.save(node);
                List<WorkflowNode> predecessorNodes = wfNodeRepository.findByIdWorkflow(workFlow.getId()).stream().filter(n -> n.getType()!= END).collect(Collectors.toList());
                List<WorkflowNode> successorNodes = wfNodeRepository.findByIdWorkflow(workFlow.getId()).stream().filter(n -> n.getType()!= START).collect(Collectors.toList());

                predecessor.setItems(predecessorNodes);
                multiplePredecessors.setItems(predecessorNodes);
                successor.setItems(successorNodes);
                successor_success.setItems(successorNodes);
                successor_failure.setItems(successorNodes);
                multipleSuccessors.setItems(successorNodes);

                //clear the input fields
                title.clear();
                executorClass.clear();
                responsible.clear();
                predecessor.clear();
                multiplePredecessors.clear();
                successor.clear();
                successor_success.clear();
                successor_failure.clear();
                multipleSuccessors.clear();
                type.clear();

                // Update the predecessor nodes
                for (ObjectId predecessorId : predecessors) {
                    WorkflowNode predecessorNode = wfNodeRepository.findById(predecessorId);
                    switch (predecessorNode.getType()){
                        case END:
                            break;
                        case START:
                        case UNION:
                        case USER_ACTION:
                        case BATCH_ACTION:
                            predecessorNode.getSuccessorNodes().clear();
                            predecessorNode.getSuccessorNodes().add(node.getId());
                            break;
                        case AND:
                            predecessorNode.getSuccessorNodes().add(node.getId());
                            if(removeRelationToEndNode){
                                WorkflowNode endNode = wfNodeRepository.findByIdWorkflowAndType(workFlow.getId(), END);
                                predecessorNode.getSuccessorNodes().remove(endNode.getId());
                                endNode.getPredecessorNodes().remove(predecessorNode.getId());
                                removeRelationToEndNode = false;
                            }
                            break;
                        case USER_DECISION:
                        case BATCH_DECISION:
                            if(nodeParentSuccessRelation.get(predecessorNode)){
                                predecessorNode.getSuccessorNodes().remove(predecessorNode.getSuccessorNode_success());
                                predecessorNode.setSuccessorNode_success(node.getId());
                                predecessorNode.getSuccessorNodes().add(node.getId());
                            } else {
                                predecessorNode.getSuccessorNodes().remove(predecessorNode.getSuccessorNode_failure());
                                predecessorNode.setSuccessorNode_failure(node.getId());
                                predecessorNode.getSuccessorNodes().add(node.getId());
                            }
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + predecessorNode.getType());
                    }
                    wfNodeRepository.save(predecessorNode);
                }

                // Update the successor nodes
                for(ObjectId successorId : successors) {
                    WorkflowNode successorNode = wfNodeRepository.findById(successorId);
                    switch (node.getType()) {
                        case START:
                            break;
                        case END:
                        case UNION:
                            successorNode.getPredecessorNodes().add(node.getId());
                            break;
                        case AND:
                        case USER_DECISION:
                        case BATCH_DECISION:
                        case USER_ACTION:
                        case BATCH_ACTION:
                            successorNode.getPredecessorNodes().clear();
                            successorNode.getPredecessorNodes().add(node.getId());
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + node.getType());
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