package com.example.edu;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v4.app.FragmentManager;

import com.example.edu.databinding.ActivityIntroBinding;
import com.example.edu.fragment.PagerFragment;
import com.pm10.library.LineIndicator;

public class IntroActivity extends AppCompatActivity {

    ActivityIntroBinding b;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_intro);

        ViewPager viewPager = b.pager;
        pagerAdapter = new PagerAdapter(getSupportFragmentManager()); //PagerAdapter를 만든다. 어댑터는 추상클래스이므로 아래에 클래스 선언
        viewPager.setAdapter(pagerAdapter); // 페이저와 어댑터 연결

        LineIndicator lineIndicator = findViewById(R.id.indicator);
        lineIndicator.setupWithViewPager(viewPager); //인디케이터 셋팅

        b.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextAct = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(nextAct);
            }
        });

    }

    private class PagerAdapter extends FragmentPagerAdapter {


        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PagerFragment.getInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
