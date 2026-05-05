package attendance;

import java.time.LocalDate;
import java.util.*;

public class AttendanceSystemDemo {

    private static final AttendanceManager manager =
            new AttendanceManager(0.75); 

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║    ATTENDANCE MANAGEMENT SYSTEM v1.0     ║");
        System.out.println("  ╚══════════════════════════════════════════╝");

        populateSampleData();
        showMainMenu();

        scanner.close();
        System.out.println("\n  Goodbye!\n");
    }

    private static void populateSampleData() {

        Teacher drPatel = new Teacher("T001", "Dr. Priya Patel",
                "p.patel@univ.edu", "Mathematics", "EMP-101");
        Teacher mrKumar = new Teacher("T002", "Mr. Rahul Kumar",
                "r.kumar@univ.edu", "Physics", "EMP-102");
        manager.addTeacher(drPatel);
        manager.addTeacher(mrKumar);

        Subject math    = new Subject("MATH101", "Mathematics",    "MTH-101", "T001", 0);
        Subject physics = new Subject("PHY101",  "Physics",        "PHY-101", "T002", 0);
        Subject cs      = new Subject("CS101",   "Computer Science","CS-101", "T001", 0);
        manager.addSubject(math);
        manager.addSubject(physics);
        manager.addSubject(cs);

        Student alice = new Student("S001", "Alice Smith",   "alice@student.edu",  "B.Sc.", 3);
        Student bob   = new Student("S002", "Bob Johnson",   "bob@student.edu",    "B.Sc.", 3);
        Student carol = new Student("S003", "Carol Williams","carol@student.edu",  "B.Sc.", 3);
        Student dave  = new Student("S004", "Dave Brown",    "dave@student.edu",   "B.Tech",3);
        Student eve   = new Student("S005", "Eve Davis",     "eve@student.edu",    "B.Tech",3);
        manager.addStudent(alice);
        manager.addStudent(bob);
        manager.addStudent(carol);
        manager.addStudent(dave);
        manager.addStudent(eve);

        String[] allStudents = {"S001","S002","S003","S004","S005"};
        for (String sid : allStudents) {
            manager.enrollStudent(sid, "MATH101");
            manager.enrollStudent(sid, "PHY101");
            manager.enrollStudent(sid, "CS101");
        }

        recordSession("MATH101", LocalDate.of(2025,1,6),  "PPPPP", "T001");
        recordSession("MATH101", LocalDate.of(2025,1,8),  "PPPPP", "T001");
        recordSession("MATH101", LocalDate.of(2025,1,10), "PPAPP", "T001");  // Carol absent
        recordSession("MATH101", LocalDate.of(2025,1,13), "PPAPP", "T001");
        recordSession("MATH101", LocalDate.of(2025,1,15), "PAPPP", "T001");  // Bob absent
        recordSession("MATH101", LocalDate.of(2025,1,17), "PAPAP", "T001");  // Bob,Dave absent
        recordSession("MATH101", LocalDate.of(2025,1,20), "PAAPP", "T001");
        recordSession("MATH101", LocalDate.of(2025,1,22), "PAAAP", "T001");  // Bob,Carol,Dave absent
        recordSession("MATH101", LocalDate.of(2025,1,24), "PAAAP", "T001");
        recordSession("MATH101", LocalDate.of(2025,1,27), "PPAAP", "T001");
        recordSession("MATH101", LocalDate.of(2025,1,29), "PPAAP", "T001");
        recordSession("MATH101", LocalDate.of(2025,1,31), "PPAPP", "T001");
        recordSession("MATH101", LocalDate.of(2025,2,3),  "PPLPP", "T001");  // Carol on leave
        recordSession("MATH101", LocalDate.of(2025,2,5),  "PPAPP", "T001");
        recordSession("MATH101", LocalDate.of(2025,2,7),  "PPAAP", "T001");
        recordSession("MATH101", LocalDate.of(2025,2,10), "PPAPP", "T001");
        recordSession("MATH101", LocalDate.of(2025,2,12), "PPPPP", "T001");
        recordSession("MATH101", LocalDate.of(2025,2,14), "PPPPP", "T001");
        recordSession("MATH101", LocalDate.of(2025,2,17), "PPPPP", "T001");
        recordSession("MATH101", LocalDate.of(2025,2,19), "PPPPP", "T001");

        recordSession("PHY101", LocalDate.of(2025,1,7),  "PPPPP", "T002");
        recordSession("PHY101", LocalDate.of(2025,1,9),  "APPPP", "T002");  // Alice absent
        recordSession("PHY101", LocalDate.of(2025,1,11), "APPPP", "T002");
        recordSession("PHY101", LocalDate.of(2025,1,14), "PAPPP", "T002");  // Bob absent
        recordSession("PHY101", LocalDate.of(2025,1,16), "PAPPP", "T002");
        recordSession("PHY101", LocalDate.of(2025,1,18), "PAPAP", "T002");
        recordSession("PHY101", LocalDate.of(2025,1,21), "PAPAP", "T002");
        recordSession("PHY101", LocalDate.of(2025,1,23), "PAPAP", "T002");
        recordSession("PHY101", LocalDate.of(2025,1,25), "PPPPP", "T002");
        recordSession("PHY101", LocalDate.of(2025,1,28), "PPPAP", "T002");
        recordSession("PHY101", LocalDate.of(2025,1,30), "PPPAP", "T002");
        recordSession("PHY101", LocalDate.of(2025,2,1),  "PPPPP", "T002");
        recordSession("PHY101", LocalDate.of(2025,2,4),  "PPPPP", "T002");
        recordSession("PHY101", LocalDate.of(2025,2,6),  "PPPPP", "T002");
        recordSession("PHY101", LocalDate.of(2025,2,8),  "LPPPP", "T002");  // Alice on leave
        recordSession("PHY101", LocalDate.of(2025,2,11), "PPPPP", "T002");
        recordSession("PHY101", LocalDate.of(2025,2,13), "PPPPP", "T002");
        recordSession("PHY101", LocalDate.of(2025,2,15), "PPPAP", "T002");
        recordSession("PHY101", LocalDate.of(2025,2,18), "PPPPP", "T002");
        recordSession("PHY101", LocalDate.of(2025,2,20), "PPPPP", "T002");

        recordSession("CS101", LocalDate.of(2025,1,6),  "PPPPP", "T001");
        recordSession("CS101", LocalDate.of(2025,1,8),  "PPPPA", "T001");  // Eve absent
        recordSession("CS101", LocalDate.of(2025,1,10), "PPPPA", "T001");
        recordSession("CS101", LocalDate.of(2025,1,13), "PPPPA", "T001");
        recordSession("CS101", LocalDate.of(2025,1,15), "PPPPA", "T001");
        recordSession("CS101", LocalDate.of(2025,1,17), "PPPPA", "T001");
        recordSession("CS101", LocalDate.of(2025,1,20), "PPPPA", "T001");
        recordSession("CS101", LocalDate.of(2025,1,22), "PPPPA", "T001");
        recordSession("CS101", LocalDate.of(2025,1,24), "PPPPA", "T001");
        recordSession("CS101", LocalDate.of(2025,1,27), "PPPPA", "T001");
        recordSession("CS101", LocalDate.of(2025,1,29), "PPPPA", "T001");
        recordSession("CS101", LocalDate.of(2025,1,31), "PPPPA", "T001");
        recordSession("CS101", LocalDate.of(2025,2,3),  "PPPPA", "T001");
        recordSession("CS101", LocalDate.of(2025,2,5),  "PPPPA", "T001");
        recordSession("CS101", LocalDate.of(2025,2,7),  "PPPPA", "T001");
        recordSession("CS101", LocalDate.of(2025,2,10), "PPPPP", "T001");
        recordSession("CS101", LocalDate.of(2025,2,12), "PPPPP", "T001");
        recordSession("CS101", LocalDate.of(2025,2,14), "PPPPP", "T001");
        recordSession("CS101", LocalDate.of(2025,2,17), "PPPPP", "T001");
        recordSession("CS101", LocalDate.of(2025,2,19), "PPPPP", "T001");

        System.out.println("\n  ✓ Sample data loaded successfully.");
        System.out.printf("  ✓ Threshold set to %.0f%%%n",
                manager.getMinAttendanceThreshold() * 100);
    }

    private static final String[] STUDENT_IDS = {"S001","S002","S003","S004","S005"};

    private static void recordSession(String subjectId, LocalDate date,
                                      String pattern, String teacherId) {
        Map<String, AttendanceStatus> map = new LinkedHashMap<>();
        for (int i = 0; i < STUDENT_IDS.length && i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            AttendanceStatus status;
            if      (c == 'A') status = AttendanceStatus.ABSENT;
            else if (c == 'L') status = AttendanceStatus.LEAVE;
            else               status = AttendanceStatus.PRESENT;
            map.put(STUDENT_IDS[i], status);
        }
        manager.recordClassSession(subjectId, date, map, teacherId);
    }


    private static void showMainMenu() {
        while (true) {
            System.out.println("\n" + "─".repeat(50));
            System.out.println("  MAIN MENU");
            System.out.println("─".repeat(50));
            System.out.println("  1. Login as Student");
            System.out.println("  2. Login as Teacher");
            System.out.println("  3. System Overview (Admin)");
            System.out.println("  0. Exit");
            System.out.print("  Choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": studentLoginMenu(); break;
                case "2": teacherLoginMenu(); break;
                case "3": systemOverview();   break;
                case "0": return;
                default:  System.out.println("  Invalid option. Try again.");
            }
        }
    }


    private static void studentLoginMenu() {
        System.out.println("\n  Available students:");
        manager.getAllStudents().forEach(s ->
                System.out.printf("    %-6s  %s%n", s.getUserId(), s.getName()));
        System.out.print("  Enter Student ID: ");
        String id = scanner.nextLine().trim().toUpperCase();
        Student student = manager.getStudent(id);
        if (student == null) {
            System.out.println("  Student not found.");
            return;
        }
        studentMenu(student);
    }

    private static void studentMenu(Student student) {
        while (true) {
            System.out.println("\n" + "─".repeat(50));
            System.out.printf("  Logged in as: %s%n", student.getName());
            System.out.println("─".repeat(50));
            System.out.println("  1. View My Attendance Dashboard");
            System.out.println("  2. View Detailed Log for a Subject");
            System.out.println("  0. Logout");
            System.out.print("  Choice: ");

            switch (scanner.nextLine().trim()) {
                case "1":
                    student.displayDashboard(manager);  
                    break;
                case "2":
                    printSubjectLogForStudent(student.getUserId());
                    break;
                case "0":
                    return;
                default:
                    System.out.println("  Invalid option.");
            }
        }
    }

    private static void printSubjectLogForStudent(String studentId) {
        System.out.println("  Subjects: MATH101 | PHY101 | CS101");
        System.out.print("  Enter Subject ID: ");
        String sid = scanner.nextLine().trim().toUpperCase();
        if (manager.getSubject(sid) == null) {
            System.out.println("  Subject not found.");
            return;
        }
        manager.printAttendanceLog(studentId, sid);
    }


    private static void teacherLoginMenu() {
        System.out.println("\n  Available teachers:");
        manager.getAllTeachers().forEach(t ->
                System.out.printf("    %-6s  %s%n", t.getUserId(), t.getName()));
        System.out.print("  Enter Teacher ID: ");
        String id = scanner.nextLine().trim().toUpperCase();
        Teacher teacher = manager.getTeacher(id);
        if (teacher == null) {
            System.out.println("  Teacher not found.");
            return;
        }
        teacherMenu(teacher);
    }

    private static void teacherMenu(Teacher teacher) {
        while (true) {
            System.out.println("\n" + "─".repeat(50));
            System.out.printf("  Logged in as: %s%n", teacher.getName());
            System.out.println("─".repeat(50));
            System.out.println("  1. View Teacher Dashboard");
            System.out.println("  2. View Subject Summary");
            System.out.println("  3. Edit an Attendance Record");
            System.out.println("  0. Logout");
            System.out.print("  Choice: ");

            switch (scanner.nextLine().trim()) {
                case "1":
                    teacher.displayDashboard(manager);  
                    break;
                case "2":
                    teacherSubjectSummary(teacher.getUserId());
                    break;
                case "3":
                    editRecord(teacher.getUserId());
                    break;
                case "0":
                    return;
                default:
                    System.out.println("  Invalid option.");
            }
        }
    }

    private static void teacherSubjectSummary(String teacherId) {
        manager.getSubjectsForTeacher(teacherId).forEach(sub -> {
            System.out.printf("%n  [ %s — %s ]%n", sub.getSubjectName(), sub.getSubjectId());
            manager.printSubjectAttendanceSummary(sub.getSubjectId());
        });
    }

    private static void editRecord(String teacherId) {
        System.out.print("  Enter Record ID to edit (e.g., REC-0003): ");
        String recordId = scanner.nextLine().trim().toUpperCase();
        System.out.println("  New status: 1=PRESENT  2=ABSENT  3=LEAVE");
        System.out.print("  Choice: ");
        String sc = scanner.nextLine().trim();
        AttendanceStatus newStatus;
        switch (sc) {
            case "1": newStatus = AttendanceStatus.PRESENT; break;
            case "2": newStatus = AttendanceStatus.ABSENT;  break;
            case "3": newStatus = AttendanceStatus.LEAVE;   break;
            default:
                System.out.println("  Invalid status.");
                return;
        }
        System.out.print("  Remarks (e.g., Medical certificate provided): ");
        String remarks = scanner.nextLine().trim();
        boolean updated = manager.editAttendance(recordId, newStatus, remarks);
        System.out.println(updated
                ? "  ✓ Record updated successfully."
                : "  ✗ Record ID not found.");
    }


    private static void systemOverview() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  SYSTEM OVERVIEW");
        System.out.println("=".repeat(60));
        System.out.printf("  Students  : %d%n", manager.getAllStudents().size());
        System.out.printf("  Teachers  : %d%n", manager.getAllTeachers().size());
        System.out.printf("  Subjects  : %d%n", manager.getAllSubjects().size());
        System.out.printf("  Threshold : %.0f%%%n",
                manager.getMinAttendanceThreshold() * 100);

        System.out.println("\n  Subjects:");
        manager.getAllSubjects().forEach(sub ->
                System.out.printf("    %-10s %-20s (Classes: %d)%n",
                        sub.getSubjectId(), sub.getSubjectName(), sub.getTotalClasses()));

        System.out.println("\n  Students:");
        manager.getAllStudents().forEach(stu ->
                System.out.printf("    %-6s %-20s %s Sem-%d%n",
                        stu.getUserId(), stu.getName(),
                        stu.getProgram(), stu.getSemester()));
        System.out.println("=".repeat(60));
    }
}
