package com.eighteen.fecom.data;

import android.os.Parcel;
import android.os.Parcelable;

public class DiscussionInfo implements Parcelable {
    private int discussID;
    private UserInfo writerInfo;
    private String discussTime;
    private String prosContent;
    private String consContent;
    private int myExpression;       //-1이면 반대, 0이면 없음, 1이면 찬성
    private int totalCount;
    private int prosCount;
    private int consCount;
    private double prosRate;
    private double consRate;

    public DiscussionInfo(int discussID, int writerID, String writerNick, String discussTime, String prosContent, String consContent,
                          int myExpression, int totalCount, int prosCount, int consCount, double prosRate, double consRate) {
        this.discussID = discussID;
        writerInfo = new UserInfo(writerID, writerNick);
        this.discussTime = discussTime;
        this.prosContent = prosContent;
        this.consContent = consContent;
        this.myExpression = myExpression;
        this.totalCount = totalCount;
        this.prosCount = prosCount;
        this.consCount = consCount;
        this.prosRate = prosRate;
        this.consRate = consRate;
    }


    protected DiscussionInfo(Parcel in) {
        discussID = in.readInt();
        writerInfo = in.readParcelable(UserInfo.class.getClassLoader());
        discussTime = in.readString();
        prosContent = in.readString();
        consContent = in.readString();
        myExpression = in.readInt();
        totalCount = in.readInt();
        prosCount = in.readInt();
        consCount = in.readInt();
        prosRate = in.readDouble();
        consRate = in.readDouble();
    }

    public static final Creator<DiscussionInfo> CREATOR = new Creator<DiscussionInfo>() {
        @Override
        public DiscussionInfo createFromParcel(Parcel in) {
            return new DiscussionInfo(in);
        }

        @Override
        public DiscussionInfo[] newArray(int size) {
            return new DiscussionInfo[size];
        }
    };

    public int getDiscussID() { return discussID; }
    public UserInfo getWriterInfo() { return writerInfo; }
    public String getDiscussTime() { return discussTime; }
    public String getProsContent() { return prosContent; }
    public String getConsContent() { return consContent; }
    public int getMyExpression() { return myExpression; }
    public int getTotalCount() { return totalCount; }
    public int getProsCount() { return prosCount; }
    public int getConsCount() { return consCount; }
    public double getProsRate() { return prosRate; }
    public double getConsRate() { return consRate; }

    public void setMyExpression(int myExpression) { this.myExpression = myExpression; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
    public void setProsCount(int prosCount) { this.prosCount = prosCount; }
    public void setConsCount(int consCount) { this.consCount = consCount; }
    public void setProsRate(double prosRate) { this.prosRate = prosRate; }
    public void setConsRate(double consRate) { this.consRate = consRate; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(discussID);
        dest.writeParcelable(writerInfo, flags);
        dest.writeString(discussTime);
        dest.writeString(prosContent);
        dest.writeString(consContent);
        dest.writeInt(myExpression);
        dest.writeInt(totalCount);
        dest.writeInt(prosCount);
        dest.writeInt(consCount);
        dest.writeDouble(prosRate);
        dest.writeDouble(consRate);
    }
}
