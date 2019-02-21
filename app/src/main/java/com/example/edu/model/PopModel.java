package com.example.edu.model;

import java.io.Serializable;

public class PopModel implements Serializable {


    public String uid;
    public String groupName;
    public String groupShortTitle;
    public int groupLimit;
    public int groupCurrentMembers;
    public String groupMemberName;
    public String groupStyle;
    public String groupTopic;
    public String groupExplain;


    public PopModel() {

    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupShortTitle() {
        return groupShortTitle;
    }

    public void setGroupShortTitle(String groupShortTitle) {
        this.groupShortTitle = groupShortTitle;
    }

    public int getGroupLimit() {
        return groupLimit;
    }

    public void setGroupLimit(int groupLimit) {
        this.groupLimit = groupLimit;
    }

    public int getGroupCurrentMembers() {
        return groupCurrentMembers;
    }

    public void setGroupCurrentMemebers(int groupCurrentMemebers) {
        this.groupCurrentMembers = groupCurrentMemebers;
    }

    public String getGroupStyle() {
        return groupStyle;
    }

    public void setGroupStyle(String groupStyle) {
        this.groupStyle = groupStyle;
    }

    public String getGroupTopic() {
        return groupTopic;
    }

    public void setGroupTopic(String groupTopic) {
        this.groupTopic = groupTopic;
    }

    public String getGroupExplain() {
        return groupExplain;
    }

    public void setGroupExplain(String groupExplain) {
        this.groupExplain = groupExplain;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setGroupCurrentMembers(int groupCurrentMembers) {
        this.groupCurrentMembers = groupCurrentMembers;
    }

    public String getGroupMemberName() {
        return groupMemberName;
    }

    public void setGroupMemberName(String groupMemberName) {
        this.groupMemberName = groupMemberName;
    }
}
