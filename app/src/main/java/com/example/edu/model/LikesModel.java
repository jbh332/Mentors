package com.example.edu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LikesModel {
    public String uid;
    public String groupName;
    public String groupShortTitle;
    public int groupLimit;
    public String groupStyle;
    public String groupTopic;
    public String groupExplain;
    public int favCount = 0;
    public Map<String, Boolean> favorites = new HashMap<>();
}
