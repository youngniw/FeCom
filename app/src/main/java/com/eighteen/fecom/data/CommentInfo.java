package com.eighteen.fecom.data;

public class CommentInfo {
    private int commentID;
    private UserInfo commenterInfo;
    private String commentTime;
    private String content;
    private int amILike;
    private int likeNum;

    public CommentInfo(int commentID, int commenterID, String commenterNick, String commentTime, String content, int amILike, int likeNum) {
        this.commentID = commentID;
        commenterInfo = new UserInfo(commenterID, commenterNick);
        this.commentTime = commentTime;
        this.content = content;
        this.amILike = amILike;
        this.likeNum = likeNum;
    }

    public int getCommentID() { return commentID; }
    public UserInfo getCommenterInfo() { return commenterInfo; }
    public String getCommentTime() { return commentTime; }
    public String getContent() { return content; }
    public int getAmILike() { return amILike; }
    public int getLikeNum() { return likeNum; }

    public void setContent(String content) { this.content = content; }
    public void setAmILike(int amILike) { this.amILike = amILike; }
    public void setLikeNum(int likeNum) { this.likeNum = likeNum; }
}
