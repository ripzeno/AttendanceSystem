package attendance;

import java.util.List;


public class Teacher extends User {

    private String department;
    private String employeeCode;

    public Teacher(String teacherId, String name, String email,
                   String department, String employeeCode) {
        super(teacherId, name, email, "TEACHER");
        this.department = department;
        this.employeeCode = employeeCode;
    }

    public String getDepartment()   { return department; }
    public String getEmployeeCode() { return employeeCode; }

    
    @Override
    public void displayDashboard(AttendanceManager manager) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  TEACHER DASHBOARD");
        System.out.println("=".repeat(60));
        System.out.printf("  Teacher    : %s%n", getName());
        System.out.printf("  Department : %s  |  Code: %s%n", department, employeeCode);
        System.out.println("=".repeat(60));

        List<Subject> subjects = manager.getSubjectsForTeacher(getUserId());
        if (subjects.isEmpty()) {
            System.out.println("  No subjects assigned.");
            System.out.println("=".repeat(60));
            return;
        }

        System.out.println("\n  [ SUBJECTS OVERVIEW ]");
        for (Subject subject : subjects) {
            System.out.printf("%n  Subject: %s (%s) | Total Classes: %d%n",
                    subject.getSubjectName(), subject.getSubjectId(),
                    subject.getTotalClasses());
            manager.printSubjectAttendanceSummary(subject.getSubjectId());
        }

        System.out.println("\n" + "-".repeat(60));
        System.out.printf("  LOW ATTENDANCE ALERT (Below %.0f%%):%n",
                manager.getMinAttendanceThreshold() * 100);
        System.out.println("-".repeat(60));
        manager.printLowAttendanceReport(getUserId());
        System.out.println("=".repeat(60));
    }

    @Override
    public String toString() {
        return String.format("Teacher{id='%s', name='%s', dept='%s'}",
                getUserId(), getName(), department);
    }
}
