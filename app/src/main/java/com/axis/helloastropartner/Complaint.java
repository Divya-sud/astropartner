package com.axis.helloastropartner;

public class Complaint {
    private String id;
    private String userId;
    private String astrologerId;
    private String complaintText;
    private String timestamp;

    public Complaint() {}

    public Complaint(String id, String userId, String astrologerId, String complaintText, String timestamp) {
        this.id = id;
        this.userId = userId;
        this.astrologerId = astrologerId;
        this.complaintText = complaintText;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAstrologerId() {
        return astrologerId;
    }

    public void setAstrologerId(String astrologerId) {
        this.astrologerId = astrologerId;
    }

    public String getComplaintText() {
        return complaintText;
    }

    public void setComplaintText(String complaintText) {
        this.complaintText = complaintText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
