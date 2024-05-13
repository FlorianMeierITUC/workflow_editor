package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.entities.WorkflowNodeTypes;
import de.ketobi.vaadinspringdemo.views.components.workflow.viewer.WorkflowView;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkflowViewTest {

    // Initialize the WorkflowNode objects
    private final static WorkflowNode startNode = new WorkflowNode();
    private final static WorkflowNode batchDecision = new WorkflowNode();
    private final static WorkflowNode batchDecision2 = new WorkflowNode();
    private final static WorkflowNode batchAction = new WorkflowNode();
    private final static WorkflowNode endNode = new WorkflowNode();

    @BeforeAll
    public static void setup() {
        startNode.setId(new ObjectId());
        startNode.setType(WorkflowNodeTypes.START);
        startNode.setTitle("Start");

        batchDecision.setId(new ObjectId());
        batchDecision.setType(WorkflowNodeTypes.BATCH_DECISION);
        batchDecision.setTitle("Batch Decision");

        batchDecision2.setId(new ObjectId());
        batchDecision2.setType(WorkflowNodeTypes.BATCH_DECISION);
        batchDecision2.setTitle("Batch Decision 2");

        batchAction.setId(new ObjectId());
        batchAction.setType(WorkflowNodeTypes.BATCH_ACTION);
        batchAction.setTitle("Batch Action");

        endNode.setId(new ObjectId());
        endNode.setType(WorkflowNodeTypes.END);
        endNode.setTitle("End");

    }

    @Test
    public void testOrderNodesIntoYLevels() {
        // Create a list of WorkflowNode objects with known YLevels
        List<WorkflowNode> nodes = new ArrayList<>();
        nodes.add(startNode);

        batchDecision.setPredecessorNodes(List.of(startNode.getId()));
        nodes.add(batchDecision);

        batchAction.setPredecessorNodes(List.of(batchDecision.getId()));
        nodes.add(batchAction);

        endNode.setPredecessorNodes(List.of(batchAction.getId(), batchDecision.getId()));
        nodes.add(endNode);

        startNode.setSuccessorNodes(List.of(batchDecision.getId()));
        batchDecision.setSuccessorNodes(List.of(batchAction.getId(), endNode.getId()));
        batchAction.setSuccessorNodes(List.of(endNode.getId()));

        // Create a WorkflowView object with this list
        WorkflowView workflowView = new WorkflowView(nodes);

        // Assert that the YLevel of each node in the WorkflowView matches the known YLevel
        assertEquals(1, workflowView.getNodes().get(0).getYLevel());
        assertEquals(2, workflowView.getNodes().get(1).getYLevel());
        assertEquals(3, workflowView.getNodes().get(2).getYLevel());
        assertEquals(4, workflowView.getNodes().get(3).getYLevel());
    }

    @Test
    public void testOrderNodesIntoYLevelsWithMinimalCycle() {
        // Create a list of WorkflowNode objects with known YLevels
        List<WorkflowNode> nodes = new ArrayList<>();
        nodes.add(startNode);

        batchDecision.setPredecessorNodes(List.of(startNode.getId()));
        nodes.add(batchDecision);

        batchDecision2.setPredecessorNodes(List.of(batchDecision.getId()));
        nodes.add(batchDecision2);

        batchAction.setPredecessorNodes(List.of(batchDecision2.getId()));
        nodes.add(batchAction);

        endNode.setPredecessorNodes(List.of(batchAction.getId(), batchDecision.getId()));
        nodes.add(endNode);

        startNode.setSuccessorNodes(List.of(batchDecision.getId()));
        batchDecision.setSuccessorNodes(List.of(batchDecision2.getId(), endNode.getId()));
        batchDecision2.setSuccessorNodes(List.of(batchAction.getId(), batchDecision.getId()));
        batchAction.setSuccessorNodes(List.of(endNode.getId()));

        // Create a WorkflowView object with this list
        WorkflowView workflowView = new WorkflowView(nodes);

        // Assert that the YLevel of each node in the WorkflowView matches the known YLevel
        assertEquals(1, workflowView.getNodes().get(0).getYLevel());
        assertEquals(2, workflowView.getNodes().get(1).getYLevel());
        assertEquals(3, workflowView.getNodes().get(2).getYLevel());
        assertEquals(4, workflowView.getNodes().get(3).getYLevel());
        assertEquals(5, workflowView.getNodes().get(4).getYLevel());
    }

    @Test
    public void testOrderNodesIntoYLevelsWithTwoStepsCycle() {
        // Create a list of WorkflowNode objects with known YLevels
        List<WorkflowNode> nodes = new ArrayList<>();
        nodes.add(startNode);

        batchDecision.setPredecessorNodes(List.of(startNode.getId()));
        nodes.add(batchDecision);

        batchAction.setPredecessorNodes(List.of(batchDecision.getId()));
        nodes.add(batchAction);

        batchDecision2.setPredecessorNodes(List.of(batchAction.getId()));
        nodes.add(batchDecision2);

        endNode.setPredecessorNodes(List.of(batchDecision.getId(), batchDecision2.getId()));
        nodes.add(endNode);

        startNode.setSuccessorNodes(List.of(batchDecision.getId()));
        batchDecision.setSuccessorNodes(List.of(batchAction.getId(), endNode.getId()));
        batchAction.setSuccessorNodes(List.of(batchDecision2.getId()));
        batchDecision2.setSuccessorNodes(List.of(batchDecision.getId(), endNode.getId()));

        // Create a WorkflowView object with this list
        WorkflowView workflowView = new WorkflowView(nodes);

        // Assert that the YLevel of each node in the WorkflowView matches the known YLevel
        assertEquals(1, workflowView.getNodes().get(0).getYLevel());
        assertEquals(2, workflowView.getNodes().get(1).getYLevel());
        assertEquals(3, workflowView.getNodes().get(2).getYLevel());
        assertEquals(4, workflowView.getNodes().get(3).getYLevel());
    }
}