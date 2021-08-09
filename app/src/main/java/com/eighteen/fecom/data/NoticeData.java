package com.eighteen.fecom.data;

public class NoticeData {
    private String noticeAbout;
    private String noticeContent;

    public NoticeData(String noticeAbout, String noticeContent) {
        this.noticeAbout = noticeAbout;
        this.noticeContent = noticeContent;
    }

    public String getNoticeAbout() { return noticeAbout; }
    public String getNoticeContent() { return noticeContent; }
}
