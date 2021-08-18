package com.eighteen.fecom.data;

public class CollegeInfo {
    private int collegeID;
    private String collegeName;

    public CollegeInfo(int collegeID, String collegeName) {
        this.collegeID = collegeID;
        this.collegeName = collegeName;
    }

    public int getCollegeID() { return collegeID; }
    public String getCollegeName() { return collegeName; }
}
