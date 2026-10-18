
package org.example;

public class Student {

    private int studentId;
    private String fullName;
    private String phone;
    private String email;
    private String course;
    private Integer roomId;

    public Student(int studentId, String fullName, String phone,
                   String email, String course, Integer roomId) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.course = course;
        this.roomId = roomId;
    }

    public Student(String fullName, String phone,
                   String email, String course) {
        this(0, fullName, phone, email, course, null);
    }

    public int getStudentId() {
        return studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCourse() {
        return course;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "Student ID: " + studentId
                + " | Name: " + fullName
                + " | Phone: " + phone
                + " | Email: " + email
                + " | Course: " + course
                + " | Room ID: " + (roomId == null ? "Not Allocated" : roomId);
    }
}
