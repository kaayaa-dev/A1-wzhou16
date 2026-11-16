/**
 * Links one patient to one professional at a time of day.
 * A draft can change its time before it is submitted.
 * AppointmentManager checks the clinic rules before storing a booking with a locked time.
 */
public final class Appointment implements Comparable<Appointment> {
    private static final int TIME_LENGTH = 5;
    private static final int COLON_INDEX = 2;
    private static final int LAST_HOUR = 23;
    private static final int LAST_MINUTE = 59;

    private final Patient patient;
    private final HealthProfessional professional;
    private String time;
    private final boolean timeLocked;

    /**
     * Stores the details after the creation method has validated them.
     *
     * @param patient a non-null patient with a nonblank name and a ten-digit phone number
     * @param professional a non-null health professional
     * @param time a valid 24-hour clock time in HH:mm format
     * @param timeLocked whether the time is locked
     */
    private Appointment(Patient patient, HealthProfessional professional, String time,
            boolean timeLocked) {
        this.patient = patient;
        this.professional = professional;
        this.time = time;
        this.timeLocked = timeLocked;
    }

    /**
     * Creates a draft after checking its required fields and clock time.
     *
     * @param patient a non-null patient with a nonblank name and a ten-digit phone number
     * @param professional a non-null health professional
     * @param time a valid 24-hour clock time in HH:mm format
     * @return the draft, or null if any required detail is missing or invalid
     */
    public static Appointment create(Patient patient, HealthProfessional professional, String time) {
        return create(patient, professional, time, false);
    }

    /**
     * Creates a booking with a locked time after the manager has checked the clinic rules.
     *
     * @param patient the patient requesting the booking
     * @param professional the chosen health professional
     * @param time the checked start time
     * @return the locked booking, or null if a required detail is invalid
     */
    static Appointment createConfirmed(Patient patient, HealthProfessional professional, String time) {
        return create(patient, professional, time, true);
    }

    /**
     * Uses the same field checks for a draft and a confirmed booking.
     *
     * @param patient the proposed patient
     * @param professional the proposed professional
     * @param time the proposed start time
     * @param timeLocked whether the time should be locked
     * @return the new appointment, or null if a required detail is invalid
     */
    private static Appointment create(Patient patient, HealthProfessional professional,
            String time, boolean timeLocked) {
        if (validationError(patient, professional, time) != null) {
            return null;
        }
        return new Appointment(patient, professional, time, timeLocked);
    }

    /**
     * Checks the fields that every appointment needs. The manager uses the same
     * check so it can return a booking rejection before applying clinic rules.
     *
     * @param patient the proposed patient
     * @param professional the proposed health professional
     * @param time the proposed start time
     * @return the first validation error, or null when the details are valid
     */
    static String validationError(Patient patient, HealthProfessional professional, String time) {
        if (patient == null || professional == null || time == null) {
            return "patient, professional and time are required.";
        }
        if (!HealthProfessional.hasText(patient.getName())
                || !Patient.isValidPhone(patient.getPhone())) {
            return "a patient name and valid phone number are required.";
        }
        if (!hasTimeFormat(time)) {
            return "time must use HH:mm.";
        }
        if (!isClockTime(time)) {
            return "the time is outside the 24-hour clock range.";
        }
        return null;
    }

    /**
     * Checks the shape of a time string before converting its digits.
     *
     * @param time the time string
     * @return whether it contains two digits, a colon and two digits
     */
    static boolean hasTimeFormat(String time) {
        if (time == null || time.length() != TIME_LENGTH || time.charAt(COLON_INDEX) != ':') {
            return false;
        }
        for (int index = 0; index < time.length(); index++) {
            if (index != COLON_INDEX) {
                char digit = time.charAt(index);
                if (digit < '0' || digit > '9') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks the clock range of a string whose format has been checked.
     *
     * @param time the formatted time
     * @return whether the hour and minute fit a 24-hour clock
     */
    static boolean isClockTime(String time) {
        return hourOf(time) <= LAST_HOUR && minuteOf(time) <= LAST_MINUTE;
    }

    /**
     * Gets the hour from a formatted time.
     *
     * @param time the formatted time
     * @return the hour
     */
    static int hourOf(String time) {
        return Integer.parseInt(time.substring(0, COLON_INDEX));
    }

    /**
     * Gets the minute from a formatted time.
     *
     * @param time the formatted time
     * @return the minute
     */
    static int minuteOf(String time) {
        return Integer.parseInt(time.substring(COLON_INDEX + 1));
    }

    // Returns the patient for this appointment.
    public Patient getPatient() {
        return patient;
    }

    // Returns the health professional for this appointment.
    public HealthProfessional getProfessional() {
        return professional;
    }

    // Returns the appointment time in HH:mm format.
    public String getTime() {
        return time;
    }

    // Updates the time of a draft if it is a valid 24-hour time in HH:mm format.
    // Keeps the current time if the booking is confirmed or the new time is invalid.
    public void setTime(String time) {
        if (!timeLocked && hasTimeFormat(time) && isClockTime(time)) {
            this.time = time;
        }
    }

    /**
     * Compares appointments by hour, then by minute. Equal times share a sort position.
     *
     * @param other the appointment to compare
     * @return a negative number, zero or a positive number in time order
     */
    @Override
    public int compareTo(Appointment other) {
        int hourDifference = hourOf(time) - hourOf(other.time);
        if (hourDifference != 0) {
            return hourDifference;
        }
        return minuteOf(time) - minuteOf(other.time);
    }

    /**
     * Formats the time, patient and full professional details.
     *
     * @return the appointment description
     */
    @Override
    public String toString() {
        return time + " | " + patient + " | " + professional;
    }
}