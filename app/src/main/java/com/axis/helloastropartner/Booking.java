package com.axis.helloastropartner;

public class Booking {
    private String id;
    private String userId;
    private String astrologerId;
    private String bookingDate;
    private double price;
    private String status;
    private String mode; // "chat", "video", "call"

    public Booking() {}  // Firestore needs this

    public Booking(String id, String userId, String astrologerId, String bookingDate, double price, String status, String mode) {
        this.id = id;
        this.userId = userId;
        this.astrologerId = astrologerId;
        this.bookingDate = bookingDate;
        this.price = price;
        this.status = status;
        this.mode = mode;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getAstrologerId() { return astrologerId; }
    public void setAstrologerId(String astrologerId) { this.astrologerId = astrologerId; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
}

