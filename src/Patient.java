/**
 * Stores the name and phone number supplied for a patient.
 * Appointment checks these values when creating an appointment, and the manager
 * uses the same check before accepting a booking.
 */
public final class Patient {
    private static final int PHONE_LENGTH = 10;

    private final String name;
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

    /**
     * Gets the patient name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the phone number, including any leading zero.
     *
     * @return the phone number
     */
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