package com.eighteen.fecom.data;

public class BoardInfo {
    private int boardID;
    private String boardName;
    private int essential;      //0은 필수x, 1은 필수
    private int amISubscribe;

    public BoardInfo(int boardID, String boardName, int essential, int isISubscribe) {
        this.boardID = boardID;
        this.boardName = boardName;
        this.essential = essential;
        this.amISubscribe = isISubscribe;
    }

    public int getBoardID() { return boardID; }
    public String getBoardName() { return boardName; }
    public int getEssential() { return essential; }
    public int getAmISubscribe() { return amISubscribe; }

    public void setAmISubscribe(int amISubscribe) { this.amISubscribe = amISubscribe; }
}
