package com.example.edu;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edu.chat.MessageActivity;
import com.example.edu.model.PopModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PopUp extends AppCompatActivity {
    Button btnChat;
    TextView tvTitle, tvShortTitle, tvStyle, tvTopic, tvLimit, tvCurrentMembers, tvExplain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);
        btnChat = findViewById(R.id.btnPopup);
        tvTitle = findViewById(R.id.tvTitle);
        tvShortTitle = findViewById(R.id.tvShortTitle);
        tvStyle = findViewById(R.id.tvStyle);
        tvTopic = findViewById(R.id.tvTopic);
        tvCurrentMembers = findViewById(R.id.tvCurrentMembers);
        tvLimit = findViewById(R.id.tvLimit);
        tvExplain = findViewById(R.id.tvExplain);

        Intent intent = getIntent();
        final PopModel popModel = (PopModel) intent.getSerializableExtra("popModel");

        tvTitle.setText(popModel.getGroupName());
        tvShortTitle.setText(popModel.getGroupShortTitle());
        tvLimit.setText(Integer.toString(popModel.getGroupLimit()));
        tvStyle.setText(popModel.getGroupStyle());
        tvTopic.setText(popModel.getGroupTopic());
        tvCurrentMembers.setText(Integer.toString(popModel.getGroupCurrentMembers()));
        tvExplain.setText(popModel.getGroupExplain());


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); // 창 크기 조절

        int width = dm.widthPixels;
        int height = dm.heightPixels; //높이 너비 가져옴

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.55)); // 크기 설정 너비 90%, 높이 85%

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MessageActivity.class);
                intent.putExtra("destinationUid", popModel.uid); // 수정


                ActivityOptions activityOptions = null;
                activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright, R.anim.toleft);
                view.getContext().startActivity(intent, activityOptions.toBundle());

            }
        });
    }

}
