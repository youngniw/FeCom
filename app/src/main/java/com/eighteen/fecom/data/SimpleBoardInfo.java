package com.eighteen.fecom.data;

public class SimpleBoardInfo {
    private int boardID;        //게시판 id
    private String name;        //게시판 이름
    private String content;

    public SimpleBoardInfo(int boardID, String name, String content) {
        this.boardID = boardID;
        this.name = name;

        if (content == null)
            this.content = "";
        else
            this.content = content;
    }

    public int getBoardID() { return boardID; }
    public String getName() { return name; }
    public String getContent() { return content; }
}
