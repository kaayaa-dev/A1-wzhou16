import java.util.ArrayList;

/**
 * Runs the PROG2004 Assignment One demonstrations and prints their results.
 * It sends booking and filter requests to AppointmentManager and displays the outcomes.
 */
public class AssignmentOne {
    /**
     * Prevents construction of the console entry point, which takes no parameters.
     */
    private AssignmentOne() {
    }

    /**
     * Runs the available demonstrations.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        String[] monday = {"Monday"};
        String[] mondayAndWednesday = {"Monday", "Wednesday"};
        String[] tuesday = {"Tuesday"};

        HealthProfessional gpAlex = GeneralPractitioner.create(101, "Alex Reed", monday);
        HealthProfessional gpJordan = GeneralPractitioner.create(102, "Jordan Lane",
                mondayAndWednesday, "North Clinic");
        HealthProfessional gpTaylor = GeneralPractitioner.create(103, "Taylor Chen", tuesday);
        HealthProfessional specialist = Specialist.create(201, "Morgan Singh", monday,
                "Cardiology");
        HealthProfessional secondSpecialist = Specialist.create(202, "Riley Adams", monday,
                "Cardiology");
        HealthProfessional nurse = Nurse.create(301, "Casey Park", monday);
        HealthProfessional physiotherapist = Physiotherapist.create(401, "Jamie Lewis", monday,
                "Sports Injuries");
        HealthProfessional secondPhysiotherapist = Physiotherapist.create(402, "Erin Brooks", monday,
                "General Rehabilitation");
        if (gpAlex == null || gpJordan == null || gpTaylor == null || specialist == null
                || secondSpecialist == null || nurse == null || physiotherapist == null
                || secondPhysiotherapist == null) {
            System.out.println("Demo warning: invalid health professional details.");
            return;
        }

        ArrayList<HealthProfessional> professionals = new ArrayList<>();
        professionals.add(nurse);
        professionals.add(gpTaylor);
        professionals.add(physiotherapist);
        professionals.add(gpAlex);
        professionals.add(specialist);
        professionals.add(gpJordan);
        professionals.add(secondSpecialist);
        professionals.add(secondPhysiotherapist);

        demonstrateHealthProfessionals(professionals);
        demonstrateProfessionalActions(professionals);
        demonstrateWorkingDaysCopy();
        demonstrateAppointments(professionals, gpAlex, gpTaylor, specialist, nurse, physiotherapist);
    }

    /**
     * Shows how the base type can display objects of different concrete types.
     *
     * @param professionals the example professionals
     */
    private static void demonstrateHealthProfessionals(
            ArrayList<HealthProfessional> professionals) {
        printHeading("Health professionals");
        for (HealthProfessional professional : professionals) {
            System.out.println(professional.toString());
            System.out.println("  Role: " + professional.performRole());
        }
    }

    /**
     * Demonstrates an interface shared by two roles and actions unique to the other roles.
     *
     * @param professionals the example professionals
     */
    private static void demonstrateProfessionalActions(
            ArrayList<HealthProfessional> professionals) {
        printHeading("Professional actions");
        for (HealthProfessional professional : professionals) {
            if (professional instanceof Prescribable) {
                Prescribable prescriber = (Prescribable) professional;
                System.out.println(prescriber.prescribe("sample medication"));
            } else if (professional instanceof Nurse) {
                Nurse nurse = (Nurse) professional;
                System.out.println(nurse.takeBloodSample());
            } else if (professional instanceof Physiotherapist) {
                Physiotherapist physiotherapist = (Physiotherapist) professional;
                System.out.println(physiotherapist.createExercisePlan());
            }
        }
    }

