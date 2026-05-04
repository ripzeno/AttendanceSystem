package attendance;


public class Subject {

    private String subjectId;
    private String subjectName;
    private String subjectCode;
    private String teacherId;
    private int    totalClasses;

    public Subject(String subjectId, String subjectName, String subjectCode,
                   String teacherId, int totalClasses) {
        this.subjectId    = subjectId;
        this.subjectName  = subjectName;
        this.subjectCode  = subjectCode;
        this.teacherId    = teacherId;
        this.totalClasses = totalClasses;
    }

    // Getters
    public String getSubjectId()   { return subjectId; }
    public String getSubjectName() { return subjectName; }
    public String getSubjectCode() { return subjectCode; }
    public String getTeacherId()   { return teacherId; }
    public int    getTotalClasses(){ return totalClasses; }

    // Setters
    public void setTotalClasses(int totalClasses) {
        if (totalClasses < 0) throw new IllegalArgumentException("Total classes cannot be negative.");
        this.totalClasses = totalClasses;
    }

    /** Increment class count when a new lecture is recorded. */
    public void incrementTotalClasses() { this.totalClasses++; }

    @Override
    public String toString() {
        return String.format("Subject{id='%s', name='%s', code='%s', classes=%d}",
                subjectId, subjectName, subjectCode, totalClasses);
    }
}
