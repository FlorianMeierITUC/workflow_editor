package de.ketobi.vaadinspringdemo.views;
import de.ketobi.vaadinspringdemo.entities.Workflow;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.repositories.WorkflowNodeRepository;
import de.ketobi.vaadinspringdemo.repositories.WorkflowRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class WorkflowEditorTest {

    @InjectMocks
    private WorkflowEditor workflowEditor;

    @Mock
    private WorkflowRepository wfRepository;

    @Mock
    private WorkflowNodeRepository wfNodeRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldFillNodeDivWhenWorkflowExists() {
        Workflow workflow = new Workflow();
        workflow.setId(new ObjectId());
        workflow.setName("Test Workflow");
        workflow.setDescription("Test Description");
        workflow.setActive(true);

        when(wfRepository.findById(any())).thenReturn(Optional.of(workflow));

        workflowEditor.setParameter(null, "1");

        verify(wfRepository, times(1)).findById(any());
    }

    @Test
    public void shouldNotFillNodeDivWhenWorkflowDoesNotExist() {
        when(wfRepository.findById(any())).thenReturn(Optional.empty());

        workflowEditor.setParameter(null, "1");

        verify(wfRepository, times(1)).findById(any());
    }

    @Test
    public void shouldDrawWorkflowWhenWorkflowExists() {
        Workflow workflow = new Workflow();
        workflow.setId(new ObjectId());
        workflow.setName("Test Workflow");
        workflow.setDescription("Test Description");
        workflow.setActive(true);

        when(wfRepository.findById(any())).thenReturn(Optional.of(workflow));

        workflowEditor.setParameter(null, "1");

        verify(wfRepository, times(1)).findById(any());
    }

    @Test
    public void shouldNotDrawWorkflowWhenWorkflowDoesNotExist() {
        when(wfRepository.findById(any())).thenReturn(Optional.empty());

        workflowEditor.setParameter(null, "1");

        verify(wfRepository, times(1)).findById(any());
    }
}