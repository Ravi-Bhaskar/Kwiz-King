package com.raam.kwizking;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class WithdrawRequest {
    private String userId;
    private String phoneNo;
    private String requestedBy;

    public WithdrawRequest() {

    }

    public WithdrawRequest(String userId, String emailAddress, String requestedBy) {
        this.userId = userId;
        this.phoneNo = emailAddress;
        this.requestedBy = requestedBy;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String emailAddress) {
        this.phoneNo = emailAddress;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    @ServerTimestamp
    private Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