    /**
     * Shows that editing the input array and the returned array leaves the stored day intact.
     */
    private static void demonstrateWorkingDaysCopy() {
        printHeading("Working days copy");
        String[] suppliedDays = {"Monday"};
        HealthProfessional professional = GeneralPractitioner.create(104, "Sam Patel",
                suppliedDays);
        if (professional == null) {
            System.out.println("Demo warning: invalid working days example.");
            return;
        }

        // Expected: changes to the input array and the getter copy leave Monday stored.
        suppliedDays[0] = "Tuesday";
        String[] returnedDays = professional.getWorkingDays();
        returnedDays[0] = "Wednesday";

        System.out.println("Input array now says: " + suppliedDays[0]);
        System.out.println("Returned copy now says: " + returnedDays[0]);
        System.out.println("Stored working day: " + professional.getWorkingDays()[0]);
    }

    /**
     * Demonstrates the patient's booking decisions, then the shorter validation examples.
     *
     * @param professionals the candidates for each service
     * @param gpAlex Alex, whose opening slot is already booked
     * @param gpTaylor Taylor, who works on a different day
     * @param specialist Morgan, a cardiology specialist
     * @param nurse Casey, who provides nursing care
     * @param physiotherapist Jamie, whose first eligible slot is already booked
     */
    private static void demonstrateAppointments(ArrayList<HealthProfessional> professionals,
            HealthProfessional gpAlex, HealthProfessional gpTaylor,
            HealthProfessional specialist, HealthProfessional nurse, HealthProfessional physiotherapist) {
        Patient ava = new Patient("Ava Brown", "0400000001");
        Patient noah = new Patient("Noah Wilson", "0400000002");
        Patient mia = new Patient("Mia Green", "0400000003");
        Patient liam = new Patient("Liam Young", "0400000004");
        AppointmentManager manager = new AppointmentManager("Monday");

        printHeading("Existing bookings");
        String noahTime = "08:00";
        String morganBookedTime = "10:30";
        // Expected: Alex's filter shows 13:00 before 08:00, and sorting reverses that order.
        printBookingRequest(manager, mia, gpAlex, "13:00");
        printBookingRequest(manager, noah, gpAlex, noahTime);
        printBookingRequest(manager, noah, specialist, morganBookedTime);
        printBookingRequest(manager, mia, physiotherapist, "11:00");

        printHeading("Ava chooses a cardiology appointment");
        String preferredTime = "10:30";
        System.out.println("Ava prefers " + preferredTime
                + " with either cardiologist. If needed, she can come from 11:00 to 12:00.");
        // Expected: Morgan offers 11:00, but Riley can offer Ava's preferred 10:30.
        Appointment cardioChoice = chooseAppointment(manager, ava, professionals, "Specialist",
                specialist, "Cardiology", preferredTime, timeInMinutes("11:00"), timeInMinutes("12:00"), true);
        Appointment cardiologyBooking = confirmChoice(manager, cardioChoice, true);
        if (cardiologyBooking != null) {
            int firstStart = timeInMinutes(cardiologyBooking.getTime());
            printHeading("Arrange physiotherapy after cardiology");
            System.out.println("Ava wants physiotherapy at least 30 minutes after cardiology.");
            // Expected: both therapists are checked from 11:00. Erin can see Ava earlier than Jamie.
            Appointment physioChoice = chooseAppointment(manager, ava, professionals,
                    "Physiotherapist", null, null, null, firstStart + 30, timeInMinutes("15:30"), true);
            confirmChoice(manager, physioChoice, true);
        }

        int earliest = timeInMinutes("08:00");
        int latest = timeInMinutes("11:30");
        String gpService = gpAlex.getProfessionalType();
        printHeading("The time matters more to Liam");
        System.out.println("Liam can only come at 08:00 and can see any GP.");
        Appointment liamChoice = chooseAppointment(manager, liam, professionals, gpService,
                gpAlex, null, noahTime, earliest, earliest, true);
        Appointment liamBooking = confirmChoice(manager, liamChoice, true);

        printHeading("Review, choose again and leave without booking");
        Appointment preview = chooseAppointment(manager, mia, professionals, gpService,
                gpAlex, null, "10:00", earliest, latest, false);
        if (preview != null) {
            System.out.println("Summary: " + preview);
            System.out.println("Mia goes back and asks for a later time.");
            Appointment later = chooseAppointment(manager, mia, professionals, gpService,
                    gpAlex, null, "10:30", timeInMinutes("10:30"), latest, true);
            confirmChoice(manager, later, false);
            if (later != null) {
                showSlotStatus(manager, later);
            }
        }

        printHeading("Nursing appointment");
        printBookingRequest(manager, mia, nurse, "09:30");

        printHeading("All appointments in booking order");
        printAppointments(manager.getAllAppointments());
        printHeading("All appointments sorted by time");
        printSortedAppointments(manager.sortAppointments(manager.getAllAppointments()));
        ArrayList<Appointment> gpBookings = printProfessionalFilter(manager, gpAlex.getId());
        if (gpBookings != null) {
            printHeading("Professional's filtered appointments sorted by time");
            printSortedAppointments(manager.sortAppointments(gpBookings));
        }

        printHeading("Filter input and empty results");
        printProfessionalFilter(manager, 0);
        printPhoneFilter(manager, "abc");
        printPhoneFilter(manager, "0400000099");

        manageAvaCancellation(manager, ava, cardiologyBooking, professionals, liam);
        demonstrateRescheduling(manager, liamBooking, professionals, gpAlex, noahTime);
        demonstrateSelectedCancellations(mia, nurse);

        manageNoahCancellation(manager, noah);

        AppointmentManager conflictManager = new AppointmentManager(manager.getBookingDay());
        HealthProfessional sameAlex = GeneralPractitioner.create(gpAlex.getId(),
                gpAlex.getName(), gpAlex.getWorkingDays());
        ArrayList<String> conflictSlots = conflictManager.getAvailableSlots(gpAlex);
        if (sameAlex != null && !conflictSlots.isEmpty()) {
            String conflictTime = conflictSlots.get(0);
            printHeading("Set up the conflict example in a separate booking list");
            printBookingRequest(conflictManager, noah, gpAlex, conflictTime);
            // Expected: after the setup succeeds, the same ID and time cause a rejection.
            if (!conflictManager.getAllAppointments().isEmpty()) {
                printHeading("Conflicting booking with the same professional ID");
                printBookingRequest(conflictManager, ava, sameAlex, conflictTime);
            }
        }

        printHeading("Outside clinic hours");
        // Expected: 16:00 is rejected because it is the closing time.
        printBookingRequest(manager, ava, gpAlex, "16:00");
        // Expected: 16:30 is rejected because it is after closing.
        printBookingRequest(manager, ava, gpAlex, "16:30");
        printHeading("Time outside the 30-minute slot grid");
        // Expected: the draft accepts 08:15, then the manager rejects it as off the slot grid.
        Appointment draft = Appointment.create(ava, gpAlex, "08:00");
        if (draft != null) {
            draft.setTime("08:15");
            printBookingRequest(manager, draft.getPatient(), draft.getProfessional(), draft.getTime());
        }

        printHeading("Time format and clock range");
        // Expected: 8:00 fails the HH:mm format check.
        printBookingRequest(manager, ava, gpAlex, "8:00");
        // Expected: 24:00 has the right shape but fails the clock range check.
        printBookingRequest(manager, ava, gpAlex, "24:00");

        printHeading("Missing or invalid booking information");
        // Expected: a missing patient is rejected by the manager.
        System.out.println("Request: Monday 09:00 | missing patient");
        System.out.println(manager.addAppointment(null, gpAlex, "09:00"));
        // Expected: an existing patient object with invalid fields is also rejected.
        System.out.println("Request: Monday 09:00 | blank name and invalid phone");
        System.out.println(manager.addAppointment(new Patient(" ", "abc"), gpAlex, "09:00"));
        // Expected: an invalid booking day is rejected before inspecting the request.
        printBookingRequest(new AppointmentManager("Funday"), ava, gpAlex, "09:00");

        printHeading("Last available start time");
        // Expected: 15:30 succeeds as the final slot before closing.
        printBookingRequest(manager, ava, gpAlex, "15:30");

        printHeading("Phone cancellation warnings");
        // Expected: abc produces an invalid-number warning.
        System.out.println("Request: cancel appointments for abc");
        System.out.println(manager.cancelAppointmentsByPhone("abc"));
        // Expected: a valid number that was never booked produces a no-match warning.
        System.out.println("Request: cancel appointments for 0400000099");
        System.out.println(manager.cancelAppointmentsByPhone("0400000099"));

        printHeading("Availability on two simulated days");
        // Expected: Taylor's 08:00 request is rejected because Taylor does not work on Monday.
        printAvailableSlots(manager, gpTaylor);
        printBookingRequest(manager, ava, gpTaylor, "08:00");
        AppointmentManager tuesdayManager = new AppointmentManager("Tuesday");
        // Expected: the same request succeeds in the separate Tuesday booking list.
        printAvailableSlots(tuesdayManager, gpTaylor);
        printBookingRequest(tuesdayManager, ava, gpTaylor, "08:00");
        printAppointments(tuesdayManager.getAllAppointments());
    }

