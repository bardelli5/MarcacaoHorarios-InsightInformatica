package com.example.marcacaohorarios.domain.usecase.impl;

import com.example.marcacaohorarios.app.dto.request.ClockingRequest;
import com.example.marcacaohorarios.app.dto.response.ClockingResponse;
import com.example.marcacaohorarios.cross.utils.ValidatorUtils;
import com.example.marcacaohorarios.cross.utils.WorkHourUtils;
import com.example.marcacaohorarios.domain.exception.InvalidClockingException;
import com.example.marcacaohorarios.domain.model.Clocking;
import com.example.marcacaohorarios.domain.model.WorkHour;
import com.example.marcacaohorarios.domain.usecase.ClockingUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@ApplicationScoped
public class ClockingUseCaseImpl implements ClockingUseCase {

    List<Clocking> createdClocking;

    public ClockingUseCaseImpl() {
        this.createdClocking = new ArrayList<>();
    }

    @Override
    public void validateRequest(ClockingRequest request) throws InvalidClockingException {
        Set<ConstraintViolation<ClockingRequest>> constraints = ValidatorUtils.validate(request);

        if (!constraints.isEmpty()) {
            String message = constraints.stream().findFirst().get().getMessage();
            throw new InvalidClockingException(message);
        }
    }

    @Override
    public List<ClockingResponse> newClocking(ClockingRequest request, List<WorkHour> createdWorkHour) {
        List<Overtime> overtime = findOvertime(request, createdWorkHour);
        List<Delay> delay = findDelay(request, createdWorkHour);

        List<String> formattedOvertime = overtime.stream().map(Overtime::toString).collect(Collectors.toList());
        List<String> formattedDelay = delay.stream().map(Delay::toString).collect(Collectors.toList());

        Clocking clocking = Clocking.builder()
                .entry(request.getEntry())
                .departure(request.getDeparture())
                .delay(formattedDelay)
                .overtime(formattedOvertime)
                .build();
        createdClocking.add(clocking);

        ClockingResponse.builder()
                .entry(request.getEntry())
                .departure(request.getDeparture())
                .delay(formattedDelay)
                .overtime(formattedOvertime)
                .build();
        return mapToResponse();
    }

