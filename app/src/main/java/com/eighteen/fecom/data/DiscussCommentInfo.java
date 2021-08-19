package com.eighteen.fecom.data;

public class DiscussCommentInfo {
    private int commentID;
    private UserInfo commenterInfo;
    private String commentTime;
    private String content;
    private int amILike;    //-1: 싫어요, 0은 없음, 1은 좋아요
    private int likeNum;
    private int notLikeNum;

    public DiscussCommentInfo(int commentID, int commenterID, String commenterNick, String commentTime, String content, int amILike, int likeNum, int notLikeNum) {
        this.commentID = commentID;
        commenterInfo = new UserInfo(commenterID, commenterNick);
        this.commentTime = commentTime;
        this.content = content;
        this.amILike = amILike;
        this.likeNum = likeNum;
        this.notLikeNum = notLikeNum;
    }

    public int getCommentID() { return commentID; }
    public UserInfo getCommenterInfo() { return commenterInfo; }
    public String getCommentTime() { return commentTime; }
    public String getContent() { return content; }
    public int getAmILike() { return amILike; }
    public int getLikeNum() { return likeNum; }
    public int getNotLikeNum() { return notLikeNum; }

    public void setContent(String content) { this.content = content; }
    public void setAmILike(int amILike) { this.amILike = amILike; }
    public void setLikeNum(int likeNum) { this.likeNum = likeNum; }
    public void setNotLikeNum(int notLikeNum) { this.notLikeNum = notLikeNum; }
}