    /**
     * Finds a proposed booking without adding it. The exact preferred time comes first.
     * Other times must fall inside the accepted window. At an equal preference, the
     * preferred professional comes first, followed by the earliest acceptable time.
     *
     * @param manager the booking list for the requested day
     * @param patient the patient making the choice
     * @param professionals the clinic's roster
     * @param service the required professional type
     * @param preferredProfessional the preferred provider, or null when any match is acceptable
     * @param specialty the required specialty for a specialist, or null for other services
     * @param preferredTime the exact first choice, or null to use the window alone
     * @param earliest the earliest alternative start measured in minutes from midnight
     * @param latest the latest alternative start measured in minutes from midnight
     * @param allowOtherProfessionals whether another provider for the service is acceptable
     * @return the proposed appointment, or null when no acceptable slot was found
     */
    private static Appointment chooseAppointment(AppointmentManager manager, Patient patient,
            ArrayList<HealthProfessional> professionals, String service, HealthProfessional preferredProfessional,
            String specialty, String preferredTime, int earliest, int latest,
            boolean allowOtherProfessionals) {
        HealthProfessional selectedProfessional = null;
        String selectedTime = null;
        int bestPreference = -1;
        for (HealthProfessional candidate : professionals) {
            boolean isPreferred = candidate.equals(preferredProfessional);
            if (!candidate.getProfessionalType().equals(service)
                    || (!isPreferred && !allowOtherProfessionals)) {
                continue;
            }
            if (candidate instanceof Specialist
                    && !((Specialist) candidate).getSpecialty().equalsIgnoreCase(specialty)) {
                continue;
            }

            ArrayList<String> slots = manager.getAvailableSlots(candidate);
            if (slots.isEmpty()) {
                if (candidate.worksOn(manager.getBookingDay())) {
                    System.out.println(candidate.getName() + " is fully booked.");
                } else {
                    System.out.println(candidate.getName() + " does not work on "
                            + manager.getBookingDay() + ".");
                }
                continue;
            }

            String candidateTime = null;
            if (slots.contains(preferredTime)) {
                candidateTime = preferredTime;
            } else {
                for (String slot : slots) {
                    int start = timeInMinutes(slot);
                    if (start >= earliest && start <= latest) {
                        candidateTime = slot;
                        break;
                    }
                }
            }
            if (candidateTime == null) {
                System.out.println(candidate.getName() + " has openings outside the requested times.");
                continue;
            }
            System.out.println(candidate.getName() + " can offer " + candidateTime + ".");

            // Keep an exact time over an alternative, then use the preferred provider as the tie-break.
            int preference = 0;
            if (candidateTime.equals(preferredTime)) {
                preference += 2;
            }
            if (isPreferred) {
                preference++;
            }
            if (preference > bestPreference
                    || (preference == bestPreference && candidateTime.compareTo(selectedTime) < 0)) {
                selectedProfessional = candidate;
                selectedTime = candidateTime;
                bestPreference = preference;
            }
        }
        if (selectedProfessional == null) {
            System.out.println("No available time matches the patient's preferences.");
            return null;
        }
        return Appointment.create(patient, selectedProfessional, selectedTime);
    }

