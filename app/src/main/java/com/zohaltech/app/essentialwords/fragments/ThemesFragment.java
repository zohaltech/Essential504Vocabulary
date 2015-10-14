package com.zohaltech.app.essentialwords.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zohaltech.app.essentialwords.adapters.ThemeAdapter;
import com.zohaltech.app.essentialwords.data.Themes;
import com.zohaltech.app.essentialwords.entities.Theme;

import java.util.ArrayList;

import com.zohaltech.app.essentialwords.R;


public class ThemesFragment extends Fragment {

    RecyclerView recyclerThemes;
    ArrayList<Theme> themes = new ArrayList<>();
    ThemeAdapter adapter;

    public ThemesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_themes, container, false);
        recyclerThemes = (RecyclerView) rootView.findViewById(R.id.recyclerThemes);
        //recyclerThemes.setHasFixedSize(true);
        recyclerThemes.setLayoutManager(new LinearLayoutManager(getActivity()));
        themes.addAll(Themes.select());
        adapter = new ThemeAdapter(getActivity(), themes);
        recyclerThemes.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        themes.clear();
        themes.addAll(Themes.select());
        adapter.notifyDataSetChanged();
    }
}