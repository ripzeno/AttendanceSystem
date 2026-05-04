package attendance;


public class AttendanceSummary {

    private final String studentId;
    private final String studentName;
    private final String subjectId;
    private final String subjectName;
    private final int    totalClasses;
    private final int    classesAttended;   
    private final int    leavesApproved;    
    private final int    classesAbsent;     
    private final double attendancePercent; 
    private final double threshold;         
    private final int    allowableAbsences;

    public AttendanceSummary(String studentId, String studentName,
                             String subjectId, String subjectName,
                             int totalClasses, int classesAttended,
                             int leavesApproved, int classesAbsent,
                             double threshold) {
        this.studentId       = studentId;
        this.studentName     = studentName;
        this.subjectId       = subjectId;
        this.subjectName     = subjectName;
        this.totalClasses    = totalClasses;
        this.classesAttended = classesAttended;
        this.leavesApproved  = leavesApproved;
        this.classesAbsent   = classesAbsent;
        this.threshold       = threshold;

        this.attendancePercent = (totalClasses == 0) ? 0.0
                : (classesAttended * 100.0) / totalClasses;

        int minRequired = (int) Math.ceil(totalClasses * threshold);
        this.allowableAbsences = Math.max(0,
                totalClasses - minRequired - classesAbsent);
    }

    public String getStudentId()       { return studentId; }
    public String getStudentName()     { return studentName; }
    public String getSubjectId()       { return subjectId; }
    public String getSubjectName()     { return subjectName; }
    public int    getTotalClasses()    { return totalClasses; }
    public int    getClassesAttended() { return classesAttended; }
    public int    getLeavesApproved()  { return leavesApproved; }
    public int    getClassesAbsent()   { return classesAbsent; }
    public double getAttendancePercent(){ return attendancePercent; }
    public double getThreshold()       { return threshold; }
    public int    getAllowableAbsences(){ return allowableAbsences; }

    public boolean isCompliant() {
        return attendancePercent >= (threshold * 100);
    }

    public String getStatusLabel() {
        return isCompliant() ? "COMPLIANT ✓" : "AT RISK  ✗";
    }
}
