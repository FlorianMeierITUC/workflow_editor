package de.ketobi.vaadinspringdemo.main.workflows.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowSchedule;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowScheduleService;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

public class ScheduleWorkflowsDialog extends Dialog {
    private final VerticalLayout patternOptions = new VerticalLayout();
    private final Div durationOptions = new Div();
    private final ObjectId workflowId;
    private final WorkflowScheduleService workflowScheduleService;
    private final IntegerField intervalField = new IntegerField();
    private final Checkbox excludeWeekends = new Checkbox("Exclude weekends");
    private final Checkbox monday = new Checkbox("Monday");
    private final Checkbox tuesday = new Checkbox("Tuesday");
    private final Checkbox wednesday = new Checkbox("Wednesday");
    private final Checkbox thursday = new Checkbox("Thursday");
    private final Checkbox friday = new Checkbox("Friday");
    private final Checkbox saturday = new Checkbox("Saturday");
    private final Checkbox sunday = new Checkbox("Sunday");
    private final IntegerField dayOfMonthField = new IntegerField();
    private final IntegerField dayInAMonthField = new IntegerField();
    private final IntegerField monthField = new IntegerField();

    public ScheduleWorkflowsDialog(ObjectId workflowId, WorkflowScheduleService workflowScheduleService, Runnable updateScheduleInfo) {
        this.workflowId = workflowId;
        this.workflowScheduleService = workflowScheduleService;

        intervalField.setValue(1);
        intervalField.setMin(1);
        intervalField.setStepButtonsVisible(true);
        dayOfMonthField.setValue(1);
        dayOfMonthField.setMin(1);
        dayOfMonthField.setMax(31);
        dayOfMonthField.setStepButtonsVisible(true);
        dayInAMonthField.setValue(1);
        dayInAMonthField.setMin(1);
        dayInAMonthField.setMax(31);
        dayInAMonthField.setStepButtonsVisible(true);
        monthField.setValue(1);
        monthField.setMin(1);
        monthField.setMax(12);
        monthField.setStepButtonsVisible(true);
        excludeWeekends.setValue(true);

        getHeader().add(new H3("Schedule workflow execution"));
        //Pattern section
        RadioButtonGroup<String> patternGroup = new RadioButtonGroup<>();
        patternGroup.setLabel("Recurrence Pattern");
        patternGroup.setItems("Daily", "Weekly", "Monthly", "Yearly");
        patternGroup.setValue("Daily");
        showDailyOptions();
        //Display different configuration options based on the selected pattern
        patternGroup.addValueChangeListener(event -> {
            if (event.getValue().equals("Daily")) {
                // Show daily options
                showDailyOptions();
            } else if (event.getValue().equals("Weekly")) {
                // Show weekly options
                showWeeklyOptions();
            } else if (event.getValue().equals("Monthly")) {
                // Show monthly options
                showMonthlyOptions();
            } else if (event.getValue().equals("Yearly")) {
                // Show yearly options
                showYearlyOptions();
            }
        });

        VerticalLayout patternLayout = new VerticalLayout(patternGroup, patternOptions);

        // Duration section
        DatePicker startDatePicker = new DatePicker("Start Date");
        startDatePicker.setValue(LocalDate.now());
        DatePicker endDatePicker = new DatePicker();
        RadioButtonGroup<String> durationGroup = new RadioButtonGroup<>();
        durationGroup.setLabel("Duration");
        durationGroup.setItems("Ends on", "No end date");
        durationGroup.setValue("Ends on");
        durationOptions.add(endDatePicker);
        durationGroup.addValueChangeListener(event -> {
            if (event.getValue().equals("Ends on")) {
                durationOptions.removeAll();
                durationOptions.add(endDatePicker);
            } else if (event.getValue().equals("No end date")) {
                durationOptions.removeAll();
            }
        });

        VerticalLayout durationLayout = new VerticalLayout(startDatePicker, durationGroup, durationOptions);

        // Buttons
        Button okButton = new Button("Save", event -> {
            WorkflowSchedule.SchedulePattern pattern;
            WorkflowSchedule schedule;
            if(workflowScheduleService.workflowIsScheduled(workflowId)){
                schedule = workflowScheduleService.get(workflowId);
                pattern = schedule.getPattern();
            } else {
                pattern = new WorkflowSchedule.SchedulePattern();
                schedule = new WorkflowSchedule();
                schedule.setId(ObjectId.get());
                schedule.setWorkflowId(workflowId);
            }
            schedule.setStart(startDatePicker.getValue());
            schedule.setEnd(durationGroup.getValue().equals("Ends on") ? endDatePicker.getValue() : null);

            pattern.setType(patternGroup.getValue());
            pattern.setInterval(intervalField.getValue().intValue());
            if (patternGroup.getValue().equals("Daily")) {
                // Set daily options
                pattern.setExcludeWeekends(excludeWeekends.getValue());
            } else if (patternGroup.getValue().equals("Weekly")) {
                // Set weekly options
                pattern.setDaysOfWeek(List.of(
                        monday.getValue() ? 1 : 0,
                        tuesday.getValue() ? 2 : 0,
                        wednesday.getValue() ? 3 : 0,
                        thursday.getValue() ? 4 : 0,
                        friday.getValue() ? 5 : 0,
                        saturday.getValue() ? 6 : 0,
                        sunday.getValue() ? 7 : 0
                ));
            } else if (patternGroup.getValue().equals("Monthly")) {
                // Set monthly options
                pattern.setDayOfMonth(dayOfMonthField.getValue().intValue());
            } else if (patternGroup.getValue().equals("Yearly")) {
                // Set yearly options
                pattern.setDayInAMonth(dayInAMonthField.getValue().intValue());
                pattern.setMonthInAYear(monthField.getValue().intValue());
            }
            schedule.setPattern(pattern);
            workflowScheduleService.save(schedule);
            updateScheduleInfo.run();
            close();
        });

        // Layout
        VerticalLayout layout = new VerticalLayout();
        layout.add(patternLayout, durationLayout, okButton);
        layout.setSizeFull();
        layout.setPadding(true);
        layout.setSpacing(true);

        add(layout);
        getFooter().add(okButton, new Button("Cancel", event -> close()));
        if(workflowScheduleService.workflowIsScheduled(workflowId)){
            WorkflowSchedule schedule = workflowScheduleService.get(workflowId);
            patternGroup.setValue(schedule.getPattern().getType());
            intervalField.setValue(schedule.getPattern().getInterval());
            excludeWeekends.setValue(schedule.getPattern().isExcludeWeekends());
            if(schedule.getPattern().getDaysOfWeek()!=null) {
                monday.setValue(schedule.getPattern().getDaysOfWeek().contains(1));
                tuesday.setValue(schedule.getPattern().getDaysOfWeek().contains(2));
                wednesday.setValue(schedule.getPattern().getDaysOfWeek().contains(3));
                thursday.setValue(schedule.getPattern().getDaysOfWeek().contains(4));
                friday.setValue(schedule.getPattern().getDaysOfWeek().contains(5));
                saturday.setValue(schedule.getPattern().getDaysOfWeek().contains(6));
                sunday.setValue(schedule.getPattern().getDaysOfWeek().contains(7));
            }
            dayOfMonthField.setValue(schedule.getPattern().getDayOfMonth());
            dayInAMonthField.setValue(schedule.getPattern().getDayInAMonth());
            monthField.setValue(schedule.getPattern().getMonthInAYear());
            startDatePicker.setValue(schedule.getStart());
            if(schedule.getEnd()==null){
                durationGroup.setValue("No end date");
            } else {
                durationGroup.setValue("Ends on");
                endDatePicker.setValue(schedule.getEnd());
            }
        }
    }

