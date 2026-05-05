# Attendance Management System — Java

## How To Run

``` Bash
javac -d out src/attendance/*.java
java -cp out attendance.AttendanceSystemDemo
```

## Class Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    <<abstract>>  User                        │
│─────────────────────────────────────────────────────────────│
│ - userId  : String                                           │
│ - name    : String                                           │
│ - email   : String                                           │
│ - role    : String                                           │
│─────────────────────────────────────────────────────────────│
│ + displayDashboard(manager: AttendanceManager) <<abstract>>  │
│ + getters / setters                                          │
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
└──────────────────┘    │ + isPresent() / isAbsent()         │
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

---

## OOP Design Decisions

### 1. Inheritance & Polymorphism
`User` is an **abstract base class** with an abstract `displayDashboard()` method.
`Student` and `Teacher` each override it differently:
- `Student.displayDashboard()` → shows only that student's own records
- `Teacher.displayDashboard()` → shows class-wide summaries and low-attendance alerts

The demo calls `user.displayDashboard(manager)` — a single polymorphic call that
produces the correct view for whoever is logged in, without any `instanceof` checks.

### 2. Encapsulation
- `AttendanceRecord.setStatus()` is the **only** way to edit a record — it
  forces a remark to be supplied, preserving the audit trail.
- `AttendanceSummary` is computed and immutable (no setters). A fresh instance is
  returned whenever records change, avoiding stale cached data.
- All collections in `AttendanceManager` are private; external code must go
  through provided query methods.

### 3. Abstraction
`AttendanceManager` is the single service layer. UI classes (`Student`,
`Teacher`, `AttendanceSystemDemo`) never touch the raw collections directly —
they call manager methods and receive clean result objects or formatted output.

### 4. Separation of Concerns
| Layer | Class(es) |
|---|---|
| Data / Entities | `User`, `Student`, `Teacher`, `Subject`, `AttendanceRecord` |
| Value Object | `AttendanceSummary` |
| Business Logic | `AttendanceManager` |
| UI / Presentation | `AttendanceSystemDemo`, `displayDashboard()` in each User subclass |

### 5. Role-Based Access
Enforced structurally — not by an if-check, but by **which `displayDashboard()`
is called**. A `Student` instance can never call teacher-only methods directly;
it calls its own overridden implementation which only queries `manager` for its
own `userId`. A `Teacher` instance calls the class-wide query methods.

### 6. Attendance Calculation Formula
```
attendancePercent   = (classesAttended / totalClasses) × 100
minRequired         = ⌈totalClasses × threshold⌉
allowableAbsences   = max(0,  totalClasses − minRequired − classesAbsent)
```
Leave-tagged absences are tracked separately and not penalised in the
compliance check (they count as neither present nor absent for threshold
purposes — an institution-level policy choice made explicit in the model).

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
