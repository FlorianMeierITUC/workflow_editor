package de.ketobi.vaadinspringdemo.main.workflows.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;

import java.time.LocalDate;

public class ScheduleWorkflowsDialog extends Dialog {
    private final VerticalLayout patternOptions = new VerticalLayout();
    private final Div durationOptions = new Div();

    public ScheduleWorkflowsDialog() {
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
            } else if (event.getValue().equals("Yearly")) {
                // Show yearly options
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
        Button okButton = new Button("Save", event -> close());

        // Layout
        VerticalLayout layout = new VerticalLayout();
        layout.add(patternLayout, durationLayout, okButton);
        layout.setSizeFull();
        layout.setPadding(true);
        layout.setSpacing(true);

        add(layout);
    }

    private void showDailyOptions() {
        patternOptions.removeAll();
        Div numberFieldDiv = new Div();
        RadioButtonGroup<String> dailyOptions = new RadioButtonGroup<>();
        dailyOptions.setItems("Every day", "Every # days", "Every Workday");
        dailyOptions.setValue("Every day");
        dailyOptions.addValueChangeListener(event -> {
            if (event.getValue().equals("Every # days")) {
                numberFieldDiv.removeAll();
                NumberField everyField = new NumberField("#");
                everyField.setValue(2d);
                numberFieldDiv.add(everyField);
            } else {
                numberFieldDiv.removeAll();
            }
        });
        patternOptions.add(dailyOptions, numberFieldDiv);
    }

    private void showWeeklyOptions() {
        patternOptions.removeAll();

        Text every = new Text("Every ");
        NumberField everyField = new NumberField();
        Text weeks = new Text(" weeks");
        HorizontalLayout numberOfWeeksLayout = new HorizontalLayout();
        numberOfWeeksLayout.add(every, everyField, weeks);

        Checkbox monday = new Checkbox("Monday");
        Checkbox tuesday = new Checkbox("Tuesday");
        Checkbox wednesday = new Checkbox("Wednesday");
        Checkbox thursday = new Checkbox("Thursday");
        Checkbox friday = new Checkbox("Friday");
        Checkbox saturday = new Checkbox("Saturday");
        Checkbox sunday = new Checkbox("Sunday");
        VerticalLayout daysLayout = new VerticalLayout(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        patternOptions.add(numberOfWeeksLayout);
        patternOptions.add(daysLayout);
    }
}
