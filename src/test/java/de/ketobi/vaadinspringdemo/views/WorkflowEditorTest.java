package de.ketobi.vaadinspringdemo.views;

import de.ketobi.vaadinspringdemo.main.workflows.WorkflowEditor;
import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowScheduleService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowService;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowEditorTest {

    private WorkflowEditor workflowEditor;

    @Mock
    private WorkflowService wfService;

    @Mock
    private WorkflowNodeService wfNodeService;

    @Mock
    private WorkflowScheduleService wfScheduleService;

    @Mock
    private UserService userService;

    private Workflow workflow;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        workflow = new Workflow();

        workflowEditor = new WorkflowEditor(wfService, wfNodeService, userService, wfScheduleService);

        workflow.setId(new ObjectId());
        workflow.setName("Test Workflow");
        workflow.setDescription("Test Description");
        workflow.setActive(true);

        WorkflowNode startNode = new WorkflowNode();
        startNode.setIdWorkflow(workflow.getId());
        startNode.setId(new ObjectId());
        startNode.setTitle("Start");
        startNode.setType(WorkflowNodeTypes.START);
        startNode.setResponsible(UserService.getSystemUser().getId());

        WorkflowNode intermediateNode = new WorkflowNode();
        intermediateNode.setIdWorkflow(workflow.getId());
        intermediateNode.setId(new ObjectId());
        intermediateNode.setTitle("Mocked Node");
        intermediateNode.setType(WorkflowNodeTypes.USER_ACTION);

        // Create end node
        WorkflowNode endNode = new WorkflowNode();
        endNode.setIdWorkflow(workflow.getId());
        endNode.setId(new ObjectId());
        endNode.setTitle("End");
        endNode.setType(WorkflowNodeTypes.END);
        endNode.setResponsible(UserService.getSystemUser().getId());

        lenient().when(wfNodeService.getStartNode(any())).thenReturn(startNode);
        lenient().when(wfNodeService.getEndNode(any())).thenReturn(endNode);
        lenient().when(wfNodeService.getAll(any()))
                .thenReturn(new ArrayList<WorkflowNode>(List.of(startNode, intermediateNode, endNode)));

    }

    @Test
    public void shouldFillNodeDivWhenWorkflowExists() {

        when(wfService.getById(any(String.class))).thenReturn(workflow);
        workflowEditor.setParameter(null, "1");

        verify(wfService, times(1)).getById(any(String.class));
        assertTrue(workflowEditor.getNodeDiv().getComponentCount() > 1);
    }

    @Test
    public void shouldNotFillNodeDivWhenWorkflowDoesNotExist() {
        when(wfService.getById(any(String.class))).thenReturn(null);
        workflowEditor.setParameter(null, "1");

        verify(wfService, times(1)).getById(any(String.class));
        assertTrue(workflowEditor.getNodeDiv().getComponentCount() == 0);
    }

    @Test
    public void shouldDrawWorkflowWhenWorkflowExists() {

        when(wfService.getById(any(String.class))).thenReturn(workflow);

        workflowEditor.setParameter(null, "1");

        verify(wfService, times(1)).getById(any(String.class));
        assertTrue(workflowEditor.getTreeDiv().getComponentCount() > 0);
    }

    @Test
    public void shouldNotDrawWhenWorkflowDoesNotExist() {
        when(wfService.getById(any(String.class))).thenReturn(null);
        workflowEditor.setParameter(null, "1");

        verify(wfService, times(1)).getById(any(String.class));
        assertTrue(workflowEditor.getTreeDiv().getComponentCount() == 0);
    }
}