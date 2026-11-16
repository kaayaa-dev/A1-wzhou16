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

    // Returns the confirmation or rejection message for this request.
    String getMessage() {
        return message;
    }

    // Returns the appointment added by this request, or null if it was rejected.
    Appointment getAppointment() {
        return appointment;
    }
}