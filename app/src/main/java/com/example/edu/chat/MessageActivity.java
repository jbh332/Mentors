package com.example.edu.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.edu.R;
import com.example.edu.model.ChatModel;
import com.example.edu.model.NotificationModel;
import com.example.edu.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageActivity extends AppCompatActivity {

    private String destinationUid;
    private Button button;
    private EditText editText;

    private String uid;
    private String chatRoomUid;

    private RecyclerView recyclerView;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    private UserModel destinationUserModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_message);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // 채팅을 요구 하는 아이디 즉, 단말기에 로그인 된 uid
        destinationUid = getIntent().getStringExtra("destinationUid"); // 채팅을 당하는 아이디
        button = findViewById(R.id.btnMessage);
        editText = findViewById(R.id.etMessage);
        recyclerView = findViewById(R.id.message_recycleView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(uid,true);
                chatModel.users.put(destinationUid, true);

                if(chatRoomUid == null) {
                    button.setEnabled(false); //전송 버튼을 연속해서 누를 경우 체크도 하기 전에 방이 n만큼 만들어질 수 있다(버그)
                    //때문에 한번 전송을 누르면 체크가 끝날 때까지 버튼을 비활성화 상태로 변경한다.
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }
                    });
                    //checkChatRoom(); 이곳에 작성하게 되면 FirebaseDatabase... 요청 시 인터넷이 끊기는 경우가 간혹 있는데
                    //이 경우 데이터를 넣지도 않았는데 방을 체크하게 되는 경우가 발생한다. 때문에 데이터 입력이 완료 되었다고 했을 때
                    //체크하도록 코딩한다.
                } else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = uid;
                    comment.message = editText.getText().toString();
                    comment.timestamp = ServerValue.TIMESTAMP; //firebase가 제공하는 메소드
                    FirebaseDatabase.getInstance().getReference()
                            .child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sendFcm();
                            editText.setText(""); //db에 메세지를 정상적으로 전송하였으면 text 부분 공백 처리
                        }
                    });
                }
            }
        });
        checkChatRoom();
    }

    void sendFcm() {
        // Notification 사용할 경우 백그라운드로 push가 전송된다
        // 포그라운드 전송을 원할 경우 data를 만들어 사용하면 된다
        // 포그라운드 전송 Model 부분은 Notification.java 파일로 합쳐서 진행한다
        Gson gson = new Gson();

        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to = destinationUserModel.pushToken;
        notificationModel.notification.title = userName;
        notificationModel.notification.text = editText.getText().toString();
        notificationModel.data.title = userName;
        notificationModel.data.text = editText.getText().toString();
        // 메세지를 data로 push 할 경우 push를 받을 때 파싱하는 부분 코드도 있어야 한다(파이어베이스 메세징 서비스 필요)
        // 이는 manifests 수정과 MyFirebaseMessagingService.java 파일을 통해 할 수 있다

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),gson.toJson(notificationModel));
        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization","key=AIzaSyDdZOnmjs3Qz6NdWPCiIJJug1mvYrD0Ewc")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { // 실패 시 실행되는 코드이다
//                Toast.makeText(MessageActivity.this, "전송 실패", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException { // 성공 시 실행되는 코드이다
//                Toast.makeText(MessageActivity.this, "전송 성공", Toast.LENGTH_SHORT).show();
            }
        });


    }

    void checkChatRoom() {
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true) //orderByChild : 지정된 하위 키의 값에 따라 결과를 정렬
                .addListenerForSingleValueEvent(new ValueEventListener() { //equalTo : 지정된 키 또는 값과 동일한 항목을 반환
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    if(chatModel.users.containsKey(destinationUid)) { //hashmap에 값이 있으면 true, 없으면 false 반환
                        chatRoomUid = item.getKey(); // room 방 id (여기서 id는 최초 생성 시 랜덤으로 생성되는 값을 말함)
                        button.setEnabled(true); // 체크가 끝났으므로 버튼 활성화
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ChatModel.Comment> comments; //덧글을 달아주는
        public RecyclerViewAdapter() {
            comments = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    destinationUserModel = dataSnapshot.getValue(UserModel.class);
                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        void getMessageList() {
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments")
                    .addValueEventListener(new ValueEventListener() {
                //메세지 리스트를 받아오는 애
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear(); //데이터가 추가 될 때마다 모든 채팅에 대한 내용을 서버가 다 보내주기 때문에 중복 데이터가 계속 쌓인다.
                    // 때문에 clear로 쌓이지 않도록 기존 데이터 삭제.
                    for(DataSnapshot item : dataSnapshot.getChildren()) {
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }
                    notifyDataSetChanged(); //메세지 갱신
                    recyclerView.scrollToPosition(comments.size() - 1);
                    //새로운 메세지가 작성될 때 화면을 맨 아래로 최신화(?) (몇 번째 포지션으로 이동할 것인지에 대한 코드)
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);

            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder)holder);

            //내가 보낸 메세지
            if(comments.get(position).uid.equals(uid)) {
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);

            //상대방이 보낸 메세지
            } else {
                messageViewHolder.textView_name.setText(destinationUserModel.userName);
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setTextSize(25);
                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
            }
            //현재 - 1970년 1월 1일 = 시간을 뺀 밀리세컨즈의 값이다. 이것을 이용하여 현재 시간을 구한다.
            long unixTime = (long) comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            messageViewHolder.textView_timestamp.setText(time);
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView textView_message;
            public TextView textView_name;
            public ImageView imageView_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public TextView textView_timestamp;

            public MessageViewHolder(View view) {
                super(view);
                textView_message = view.findViewById(R.id.messageItem_tvMessage);
                textView_name = view.findViewById(R.id.messageItem_tvName);
                imageView_profile = view.findViewById(R.id.messageItem_ivProfile);
                linearLayout_destination = view.findViewById(R.id.messageItem_linear_Destination);
                linearLayout_main = view.findViewById(R.id.messageItem_linear_main); //메세지 왼쪽 or 오른쪽 정렬하기 위해 만듬
                textView_timestamp = view.findViewById(R.id.messageItem_tvTimeStamp);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fromleft, R.anim.toright);
        //finish 밑에 overridePendingTransition 넣어줘야 애니메이션이 작동된다
    }
}
