package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.entities.WorkflowNodeTypes;
import de.ketobi.vaadinspringdemo.views.components.workflow.viewer.WorkflowView;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkflowViewTest {

    @Test
    public void testOrderNodesIntoYLevels() {
        // Create a list of WorkflowNode objects with known YLevels
        List<WorkflowNode> nodes = new ArrayList<>();
        WorkflowNode startNode = new WorkflowNode();
        startNode.setId(new ObjectId());
        startNode.setType(WorkflowNodeTypes.START);
        nodes.add(startNode);

        WorkflowNode batchDecision = new WorkflowNode();
        batchDecision.setId(new ObjectId());
        batchDecision.setType(WorkflowNodeTypes.BATCH_DECISION);
        batchDecision.setTitle("Batch Decision");
        batchDecision.setPredecessorNodes(List.of(startNode.getId()));
        nodes.add(batchDecision);

        WorkflowNode batchAction = new WorkflowNode();
        batchAction.setId(new ObjectId());
        batchAction.setType(WorkflowNodeTypes.BATCH_ACTION);
        batchAction.setTitle("Batch Action");
        batchAction.setPredecessorNodes(List.of(batchDecision.getId()));
        nodes.add(batchAction);

        WorkflowNode endNode = new WorkflowNode();
        endNode.setId(new ObjectId());
        endNode.setType(WorkflowNodeTypes.END);
        endNode.setPredecessorNodes(List.of(batchAction.getId(), batchDecision.getId()));
        nodes.add(endNode);

        startNode.setSuccessorNodes(List.of(batchDecision.getId()));
        batchDecision.setSuccessorNodes(List.of(batchAction.getId(), endNode.getId()));
        batchAction.setSuccessorNodes(List.of(endNode.getId()));

        // Create a WorkflowView object with this list
        WorkflowView workflowView = new WorkflowView(nodes);

        // Assert that the YLevel of each node in the WorkflowView matches the known YLevel
        assertEquals(0, workflowView.getNodes().get(0).getYLevel());
        assertEquals(1, workflowView.getNodes().get(1).getYLevel());
        assertEquals(2, workflowView.getNodes().get(2).getYLevel());
        assertEquals(3, workflowView.getNodes().get(3).getYLevel());
    }
}