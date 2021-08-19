package com.eighteen.fecom.data;

public class NoticeInfo {
    private int postID = -1;
    private String postContent;
    private int commentID = -1;
    private String commentContent;
    private String noticeTime;


    public NoticeInfo(int postID, String postContent, int commentID, String commentContent, String noticeTime) {
        this.postID = postID;
        this.postContent = postContent;
        this.commentID = commentID;
        this.commentContent = commentContent;
        this.noticeTime = noticeTime;
    }

    public int getPostID() { return postID; }
    public String getPostContent() { return postContent; }
    public int getCommentID() { return commentID; }
    public String getCommentContent() { return commentContent; }
    public String getNoticeTime() { return noticeTime; }
}
