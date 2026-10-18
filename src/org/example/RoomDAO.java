
package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomDAO {


    public void addRoom(String roomType, int capacity) {

        if (capacity <= 0) {
            System.out.println("Capacity must be greater than 0.");
            return;
        }

        String sql = "INSERT INTO rooms (room_type, capacity, occupied) "
                + "VALUES (?, ?, 0)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, roomType);
            ps.setInt(2, capacity);

            if (ps.executeUpdate() > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        System.out.println(
                                "Room added successfully! Room ID: "
                                        + keys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error adding room: " + e.getMessage());
        }
    }


    public void viewRooms() {

        String sql = "SELECT * FROM rooms";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                found = true;

                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_type"),
                        rs.getInt("capacity"),
                        rs.getInt("occupied")
                );

                System.out.println(room);
            }

            if (!found) {
                System.out.println("No rooms found.");
            }

        } catch (SQLException e) {
            System.out.println("Error viewing rooms: " + e.getMessage());
        }
    }


    public boolean hasAvailableRoom(int roomId) {

        String sql = "SELECT capacity, occupied FROM rooms "
                + "WHERE room_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("occupied")
                            < rs.getInt("capacity");
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error checking room: " + e.getMessage());
        }

        return false;
    }


    public boolean allocateRoom(int roomId) {

        String sql = "UPDATE rooms SET occupied = occupied + 1 "
                + "WHERE room_id = ? AND occupied < capacity";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);

            if (ps.executeUpdate() > 0) {
                return true;
            }

            System.out.println("Room not found or room is full.");

        } catch (SQLException e) {
            System.out.println(
                    "Error allocating room: " + e.getMessage());
        }

        return false;
    }


    public void vacateRoom(int roomId) {
        releaseRoom(roomId);
    }

    public boolean releaseRoom(int roomId) {

        String sql = "UPDATE rooms SET occupied = occupied - 1 "
                + "WHERE room_id = ? AND occupied > 0";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);

            if (ps.executeUpdate() > 0) {
                return true;
            }

            System.out.println(
                    "Room not found or no students are occupying it.");

        } catch (SQLException e) {
            System.out.println(
                    "Error releasing room: " + e.getMessage());
        }

        return false;
    }
}
