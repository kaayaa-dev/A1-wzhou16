# PROG2004 Assessment 1 — Clinic Appointment System

This Java project models a clinic with four types of health professionals. The `AssignmentOne` class runs a scripted console demonstration of the appointment system.

## Running the demo

The project was tested with JDK 21 and uses the Java standard library.

In VS Code, open the project folder, then open `src/AssignmentOne.java` and click **Run** above the `main` method.

You can also run the program from a terminal in the project folder that contains `src`.

**Windows PowerShell**

```powershell
$javaSources = Get-ChildItem -Path src -Filter '*.java' -File | Select-Object -ExpandProperty FullName
javac -d out $javaSources
java -cp out AssignmentOne
```

**macOS or Linux**

```sh
javac -d out src/*.java
java -cp out AssignmentOne
```

## What the demo shows

- A patient checks available times, chooses a health professional, and either confirms or leaves without booking.
- Appointments can be filtered by professional ID or patient phone number. Sorting returns a separate list in time order and preserves the order of the original list.
- Patients can reschedule appointments, cancel one or more bookings, and view cancelled appointments.
- The manager checks the professional's working days and rejects booking conflicts, times outside clinic hours, and times that do not match a 30-minute slot. The first slot starts at 08:00 and the last at 15:30.

The time on a draft appointment can be changed before confirmation. The manager validates the request before adding the confirmed appointment to the booking list.

Sorting returns a `SortResult`. If the input list is missing or contains a missing appointment, the result includes a warning.

## Class diagram

[View the UML class diagram](docs/UML.jpg)