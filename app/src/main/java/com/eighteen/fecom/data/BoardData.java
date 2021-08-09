package com.eighteen.fecom.data;

public class BoardData {
    private String boardName;
    private boolean isISubscribe;

    public BoardData(String boardName, boolean isISubscribe) {
        this.boardName = boardName;
        this.isISubscribe = isISubscribe;
    }

    public String getBoardName() { return boardName; }
    public boolean getIsISubscribe() { return isISubscribe; }
}
