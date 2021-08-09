package com.eighteen.fecom.data;

public class PostInfo {
    private int postID;
    private String writerName;
    private String postTime;
    private String content;
    private boolean isILike;    //0이면 false, 1이면 true
    private int likeNum;
    private int commentNum;

    public PostInfo(int postID, String writerName, String postTime, String content, boolean isILike, int likeNum, int commentNum) {
        this.postID = postID;
        this.writerName = writerName;
        this.postTime = postTime;
        this.content = content;
        this.isILike = isILike;
        this.likeNum = likeNum;
        this.commentNum = commentNum;
    }

    public int getPostID() { return postID; }
    public String getWriterName() { return writerName; }
    public String getPostTime() { return postTime; }
    public String getContent() { return content; }
    public boolean getIsILike() { return isILike; }
    public int getLikeNum() { return likeNum; }
    public int getCommentNum() { return commentNum; }

    public void changeLike() {
        isILike = !isILike;
    }
}
