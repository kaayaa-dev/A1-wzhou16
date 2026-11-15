/**
 * Stores the identity, name and working days shared by health professionals.
 * Subclass creation methods use this class to validate the common staff details.
 * The constructor stores those details and keeps its own copy of the schedule.
 */
public abstract class HealthProfessional {
    private static final String[] DAY_NAMES = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };

    private final int id;
    private final String name;
    private final String[] workingDays;

    /**
     * Initializes the validated common fields and stores a copy of the working days.
     *
     * @param id the positive professional ID
     * @param name the nonblank name
     * @param workingDays a nonempty array of distinct full English day names, Monday to Sunday
     */
    protected HealthProfessional(int id, String name, String[] workingDays) {
        this.id = id;
        this.name = name.trim();
        // Keep our own schedule in case the caller edits the input array later.
        this.workingDays = copyDays(workingDays);
    }

    /**
     * Checks the common arguments before a concrete professional is created.
     *
     * @param id the proposed ID
     * @param name the proposed name
     * @param workingDays the proposed working days
     * @return whether the ID, name and working days are valid
     */
    static boolean validDetails(int id, String name, String[] workingDays) {
        if (id <= 0 || !hasText(name) || workingDays == null || workingDays.length == 0) {
            return false;
        }

        for (int index = 0; index < workingDays.length; index++) {
            String day = workingDays[index];
            if (!isDayName(day)) {
                return false;
            }
            for (int earlier = 0; earlier < index; earlier++) {
                if (day.equals(workingDays[earlier])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks whether a string contains text after trimming spaces.
     *
     * @param value the string to check
     * @return whether the string contains text
     */
    static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Checks for a full day name with standard capitalization.
     *
     * @param day the day name to check
     * @return whether it is one of the seven recognized day names
     */
    static boolean isDayName(String day) {
        for (String dayName : DAY_NAMES) {
            if (dayName.equals(day)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Copies the day names into a separate array.
     *
     * @param days the source array
     * @return a copy of the array
     */
    private static String[] copyDays(String[] days) {
        String[] copy = new String[days.length];
        for (int index = 0; index < days.length; index++) {
            copy[index] = days[index];
        }
        return copy;
    }

    /**
     * Gets the professional ID.
     *
     * @return the ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the professional's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the working days.
     *
     * @return an independent copy of the working days
     */
    public String[] getWorkingDays() {
        // Returning the stored array would let a caller change the days used by worksOn.
        return copyDays(workingDays);
    }

    /**
     * Checks whether the professional works on a given day.
     *
     * @param day the full day name
     * @return whether the day is in the professional's schedule
     */
    public boolean worksOn(String day) {
        for (String workingDay : workingDays) {
            if (workingDay.equals(day)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gives the type of health professional.
     *
     * @return the type to display
     */
    public abstract String getProfessionalType();

    /**
     * Describes the work performed by this professional.
     *
     * @return the role description
     */
    public abstract String performRole();

    /**
     * Formats the common professional details.
     *
     * @return the type, ID, name and working days
     */
    @Override
    public String toString() {
        String days = workingDays[0];
        for (int index = 1; index < workingDays.length; index++) {
            days += ", " + workingDays[index];
        }
        return getProfessionalType() + " [id=" + id + ", name=" + name
                + ", working days=" + days + "]";
    }

    /**
     * Compares professional identity across concrete types.
     *
     * @param other the object to compare
     * @return whether both objects are professionals with the same ID
     */
    @Override
    public final boolean equals(Object other) {
        if (!(other instanceof HealthProfessional)) {
            return false;
        }
        HealthProfessional professional = (HealthProfessional) other;
        return id == professional.id;
    }

    /**
     * Hashes the same ID used for equality.
     *
     * @return the professional ID as a hash code
     */
    @Override
    public final int hashCode() {
        return id;
    }
}