package attendance;

import java.util.List;


public class Student extends User {

    private String program;
    private int semester;

    public Student(String studentId, String name, String email,
                   String program, int semester) {
        super(studentId, name, email, "STUDENT");
        this.program = program;
        this.semester = semester;
    }

    public String getProgram()  { return program; }
    public int    getSemester() { return semester; }

   
    @Override
    public void displayDashboard(AttendanceManager manager) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  STUDENT ATTENDANCE DASHBOARD");
        System.out.println("=".repeat(60));
        System.out.printf("  Student : %s%n", getName());
        System.out.printf("  ID      : %s%n", getUserId());
        System.out.printf("  Program : %s  |  Semester: %d%n", program, semester);
        System.out.println("=".repeat(60));

        List<Subject> subjects = manager.getSubjectsForStudent(getUserId());
        if (subjects.isEmpty()) {
            System.out.println("  No subjects enrolled.");
        } else {
            for (Subject subject : subjects) {
                manager.printStudentSubjectReport(getUserId(), subject.getSubjectId());
            }
        }
        System.out.println("=".repeat(60));
    }

    @Override
    public String toString() {
        return String.format("Student{id='%s', name='%s', program='%s', sem=%d}",
                getUserId(), getName(), program, semester);
    }
}
