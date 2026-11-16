/**
 * Represents a health professional of type Nurse.
 * The creation methods check the common staff details and the department.
 */
public final class Nurse extends HealthProfessional {
    private static final String DEFAULT_DEPARTMENT = "Outpatient Care";
    private String department;

    /**
     * Stores the validated staff details and department.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     * @param department a nonblank department
     */
    private Nurse(int id, String name, String[] workingDays, String department) {
        super(id, name, workingDays);
        this.department = department.trim();
    }

    /**
     * Creates a professional with the default department.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     * @return the new professional, or null if the staff details are invalid
     */
    public static Nurse create(int id, String name, String[] workingDays) {
        return create(id, name, workingDays, DEFAULT_DEPARTMENT);
    }

    /**
     * Creates a professional with a custom department after checking the details.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     * @param department a nonblank department
     * @return the new professional, or null if the staff details or department are invalid
     */
    public static Nurse create(int id, String name, String[] workingDays,
            String department) {
        if (!validDetails(id, name, workingDays) || !hasText(department)) {
            return null;
        }
        return new Nurse(id, name, workingDays, department);
    }

    // Returns the nurse's current department.
    public String getDepartment() {
        return department;
    }

    // Updates the nurse's department after trimming leading and trailing spaces.
    // Keeps the current value if the new value is null, empty, or only spaces.
    public void setDepartment(String department) {
        if (hasText(department)) {
            this.department = department.trim();
        }
    }

    // Returns "Nurse" as the professional type.
    @Override
    public String getProfessionalType() {
        return "Nurse";
    }

    /**
     * Describes the work performed by this professional.
     *
     * @return the role description
     */
    @Override
    public String performRole() {
        return "Monitors patients and provides nursing care.";
    }

    /**
     * Simulates taking a blood sample in the nurse's department.
     *
     * @return the simulated action and its setting
     */
    public String takeBloodSample() {
        return getName() + " collected a blood sample in " + department + ".";
    }

    /**
     * Formats the common details and the department.
     *
     * @return the complete description
     */
    @Override
    public String toString() {
        return super.toString() + ", department=" + department;
    }
}