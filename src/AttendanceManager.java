package attendance;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AttendanceManager {
    
    private final Map<String, Student>         students   = new HashMap<>();
    private final Map<String, Teacher>         teachers   = new HashMap<>();
    private final Map<String, Subject>         subjects   = new HashMap<>();
    private final List<AttendanceRecord>       records    = new ArrayList<>();

    rivate final Map<String, Set<String>>     enrollment = new HashMap<>();

    private final double minAttendanceThreshold; 
    private int recordCounter = 1;

    public AttendanceManager(double minAttendanceThreshold) {
        if (minAttendanceThreshold <= 0 || minAttendanceThreshold > 1)
            throw new IllegalArgumentException("Threshold must be between 0 and 1.");
        this.minAttendanceThreshold = minAttendanceThreshold;
    }


    public void addStudent(Student student) {
        students.put(student.getUserId(), student);
        enrollment.put(student.getUserId(), new LinkedHashSet<>());
    }

    public void addTeacher(Teacher teacher) {
        teachers.put(teacher.getUserId(), teacher);
    }

    public void addSubject(Subject subject) {
        subjects.put(subject.getSubjectId(), subject);
    }

       public void enrollStudent(String studentId, String subjectId) {
        if (!students.containsKey(studentId))
            throw new IllegalArgumentException("Student not found: " + studentId);
        if (!subjects.containsKey(subjectId))
            throw new IllegalArgumentException("Subject not found: " + subjectId);
        enrollment.get(studentId).add(subjectId);
    }
    public AttendanceRecord recordAttendance(String studentId, String subjectId,
                                             LocalDate date, AttendanceStatus status,
                                             String remarks, String teacherId) {
        validateEnrollment(studentId, subjectId);

        String recordId = String.format("REC-%04d", recordCounter++);
        AttendanceRecord record = new AttendanceRecord(
                recordId, studentId, subjectId, date, status, remarks, teacherId);
        records.add(record);
        return record;
    }

     public void recordClassSession(String subjectId, LocalDate date,
                                   Map<String, AttendanceStatus> attendanceMap,
                                   String teacherId) {
        Subject subject = getSubjectOrThrow(subjectId);
        subject.incrementTotalClasses(); // one class = one session

        for (Map.Entry<String, AttendanceStatus> entry : attendanceMap.entrySet()) {
            String studentId = entry.getKey();
            AttendanceStatus status = entry.getValue();
            validateEnrollment(studentId, subjectId);
            String recordId = String.format("REC-%04d", recordCounter++);
            records.add(new AttendanceRecord(
                    recordId, studentId, subjectId, date, status, "", teacherId));
        }
    }

     public boolean editAttendance(String recordId, AttendanceStatus newStatus,
                                  String editRemarks) {
        for (AttendanceRecord r : records) {
            if (r.getRecordId().equals(recordId)) {
                r.setStatus(newStatus, editRemarks);
                return true;
            }
        }
        return false;

    public AttendanceSummary computeSummary(String studentId, String subjectId) {
        Student student = getStudentOrThrow(studentId);
        Subject subject = getSubjectOrThrow(subjectId);

        List<AttendanceRecord> studentSubjectRecords = records.stream()
                .filter(r -> r.getStudentId().equals(studentId)
                          && r.getSubjectId().equals(subjectId))
                .collect(Collectors.toList());

        int attended = (int) studentSubjectRecords.stream()
                .filter(AttendanceRecord::isPresent).count();
        int leaves = (int) studentSubjectRecords.stream()
                .filter(AttendanceRecord::isLeave).count();
        int absent = (int) studentSubjectRecords.stream()
                .filter(AttendanceRecord::isAbsent).count();

        return new AttendanceSummary(
                studentId, student.getName(),
                subjectId, subject.getSubjectName(),
                subject.getTotalClasses(),
                attended, leaves, absent,
                minAttendanceThreshold);
    }

    public void printStudentSubjectReport(String studentId, String subjectId) {
        AttendanceSummary s = computeSummary(studentId, subjectId);
        System.out.println();
        System.out.printf("  Subject         : %s (%s)%n",
                s.getSubjectName(), s.getSubjectId());
        System.out.printf("  Total Classes   : %d%n", s.getTotalClasses());
        System.out.printf("  Attended        : %d   |  Absent: %d  |  Leave: %d%n",
                s.getClassesAttended(), s.getClassesAbsent(), s.getLeavesApproved());
        System.out.printf("  Attendance %%    : %.1f%%%n", s.getAttendancePercent());
        System.out.printf("  Minimum Required: %.0f%%%n",
                s.getThreshold() * 100);
        System.out.printf("  Status          : %s%n", s.getStatusLabel());
        System.out.printf("  Allowable Absences Remaining: %d%n",
                s.getAllowableAbsences());
        System.out.println("  " + "-".repeat(50));
    }

    public void printSubjectAttendanceSummary(String subjectId) {
        Subject subject = getSubjectOrThrow(subjectId);

                List<String> enrolled = enrollment.entrySet().stream()
                .filter(e -> e.getValue().contains(subjectId))
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());

        System.out.printf("  %-20s %-10s %-8s %-8s %-10s%n",
                "Student", "Attended", "Absent", "Leave", "Percent");
        System.out.println("  " + "-".repeat(56));

        for (String sid : enrolled) {
            AttendanceSummary s = computeSummary(sid, subjectId);
            System.out.printf("  %-20s %-10d %-8d %-8d %.1f%%%n",
                    s.getStudentName(),
                    s.getClassesAttended(),
                    s.getClassesAbsent(),
                    s.getLeavesApproved(),
                    s.getAttendancePercent());
        }
    }
    public void printLowAttendanceReport(String teacherId) {
        List<Subject> teacherSubjects = getSubjectsForTeacher(teacherId);
        boolean anyFound = false;

        for (Subject subject : teacherSubjects) {
            List<String> enrolled = enrollment.entrySet().stream()
                    .filter(e -> e.getValue().contains(subject.getSubjectId()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            for (String sid : enrolled) {
                AttendanceSummary s = computeSummary(sid, subject.getSubjectId());
                if (!s.isCompliant()) {
                    System.out.printf("  ⚠  %-20s (%s): %.1f%% (%d/%d classes)%n",
                            s.getStudentName(), s.getSubjectName(),
                            s.getAttendancePercent(),
                            s.getClassesAttended(), s.getTotalClasses());
                    anyFound = true;
                }
            }
        }

        if (!anyFound) {
            System.out.println("  ✓  All students meet the attendance threshold.");
        }
    }

     public void printAttendanceLog(String studentId, String subjectId) {
        System.out.println("\n  Attendance Log — " + subjectId);
        System.out.println("  " + "-".repeat(50));
        records.stream()
               .filter(r -> r.getStudentId().equals(studentId)
                         && r.getSubjectId().equals(subjectId))
               .sorted(Comparator.comparing(AttendanceRecord::getDate))
               .forEach(System.out::println);
    }
    public List<Subject> getSubjectsForStudent(String studentId) {
        Set<String> subjectIds = enrollment.getOrDefault(studentId, Collections.emptySet());
        return subjectIds.stream()
                .map(subjects::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Subject> getSubjectsForTeacher(String teacherId) {
        return subjects.values().stream()
                .filter(s -> s.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }

    public Student getStudent(String studentId) { return students.get(studentId); }
    public Teacher getTeacher(String teacherId) { return teachers.get(teacherId); }
    public Subject getSubject(String subjectId) { return subjects.get(subjectId); }

    public Collection<Student> getAllStudents() { return students.values(); }
    public Collection<Teacher> getAllTeachers() { return teachers.values(); }
    public Collection<Subject> getAllSubjects() { return subjects.values(); }

    public double getMinAttendanceThreshold() { return minAttendanceThreshold; }
    private void validateEnrollment(String studentId, String subjectId) {
        if (!students.containsKey(studentId))
            throw new IllegalArgumentException("Student not found: " + studentId);
        if (!subjects.containsKey(subjectId))
            throw new IllegalArgumentException("Subject not found: " + subjectId);
        if (!enrollment.getOrDefault(studentId, Collections.emptySet()).contains(subjectId))
            throw new IllegalArgumentException(
                    "Student " + studentId + " is not enrolled in " + subjectId);
    }

    private Student getStudentOrThrow(String id) {
        Student s = students.get(id);
        if (s == null) throw new IllegalArgumentException("Student not found: " + id);
        return s;
    }

    private Subject getSubjectOrThrow(String id) {
        Subject s = subjects.get(id);
        if (s == null) throw new IllegalArgumentException("Subject not found: " + id);
        return s;
    }
}
