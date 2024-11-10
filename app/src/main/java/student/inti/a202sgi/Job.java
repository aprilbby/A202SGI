package student.inti.a202sgi;

public class Job {
    private String title;
    private String company;
    private String location;

    // Empty constructor required for Firebase
    public Job() {
    }

    // Constructor with parameters
    public Job(String title, String company, String location) {
        this.title = title;
        this.company = company;
        this.location = location;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
