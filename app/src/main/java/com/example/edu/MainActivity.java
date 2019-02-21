package com.example.edu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.edu.RecyclerAdpater.ViewPagerAdapter;
import com.example.edu.fragment.AccountFragment;
import com.example.edu.fragment.ChatFragment;
import com.example.edu.fragment.MainFragment_1;
import com.example.edu.fragment.MainFragment_2;
import com.example.edu.model.StudyRoomModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private FirebaseAuth firebaseAuth;

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    public static ViewPagerAdapter adapter;
    private MenuItem prevMenuItem;

    private String week;
    private String roomNumber;


    public static Fragment sF1, sF2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // 툴바 설정
        toolbar = findViewById(R.id.tBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        // Navigation과 ViewPager 설정
        bottomNavigationView = findViewById(R.id.main_navigationView);
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MainFragment_1.newInstance());
        adapter.addFragment(ChatFragment.newInstance());
        adapter.addFragment(MainFragment_2.newInstance());
        adapter.addFragment(AccountFragment.newInstance());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.notifyDataSetChanged(); // TODO: 전체 갱신으로 보이는데 부분 갱신 가능한지 확인 (getitem 메소드 등으로)
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OpenMeetingActivity.class);
                startActivity(intent);

            }
        });
        passPushTokenToServer(); //push 테스트
    }

    void passPushTokenToServer() { //push 테스트
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        //firebase상에서는 해쉬맵으로만 업데이트 가능하다 (push 토큰)
        Map<String, Object> map = new HashMap<>();
        map.put("pushToken", token);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "예약하기");
        menu.add(0, 2, 0, "로그아웃");
        menu.add(0, 3, 0, "관리자 db 생성");

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case 1:
                Intent Intent = new Intent(MainActivity.this, ReservationActivity.class);//예약화면 임시 확인용!!
                startActivity(Intent);
                return true;
            case 2:
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut(); // 로그아웃
                Intent logOut = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(logOut);
                finish();
                return true;
            case 3:
                int i, j, k;
                StudyRoomModel.Day day = new StudyRoomModel.Day();
                day.uid = "uidtest";
                day.reservation = false;
                for (i = 0; i < 3; i++) {
                    switch (i) {
                        case 0:
                            roomNumber = "No101";
                            break;
                        case 1:
                            roomNumber = "No102";
                            break;
                        case 2:
                            roomNumber = "No103";
                            break;
                    }
                    for (j = 0; j < 5; j++) {
                        switch (j) {
                            case 0:
                                week = "Monday";
                                break;
                            case 1:
                                week = "Tuesday";
                                break;
                            case 2:
                                week = "Wednesday";
                                break;
                            case 3:
                                week = "Thursday";
                                break;
                            case 4:
                                week = "Friday";
                                break;
                        }
                        for (k = 9; k < 17; k++) {
                            day.time = k;
                            FirebaseDatabase.getInstance().getReference().child("studyroom").child(roomNumber).child(week).push().setValue(day).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                Toast.makeText(getApplicationContext(),"월요일 추가 완료",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        adapter.notifyDataSetChanged();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_list:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.action_chat:
                viewPager.setCurrentItem(1);
                return true;
            case R.id.action_star:
                viewPager.setCurrentItem(2);
                return true;
            case R.id.action_account:
                viewPager.setCurrentItem(3);
                return true;
        }
        return false;
    }
}