    private List<Overtime> findOvertime(ClockingRequest request, List<WorkHour> createdWorkHour) {
        int entryClockingHour = WorkHourUtils.stringToHourInt(request.getEntry());
        int entryClockingMinute = WorkHourUtils.stringToMinuteInt(request.getEntry());

        int departureClockingHour = WorkHourUtils.stringToHourInt(request.getDeparture());
        int departureClockingMinute = WorkHourUtils.stringToMinuteInt(request.getDeparture());

        List<Overtime> overtimeEntry = createdWorkHour.stream().map(wh -> {
                    int entryWhHour = WorkHourUtils.stringToHourInt(wh.getEntry());
                    int entryWhMinute = WorkHourUtils.stringToMinuteInt(wh.getEntry());

                    if (entryWhHour == entryClockingHour) {
                        if (entryWhMinute < entryClockingMinute) return new Overtime(wh.getEntry(), request.getEntry());
                    }

                    if (entryWhHour > entryClockingHour) return new Overtime(request.getEntry(), wh.getEntry());

                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Overtime> overtimeDeparture = createdWorkHour.stream().map(wh -> {
                    int departureWhHour = WorkHourUtils.stringToHourInt(wh.getDeparture());
                    int departureWhMinute = WorkHourUtils.stringToMinuteInt(wh.getDeparture());

                    if (departureWhHour == departureClockingHour) {
                        if (departureWhMinute > departureClockingMinute)
                            return new Overtime(wh.getDeparture(), request.getDeparture());
                    }

                    if (departureWhHour < departureClockingHour)
                        return new Overtime(request.getDeparture(), wh.getDeparture());

                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Overtime> overtime = new ArrayList<>();
        overtime.addAll(overtimeEntry);
        overtime.addAll(overtimeDeparture);

        List<Overtime> duplicateEntries = overtime.stream().filter(ot -> ot.hour1.equals(request.getEntry()))
                .collect(Collectors.toList());

        if (duplicateEntries.size() > 1) {
            removeDuplicateOvertime(duplicateEntries, overtime, true);
        }

        List<Overtime> duplicateDepartures = overtime.stream().filter(ot -> ot.hour1.equals(request.getDeparture()))
                .collect(Collectors.toList());

        if (duplicateDepartures.size() > 1) {
            removeDuplicateOvertime(duplicateDepartures, overtime, false);
        }

        if (createdWorkHour.size() > 1) {
            int departureHour = WorkHourUtils.stringToHourInt(request.getDeparture());
            int firstWorkHour = WorkHourUtils.stringToHourInt(createdWorkHour.get(0).getDeparture());
            int secondWorkHour = WorkHourUtils.stringToHourInt(createdWorkHour.get(1).getEntry());
            if (departureHour > firstWorkHour && departureHour > secondWorkHour) {
                overtime.add(1, new Overtime(createdWorkHour.get(0).getDeparture(), createdWorkHour.get(1).getEntry()));
            }
        }
        return overtime;
    }

    private void removeDuplicateOvertime(List<Overtime> duplicates, List<Overtime> overtimes, boolean smaller) {
        AtomicReference<Integer> correctHour =
                new AtomicReference<>(WorkHourUtils.stringToHourInt(duplicates.get(0).hour2));

        if (smaller) {
            duplicates.forEach(ot -> {
                int hour = WorkHourUtils.stringToHourInt(ot.hour2);
                if (hour < correctHour.get()) correctHour.set(hour);
            });
        } else {
            duplicates.forEach(ot -> {
                int hour = WorkHourUtils.stringToHourInt(ot.hour2);
                if (hour > correctHour.get()) correctHour.set(hour);
            });
        }

        List<Overtime> duplicateToRemove = duplicates.stream()
                .filter(ot -> WorkHourUtils.stringToHourInt(ot.hour2) != correctHour.get())
                .collect(Collectors.toList());

        overtimes.removeAll(duplicateToRemove);
    }

    private List<Delay> findDelay(ClockingRequest request, List<WorkHour> createdWorkHour) {
        int entryClockingHour = WorkHourUtils.stringToHourInt(request.getEntry());
        int entryClockingMinute = WorkHourUtils.stringToMinuteInt(request.getEntry());

        int departureClockingHour = WorkHourUtils.stringToHourInt(request.getDeparture());
        int departureClockingMinute = WorkHourUtils.stringToMinuteInt(request.getDeparture());

        List<Delay> delayEntry = createdWorkHour.stream().map(wh -> {
                    int entryWhHour = WorkHourUtils.stringToHourInt(wh.getEntry());
                    int entryWhMinute = WorkHourUtils.stringToMinuteInt(wh.getEntry());

                    int departureWhHour = WorkHourUtils.stringToHourInt(wh.getDeparture());

                    if (entryWhHour == entryClockingHour) {
                        if (entryWhMinute > entryClockingMinute) return new Delay(wh.getEntry(), request.getEntry());
                    }

                    if (entryClockingHour > entryWhHour && entryClockingHour < departureWhHour)
                        return new Delay(wh.getEntry(), request.getEntry());

                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Delay> delayDeparture = createdWorkHour.stream().map(wh -> {
                    int departureWhHour = WorkHourUtils.stringToHourInt(wh.getDeparture());
                    int departureWhMinute = WorkHourUtils.stringToMinuteInt(wh.getDeparture());

                    if (departureWhHour == departureClockingHour) {
                        if (departureClockingMinute < departureWhMinute)
                            return new Delay(request.getDeparture(), wh.getDeparture());
                    }

                    if (departureClockingHour < departureWhHour) return new Delay(request.getDeparture(), wh.getDeparture());

                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Delay> delays = new ArrayList<>();
        delays.addAll(delayEntry);
        delays.addAll(delayDeparture);

        return delays;
    }

    private List<ClockingResponse> mapToResponse() {
        return createdClocking.stream().map(c ->
                ClockingResponse.builder()
                        .entry(c.getEntry())
                        .departure(c.getDeparture())
                        .overtime(c.getOvertime())
                        .delay(c.getDelay())
                        .build()
        ).collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    private static class Overtime {
        String hour1;
        String hour2;

        public String toString() {
            return hour1 + " " + hour2;
        }
    }

    @Data
    @AllArgsConstructor
    private static class Delay {
        String hour1;
        String hour2;

        public String toString() {
            return hour1 + " " + hour2;
        }
    }
}