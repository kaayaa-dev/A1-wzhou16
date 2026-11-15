import java.util.ArrayList;

/**
 * Manages one booking day's appointments and enforces the clinic's booking rules.
 * The constructor checks the booking day. This manager validates booking, rescheduling and cancellation
 * requests before changing its list. It validates filter inputs before searching.
 */
public final class AppointmentManager {
    private static final int OPENING_HOUR = 8;
    private static final int CLOSING_HOUR = 16;
    private static final int SLOT_MINUTES = 30;
    private static final int MINUTES_PER_HOUR = 60;

    private final String bookingDay;
    private final boolean validBookingDay;
    private final ArrayList<Appointment> appointments;
    private final ArrayList<Appointment> cancelledAppointments;

    /**
     * Opens a booking list for one simulated current day.
     * An invalid day leaves the list unable to accept bookings.
     *
     * @param bookingDay a full English day name from Monday to Sunday, with standard capitalization
     */
    public AppointmentManager(String bookingDay) {
        this.bookingDay = bookingDay;
        this.validBookingDay = HealthProfessional.isDayName(bookingDay);
        appointments = new ArrayList<>();
        cancelledAppointments = new ArrayList<>();
    }

    /**
     * Gives the day represented by this booking list.
     *
     * @return the booking day's name
     */
    public String getBookingDay() {
        return bookingDay;
    }

    /**
     * Finds the start times still available for one professional on the booking day.
     * This query leaves the appointment records as they are.
     *
     * @param professional the health professional whose schedule to check
     * @return a new list in time order, empty for invalid input, a day off or a full schedule
     */
    public ArrayList<String> getAvailableSlots(HealthProfessional professional) {
        ArrayList<String> availableSlots = new ArrayList<>();
        if (!validBookingDay || professional == null || !professional.worksOn(bookingDay)) {
            return availableSlots;
        }

        for (int start = OPENING_HOUR * MINUTES_PER_HOUR;
                start < CLOSING_HOUR * MINUTES_PER_HOUR; start += SLOT_MINUTES) {
            String time = twoDigits(start / MINUTES_PER_HOUR) + ":"
                    + twoDigits(start % MINUTES_PER_HOUR);
            if (!isSlotBooked(professional, time, null)) {
                availableSlots.add(time);
            }
        }
        return availableSlots;
    }

    /**
     * Adds the leading zero needed for single-digit hours and minutes.
     *
     * @param value the hour or minute to display
     * @return the two-digit text
     */
    private static String twoDigits(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return "" + value;
    }

