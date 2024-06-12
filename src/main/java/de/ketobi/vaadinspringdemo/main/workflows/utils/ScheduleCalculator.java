package de.ketobi.vaadinspringdemo.main.workflows.utils;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowSchedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class ScheduleCalculator {

    public static LocalDate calculateNextRun(LocalDate start, WorkflowSchedule.SchedulePattern pattern, LocalDate now) {
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
        if (!pattern.getDaysOfWeek().isEmpty()) {
            DayOfWeek targetDay = DayOfWeek.valueOf(pattern.getDaysOfWeek().get(0).toUpperCase());
            return date.with(TemporalAdjusters.nextOrSame(targetDay));
        }
        return date;
    }

    private static LocalDate adjustToDayOfMonth(LocalDate date, WorkflowSchedule.SchedulePattern pattern) {
        if (!pattern.getDayOfMonth().isEmpty()) {
            int day = Math.min(date.lengthOfMonth(), pattern.getDayOfMonth().get(0));
            return date.withDayOfMonth(day);
        }
        return date;
    }

    private static LocalDate adjustToSpecificDate(LocalDate date, WorkflowSchedule.SchedulePattern pattern) {
        if (pattern.getMonthInAYear() > 0 && pattern.getDayInAMonth() > 0) {
            int day = Math.min(date.lengthOfMonth(), pattern.getDayInAMonth());
            return LocalDate.of(date.getYear(), pattern.getMonthInAYear(), day);
        }
        return date;
    }
}
