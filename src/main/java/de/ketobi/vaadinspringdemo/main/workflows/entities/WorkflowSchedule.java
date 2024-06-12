package de.ketobi.vaadinspringdemo.main.workflows.entities;

import lombok.Builder;
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
@Builder
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
        private int interval; // Interval count, applicable for any type like 'Every N weeks/days/months/years'
        private boolean excludeWeekends; // Whether to exclude weekends

        private int dayOfMonth; // For monthly schedules, can handle cases like "on the 15th"
        private List<Integer> daysOfWeek; // Applicable for weekly schedules
        private int dayInAMonth; // For yearly schedules, can handle cases like "on the 3rd of May"
        private int monthInAYear; // For yearly schedules, can handle cases like "on the 3rd of May"

        //Override the toString Method to display the schedule pattern in a human-readable format
        @Override
        public String toString() {
            String scheduleString = "";
            switch (type) {
                case "Daily":
                    scheduleString = "Every " + interval + " day(s). ";
                    if (excludeWeekends) {
                        scheduleString += "Excluding weekends.";
                    }
                    break;
                case "Weekly":
                    scheduleString = "Every " + interval + " week(s) on ";
                    for (int i = 0; i < daysOfWeek.size(); i++) {
                        if(null != daysOfWeek.get(i) && daysOfWeek.get(i)==1){
                            scheduleString += "Monday";
                        } else if(null != daysOfWeek.get(i) && daysOfWeek.get(i)==2){
                            scheduleString += "Tuesday";
                        } else if(null != daysOfWeek.get(i) && daysOfWeek.get(i)==3){
                            scheduleString += "Wednesday";
                        } else if(null != daysOfWeek.get(i) && daysOfWeek.get(i)==4){
                            scheduleString += "Thursday";
                        } else if(null != daysOfWeek.get(i) && daysOfWeek.get(i)==5){
                            scheduleString += "Friday";
                        } else if(null != daysOfWeek.get(i) && daysOfWeek.get(i)==6){
                            scheduleString += "Saturday";
                        } else if(null != daysOfWeek.get(i) && daysOfWeek.get(i)==7){
                            scheduleString += "Sunday";
                        }

                        if (i < daysOfWeek.size() - 1) {
                            scheduleString += ", ";
                        }
                    }
                    break;
                case "Monthly":
                    scheduleString = "Every " + interval + " month(s) on the " + dayOfMonth + "th day";
                    break;
                case "Yearly":
                    scheduleString = "Every " + interval + " year(s) on the " + dayInAMonth + "th day of " + monthInAYear;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid schedule type: " + type);
            }
            return scheduleString;
        }
    }
}
