package com.eighteen.fecom.data;

public class CommentInfo {
    private int commentID;
    private int anonymous;
    private UserInfo commenterInfo;
    private String commentTime;
    private String content;
    private int amILike;
    private int likeNum;

    public CommentInfo(int commentID, int anonymous, int commenterID, String commenterNick, String commentTime, String content, int amILike, int likeNum) {
        this.commentID = commentID;
        this.anonymous = anonymous;
        commenterInfo = new UserInfo(commenterID, commenterNick);
        this.commentTime = commentTime;
        if (content == null)
            this.content = "";
        else
            this.content = content;
        this.amILike = amILike;
        this.likeNum = likeNum;
    }

    public int getCommentID() { return commentID; }
    public int getAnonymous() { return anonymous; }
    public UserInfo getCommenterInfo() { return commenterInfo; }
    public String getCommentTime() { return commentTime; }
    public String getContent() { return content; }
    public int getAmILike() { return amILike; }
    public int getLikeNum() { return likeNum; }
}
