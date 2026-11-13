/**
 * Represents a health professional of type Physiotherapist.
 * The creation methods check the common staff details and the focus area.
 */
public final class Physiotherapist extends HealthProfessional {
    private static final String DEFAULT_FOCUS_AREA = "General Rehabilitation";
    private final String focusArea;

    /**
     * Stores the validated staff details and focus area.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     * @param focusArea a nonblank focus area
     */
    private Physiotherapist(int id, String name, String[] workingDays, String focusArea) {
        super(id, name, workingDays);
        this.focusArea = focusArea.trim();
    }

    /**
     * Creates a professional with the default focus area.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     * @return the new professional, or null if the staff details are invalid
     */
    public static Physiotherapist create(int id, String name, String[] workingDays) {
        return create(id, name, workingDays, DEFAULT_FOCUS_AREA);
    }

    /**
     * Creates a professional with a custom focus area after checking the details.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     * @param focusArea a nonblank focus area
     * @return the new professional, or null if the staff details or focus area are invalid
     */
    public static Physiotherapist create(int id, String name, String[] workingDays,
            String focusArea) {
        if (!validDetails(id, name, workingDays) || !hasText(focusArea)) {
            return null;
        }
        return new Physiotherapist(id, name, workingDays, focusArea);
    }

    /**
     * Gets the focus area.
     *
     * @return the focus area
     */
    public String getFocusArea() {
        return focusArea;
    }

    /**
     * Gets the professional type.
     *
     * @return the type to display
     */
    @Override
    public String getProfessionalType() {
        return "Physiotherapist";
    }

    /**
     * Describes the work performed by this professional.
     *
     * @return the role description
     */
    @Override
    public String performRole() {
        return "Supports recovery through movement and exercise.";
    }

    /**
     * Simulates preparing an exercise plan for the physiotherapist's focus area.
     *
     * @return the simulated action and its setting
     */
    public String createExercisePlan() {
        return getName() + " created an exercise plan for " + focusArea + ".";
    }

    /**
     * Formats the common details and the focus area.
     *
     * @return the complete description
     */
    @Override
    public String toString() {
        return super.toString() + ", focus area=" + focusArea;
    }
}