    /**
     * Shows a selection and submits it only when the script models confirmation.
     *
     * @param manager the booking list for the requested day
     * @param choice the proposed appointment, or null when no slot was found
     * @param confirmed whether the patient chooses to submit the request
     * @return the record added by this request, or null if declined or rejected
     */
    private static Appointment confirmChoice(AppointmentManager manager, Appointment choice,
            boolean confirmed) {
        if (choice == null) {
            return null;
        }
        System.out.println("Summary: " + choice);
        Patient patient = choice.getPatient();
        if (!confirmed) {
            System.out.println(patient.getName() + " leaves without submitting this booking.");
            return null;
        }
        System.out.println(patient.getName() + " confirms the selection.");
        return printBookingRequest(manager, patient, choice.getProfessional(), choice.getTime());
    }

    /**
     * Finds a known choice in the patient's queried bookings.
     *
     * @param bookings the bookings for the patient's phone number
     * @param choice the requested provider and start time
     * @return the stored booking, or null if it is absent
     */
    private static Appointment findBooking(ArrayList<Appointment> bookings, Appointment choice) {
        for (Appointment booking : bookings) {
            if (booking.getProfessional().equals(choice.getProfessional())
                    && booking.getTime().equals(choice.getTime())) {
                return booking;
            }
        }
        return null;
    }

