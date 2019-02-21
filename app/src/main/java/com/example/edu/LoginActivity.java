package com.example.edu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.victor.loading.rotate.RotateLoading;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin, btnRegister;
    private TextView btnFindPassword;
    private EditText etEmail, etPassword;
    private ImageView ivCheckEmail, ivCheckPassword;
    //private FirebaseRemoteConfig firebaseRemoteConfig;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private FirebaseAuth firebaseAuth;
    private RotateLoading rotateLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        rotateLoading = findViewById(R.id.rotateloading);
        //firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnFindPassword = findViewById(R.id.btnFindPassword);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        ivCheckEmail = findViewById(R.id.ivCheckEmail);
        ivCheckPassword = findViewById(R.id.ivCheckPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnLogin.setEnabled(false);
                etEmail.setEnabled(false);
                etPassword.setEnabled(false);
                rotateLoading.start();

                if (validate() == false) { // 데이터 로컬에서 자체 검증
                    Log.e("test", "로그인 이벤트 실행 전 로컬 검증");
                    rotateLoading.stop();
                    btnLogin.setEnabled(true);
                    etPassword.setEnabled(true);
                    etEmail.setEnabled(true);
                    return;
                } else { // 로컬 자체 검증이 끝나면 서버 검증을 통해 로그인이 정상적으로 되었는지 체크
                    Log.e("test", "로그인 이벤트 실행 else 부분까지 옴");
                    loginEvent();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //비밀번호찾기 연결
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });

        etEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                checkInputEmail();
                return false;
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkInputEmail();
            }
        });

        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                checkInputPassword();
                return false;
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkInputPassword();
            }
        });
    }

    void loginEvent() { // 로그인이 정상적으로 됐는지 확인 후 메인화면 전환까지.
        firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) { // 로그인 실패 시 오류 메시지 출력
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            btnLogin.setEnabled(true);
                            etPassword.setEnabled(true);
                            etEmail.setEnabled(true);
                            rotateLoading.stop();

                        } else { // 로그인 성공 시
                            FirebaseUser user = firebaseAuth.getCurrentUser(); // user 받아오기
                            if (user != null) { // 로그인이 정상적으로 되었다면 user에는 값이 있을 것이다.
                                //로그인
                                // TODO:로그인하면 name 출력 될 수 있도록 하고 싶다.
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                //로그아웃
                            }
                        }
                    }
                });
    }

    private boolean validate() {
        boolean valid = true;
        String id, password;
        id = etEmail.getText().toString();
        password = etPassword.getText().toString();

        if (id.isEmpty()) {
            etEmail.setError("Email를 입력해 주세요!");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty()) {
            etPassword.setError("Password를 입력해 주세요!");
            valid = false;
        } else {
            etPassword.setError(null);
        }
        return valid;
    }

    private void checkInputEmail() {
        String Email = etEmail.getText().toString();
        if (Email.isEmpty()) {
            ivCheckEmail.setImageResource(R.drawable.ic_check_gray);
        } else {
            ivCheckEmail.setImageResource(R.drawable.ic_check_black);
        }
    }

    private void checkInputPassword() {
        String password = etPassword.getText().toString();
        if (password.isEmpty()) {
            ivCheckPassword.setImageResource(R.drawable.ic_check_gray);
        } else {
            ivCheckPassword.setImageResource(R.drawable.ic_check_black);
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
            Toast.makeText(getApplicationContext(), "한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}