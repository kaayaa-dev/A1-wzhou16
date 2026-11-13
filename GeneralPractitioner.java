/**
 * Represents a health professional of type General Practitioner.
 * The creation methods check the common staff details and the clinic name.
 */
public final class GeneralPractitioner extends HealthProfessional implements Prescribable {
    private static final String DEFAULT_CLINIC_NAME = "Community Clinic";
    private final String clinicName;

    /**
     * Stores the validated staff details and clinic name.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     * @param clinicName a nonblank clinic name
     */
    private GeneralPractitioner(int id, String name, String[] workingDays, String clinicName) {
        super(id, name, workingDays);
        this.clinicName = clinicName.trim();
    }

    /**
     * Creates a professional with the default clinic name.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     * @return the new professional, or null if the staff details are invalid
     */
    public static GeneralPractitioner create(int id, String name, String[] workingDays) {
        return create(id, name, workingDays, DEFAULT_CLINIC_NAME);
    }

    /**
     * Creates a professional with a custom clinic name after checking the details.
     *
     * @param id a positive professional ID
     * @param name a nonblank professional name
     * @param workingDays a nonempty array of distinct full day names, Monday to Sunday
     * @param clinicName a nonblank clinic name
     * @return the new professional, or null if the staff details or clinic name are invalid
     */
    public static GeneralPractitioner create(int id, String name, String[] workingDays,
            String clinicName) {
        if (!validDetails(id, name, workingDays) || !hasText(clinicName)) {
            return null;
        }
        return new GeneralPractitioner(id, name, workingDays, clinicName);
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