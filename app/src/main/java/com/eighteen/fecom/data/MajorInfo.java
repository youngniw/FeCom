package com.eighteen.fecom.data;

import java.util.ArrayList;

public class MajorInfo {
    String collegeName;
    ArrayList<String> departmentLists;

    public MajorInfo(String collegeName, ArrayList<String> departmentLists) {
        this.collegeName = collegeName;

        if (departmentLists != null)        //TODO: 확인해봐야 함!!
            this.departmentLists = departmentLists;
        else
            this.departmentLists = new ArrayList<>();
    }

    public String getCollegeName() { return collegeName; }
    public ArrayList<String> getDepartmentLists() { return departmentLists; }
}