    /**
     * Displays the current status of one booking's start time.
     *
     * @param manager the day to check
     * @param booking the professional and time to look up
     */
    private static void showSlotStatus(AppointmentManager manager, Appointment booking) {
        HealthProfessional professional = booking.getProfessional();
        String time = booking.getTime();
        if (manager.getAvailableSlots(professional).contains(time)) {
            System.out.println(professional.getName() + " at " + time + " is available.");
        } else {
            System.out.println(professional.getName() + " at " + time + " is unavailable.");
        }
    }

    /**
     * Previews and confirms cancellation of Ava's actual cardiology booking, then offers a rebooking.
     *
     * @param manager the booking list for the simulated day
     * @param patient Ava, whose bookings are being reviewed
     * @param cardiologyBooking the confirmed cardiology booking, if there is one
     * @param nextPatient the patient who takes the released slot in the rebooking example
     * @param professionals the clinic's roster
     */
    private static void manageAvaCancellation(AppointmentManager manager, Patient patient,
            Appointment cardiologyBooking, ArrayList<HealthProfessional> professionals, Patient nextPatient) {
        printHeading("Ava reviews and selects a booking to cancel");
        String phone = patient.getPhone();
        SortResult sorted = manager.sortAppointments(
                manager.filterByPhone(phone).getMatches());
        if (sorted.getWarning() != null) {
            System.out.println(sorted.getWarning());
            return;
        }
        ArrayList<Appointment> bookings = sorted.getAppointments();
        printAppointments(bookings);
        if (cardiologyBooking == null) {
            return;
        }
        Appointment selectedAppointment = findBooking(bookings, cardiologyBooking);
        if (selectedAppointment == null) {
            return;
        }

        // Expected: the first decision keeps the cardiology booking in the active results.
        reviewCancellation(manager, selectedAppointment, false);
        printPhoneFilter(manager, phone);

        // Expected: confirming releases the cardiologist's slot and leaves physiotherapy booked.
        reviewCancellation(manager, selectedAppointment, true);
        printPhoneFilter(manager, phone);
        showSlotStatus(manager, selectedAppointment);
        printHeading("Cancellation warning for an already removed appointment");
        cancelSelected(manager, selectedAppointment);

        demonstrateRebooking(manager, selectedAppointment, professionals, nextPatient);
    }

