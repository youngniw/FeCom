package com.eighteen.fecom.data;

public class NoticeInfo {
    private int noticeID;
    private String noticeAbout;
    private String noticeContent;

    public NoticeInfo(String noticeAbout, String noticeContent) {
        this.noticeAbout = noticeAbout;
        this.noticeContent = noticeContent;
    }

    public int getNoticeID() { return noticeID; }
    public String getNoticeAbout() { return noticeAbout; }
    public String getNoticeContent() { return noticeContent; }
}
