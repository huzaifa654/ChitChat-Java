package com.example.chitchat.Models;

public class Users {
    String profilepic, userName, mail, passward, userId, lastMessage, status;

    public Users(String profilepic, String userName, String mail, String passward, String userId, String lastMessage) {
        this.profilepic = profilepic;
        this.userName = userName;
        this.mail = mail;
        this.passward = passward;
        this.userId = userId;
        this.lastMessage = lastMessage;
    }


    public Users() {
    }

    //singUP Constructor
    public Users(String userName, String mail, String passward) {

        this.userName = userName;
        this.mail = mail;
        this.passward = passward;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassward() {
        return passward;
    }

    public void setPassward(String passward) {
        this.passward = passward;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

}
