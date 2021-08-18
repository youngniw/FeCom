package com.eighteen.fecom.data;

import android.os.Parcel;
import android.os.Parcelable;

public class CollegePostInfo implements Parcelable {
    private int postID;
    private UserInfo writerInfo;
    private String postTime;
    private String content;
    private int amILike;    //0이면 false, 1이면 true
    private int likeNum;
    private int commentNum;

    public CollegePostInfo(int postID, int writerID, int univCode, String univName, String postTime, String content, int amILike, int likeNum, int commentNum) {
        this.postID = postID;
        writerInfo = new UserInfo(writerID, univCode, univName);
        this.postTime = postTime;
        this.content = content;
        this.amILike = amILike;
        this.likeNum = likeNum;
        this.commentNum = commentNum;
    }

    protected CollegePostInfo(Parcel in) {
        postID = in.readInt();
        writerInfo = in.readParcelable(UserInfo.class.getClassLoader());
        postTime = in.readString();
        content = in.readString();
        amILike = in.readInt();
        likeNum = in.readInt();
        commentNum = in.readInt();
    }

    public static final Creator<CollegePostInfo> CREATOR = new Creator<CollegePostInfo>() {
        @Override
        public CollegePostInfo createFromParcel(Parcel in) {
            return new CollegePostInfo(in);
        }

        @Override
        public CollegePostInfo[] newArray(int size) {
            return new CollegePostInfo[size];
        }
    };

    public int getPostID() { return postID; }
    public UserInfo getWriterInfo() { return writerInfo; }
    public String getPostTime() { return postTime; }
    public String getContent() { return content; }
    public int getAmILike() { return amILike; }
    public int getLikeNum() { return likeNum; }
    public int getCommentNum() { return commentNum; }

    public void setAmILike(int amILike) { this.amILike = amILike; }
    public void setLikeNum(int likeNum) { this.likeNum = likeNum; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(postID);
        dest.writeParcelable(writerInfo, flags);
        dest.writeString(postTime);
        dest.writeString(content);
        dest.writeInt(amILike);
        dest.writeInt(likeNum);
        dest.writeInt(commentNum);
    }
}
