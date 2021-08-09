package com.eighteen.fecom.data;

public class BoardInfo {
    private String boardName;
    private boolean isISubscribe;

    public BoardInfo(String boardName, boolean isISubscribe) {
        this.boardName = boardName;
        this.isISubscribe = isISubscribe;
    }

    public String getBoardName() { return boardName; }
    public boolean getIsISubscribe() { return isISubscribe; }
}
