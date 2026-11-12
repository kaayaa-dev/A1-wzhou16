/**
 * Represents a health professional of type General Practitioner.
 * The caller checks the common staff details and provides a nonblank clinic
 * when using the constructor with a custom value.
 */
public final class GeneralPractitioner extends HealthProfessional implements Prescribable {
    private static final String DEFAULT_CLINIC_NAME = "Community Clinic";
    private final String clinicName;

    /**
     * Initializes a professional with the default clinic.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     */
    public GeneralPractitioner(int id, String name, String[] workingDays) {
        this(id, name, workingDays, DEFAULT_CLINIC_NAME);
    }

    /**
     * Initializes a professional with a nonblank clinic supplied by the caller.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     * @param clinicName a nonblank clinic
     */
    public GeneralPractitioner(int id, String name, String[] workingDays, String clinicName) {
        super(id, name, workingDays);
        this.clinicName = clinicName.trim();
    }

    /**
     * Gets the clinic.
     *
     * @return the clinic
     */
    public String getClinicName() {
        return clinicName;
    }

    /**
     * Gets the professional type.
     *
     * @return the type to display
     */
    @Override
    public String getProfessionalType() {
        return "General Practitioner";
    }

    /**
     * Describes the work performed by this professional.
     *
     * @return the role description
     */
    @Override
    public String performRole() {
        return "Provides primary care and routine health checks.";
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
        return getName() + " prescribed " + medication.trim() + " (" + clinicName + ").";
    }

    /**
     * Formats the common details and the clinic.
     *
     * @return the complete description
     */
    @Override
    public String toString() {
        return super.toString() + ", clinic=" + clinicName;
    }
}
