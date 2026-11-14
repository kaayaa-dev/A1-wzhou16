/**
 * Holds the message and any appointment created by one booking request.
 */
final class BookingResult {
    private final String message;
    private final Appointment appointment;

    /**
     * Stores the outcome of the current request.
     *
     * @param message the booking confirmation or rejection reason
     * @param appointment the newly added appointment, or null if rejected
     */
    BookingResult(String message, Appointment appointment) {
        this.message = message;
        this.appointment = appointment;
    }

    /** @return the confirmation or rejection message */
    String getMessage() {
        return message;
    }

    /** @return the appointment added by this request, or null if rejected */
    Appointment getAppointment() {
        return appointment;
    }
}