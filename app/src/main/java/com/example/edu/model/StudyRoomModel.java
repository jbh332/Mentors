package com.example.edu.model;

import java.util.HashMap;
import java.util.Map;

public class StudyRoomModel {

    public Map<String,StudyRoomModel.Day> day = new HashMap<>();

    public static class Day {
        public String uid;
        public int time;
        public boolean reservation;
    }
}