    /**
     * Shows Noah's bookings before cancelling them by phone and displaying his history.
     *
     * @param manager the booking list for the simulated day
     * @param noah the patient whose bookings are being reviewed
     */
    private static void manageNoahCancellation(AppointmentManager manager, Patient noah) {
        printHeading("Cancel all of Noah's appointments by phone");
        String phone = noah.getPhone();
        SortResult sorted = manager.sortAppointments(manager.filterByPhone(phone).getMatches());
        if (sorted.getWarning() != null) {
            System.out.println(sorted.getWarning());
            return;
        }
        ArrayList<Appointment> bookings = sorted.getAppointments();
        System.out.println("Noah reviews " + bookings.size() + " bookings before confirming.");
        printAppointments(bookings);
        boolean confirmAll = true;
        if (confirmAll && !bookings.isEmpty()) {
            System.out.println("Noah confirms cancellation of all listed bookings.");
            System.out.println(manager.cancelAppointmentsByPhone(phone));
        }
        printPhoneFilter(manager, phone);
        printHeading("Noah's cancelled bookings");
        printSortedAppointments(manager.sortAppointments(manager.filterCancelledByPhone(phone).getMatches()));
    }

    /**
     * Shows the exact booking and cancels it only after the patient's scripted confirmation.
     *
     * @param manager the manager holding the booking
     * @param booking the booking selected by the patient
     * @param confirmed whether the patient accepts the cancellation
     */
    private static void reviewCancellation(AppointmentManager manager, Appointment booking,
            boolean confirmed) {
        System.out.println("Cancellation summary: " + booking);
        if (confirmed) {
            System.out.println(booking.getPatient().getName() + " confirms cancellation.");
            cancelSelected(manager, booking);
        } else {
            System.out.println(booking.getPatient().getName() + " chooses to keep this booking.");
        }
    }

    /**
     * Selects one record from cancellation history and checks current slots before rebooking.
     *
     * @param manager the booking list and cancellation history for the simulated day
     * @param cancelled the recently cancelled booking to select from history
     * @param professionals the clinic's roster
     * @param nextPatient the patient who takes the released slot in this example
     */
    private static void demonstrateRebooking(AppointmentManager manager, Appointment cancelled,
            ArrayList<HealthProfessional> professionals, Patient nextPatient) {
        printHeading("Ava checks cancelled bookings and rebooks");
        Patient patient = cancelled.getPatient();
        String phone = patient.getPhone();
        ArrayList<Appointment> history = manager.filterCancelledByPhone(phone).getMatches();
        printAppointments(history);
        if (!history.contains(cancelled)) {
            return;
        }

        HealthProfessional originalProfessional = cancelled.getProfessional();
        String originalTime = cancelled.getTime();
        // Expected: another patient can take the released slot before Ava asks to rebook it.
        if (manager.getAvailableSlots(originalProfessional).contains(originalTime)) {
            printBookingRequest(manager, nextPatient, originalProfessional, originalTime);
        }
        int earliest = timeInMinutes("10:00");
        int latest = timeInMinutes("15:30");
        for (Appointment booking : manager.filterByPhone(phone).getMatches()) {
            if (booking.getProfessional() instanceof Physiotherapist) {
                int latestBeforePhysio = timeInMinutes(booking.getTime()) - 30;
                if (latestBeforePhysio < latest) {
                    latest = latestBeforePhysio;
                }
            }
        }
        // The original time is a preference only when it also fits the rebooking window.
        int originalStart = timeInMinutes(originalTime);
        String preferredTime = originalTime;
        if (originalStart < earliest || originalStart > latest) {
            preferredTime = null;
            System.out.println("Ava's original time is outside her rebooking window."
                    + " She checks other times that fit before physiotherapy.");
        } else if (!manager.getAvailableSlots(originalProfessional).contains(originalTime)) {
            System.out.println("Ava's original slot is unavailable. She checks other cardiology slots"
                    + " that fit before physiotherapy.");
        }
        String specialty = ((Specialist) originalProfessional).getSpecialty();
        Appointment choice = chooseAppointment(manager, patient, professionals,
                originalProfessional.getProfessionalType(), originalProfessional, specialty,
                preferredTime, earliest, latest, true);
        Appointment newBooking = confirmChoice(manager, choice, true);
        if (newBooking != null) {
            printPhoneFilter(manager, phone);
            printHeading("Ava's cancelled booking stays in history");
            printAppointments(manager.filterCancelledByPhone(phone).getMatches());
        }
    }

