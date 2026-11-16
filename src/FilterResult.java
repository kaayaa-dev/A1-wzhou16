import java.util.ArrayList;

/**
 * Holds the matches and any warning from a booking filter.
 * AppointmentManager validates the query and supplies a new list for each result.
 */
public final class FilterResult {
    private final ArrayList<Appointment> matches;
    private final String warning;

    /**
     * Stores the results of a filter request.
     *
     * @param matches the non-null list of matches, empty if the query was rejected or found nothing
     * @param warning the reason a query was rejected, or null for a valid query
     */
    FilterResult(ArrayList<Appointment> matches, String warning) {
        this.matches = matches;
        this.warning = warning;
    }

    // Returns the matching appointments in the filter result's own list.
    public ArrayList<Appointment> getMatches() {
        return matches;
    }

    // Returns the warning for an invalid query, or null if the query was valid.
    public String getWarning() {
        return warning;
    }
}