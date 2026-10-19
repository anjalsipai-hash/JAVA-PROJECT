
package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        StudentDAO studentDAO = new StudentDAO();
        RoomDAO roomDAO = new RoomDAO();

        while (true) {

            System.out.println("\n================================");
            System.out.println("    HOSTEL MANAGEMENT SYSTEM");
            System.out.println("================================");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Add Room");
            System.out.println("6. View Rooms");
            System.out.println("7. Allocate Room to Student");
            System.out.println("8. Vacate Student Room");
            System.out.println("9. Exit");
            System.out.println("================================");

            int choice = readInt(sc, "Enter your choice: ");

            switch (choice) {

                case 1: {
                    System.out.println("\n--- Add Student ---");

                    System.out.print("Enter full name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter phone: ");
                    String phone = sc.nextLine();

                    System.out.print("Enter email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter course: ");
                    String course = sc.nextLine();

                    if (name.trim().isEmpty()) {
                        System.out.println("Name cannot be empty.");
                        break;
                    }

                    Student student =
                            new Student(name, phone, email, course);

                    studentDAO.addStudent(student);
                    break;
                }

                case 2: {
                    System.out.println("\n--- Student List ---");
                    studentDAO.viewStudents();
                    break;
                }

                case 3: {
                    System.out.println("\n--- Update Student ---");

                    int id = readInt(sc, "Enter student ID: ");

                    System.out.print("Enter new phone: ");
                    String phone = sc.nextLine();

                    System.out.print("Enter new email: ");
                    String email = sc.nextLine();

                    studentDAO.updateStudent(id, phone, email);
                    break;
                }

                case 4: {
                    System.out.println("\n--- Delete Student ---");

                    int id = readInt(sc, "Enter student ID: ");

                    studentDAO.deleteStudent(id);
                    break;
                }

                case 5: {
                    System.out.println("\n--- Add Room ---");

                    System.out.print(
                            "Enter room type (Single/Double/Shared): ");
                    String roomType = sc.nextLine();

                    int capacity =
                            readInt(sc, "Enter room capacity: ");

                    if (roomType.trim().isEmpty()) {
                        System.out.println(
                                "Room type cannot be empty.");
                        break;
                    }

                    roomDAO.addRoom(roomType, capacity);
                    break;
                }

                case 6: {
                    System.out.println("\n--- Room List ---");
                    roomDAO.viewRooms();
                    break;
                }

                case 7: {
                    System.out.println("\n--- Allocate Room ---");

                    int studentId =
                            readInt(sc, "Enter student ID: ");

                    int roomId =
                            readInt(sc, "Enter room ID: ");

                    Student student =
                            studentDAO.findStudent(studentId);

                    if (student == null) {
                        System.out.println("Student ID not found.");
                        break;
                    }

                    if (student.getRoomId() != null) {
                        System.out.println(
                                "This student already has a room.");
                        break;
                    }

                    if (!roomDAO.hasAvailableRoom(roomId)) {
                        System.out.println(
                                "Room not found or room is full.");
                        break;
                    }

                    boolean studentUpdated =
                            studentDAO.allocateRoom(studentId, roomId);

                    if (!studentUpdated) {
                        System.out.println(
                                "Could not allocate room to student.");
                        break;
                    }


                    boolean roomUpdated =
                            roomDAO.allocateRoom(roomId);

                    if (roomUpdated) {
                        System.out.println(
                                "Room allocated successfully!");
                    } else {

                        studentDAO.vacateRoom(studentId);
                        System.out.println(
                                "Allocation failed. Student record restored.");
                    }

                    break;
                }

                case 8: {
                    System.out.println("\n--- Vacate Room ---");

                    int studentId =
                            readInt(sc, "Enter student ID: ");

                    Student student =
                            studentDAO.findStudent(studentId);

                    if (student == null) {
                        System.out.println("Student ID not found.");
                        break;
                    }

                    if (student.getRoomId() == null) {
                        System.out.println(
                                "This student has no allocated room.");
                        break;
                    }

                    int oldRoomId = student.getRoomId();

                    // First, clear the student's room assignment.
                    boolean studentUpdated =
                            studentDAO.vacateRoom(studentId);

                    if (!studentUpdated) {
                        System.out.println(
                                "Could not vacate the student's room.");
                        break;
                    }

                    // Then decrease the room's occupied count.
                    boolean roomUpdated =
                            roomDAO.releaseRoom(oldRoomId);

                    if (roomUpdated) {
                        System.out.println(
                                "Room vacated successfully!");
                    } else {
                        // Restore the previous assignment if needed.
                        boolean restored =
                                studentDAO.allocateRoom(
                                        studentId, oldRoomId);

                        if (restored) {
                            System.out.println(
                                    "Vacating failed. Previous room assignment restored.");
                        } else {
                            System.out.println(
                                    "Warning: Please verify student and room records in MySQL.");
                        }
                    }

                    break;
                }

                case 9: {
                    System.out.println(
                            "Thank you for using Hostel Management System!");
                    sc.close();
                    return;
                }

                default:
                    System.out.println(
                            "Invalid choice! Please enter 1 to 9.");
            }
        }
    }


    private static int readInt(Scanner sc, String message) {

        while (true) {
            System.out.print(message);

            String input = sc.nextLine();

            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid input! Please enter a number.");
            }
        }
    }
}
