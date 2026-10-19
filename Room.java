
package org.example;

public class Room {

    private int roomId;
    private String roomType;
    private int capacity;
    private int occupied;


    public Room(int roomId, String roomType, int capacity, int occupied) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.capacity = capacity;
        this.occupied = occupied;
    }


    public Room(String roomType, int capacity) {
        this(0, roomType, capacity, 0);
    }


    public int getRoomId() {
        return roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getOccupied() {
        return occupied;
    }


    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }


    public int getAvailableBeds() {
        return capacity - occupied;
    }


    public boolean hasAvailableBed() {
        return occupied < capacity;
    }


    @Override
    public String toString() {
        return "Room ID: " + roomId
                + " | Type: " + roomType
                + " | Capacity: " + capacity
                + " | Occupied: " + occupied
                + " | Available: " + getAvailableBeds();
    }
}
