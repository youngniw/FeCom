package com.eighteen.fecom.data;

import java.util.ArrayList;

public class MajorInfo {
    private int collegeID;
    private String collegeName;
    private ArrayList<String> departmentLists;      //TODO: 추가로 departmentID가 포함되어야 함!

    public MajorInfo(String collegeName, ArrayList<String> departmentLists) {
        this.collegeName = collegeName;

        if (departmentLists != null)
            this.departmentLists = departmentLists;
        else
            this.departmentLists = new ArrayList<>();
    }

    public int getCollegeID() { return collegeID; }
    public String getCollegeName() { return collegeName; }
    public ArrayList<String> getDepartmentLists() { return departmentLists; }
}
