package model;

import java.util.Date;

public class Student {

    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private Date birthDate;
    private String groupName;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public Student(Long id, String firstName, String lastName, String middleName, Date birthDate, String groupName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.groupName = groupName;
    }
}