    /**
     * Checks whether a professional ID already has an appointment at the given start time.
     *
     * @param professional the professional whose ID to match
     * @param time the start time in HH:mm format
     * @param original the appointment being moved, or null for a new booking or query
     * @return whether the slot is occupied for this professional
     */
    private boolean isSlotBooked(HealthProfessional professional, String time, Appointment original) {
        // Separate objects with the same professional ID share the same slots.
        for (Appointment existing : appointments) {
            if (existing != original && existing.getProfessional().equals(professional)
                    && existing.getTime().equals(time)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Books a start time on the clinic's configured slot grid.
     * The patient needs a name and valid phone number. The professional must work
     * on the booking day and have no existing appointment in the requested slot.
     *
     * @param patient the non-null patient requesting the booking
     * @param professional the non-null professional requested
     * @param time the requested start time in 24-hour HH:mm format
     * @return a booking confirmation or a message explaining why the request was rejected
     */
    public String addAppointment(Patient patient, HealthProfessional professional,
            String time) {
        return submitAppointment(patient, professional, time).getMessage();
    }

    /**
     * Submits a booking and returns the outcome of this request.
     *
     * @param patient the patient requesting an appointment
     * @param professional the requested professional
     * @param time the requested start time in HH:mm format
     * @return a message and the newly added appointment, if the request succeeded
     */
    BookingResult submitAppointment(Patient patient, HealthProfessional professional, String time) {
        String error = bookingError(patient, professional, time, null);
        if (error != null) {
            return new BookingResult("Booking rejected: " + error, null);
        }
        Appointment appointment = Appointment.create(patient, professional, time);
        if (appointment == null) {
            return new BookingResult("Booking rejected: unable to create the appointment.", null);
        }
        appointments.add(appointment);
        return new BookingResult("Booked " + time + " with professional " + professional.getId() + ".",
                appointment);
    }

    /**
     * Checks the requested slot for a new or replacement booking.
     *
     * @param patient the patient requesting the booking
     * @param professional the requested professional
     * @param time the requested start time
     * @param original the booking being moved, or null for a new booking
     * @return the rejection reason, or null if the request passes every check
     */
    private String bookingError(Patient patient, HealthProfessional professional, String time,
            Appointment original) {
        if (!validBookingDay) {
            return "enter a valid booking day.";
        }
        String error = Appointment.validationError(patient, professional, time);
        if (error != null) {
            return error;
        }

        int hour = Appointment.hourOf(time);
        int minute = Appointment.minuteOf(time);
        if (hour < OPENING_HOUR || hour >= CLOSING_HOUR) {
            return time + " is outside clinic hours.";
        }
        // Anchor the grid at opening so the slot interval can change independently.
        int minutesSinceOpening = (hour - OPENING_HOUR) * MINUTES_PER_HOUR + minute;
        if (minutesSinceOpening % SLOT_MINUTES != 0) {
            return time + " is outside the " + SLOT_MINUTES
                    + "-minute slot grid.";
        }
        if (!professional.worksOn(getBookingDay())) {
            return "professional " + professional.getId()
                    + " does not work on " + bookingDay + ".";
        }

        if (isSlotBooked(professional, time, original)) {
            return "professional " + professional.getId()
                    + " already has an appointment at " + time + ".";
        }

        return null;
    }

    /**
     * Finds the original booking by professional ID and start time, then verifies the phone number.
     * A matching new ID and time keeps the original booking. A valid change replaces it at the
     * same list position. A rejected request leaves every existing booking in place.
     *
     * @param phone the booked patient's ten-digit phone number
     * @param originalId the original professional's positive ID
     * @param originalTime the original start time in HH:mm format
     * @param newProfessional the requested professional
     * @param newTime the requested start time in HH:mm format
     * @return a confirmation, a message that the original was kept, or a warning with the reason
     */
    public String rescheduleAppointment(String phone, int originalId, String originalTime,
            HealthProfessional newProfessional, String newTime) {
        if (!Patient.isValidPhone(phone)) {
            return "Reschedule warning: enter a valid phone number.";
        }
        if (originalId <= 0 || !Appointment.hasTimeFormat(originalTime)
                || !Appointment.isClockTime(originalTime)) {
            return "Reschedule warning: enter a valid original professional ID and time.";
        }
        int index = findAppointmentIndex(originalId, originalTime);
        if (index == -1) {
            return "Reschedule warning: original appointment was not found.";
        }
        Appointment original = appointments.get(index);
        Patient patient = original.getPatient();
        if (!patient.getPhone().equals(phone)) {
            return "Reschedule warning: phone number does not match the original appointment.";
        }
        if (original.getProfessional().equals(newProfessional) && originalTime.equals(newTime)) {
            return "Kept the existing " + originalTime + " appointment with professional "
                    + originalId + ".";
        }
        String error = bookingError(patient, newProfessional, newTime, original);
        if (error != null) {
            return "Reschedule warning: " + error;
        }
        Appointment replacement = Appointment.create(patient, newProfessional, newTime);
        if (replacement == null) {
            return "Reschedule warning: unable to create the replacement appointment.";
        }
        appointments.set(index, replacement);
        return "Rescheduled from " + originalTime + " with professional " + originalId
                + " to " + newTime + " with professional " + newProfessional.getId() + ".";
    }

    /**
     * Gets the bookings in their current insertion order.
     *
     * @return a new list of all bookings
     */
    public ArrayList<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    /**
     * Finds bookings for one professional.
     *
     * @param professionalId the positive ID to match
     * @return matching bookings, or a warning and an empty list if the ID is invalid
     */
    public FilterResult filterByProfessionalId(int professionalId) {
        ArrayList<Appointment> matches = new ArrayList<>();
        if (professionalId <= 0) {
            return new FilterResult(matches, "Filter rejected: enter a positive professional ID.");
        }
        for (Appointment appointment : appointments) {
            if (appointment.getProfessional().getId() == professionalId) {
                matches.add(appointment);
            }
        }
        return new FilterResult(matches, null);
    }

    /**
     * Finds bookings for one patient phone number.
     *
     * @param phone the ten-digit phone number to match
     * @return matching bookings, or a warning and an empty list if the number is invalid
     */
    public FilterResult filterByPhone(String phone) {
        return filterByPhone(appointments, phone);
    }

    /**
     * Finds successfully cancelled bookings for the patient, including records that were rebooked.
     * The returned list is separate from the manager's cancellation history.
     *
     * @param phone the patient's ten-digit phone number
     * @return matching cancelled bookings or a warning for an invalid number
     */
    public FilterResult filterCancelledByPhone(String phone) {
        return filterByPhone(cancelledAppointments, phone);
    }

    /**
     * Filters a list of active or cancelled bookings by patient phone number.
     *
     * @param source the booking list to search
     * @param phone the ten-digit phone number to match
     * @return a new matching list or a warning for an invalid number
     */
    private FilterResult filterByPhone(ArrayList<Appointment> source, String phone) {
        ArrayList<Appointment> matches = new ArrayList<>();
        if (!Patient.isValidPhone(phone)) {
            return new FilterResult(matches, "Filter rejected: enter a valid phone number.");
        }
        for (Appointment appointment : source) {
            if (appointment.getPatient().getPhone().equals(phone)) {
                matches.add(appointment);
            }
        }
        return new FilterResult(matches, null);
    }

    /**
     * Sorts a copy of the supplied bookings from earliest to latest.
     * Bookings at the same time keep their relative order.
     *
     * @param source a non-null list containing only non-null appointments
     * @return a new list in time order, with equal times in their original order
     */
    public ArrayList<Appointment> sortAppointments(ArrayList<Appointment> source) {
        ArrayList<Appointment> sorted = new ArrayList<>(source);
        for (int index = 1; index < sorted.size(); index++) {
            Appointment current = sorted.get(index);
            int position = index;
            // Move strictly later bookings so entries with equal times keep their order.
            while (position > 0 && sorted.get(position - 1).compareTo(current) > 0) {
                sorted.set(position, sorted.get(position - 1));
                position--;
            }
            sorted.set(position, current);
        }
        return sorted;
    }

    /**
     * Cancels the booking identified by a professional ID and start time and records it in history.
     * If the input is invalid or no booking matches, the method returns a warning
     * and leaves the booking list as it was.
     *
     * @param professionalId the positive professional ID to match
     * @param time the start time in 24-hour HH:mm format
     * @return a cancellation confirmation or a warning explaining why it failed
     */
    public String cancelAppointment(int professionalId, String time) {
        if (professionalId <= 0) {
            return "Cancellation warning: enter a positive professional ID.";
        }
        if (!Appointment.hasTimeFormat(time) || !Appointment.isClockTime(time)) {
            return "Cancellation warning: enter a valid time in HH:mm.";
        }

        int index = findAppointmentIndex(professionalId, time);
        if (index != -1) {
            cancelledAppointments.add(appointments.remove(index));
            return "Cancelled the " + time + " appointment with professional " + professionalId + ".";
        }
        return "Cancellation warning: no appointment with professional " + professionalId
                + " at " + time + ".";
    }

    /**
     * Locates the booking at a professional's start time.
     *
     * @param professionalId the ID to match
     * @param time the start time to match
     * @return the matching list index, or -1 when there is no match
     */
    private int findAppointmentIndex(int professionalId, String time) {
        for (int index = 0; index < appointments.size(); index++) {
            Appointment appointment = appointments.get(index);
            if (appointment.getProfessional().getId() == professionalId
                    && appointment.getTime().equals(time)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Cancels and records every booking for a given phone number. If the number is invalid
     * or has no matching appointments, a warning is returned and the list stays as it was.
     *
     * @param phone the ten-digit phone number to match
     * @return a warning or a confirmation with the number of bookings removed
     */
    public String cancelAppointmentsByPhone(String phone) {
        if (!Patient.isValidPhone(phone)) {
            return "Cancellation warning: enter a valid phone number.";
        }
        int removed = 0;
        // Scanning backwards avoids skipping the next match when an item is removed.
        for (int index = appointments.size() - 1; index >= 0; index--) {
            if (appointments.get(index).getPatient().getPhone().equals(phone)) {
                cancelledAppointments.add(appointments.remove(index));
                removed++;
            }
        }
        if (removed == 0) {
            return "Cancellation warning: no appointments for " + phone + ".";
        }
        return "Cancelled " + removed + " appointments for " + phone + ".";
    }
}