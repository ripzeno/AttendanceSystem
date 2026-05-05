package attendance;


public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String role;

    public User(String userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    
    public abstract void displayDashboard(AttendanceManager manager);

    
    public String getUserId() { return userId; }
    public String getName()   { return name; }
    public String getEmail()  { return email; }
    public String getRole()   { return role; }

    
    public void setName(String name)   { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return String.format("[%s] %s (ID: %s)", role, name, userId);
    }
}