    /**
     * Reviews Liam's stored GP booking and demonstrates keeping it and trying a change.
     *
     * @param manager the booking list for the simulated day
     * @param firstBooking Liam's confirmed GP booking, if one was made
     * @param professionals the clinic's roster
     * @param occupiedProfessional the provider who already has the opening booking
     * @param occupiedTime the start time of an existing GP booking for another patient
     */
    private static void demonstrateRescheduling(AppointmentManager manager,
            Appointment firstBooking, ArrayList<HealthProfessional> professionals,
            HealthProfessional occupiedProfessional, String occupiedTime) {
        printHeading("Liam reviews a GP appointment change");
        if (firstBooking == null) {
            return;
        }
        String phone = firstBooking.getPatient().getPhone();
        Appointment selectedAppointment = findBooking(manager.filterByPhone(phone).getMatches(), firstBooking);
        if (selectedAppointment == null) {
            return;
        }

        HealthProfessional oldProfessional = selectedAppointment.getProfessional();
        int oldId = oldProfessional.getId();
        String oldTime = selectedAppointment.getTime();
        System.out.println("Liam chooses to keep " + oldProfessional.getName() + " at " + oldTime + ".");
        System.out.println(manager.rescheduleAppointment(phone, oldId, oldTime, oldProfessional, oldTime));

        // Expected: the occupied slot is rejected and Liam's original booking remains.
        System.out.println("Liam asks to move to " + occupiedTime + " with "
                + occupiedProfessional.getName() + ".");
        System.out.println(manager.rescheduleAppointment(phone, oldId, oldTime,
                occupiedProfessional, occupiedTime));
        printPhoneFilter(manager, phone);

        // Expected: an available later time with the same GP releases the previous slot.
        int earliest = timeInMinutes(oldTime) + 30;
        Appointment newChoice = chooseAppointment(manager, firstBooking.getPatient(), professionals,
                oldProfessional.getProfessionalType(), oldProfessional, null, null,
                earliest, timeInMinutes("15:30"), false);
        if (newChoice != null) {
            System.out.println("Liam confirms the change to " + newChoice.getTime() + ".");
            System.out.println(manager.rescheduleAppointment(phone, oldId, oldTime,
                    newChoice.getProfessional(), newChoice.getTime()));
            showSlotStatus(manager, selectedAppointment);
            printPhoneFilter(manager, phone);
        }
    }

    /**
     * Cancels the exact professional and time identified by a selected booking.
     *
     * @param manager the day holding the booking
     * @param booking the record selected from the patient's results
     */
    private static void cancelSelected(AppointmentManager manager, Appointment booking) {
        System.out.println(manager.cancelAppointment(booking.getProfessional().getId(), booking.getTime()));
    }

    /**
     * Cancels two morning bookings selected from a separate demo list and keeps the afternoon one.
     *
     * @param patient the patient with three nursing appointments
     * @param nurse the professional providing the appointments
     */
    private static void demonstrateSelectedCancellations(Patient patient, HealthProfessional nurse) {
        printHeading("Cancel selected morning appointments");
        AppointmentManager manager = new AppointmentManager("Monday");
        printBookingRequest(manager, patient, nurse, "09:00");
        printBookingRequest(manager, patient, nurse, "10:00");
        printBookingRequest(manager, patient, nurse, "13:00");
        ArrayList<Appointment> selected = new ArrayList<>();
        for (Appointment booking : manager.filterByPhone(patient.getPhone()).getMatches()) {
            if (Appointment.hourOf(booking.getTime()) < 12) {
                selected.add(booking);
            }
        }
        System.out.println(patient.getName() + " reviews the morning bookings to cancel.");
        printAppointments(selected);
        for (Appointment booking : selected) {
            reviewCancellation(manager, booking, true);
            showSlotStatus(manager, booking);
        }
        printPhoneFilter(manager, patient.getPhone());
    }

