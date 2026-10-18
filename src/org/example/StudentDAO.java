
package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO {


    public void addStudent(Student student) {

        String sql = "INSERT INTO students "
                + "(full_name, phone, email, course) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, student.getFullName());
            ps.setString(2, student.getPhone());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getCourse());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        System.out.println(
                                "Student added successfully! ID: "
                                        + keys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }


    public void viewStudents() {

        String sql = "SELECT * FROM students";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                found = true;

                Student student = new Student(
                        rs.getInt("student_id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getObject("room_id") == null
                                ? null : rs.getInt("room_id")
                );

                System.out.println(student);
            }

            if (!found) {
                System.out.println("No students found.");
            }

        } catch (SQLException e) {
            System.out.println("Error viewing students: " + e.getMessage());
        }
    }


    public void updateStudent(int id, String phone, String email) {

        String sql = "UPDATE students SET phone = ?, email = ? "
                + "WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, phone);
            ps.setString(2, email);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }


    public void deleteStudent(int id) {

        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student ID not found.");
            return;
        }

        if (student.getRoomId() != null) {
            System.out.println(
                    "Vacate the student's room before deleting.");
            return;
        }

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }


    public Student findStudent(int id) {

        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Integer roomId = rs.getObject("room_id") == null
                            ? null : rs.getInt("room_id");

                    return new Student(
                            rs.getInt("student_id"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("course"),
                            roomId
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding student: " + e.getMessage());
        }

        return null;
    }


    public boolean allocateRoom(int studentId, int roomId) {

        String sql = "UPDATE students SET room_id = ? "
                + "WHERE student_id = ? AND room_id IS NULL";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ps.setInt(2, studentId);

            if (ps.executeUpdate() > 0) {
                return true;
            }

            System.out.println(
                    "Student not found or already has a room.");

        } catch (SQLException e) {
            System.out.println("Error allocating room: " + e.getMessage());
        }

        return false;
    }


    public boolean vacateRoom(int studentId) {

        String sql = "UPDATE students SET room_id = NULL "
                + "WHERE student_id = ? AND room_id IS NOT NULL";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            if (ps.executeUpdate() > 0) {
                return true;
            }

            System.out.println(
                    "Student not found or has no allocated room.");

        } catch (SQLException e) {
            System.out.println("Error vacating room: " + e.getMessage());
        }

        return false;
    }
}
