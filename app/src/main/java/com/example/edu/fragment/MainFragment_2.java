package com.example.edu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edu.MainActivity;
import com.example.edu.R;
import com.example.edu.RecyclerAdpater.RecyclerAdapter_Likes;

public class MainFragment_2 extends Fragment {
    @Nullable
    RecyclerView recycle2;
    RecyclerView.LayoutManager manager;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment2_main, container, false);

        recycle2 = view.findViewById(R.id.recycleView2);
        recycle2.setHasFixedSize(true);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycle2.setLayoutManager(manager);

        final RecyclerAdapter_Likes adapter = new RecyclerAdapter_Likes(getActivity());
        recycle2.setAdapter(adapter);

        MainActivity.sF2 = this;
        return view;

    }

    public static MainFragment_2 newInstance() {
        return new MainFragment_2();
    }
}
