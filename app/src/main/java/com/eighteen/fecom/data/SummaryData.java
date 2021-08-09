package com.eighteen.fecom.data;

public class SummaryData {
    private String topic;       //게시판 이름 or 데일리톡 작성자 이름
    private String content;

    public SummaryData(String topic, String content) {
        this.topic = topic;
        this.content = content;
    }

    public String getTopic() { return topic; }
    public String getContent() { return content; }
}
