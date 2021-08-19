package com.eighteen.fecom.data;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {
    private int userID;
    private String name = "";
    private String nickname = "";
    private String email = "";
    private int univCode = -1;      //-1이면 인증 x
    private String univName = "";   //""이면 학교 인증 x

    public UserInfo(int userID, String nick) {      //게시판의 게시글목록, 데일리톡, 댓글, 대결에서 사용됨
        this.userID = userID;
        this.nickname = nick;
    }

    public UserInfo(int userID, int univCode, String univName) {  //전공커뮤니티 게시글 목록에서 사용
        this.userID = userID;
        this.univCode = univCode;
        this.univName = univName;
    }

    public UserInfo(int userID, String name, String nickname, String email, int univCode, String univName) {
        this.userID = userID;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.univCode = univCode;
        this.univName = univName;
    }

    protected UserInfo(Parcel in) {
        userID = in.readInt();
        name = in.readString();
        nickname = in.readString();
        email = in.readString();
        univCode = in.readInt();
        univName = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public int getUserID() { return userID; }
    public String getName() { return name; }
    public String getNick() { return nickname; }
    public String getEmail() { return email; }
    public int getUnivCode() { return univCode; }
    public String getUnivName() { return univName; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userID);
        dest.writeString(name);
        dest.writeString(nickname);
        dest.writeString(email);
        dest.writeInt(univCode);
        dest.writeString(univName);
    }
}
