# Attendance Management System — Java

## How To Run

``` Bash
javac -d out src/attendance/*.java
java -cp out attendance.AttendanceSystemDemo
```

## Team Members
```
┌─────────────────────────────────────────────┐
|  Roll No                   Student Name     |
│─────────────────────────────────────────────│
|  AM.SC.U4CSE25323  :   Ishaan S Vanneri     |
|  AM.SC.U4CSE25338  :  Niharika S H          |
|  AM.SC.U4CSE25362  :   Ganga J              |
|  AM.SC.U4CSE25326  :   Karthikeya S Arun    |
|  AM.SC.U4CSE25330  :   Madhav S Thampi      |
└─────────────────────────────────────────────┘
```

## Class Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    <<abstract>>  User                       │
│─────────────────────────────────────────────────────────────│
│ - userId  : String                                          │
│ - name    : String                                          │
│ - email   : String                                          │
│ - role    : String                                          │
│─────────────────────────────────────────────────────────────│
│ + displayDashboard(manager: AttendanceManager) <<abstract>> │
│ + getters / setters                                         │
└───────────────────┬─────────────────────┬───────────────────┘
                    │ extends              │ extends
          ┌─────────┴─────────┐  ┌────────┴────────┐
          │     Student       │  │     Teacher     │
          │───────────────────│  │─────────────────│
          │ - program         │  │ - department    │
          │ - semester        │  │ - employeeCode  │
          │───────────────────│  │─────────────────│
          │ + displayDashboard│  │ + displayDashboard│
          └───────────────────┘  └─────────────────┘

┌──────────────────┐    ┌─────────────────────────────────────┐
│    Subject       │    │         AttendanceRecord            │
│──────────────────│    │─────────────────────────────────────│
│ - subjectId      │    │ - recordId    : String              │
│ - subjectName    │    │ - studentId   : String              │
│ - subjectCode    │    │ - subjectId   : String              │
│ - teacherId      │    │ - date        : LocalDate           │
│ - totalClasses   │    │ - status      : AttendanceStatus    │
│──────────────────│    │ - remarks     : String              │
│ + getters/setters│    │ - recordedBy  : String              │
│ + incrementTotal │    │─────────────────────────────────────│
└──────────────────┘    │ + isPresent() / isAbsent()          │
                        │ + setStatus() [controlled edit]     │
                        └─────────────────────────────────────┘

<<enum>> AttendanceStatus { PRESENT, ABSENT, LEAVE }

┌──────────────────────────────────────────────────────────────┐
│                   AttendanceSummary (value object)           │
│──────────────────────────────────────────────────────────────│
│ - studentId / studentName / subjectId / subjectName          │
│ - totalClasses / classesAttended / leavesApproved            │
│ - attendancePercent : double  (calculated on construction)   │
│ - allowableAbsences : int     (calculated on construction)   │
│ - threshold         : double                                 │
│──────────────────────────────────────────────────────────────│
│ + isCompliant() : boolean                                    │
│ + getStatusLabel() : String                                  │
└──────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│                   AttendanceManager (service)                │
│──────────────────────────────────────────────────────────────│
│ - students   : Map<String, Student>                          │
│ - teachers   : Map<String, Teacher>                          │
│ - subjects   : Map<String, Subject>                          │
│ - records    : List<AttendanceRecord>                        │
│ - enrollment : Map<String, Set<String>>                      │
│ - minAttendanceThreshold : double                            │
│──────────────────────────────────────────────────────────────│
│ + addStudent / addTeacher / addSubject / enrollStudent       │
│ + recordAttendance()                                         │
│ + recordClassSession()   [bulk — whole class at once]        │
│ + editAttendance()                                           │
│ + computeSummary()       [core calculation]                  │
│ + printStudentSubjectReport()                                │
│ + printSubjectAttendanceSummary()                            │
│ + printLowAttendanceReport()                                 │
│ + printAttendanceLog()   [audit trail]                       │
│ + getSubjectsForStudent() / getSubjectsForTeacher()          │
└──────────────────────────────────────────────────────────────┘
```

## Project Description

This project is an Attendance Management System built using Java and object-oriented principles.
It uses an abstract User class with Student and Teacher subclasses to demonstrate abstraction, inheritance, and polymorphism.
The system manages students, subjects, and attendance records using Java Collections like HashMap, ArrayList, and Set.
Attendance is recorded per session and analyzed using Streams API to generate summaries and reports.
It calculates attendance percentage and checks against a minimum threshold to identify low attendance.
Overall, the system is modular, scalable, and simulates a real-world academic attendance tracking system.

---


## Sample Console Output

### Student View (Bob Johnson / MATH101)
```
============================================================
  STUDENT ATTENDANCE DASHBOARD
============================================================
  Student : Bob Johnson
  ID      : S002
  Program : B.Sc.  |  Semester: 3
============================================================

  Subject         : Mathematics (MATH101)
  Total Classes   : 20
  Attended        : 12   |  Absent: 8  |  Leave: 0
  Attendance %    : 60.0%
  Minimum Required: 75%
  Status          : AT RISK  ✗
  Allowable Absences Remaining: 0
  --------------------------------------------------

  Subject         : Physics (PHY101)
  Total Classes   : 20
  Attended        : 14   |  Absent: 6  |  Leave: 0
  Attendance %    : 70.0%
  Minimum Required: 75%
  Status          : AT RISK  ✗
  Allowable Absences Remaining: 0
  --------------------------------------------------
============================================================
```

### Teacher Dashboard (Dr. Priya Patel)
```
============================================================
  TEACHER DASHBOARD
============================================================
  Teacher    : Dr. Priya Patel
  Department : Mathematics  |  Code: EMP-101
============================================================

  [ SUBJECTS OVERVIEW ]

  Subject: Mathematics (MATH101) | Total Classes: 20
  Student              Attended   Absent   Leave    Percent
  --------------------------------------------------------
  Alice Smith          20         0        0        100.0%
  Bob Johnson          12         8        0        60.0%
  Carol Williams       13         6        1        65.0%
  Dave Brown           14         6        0        70.0%
  Eve Davis            19         1        0        95.0%

------------------------------------------------------------
  LOW ATTENDANCE ALERT (Below 75%):
------------------------------------------------------------
  ⚠  Bob Johnson         (Mathematics): 60.0% (12/20 classes)
  ⚠  Carol Williams      (Mathematics): 65.0% (13/20 classes)
  ⚠  Dave Brown          (Mathematics): 70.0% (14/20 classes)
============================================================
```
