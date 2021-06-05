package com.example.asyncgame;

public class User {

    private String userName;
    private String userEmail;
    private String userID;
    private WordList wordList;

    public User(){

    }
    public User(String userName, String userEmail, String userID){
        setUserName(userName);
        setUserEmail(userEmail);
        setUserID(userID);
        wordList = new WordList();
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }

    public String getUserID(){
        return userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

    public WordList getWordList(){
        return wordList;
    }

}

