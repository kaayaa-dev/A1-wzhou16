/**
 * Stores the name and phone number supplied for a patient.
 * Appointment checks these values when creating an appointment, and the manager
 * uses the same check before accepting a booking.
 */
public final class Patient {
    private static final int PHONE_LENGTH = 10;

    private String name;
    private final String phone;

    /**
     * Stores the candidate details and trims the name when it is present.
     * The fields are checked when the patient is used in an appointment request.
     *
     * @param name the proposed patient name, which must be nonblank to book
     * @param phone the proposed phone number, which must be exactly ten digits to book
     */
    public Patient(String name, String phone) {
        this.name = name == null ? null : name.trim();
        this.phone = phone;
    }

    /**
     * Checks for the required number of decimal digits.
     *
     * @param phone the proposed phone number
     * @return whether the phone number is valid
     */
    static boolean isValidPhone(String phone) {
        if (phone == null || phone.length() != PHONE_LENGTH) {
            return false;
        }
        for (int index = 0; index < phone.length(); index++) {
            char digit = phone.charAt(index);
            if (digit < '0' || digit > '9') {
                return false;
            }
        }
        return true;
    }

    // Returns the patient's current name.
    public String getName() {
        return name;
    }

    // Updates the patient's name after trimming leading and trailing spaces.
    // Keeps the current value if the new value is null, empty, or only spaces.
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }

    // Returns the patient's phone number, including any leading zero.
    public String getPhone() {
        return phone;
    }

    /**
     * Formats the patient's details.
     *
     * @return the name and phone number
     */
    @Override
    public String toString() {
        return name + " (phone=" + phone + ")";
    }
}