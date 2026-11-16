import java.util.ArrayList;

/**
 * Holds the sorted appointments and any warning from a sort request.
 */
public final class SortResult {
    private final ArrayList<Appointment> appointments;
    private final String warning;

    /**
     * Stores the result returned by the manager.
     *
     * @param appointments the sorted list, or an empty list when sorting fails
     * @param warning the reason sorting failed, or null for a successful sort
     */
    SortResult(ArrayList<Appointment> appointments, String warning) {
        this.appointments = appointments;
        this.warning = warning;
    }

    // Returns the sorted appointments in a separate list.
    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    // Returns the sorting warning, or null if sorting succeeded.
    public String getWarning() {
        return warning;
    }
}