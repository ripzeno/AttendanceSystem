package attendance;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


enum AttendanceStatus {
    PRESENT("P"),
    ABSENT("A"),
    LEAVE("L");
    private final String code;
    AttendanceStatus(String code) { this.code = code; }
    public String getCode() { return code; }
}

public class AttendanceRecord {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    private String           recordId;
    private String           studentId;
    private String           subjectId;
    private LocalDate        date;
    private AttendanceStatus status;
    private String           remarks;
    private String           recordedBy;
    public AttendanceRecord(String recordId, String studentId, String subjectId,
                            LocalDate date, AttendanceStatus status,
                            String remarks, String recordedBy) {
        this.recordId   = recordId;
        this.studentId  = studentId;
        this.subjectId  = subjectId;
        this.date       = date;
        this.status     = status;
        this.remarks    = (remarks != null) ? remarks : "";
        this.recordedBy = recordedBy;
    }
    public String           getRecordId()   { return recordId; }
    public String           getStudentId()  { return studentId; }
    public String           getSubjectId()  { return subjectId; }
    public LocalDate        getDate()       { return date; }
    public AttendanceStatus getStatus()     { return status; }
    public String           getRemarks()    { return remarks; }
    public String           getRecordedBy() { return recordedBy; }
    public void setStatus(AttendanceStatus status, String remarks) {
        this.status  = status;
        this.remarks = (remarks != null) ? remarks : "";
    }

    public boolean isPresent() { return status == AttendanceStatus.PRESENT; }
    public boolean isLeave()   { return status == AttendanceStatus.LEAVE; }
    public boolean isAbsent()  { return status == AttendanceStatus.ABSENT; }

    @Override
    public String toString() {
        return String.format("  %s | %-12s | %s [%s] %s",
                recordId, date.format(DATE_FMT),
                status.getCode(), status.name(),
                remarks.isEmpty() ? "" : "(" + remarks + ")");
    }
}
