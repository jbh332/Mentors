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
import com.example.edu.RecyclerAdpater.BoardRecyclerAdapter;


public class MainFragment_1 extends Fragment {
    RecyclerView.LayoutManager manager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_main, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new BoardRecyclerAdapter(getContext()));

        MainActivity.sF1 = this;
        return view;
    }

    public static MainFragment_1 newInstance() {
        return new MainFragment_1();
    }


}
