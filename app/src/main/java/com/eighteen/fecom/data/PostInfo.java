package com.eighteen.fecom.data;

import android.os.Parcel;
import android.os.Parcelable;

public class PostInfo implements Parcelable {
    private int postID;
    private int anonymous;
    private UserInfo writerInfo;
    private String postTime;
    private String content;
    private int amILike;    //0이면 false, 1이면 true
    private int likeNum;
    private int commentNum;

    public PostInfo(int postID, int anonymous, int writerID, String writerNick, String postTime, String content, int amILike, int likeNum, int commentNum) {
        this.postID = postID;
        this.anonymous = anonymous;
        writerInfo = new UserInfo(writerID, writerNick);
        this.postTime = postTime;
        this.content = content;
        this.amILike = amILike;
        this.likeNum = likeNum;
        this.commentNum = commentNum;
    }

    protected PostInfo(Parcel in) {
        postID = in.readInt();
        anonymous = in.readInt();
        writerInfo = in.readParcelable(UserInfo.class.getClassLoader());
        postTime = in.readString();
        content = in.readString();
        amILike = in.readInt();
        likeNum = in.readInt();
        commentNum = in.readInt();
    }

    public static final Creator<PostInfo> CREATOR = new Creator<PostInfo>() {
        @Override
        public PostInfo createFromParcel(Parcel in) {
            return new PostInfo(in);
        }

        @Override
        public PostInfo[] newArray(int size) {
            return new PostInfo[size];
        }
    };

    public int getPostID() { return postID; }
    public int getAnonymous() { return anonymous; }
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
        dest.writeInt(anonymous);
        dest.writeParcelable(writerInfo, flags);
        dest.writeString(postTime);
        dest.writeString(content);
        dest.writeInt(amILike);
        dest.writeInt(likeNum);
        dest.writeInt(commentNum);
    }
}
