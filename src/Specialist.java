/**
 * Represents a health professional of type Specialist.
 * The creation methods check the common staff details and the specialty.
 */
public final class Specialist extends HealthProfessional implements Prescribable {
    private static final String DEFAULT_SPECIALTY = "General Medicine";
    private String specialty;

    /**
     * Stores the validated staff details and specialty.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     * @param specialty a nonblank specialty
     */
    private Specialist(int id, String name, String[] workingDays, String specialty) {
        super(id, name, workingDays);
        this.specialty = specialty.trim();
    }

    /**
     * Creates a professional with the default specialty.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     * @return the new professional, or null if the staff details are invalid
     */
    public static Specialist create(int id, String name, String[] workingDays) {
        return create(id, name, workingDays, DEFAULT_SPECIALTY);
    }

    /**
     * Creates a professional with a custom specialty after checking the details.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     * @param specialty a nonblank specialty
     * @return the new professional, or null if the staff details or specialty are invalid
     */
    public static Specialist create(int id, String name, String[] workingDays,
            String specialty) {
        if (!validDetails(id, name, workingDays) || !hasText(specialty)) {
            return null;
        }
        return new Specialist(id, name, workingDays, specialty);
    }

    // Returns the specialist's current specialty.
    public String getSpecialty() {
        return specialty;
    }

    // Updates the specialist's specialty after trimming leading and trailing spaces.
    // Keeps the current value if the new value is null, empty, or only spaces.
    public void setSpecialty(String specialty) {
        if (hasText(specialty)) {
            this.specialty = specialty.trim();
        }
    }

    // Returns "Specialist" as the professional type.
    @Override
    public String getProfessionalType() {
        return "Specialist";
    }

    /**
     * Describes the work performed by this professional.
     *
     * @return the role description
     */
    @Override
    public String performRole() {
        return "Assesses and treats conditions in a specialty.";
    }

    /**
     * Describes a simulated prescription issued by this professional.
     *
     * @param medication the requested medication name
     * @return the prescription summary or a warning if the name is missing
     */
    @Override
    public String prescribe(String medication) {
        if (!hasText(medication)) {
            return "Prescription warning: enter a medication name.";
        }
        return getName() + " prescribed " + medication.trim() + " (" + specialty + ").";
    }

    /**
     * Formats the common details and the specialty.
     *
     * @return the complete description
     */
    @Override
    public String toString() {
        return super.toString() + ", specialty=" + specialty;
    }
}