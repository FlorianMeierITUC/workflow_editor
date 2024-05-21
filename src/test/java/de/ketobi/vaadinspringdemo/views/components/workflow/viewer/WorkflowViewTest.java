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
    private final static WorkflowNode batchAction2 = new WorkflowNode();
    private final static WorkflowNode batchAction3 = new WorkflowNode();
    private final static WorkflowNode batchAction4 = new WorkflowNode();
    private final static WorkflowNode batchAction5 = new WorkflowNode();
    private final static WorkflowNode endNode = new WorkflowNode();
    private final static WorkflowNode orNode = new WorkflowNode();
    private final static WorkflowNode andNode = new WorkflowNode();

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

        batchAction2.setId(new ObjectId());
        batchAction2.setType(WorkflowNodeTypes.BATCH_ACTION);
        batchAction2.setTitle("Batch Action2");

        batchAction3.setId(new ObjectId());
        batchAction3.setType(WorkflowNodeTypes.BATCH_ACTION);
        batchAction3.setTitle("Batch Action3");

        batchAction4.setId(new ObjectId());
        batchAction4.setType(WorkflowNodeTypes.BATCH_ACTION);
        batchAction4.setTitle("Batch Action4");

        batchAction5.setId(new ObjectId());
        batchAction5.setType(WorkflowNodeTypes.BATCH_ACTION);
        batchAction5.setTitle("Batch Action5");

        endNode.setId(new ObjectId());
        endNode.setType(WorkflowNodeTypes.END);
        endNode.setTitle("End");

        orNode.setId(new ObjectId());
        orNode.setType(WorkflowNodeTypes.OR);
        orNode.setTitle("Or");

        andNode.setId(new ObjectId());
        andNode.setType(WorkflowNodeTypes.AND);
        andNode.setTitle("And");
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
        batchDecision.setSuccessorNode_success(batchAction.getId());
        batchDecision.setSuccessorNode_failure(endNode.getId());
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
        batchDecision.setSuccessorNode_success(batchDecision2.getId());
        batchDecision.setSuccessorNode_failure(endNode.getId());
        batchDecision2.setSuccessorNodes(List.of(batchAction.getId(), batchDecision.getId()));
        batchDecision2.setSuccessorNode_success(batchAction.getId());
        batchDecision2.setSuccessorNode_failure(batchDecision.getId());
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
        batchDecision.setSuccessorNode_success(batchAction.getId());
        batchDecision.setSuccessorNode_failure(endNode.getId());
        batchAction.setSuccessorNodes(List.of(batchDecision2.getId()));
        batchDecision2.setSuccessorNodes(List.of(batchDecision.getId(), endNode.getId()));
        batchDecision2.setSuccessorNode_success(batchDecision.getId());
        batchDecision2.setSuccessorNode_failure(endNode.getId());

        // Create a WorkflowView object with this list
        WorkflowView workflowView = new WorkflowView(nodes);

        // Assert that the YLevel of each node in the WorkflowView matches the known YLevel
        assertEquals(1, workflowView.getNodes().get(0).getYLevel());
        assertEquals(2, workflowView.getNodes().get(1).getYLevel());
        assertEquals(3, workflowView.getNodes().get(2).getYLevel());
        assertEquals(4, workflowView.getNodes().get(3).getYLevel());
    }

    @Test
    public void testOrderNodesIntoXLevels() {
        // Create a list of WorkflowNode objects with known XLevels
        List<WorkflowNode> nodes = new ArrayList<>();
        nodes.add(startNode);

        batchDecision.setPredecessorNodes(List.of(startNode.getId()));
        nodes.add(batchDecision);

        orNode.setPredecessorNodes(List.of(batchDecision.getId()));
        nodes.add(orNode);

        andNode.setPredecessorNodes(List.of(batchDecision.getId()));
        nodes.add(andNode);

        batchAction.setPredecessorNodes(List.of(orNode.getId()));
        nodes.add(batchAction);

        batchAction2.setPredecessorNodes(List.of(orNode.getId()));
        nodes.add(batchAction2);

        batchAction3.setPredecessorNodes(List.of(andNode.getId()));
        nodes.add(batchAction3);

        batchAction4.setPredecessorNodes(List.of(andNode.getId()));
        nodes.add(batchAction4);

        batchAction5.setPredecessorNodes(List.of(andNode.getId()));
        nodes.add(batchAction5);

        endNode.setPredecessorNodes(List.of(batchAction.getId(), batchAction2.getId(), batchAction3.getId(), batchAction4.getId(), batchAction5.getId()));
        nodes.add(endNode);

        startNode.setSuccessorNodes(List.of(batchDecision.getId()));
        batchDecision.setSuccessorNodes(List.of(orNode.getId(), andNode.getId()));
        batchDecision.setSuccessorNode_success(orNode.getId());
        batchDecision.setSuccessorNode_failure(andNode.getId());
        orNode.setSuccessorNodes(List.of(batchAction.getId(), batchAction2.getId()));
        andNode.setSuccessorNodes(List.of(batchAction3.getId(), batchAction4.getId(), batchAction5.getId()));
        batchAction.setSuccessorNodes(List.of(endNode.getId()));
        batchAction2.setSuccessorNodes(List.of(endNode.getId()));
        batchAction3.setSuccessorNodes(List.of(endNode.getId()));
        batchAction4.setSuccessorNodes(List.of(endNode.getId()));
        batchAction5.setSuccessorNodes(List.of(endNode.getId()));

        // Create a WorkflowView object with this list
        WorkflowView workflowView = new WorkflowView(nodes);

        // Assert that the XLevel of each node in the WorkflowView matches the known XLevel
        assertEquals(6, workflowView.getNodes().get(0).getXLevel());
        assertEquals(6, workflowView.getNodes().get(1).getXLevel());
        assertEquals(3, workflowView.getNodes().get(2).getXLevel());
        assertEquals(8, workflowView.getNodes().get(3).getXLevel());
        assertEquals(2, workflowView.getNodes().get(4).getXLevel());
        assertEquals(4, workflowView.getNodes().get(5).getXLevel());
        assertEquals(6, workflowView.getNodes().get(6).getXLevel());
        assertEquals(8, workflowView.getNodes().get(7).getXLevel());
        assertEquals(10, workflowView.getNodes().get(8).getXLevel());
        assertEquals(6, workflowView.getNodes().get(9).getXLevel());
    }
}