package de.ketobi.vaadinspringdemo.main.workflows.services;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.notification.Notification;

import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes;
import de.ketobi.vaadinspringdemo.main.workflows.utils.BeanLister;

@Service
public class WorkflowValidation {
    private final WorkflowNodeService wfNodeService;

    @Autowired
    public WorkflowValidation(WorkflowNodeService wfNodeService) {
        this.wfNodeService = wfNodeService;
    }

    public boolean validateWorkflow(WorkflowNodeService wfNodeService, Workflow workFlow) {
        boolean nodesValid = false;
        try {
            System.out.println(workFlow.getId());
            List<WorkflowNode> nodes = wfNodeService.getAll(workFlow.getId());
            List<WorkflowNode> batchNodes = wfNodeService.getAllBatchNodes(workFlow.getId());

            nodesValid = validateNodes(nodes) && allBatchNodesImplemented(batchNodes);
            System.out.println("Nodes Valid" + nodesValid);

            // boolean allAndNodesEndInUnionNode = validateAndNodeEndsInUnionNode(nodes);
            // System.out.println(allAndNodesEndInUnionNode);

        } catch (Exception e) {
            System.out.println("Exception");
        }

        return nodesValid;
    }

    private boolean allBatchNodesImplemented(List<WorkflowNode> batchNodes) {

        String[] beanNames = new BeanLister().listAllBeans();
        List<String> beanNamesList = Arrays.asList(beanNames);

        boolean allBatchNodesImplemented = true;

        for (WorkflowNode batchNode : batchNodes) {
            System.out.println("Batch node: " + batchNode.getId().toString() + batchNode.getTitle());
            if (!beanNamesList.contains(batchNode.getId().toString())) {
                allBatchNodesImplemented = false;
                Notification.show("Batch action not found: " + batchNode.getTitle());
            }
        }
        return allBatchNodesImplemented;
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

}
