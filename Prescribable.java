/**
 * Describes the prescribing action supported by the GP and specialist examples.
 * Implementations check the medication name and return a warning when it is blank or missing.
 */
public interface Prescribable {
    /**
     * Simulates writing a prescription for a named medication.
     *
     * @param medication the medication name, which must contain text
     * @return a simulated prescription message or a warning for a blank name
     */
    String prescribe(String medication);
}