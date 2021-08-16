package com.eighteen.fecom.data;

public class UserInfo {
    private int userID;
    private String name;
    private String nickname;
    private String email;
    private int univCode;
    private String univName;

    public UserInfo(int userID, String name, String nickname, String email, int univCode, String univName) {
        this.userID = userID;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.univCode = univCode;
        this.univName = univName;
    }

    public int getUserID() { return userID; }
    public String getName() { return name; }
    public String getNickname() { return nickname; }
    public String getEmail() { return email; }
    public int getUnivCode() { return univCode; }
    public String getUnivName() { return univName; }
}
