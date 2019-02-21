package com.example.edu.model;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {

    public Map<String,Boolean> users = new HashMap<>(); //채팅방의 유저들이라고 생각하면 됨(destination과 uid 모두 가지고 있음)
    public Map<String,Comment> comments = new HashMap<>(); //채팅방의 내용

    public static class Comment {
        public String uid;
        public String message;
        public Object timestamp;
    }
}
