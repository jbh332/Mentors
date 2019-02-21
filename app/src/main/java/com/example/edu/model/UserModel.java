package com.example.edu.model;

import java.util.HashMap;
import java.util.Map;

public class UserModel {
    public String uid; //나중에 채팅하고 싶은 사람의 uid를 받기 위해 만듬
    public String userName;
    public String userGender;
    //public String userFavorites;
    public String userPwQuestion;
    public String userPwAnswer;
    public String pushToken;
    public Map<String, Boolean> userFavorites = new HashMap<>();
    public int favCount = 0;

}
