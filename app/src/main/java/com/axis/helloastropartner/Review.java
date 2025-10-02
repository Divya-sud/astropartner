package com.axis.helloastropartner;

public class Review {
    private String userId;
    private String comment;
    private double rating;

    public Review() {}

    public Review(String userId, String comment, double rating) {
        this.userId = userId;
        this.comment = comment;
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
