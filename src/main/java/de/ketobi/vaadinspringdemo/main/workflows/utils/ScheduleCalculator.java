package de.ketobi.vaadinspringdemo.main.workflows.utils;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowSchedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleCalculator {

    public static LocalDate calculateNextRun(LocalDate start, WorkflowSchedule.SchedulePattern pattern, LocalDate now) {
        System.out.println("Calculating next run for pattern "+pattern+" with start date "+start+" and now "+now);
        if (start == null || now.isBefore(start)) {
            return null; // No run scheduled before the start date
        }

        LocalDate nextRun = start;
        switch (pattern.getType()) {
            case "Daily":
                nextRun = calculateFixedIntervalRun(start, now, pattern.getInterval(), ChronoUnit.DAYS);
                break;
            case "Weekly":
                nextRun = calculateFixedIntervalRun(start, now, pattern.getInterval() * 7, ChronoUnit.DAYS);
                nextRun = adjustToNextWeekday(nextRun, pattern);
                break;
            case "Monthly":
                nextRun = calculateFixedIntervalRun(start, now, pattern.getInterval(), ChronoUnit.MONTHS);
                nextRun = adjustToDayOfMonth(nextRun, pattern);
                break;
            case "Yearly":
                nextRun = calculateFixedIntervalRun(start, now, pattern.getInterval(), ChronoUnit.YEARS);
                nextRun = adjustToSpecificDate(nextRun, pattern);
                break;
                default:
                    throw new IllegalArgumentException("Unsupported schedule pattern type: " + pattern.getType());
        }
        return nextRun;
    }

    private static LocalDate calculateFixedIntervalRun(LocalDate start, LocalDate now, long interval, ChronoUnit unit) {
        LocalDate nextRun = start;
        while (!nextRun.isAfter(now)) {
            nextRun = nextRun.plus(interval, unit);
        }
        return nextRun;
    }

    private static LocalDate adjustToNextWeekday(LocalDate date, WorkflowSchedule.SchedulePattern pattern) {
        if (pattern.getDaysOfWeek().isEmpty()) {
            return date;
        }

        // Convert integer days to DayOfWeek enum sorted list
        List<DayOfWeek> daysOfWeek = pattern.getDaysOfWeek().stream()
                .map(day -> DayOfWeek.of(day % 7 + 1)) // Convert 1-7 to DayOfWeek enum (where 1 = Monday, 7 = Sunday)
                .sorted()
                .collect(Collectors.toList());

        // Check for the next or same valid day from the sorted list
        LocalDate nextValidDay = date;
        for (DayOfWeek day : daysOfWeek) {
            if (nextValidDay.getDayOfWeek().getValue() > day.getValue() || nextValidDay.getDayOfWeek() == day) {
                nextValidDay = nextValidDay.with(TemporalAdjusters.nextOrSame(day));
                break;
            }
            nextValidDay = nextValidDay.with(TemporalAdjusters.next(day));
        }

        return nextValidDay;
    }

    private static LocalDate adjustToDayOfMonth(LocalDate date, WorkflowSchedule.SchedulePattern pattern) {
        if (null != pattern.getDayOfMonth()) {
            int day = Math.min(date.lengthOfMonth(), pattern.getDayOfMonth());
            return date.withDayOfMonth(day);
        }
        return date;
    }

    private static LocalDate adjustToSpecificDate(LocalDate date, WorkflowSchedule.SchedulePattern pattern) {
        if (pattern.getMonthInAYear() != null && pattern.getDayInAMonth() != null && pattern.getMonthInAYear() > 0 && pattern.getDayInAMonth() > 0) {
            int day = Math.min(date.lengthOfMonth(), pattern.getDayInAMonth());
            return LocalDate.of(date.getYear(), pattern.getMonthInAYear(), day);
        }
        return date;
    }

    public boolean workflowIsDue(WorkflowSchedule schedule){
        //TODO exclude weekends if it is set
        //TODO exclude inactive workflows
        //TODO calculate if a workflow is due on this day
        //TODO check if the workflow ran on this day
        //TODO check if the end date was reached
        //TODO check if the start date is set and reached
        //TODO calculate the last due date and check if the workflow ran on that day if not execute the workflow immediately
        //without any changes to the schedule

        LocalDate now = LocalDate.now();
        LocalDate start = schedule.getStart();
        LocalDate end = schedule.getEnd();
        LocalDate lastRun = schedule.getLastRun();

        return true;
    }
}
