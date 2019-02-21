package com.example.edu.RecyclerAdpater;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edu.R;
import com.example.edu.chat.MessageActivity;
import com.example.edu.model.ChatModel;
import com.example.edu.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static List<ChatModel> chatModels = new ArrayList<>(); //채팅에 대한 정보를 가지는
    private String uid;
    private ArrayList<String> destinationUsers = new ArrayList<>(); //대화할 사람들의 데이터가 담김
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    public ChatRecyclerAdapter() {

        //데이터 받아오는 준비 코드들
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatModels.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    chatModels.add(item.getValue(ChatModel.class));
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //준비과 완료 됐으니 보여주면 된다. 보여주기 코드.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);

        return new CustomViewHolder(view); //view만 하면 오류난다, 뷰홀더 사용 -> 메모리 절약 -> 재사용 목적
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final CustomViewHolder customViewHolder = (CustomViewHolder)holder;
        String destinationUid = null;

        //채팅방에 있는 유저를 체크(전부 체크)
        for(String user : chatModels.get(position).users.keySet()) {
            if(!user.equals(uid)) {
                destinationUid = user;
                destinationUsers.add(destinationUid);
            }
        }
        //채팅방 이름
        FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                customViewHolder.textView_title.setText(userModel.userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //메세지를 내림 차순로 정렬 후 마지막 메세지의 키값 가져오기
        //TODO: error 채팅방이 만들어지고 대화가 없는 방이 있는 상태에서 chat 네비게이션을 누를 경우 error 발생 / 수정할 예정
        //TODO: error 자기 자신의 게시글에서 채팅방으로 들어갈 경우 다른 사람과의 채팅방에 들어가지는 오류 발생 / 수정할 예정
        Map<String,ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder()); //reverseOrder 내림차순
        commentMap.putAll(chatModels.get(position).comments); //채팅에 대한 내용 넣기
        String lastMessageKey = (String) commentMap.keySet().toArray()[0]; //채팅에 대한 첫 번째 값만 가져오면 된다
        customViewHolder.textView_last_message.setText(chatModels.get(position).comments.get(lastMessageKey).message);

        customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MessageActivity.class);
                intent.putExtra("destinationUid",destinationUsers.get(position));

                ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright,R.anim.toleft);
                view.getContext().startActivity(intent, activityOptions.toBundle());
            }
        });

        // TimeStamp
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        long unixTime = (long) chatModels.get(position).comments.get(lastMessageKey).timestamp;
        Date date = new Date(unixTime);
        customViewHolder.textView_timestamp.setText(simpleDateFormat.format(date));

    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }


    private class CustomViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView_title;
        public TextView textView_last_message;
        public TextView textView_timestamp;

        public CustomViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.chatItem_ivChat);
            textView_title = view.findViewById(R.id.chatItem_tvChatTitle);
            textView_last_message = view.findViewById(R.id.chatItem_tvChatLastMessage);
            textView_timestamp = view.findViewById(R.id.chatItem_tvChatTimeStamp);
        }
    }
}
