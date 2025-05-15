public class Employee {
    private String name;
    private String id;
    private String password;
    private String department;
    private String contact;

    public Employee(String name, String id, String password, String department, String contact) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.department = department;
        this.contact = contact;
    }

    // Getters and setters
    public String getName() { return name; }
    public String getId() { return id; }
    public String getPassword() { return password; }
    public String getDepartment() { return department; }
    public String getContact() { return contact; }
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setDepartment(String department) { this.department = department; }
    public void setContact(String contact) { this.contact = contact; }

    @Override
    public String toString() {
        // Format: name,id,password,department,contact
        return name + "," + id + "," + password + "," + department + "," + contact;
    }
}
