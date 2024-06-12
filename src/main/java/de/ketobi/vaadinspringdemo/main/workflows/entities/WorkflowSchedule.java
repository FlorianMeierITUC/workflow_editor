package de.ketobi.vaadinspringdemo.main.workflows.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document
public class WorkflowSchedule {
    @EqualsAndHashCode.Include
    @Id
    @NonNull
    ObjectId id;

    @NonNull
    ObjectId workflowId;

    LocalDate start;

    LocalDate end;

    LocalDate lastRun;

    SchedulePattern pattern;

    @Data
    @Document
    public static class SchedulePattern {
        private String type; // Daily, Weekly, Monthly, Yearly
        private List<Integer> dayOfMonth; // For monthly schedules, can handle cases like "on the 15th"
        private List<String> daysOfWeek; // Applicable for weekly or custom schedules
        private int interval; // Interval count, applicable for any type like 'Every N weeks/days/months/years'
        private boolean excludeWeekends; // Whether to exclude weekends
        private int dayInAMonth; // For yearly schedules, can handle cases like "on the 3rd of May"
        private int monthInAYear; // For yearly schedules, can handle cases like "on the 3rd of May"
    }
}
