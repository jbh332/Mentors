package com.example.edu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoardModel {
    public String uid;
    public String groupName;
    public String groupShortTitle;
    public int groupLimit;
    public String groupStyle;
    public String groupTopic;
    public String groupExplain;
    public int joinCount = 0;
    public Map<String, Boolean> join = new HashMap<>();
    public Map<String, Boolean> userFavorites = new HashMap<>();
    public int favCount = 0;
}
