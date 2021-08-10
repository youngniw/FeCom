package com.eighteen.fecom.data;

public class CommentInfo {
    private int commentID;
    private String writerName;
    private String commentTime;
    private String content;
    private boolean isILike;    //0이면 false, 1이면 true
    private int likeNum;

    public CommentInfo(int commentID, String writerName, String commentTime, String content, boolean isILike, int likeNum) {
        this.commentID = commentID;
        this.writerName = writerName;
        this.commentTime = commentTime;
        this.content = content;
        this.isILike = isILike;
        this.likeNum = likeNum;
    }

    public int getCommentID() { return commentID; }
    public String getWriterName() { return writerName; }
    public String getCommentTime() { return commentTime; }
    public String getContent() { return content; }
    public boolean getIsILike() { return isILike; }
    public int getLikeNum() { return likeNum; }

    public void changeLike() {
        isILike = !isILike;
    }
}
