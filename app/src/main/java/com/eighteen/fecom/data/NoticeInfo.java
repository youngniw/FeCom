package com.eighteen.fecom.data;

public class NoticeInfo {
    private String noticeAbout;
    private String noticeContent;

    public NoticeInfo(String noticeAbout, String noticeContent) {
        this.noticeAbout = noticeAbout;
        this.noticeContent = noticeContent;
    }

    public String getNoticeAbout() { return noticeAbout; }
    public String getNoticeContent() { return noticeContent; }
}
