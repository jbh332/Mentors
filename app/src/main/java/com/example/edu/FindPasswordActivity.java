package com.example.edu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindPasswordActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button btnCommit;
    EditText etEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_find_password);

        btnCommit = findViewById(R.id.btnCommit);
        etEmail = findViewById(R.id.etEmail);

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPassword();
            }
        });

        toolbar = findViewById(R.id.tBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                //뒤로가기 버튼 클릭 시 로그인 화면 연결
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void findPassword() {
        String email = etEmail.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Email을 입력해 주세요!");
        } else {
            etEmail.setError(null);
            //여기부터 기능추가
            Toast.makeText(this, "비밀번호 찾기 성공", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FindPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); //스택에 있는 모든 엑티비티 종료(삭제)
        }
    }
}
