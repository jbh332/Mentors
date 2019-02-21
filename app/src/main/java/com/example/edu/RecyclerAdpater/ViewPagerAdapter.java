package com.example.edu.RecyclerAdpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.edu.fragment.AccountFragment;
import com.example.edu.fragment.ChatFragment;
import com.example.edu.fragment.MainFragment_1;
import com.example.edu.fragment.MainFragment_2;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
//        return fragmentList.get(position);
        switch (position) {
            case 0:
                return new MainFragment_1();
            case 1:
                return new ChatFragment();
            case 2:
                return new MainFragment_2();
            case 3:
                return new AccountFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        // 주석으로 처리하면 한 번 생성된 뷰를 프레그먼트 이동할 때마다 지우지 않고 계속 보여지가 한다
    }

    @Override
    public int getItemPosition(Object object) {
            return POSITION_NONE;

    }


}
