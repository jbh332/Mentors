package com.example.edu;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.edu.RecyclerAdpater.RecyclerAdapter_Likes;
import com.example.edu.databinding.ActivityOpenMeetingBinding;
import com.example.edu.model.BoardModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class OpenMeetingActivity extends AppCompatActivity {

    ActivityOpenMeetingBinding b;
    ArrayAdapter spinnerAdapter;
    RecyclerAdapter_Likes adapter;

    Spinner spinner;
    RadioGroup rgStyle;
    Button btnRegister;
    EditText etGroupTitle, etShortTitle, etLimit, etDetail;
    ImageView ivCheckTitle, ivCheckSimple, ivCheckLimit, ivCheckDetail;
    RecyclerView recycle;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    private View h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        b = DataBindingUtil.setContentView(this, R.layout.activity_open_meeting);

        btnRegister = findViewById(R.id.btnRegister);
        etGroupTitle = findViewById(R.id.etGroupTitle);
        etShortTitle = findViewById(R.id.etShortTitle);
        etLimit = findViewById(R.id.etLimit);
        etDetail = findViewById(R.id.etDetail);
        ivCheckLimit = findViewById(R.id.ivCheckLimit);
        ivCheckSimple = findViewById(R.id.ivCheckSimple);
        ivCheckDetail = findViewById(R.id.ivCheckDetail);
        ivCheckTitle = findViewById(R.id.ivCheckTitle);
        h = getLayoutInflater().inflate(R.layout.fragment1_main, null, false);
        recycle = h.findViewById(R.id.recycleView);
        spinner = findViewById(R.id.spnTopic);
        rgStyle = findViewById(R.id.rgStyle);
        Toolbar toolbar = findViewById(R.id.tBar);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.topic, android.R.layout.simple_dropdown_item_1line);
        adapter = new RecyclerAdapter_Likes(this);

        recycle.setAdapter(adapter);
        spinner.setAdapter(spinnerAdapter);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate() == false) { // 데이터 로컬에서 자체 검증
                    return;
                } else { // 로컬 자체 검증이 끝나면 서버 검증을 통해 로그인이 정상적으로 되었는지 체크
                    RegisterEvent();
                }
            }
        });

        etGroupTitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                checkInputTitle();
                return false;
            }
        });

        etGroupTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkInputTitle();
            }
        });

        etLimit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                checkInputLimit();
                return false;
            }
        });

        etLimit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkInputLimit();
            }
        });

        etShortTitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                checkInputShortTitle();
                return false;
            }
        });

        etShortTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkInputShortTitle();
            }
        });

        etDetail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                checkInputDetail();
                return false;
            }
        });

        etDetail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkInputDetail();
            }
        });
    }


    void RegisterEvent() { // 게시글 등록이 정상적으로 됐는지 확인해주고 다음 화면으로 넘겨줌
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        int id = rgStyle.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) findViewById(id);

        BoardModel BoardModel = new BoardModel();
        BoardModel.groupName = etGroupTitle.getText().toString();
        BoardModel.groupShortTitle = etShortTitle.getText().toString();
        BoardModel.groupLimit = Integer.parseInt(etLimit.getText().toString());
        BoardModel.groupStyle = rb.getText().toString();
        BoardModel.groupTopic = spinner.getSelectedItem().toString();
        BoardModel.groupExplain = etDetail.getText().toString();
        BoardModel.joinCount = 1;

        BoardModel.uid = uid;

//        FirebaseDatabase.getInstance().getReference().child("group").child(uid).setValue(BoardModel).addOnSuccessListener(new OnSuccessListener<Void>() {
//        FirebaseDatabase.getInstance().getReference().child("group").push().child(uid).setValue(BoardModel).addOnSuccessListener(new OnSuccessListener<Void>() {
        FirebaseDatabase.getInstance().getReference().child("group").push().setValue(BoardModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override //게시글 작성이 성공하면 화면 toast메시지 출력 및 finish() 실행
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "게시글 등록이 완료되었습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private boolean validate() {
        boolean valid = true;

        if (etGroupTitle.getText().toString().isEmpty()) {
            etGroupTitle.setError("제목을 입력해 주세요!");
            valid = false;
        } else {
            etGroupTitle.setError(null);
        }
        if (etShortTitle.getText().toString().isEmpty()) {
            etShortTitle.setError("짧은 소개 부분를 입력해 주세요!");
            valid = false;
        } else {
            etShortTitle.setError(null);
        }
        if (etLimit.getText().toString().isEmpty()) {
            etLimit.setError("제한 인원을 입력해 주세요!");
            valid = false;
        } else {
            etLimit.setError(null);
        }

        if (Integer.parseInt(etLimit.getText().toString()) > 20 || Integer.parseInt(etLimit.getText().toString()) < 2 ) {
            etLimit.setError("2명이상 20명이하로 입력해주세요!");
            valid = false;
        } else {
            etLimit.setError(null);
        }

//        if (spinner.getSelectedItem().toString().isEmpty()) {
//            spinner.setError("Password를 입력해 주세요!");
//            valid = false;
//        } else {
//            spinner.setError(null);
//        }
        if (etDetail.getText().toString().isEmpty()) {
            etDetail.setError("상세 설명을 입력해 주세요!");
            valid = false;
        } else {
            etDetail.setError(null);
        }
        return valid;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkInputTitle() {
        String group = etGroupTitle.getText().toString();
        if (group.isEmpty()) {
            ivCheckTitle.setImageResource(R.drawable.ic_check_gray);
        } else {
            ivCheckTitle.setImageResource(R.drawable.ic_check_black);
        }
    }

    private void checkInputLimit() {
        String limit = etLimit.getText().toString();
        if (limit.isEmpty() || Integer.parseInt(limit) > 20) {
            ivCheckLimit.setImageResource(R.drawable.ic_check_gray);
        } else {
            ivCheckLimit.setImageResource(R.drawable.ic_check_black);
        }
    }

    private void checkInputShortTitle() {
        String ShortTitle = etShortTitle.getText().toString();
        if (ShortTitle.isEmpty()) {
            ivCheckSimple.setImageResource(R.drawable.ic_check_gray);
        } else {
            ivCheckSimple.setImageResource(R.drawable.ic_check_black);
        }
    }

    private void checkInputDetail() {
        String Detail = etDetail.getText().toString();
        if (Detail.isEmpty()) {
            ivCheckDetail.setImageResource(R.drawable.ic_check_gray);
        } else {
            ivCheckDetail.setImageResource(R.drawable.ic_check_black);
        }
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한 번 더 누르시면 전 화면으로 돌아갑니다", Toast.LENGTH_SHORT).show();
        }
    }
}