    private void showDailyOptions() {
        patternOptions.removeAll();

        Text every = new Text("Every ");
        Text days = new Text(" days");
        HorizontalLayout numberOfDaysLayout = new HorizontalLayout();
        numberOfDaysLayout.add(every, intervalField, days);

        patternOptions.add(numberOfDaysLayout, excludeWeekends);
    }

    private void showWeeklyOptions() {
        patternOptions.removeAll();

        Text every = new Text("Every ");
        Text weeks = new Text(" weeks");
        HorizontalLayout numberOfWeeksLayout = new HorizontalLayout();
        numberOfWeeksLayout.add(every, intervalField, weeks);

        VerticalLayout daysLayout = new VerticalLayout(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        patternOptions.add(numberOfWeeksLayout);
        patternOptions.add(daysLayout);
    }

    private void showMonthlyOptions() {
        patternOptions.removeAll();

        Text every = new Text("Every ");
        Text month = new Text(" months");
        HorizontalLayout numberOfMonthsLayout = new HorizontalLayout();
        numberOfMonthsLayout.add(every, intervalField, month);

        HorizontalLayout numberFieldDiv = new HorizontalLayout();
        numberFieldDiv.add("Day of month");
        numberFieldDiv.add(dayOfMonthField);

        patternOptions.add(numberOfMonthsLayout, numberFieldDiv);
    }

    private void showYearlyOptions() {
        patternOptions.removeAll();

        Text every = new Text("Every ");
        Text year = new Text(" years");
        HorizontalLayout numberOfYearsLayout = new HorizontalLayout();
        numberOfYearsLayout.add(every, intervalField, year);

        VerticalLayout numberFieldDiv = new VerticalLayout();
        numberFieldDiv.add("On Day ");
        numberFieldDiv.add(dayInAMonthField);
        numberFieldDiv.add(" of Month ");
        numberFieldDiv.add(monthField);

        patternOptions.add(numberOfYearsLayout, numberFieldDiv);
    }
}