    /**
     * Converts a validated time to its start minute for same-day comparisons.
     *
     * @param time a valid HH:mm time
     * @return the number of minutes after midnight
     */
    private static int timeInMinutes(String time) {
        return Appointment.hourOf(time) * 60 + Appointment.minuteOf(time);
    }

    /**
     * Prints matching appointments or the manager's warning for an invalid ID.
     *
     * @param manager the booking list to filter
     * @param professionalId the requested professional ID
     * @return matching appointments, or null if the ID was invalid
     */
    private static ArrayList<Appointment> printProfessionalFilter(AppointmentManager manager,
            int professionalId) {
        printHeading("Filter by professional ID " + professionalId);
        FilterResult result = manager.filterByProfessionalId(professionalId);
        if (result.getWarning() != null) {
            System.out.println(result.getWarning());
            return null;
        }
        ArrayList<Appointment> matches = result.getMatches();
        printAppointments(matches);
        return matches;
    }

    /**
     * Prints matching appointments or the manager's warning for an invalid phone number.
     *
     * @param manager the booking list to filter
     * @param phone the requested patient phone number
     */
    private static void printPhoneFilter(AppointmentManager manager, String phone) {
        printHeading("Filter by phone " + phone);
        FilterResult result = manager.filterByPhone(phone);
        if (result.getWarning() != null) {
            System.out.println(result.getWarning());
        } else {
            printAppointments(result.getMatches());
        }
    }

    /**
     * Displays the request details and the manager's booking result.
     *
     * @param manager the booking list for the simulated day
     * @param patient the patient requesting an appointment
     * @param professional the chosen professional
     * @param time the requested start time
     * @return the appointment added by this request, or null if rejected
     */
    private static Appointment printBookingRequest(AppointmentManager manager, Patient patient,
            HealthProfessional professional, String time) {
        System.out.println("Request: " + manager.getBookingDay() + " " + time + " | "
                + patient.getName() + " | " + professional.getName() + " (#"
                + professional.getId() + ")");
        BookingResult result = manager.submitAppointment(patient, professional, time);
        System.out.println(result.getMessage());
        return result.getAppointment();
    }

    /**
     * Prints the start times currently offered for a professional on the booking day.
     *
     * @param manager the booking list for the simulated day
     * @param professional the professional whose slots to display
     * @return the available start times shown to the patient
     */
    private static ArrayList<String> printAvailableSlots(AppointmentManager manager,
            HealthProfessional professional) {
        ArrayList<String> availableSlots = manager.getAvailableSlots(professional);
        System.out.println("Available slots for " + professional.getName() + " on "
                + manager.getBookingDay() + ": " + availableSlots);
        return availableSlots;
    }

    /**
     * Shows a sorting warning or prints the appointments in the result.
     *
     * @param result the outcome of sorting the appointments
     */
    private static void printSortedAppointments(SortResult result) {
        if (result.getWarning() != null) {
            System.out.println(result.getWarning());
        } else {
            printAppointments(result.getAppointments());
        }
    }

    /**
     * Prints each appointment in the supplied list.
     *
     * @param appointments the appointments to display
     */
    private static void printAppointments(ArrayList<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("No appointments.");
        }
        for (Appointment appointment : appointments) {
            System.out.println(appointment.toString());
        }
    }

    /**
     * Prints a heading for a console demonstration section.
     *
     * @param title the section title
     */
    private static void printHeading(String title) {
        System.out.println();
        System.out.println("========== " + title + " ==========");
    }
}