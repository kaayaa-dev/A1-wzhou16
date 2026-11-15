# PROG2004 Assignment 1 — Appointment System

This program shows how patients can book, check, change and cancel appointments. The demo uses eight health professionals: three GPs, two specialists, one nurse and two physiotherapists. Each booking list is for one day. Appointments start every 30 minutes from 08:00 to 15:30.

## How to run

Open the project folder in VS Code. Open `src/AssignmentOne.java` and click **Run** above `main`. The demo runs by itself and prints the results in the terminal. The demo was tested with JDK 21.

If using PowerShell, run these commands from the project folder:

```powershell
$javaFiles = @(Get-ChildItem -Path .\src -Filter '*.java' -File | ForEach-Object { $_.FullName })
javac -d out @javaFiles
if ($LASTEXITCODE -eq 0) { java -cp out AssignmentOne }
```

## What to check in the output

- Ava wants a cardiology appointment at 10:30. Morgan's slot is taken, so she books Riley at the same time.
- Filter Alex's bookings by ID 101. Expect 13:00 before 08:00 in booking order. After sorting, 08:00 comes first.
- Ava first keeps her cardiology booking. After she confirms cancellation, Riley's 10:30 slot is available again. Her physiotherapy booking stays in the list.
- A second booking for the same professional and time is rejected. If a change is rejected, the original booking stays in the list.

The demo also shows filtering and cancelling by phone number, checking cancelled appointments and making a new booking from a cancelled record.

## Class diagram

[View the UML class diagram](docs/UML.jpg).
