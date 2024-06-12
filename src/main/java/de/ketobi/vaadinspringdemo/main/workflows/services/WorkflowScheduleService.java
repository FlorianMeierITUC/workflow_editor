package de.ketobi.vaadinspringdemo.main.workflows.services;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowEntity;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowSchedule;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTypes;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowScheduleRepository;
import de.ketobi.vaadinspringdemo.main.workflows.utils.ScheduleCalculator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

@Service
public class WorkflowScheduleService {
    @Autowired
    private WorkflowScheduleRepository scheduleRepository;
    @Autowired
    private WorkflowEntityService workflowEntityService;

    @Scheduled(fixedRate = 3600000) // Every hour
    public void checkAndExecuteTasks() {
        List<WorkflowSchedule> schedules = scheduleRepository.findAll();
        LocalDate now = LocalDate.now();
        for (WorkflowSchedule schedule : schedules) {
            LocalDate nextRun = ScheduleCalculator.calculateNextRun(schedule.getStart(), schedule.getPattern(), now);
            if (!now.isBefore(nextRun)) {
                executeTask(schedule.getWorkflowId());
                // Update last run to now, successful or not, for logging and audit purposes
                schedule.setLastRun(now);
                scheduleRepository.save(schedule);
            }
        }
    }

    private void executeTask(ObjectId workflowId) {
        try {
            WorkflowTypes workflowType = WorkflowTypes.fromId(workflowId);
            Class<? extends WorkflowEntity> entityClass = workflowType.getEntity();

            Constructor<?> constructor = entityClass.getDeclaredConstructor();
            WorkflowEntity instance = (WorkflowEntity) constructor.newInstance();

            Method getScheduledWorkflowStartEntityMethod = entityClass.getMethod("getScheduledWorkflowStartEntity");
            WorkflowEntity scheduledWorkflowStartEntity = (WorkflowEntity) getScheduledWorkflowStartEntityMethod.invoke(instance);

            System.out.println("Starting workflow "+workflowType.getName()+". Scheduled Workflow Start Entity: " + scheduledWorkflowStartEntity.getName());
            workflowEntityService.startWorkflow(scheduledWorkflowStartEntity);
        } catch (NoSuchMethodException e) {
            System.out.println("Method getScheduledWorkflowStartEntity not found in the class");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
