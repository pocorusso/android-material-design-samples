package com.pocorusso.mdsamples;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pocorusso.mdsamples.adapter.RecyclerViewAdapter;

public class ScrollFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scroll1, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.technique_one_toolbar);
        RecyclerView days_list = (RecyclerView) v.findViewById(R.id.days_list_1);

        days_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        days_list.setAdapter(new RecyclerViewAdapter(
                getResources().getStringArray(R.array.days_names)
        ));


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_technique1);
        return v;
    }
}
