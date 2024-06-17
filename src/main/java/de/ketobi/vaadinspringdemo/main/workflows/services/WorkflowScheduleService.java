package de.ketobi.vaadinspringdemo.main.workflows.services;

import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowEntity;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowSchedule;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTypes;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowRepository;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowScheduleRepository;
import de.ketobi.vaadinspringdemo.main.workflows.utils.ScheduleCalculator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private WorkflowRepository workflowRepository;

    //@Scheduled(fixedRate = 4*60*60*1000) // Every 4 hours
    @Scheduled(fixedRate = 60*1000) // Every minute //TODO change this after testing
    public void checkAndExecuteTasks() {
        //TODO don't execute inactive workflows!
        System.out.println("Checking and executing scheduled tasks");

        List<WorkflowSchedule> schedules = scheduleRepository.findAll();
        System.out.println("Found "+schedules.size()+" schedules");
        LocalDate now = LocalDate.now();
        ScheduleCalculator calculator = new ScheduleCalculator();
        for (WorkflowSchedule schedule : schedules) {
            Workflow workflow = workflowRepository.findById(schedule.getWorkflowId()).orElse(null);
            if(workflow == null){
                System.out.println("Workflow "+schedule.getWorkflowId()+" not found in the database. Removing schedule");
                delete(schedule.getWorkflowId());
                continue;
            }
            System.out.println("Checking schedule for workflow "+schedule.getWorkflowId());
            LocalDate nextRun = ScheduleCalculator.calculateNextRun(schedule.getStart(), schedule.getPattern(), now);
            //nextRun = nextRun.minusDays(1); //TODO remove this after testing
            System.out.println("Next run: "+nextRun);
            System.out.println("Trying to execute the task? "+!now.isBefore(nextRun));
            if (calculator.workflowIsDue(schedule)) {
                System.out.println("Workflow is due today");
            }
            //if (!now.isBefore(nextRun) && !nextRun.isEqual(schedule.getLastRun())) {
            if(false){
                executeTask(schedule.getWorkflowId());
                System.out.println("Update last run to now");
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
            mongoTemplate.save(scheduledWorkflowStartEntity);
            System.out.println("Entity created and saved to database" + scheduledWorkflowStartEntity);

            System.out.println("Starting workflow "+workflowType.getName()+". Scheduled Workflow Start Entity: " + scheduledWorkflowStartEntity.getName());
            workflowEntityService.startWorkflow(scheduledWorkflowStartEntity);
        } catch (NoSuchMethodException e) {
            System.out.println("Method getScheduledWorkflowStartEntity not found in the class");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(WorkflowSchedule schedule) {
        scheduleRepository.save(schedule);
    }

    public boolean workflowIsScheduled(ObjectId workflowId) {
        return scheduleRepository.existsByWorkflowId(workflowId);
    }

    public WorkflowSchedule get(ObjectId workflowId) {
        return scheduleRepository.findByWorkflowId(workflowId);
    }

    public void delete(ObjectId workflowId) {
        scheduleRepository.deleteByWorkflowId(workflowId);
    }
}
