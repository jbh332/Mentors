package com.example.edu.fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edu.R;

public class PagerFragment extends Fragment {

    private int position;

    public static PagerFragment getInstance(int position) {

        PagerFragment pagerFragment = new PagerFragment();
        pagerFragment.position = position;
        return pagerFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pager_fragment, null);

        switch (position) {
            case 0:
               // ((ImageView) view.findViewById(R.id.position)).setImageResource(R.mipmap.ic_launcher);
                return view;
            case 1:
               // ((ImageView) view.findViewById(R.id.position)).setImageResource(R.mipmap.ic_launcher_round);
                return view;
            case 2:
               // ((ImageView) view.findViewById(R.id.position)).setImageResource(R.mipmap.ic_launcher);
                return view;
        }
        return view;
    }
}
