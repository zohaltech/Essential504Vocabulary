package com.zohaltech.app.essential504vocabulary.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.zohaltech.app.essential504vocabulary.adapters.NoteAdapter;
import com.zohaltech.app.essential504vocabulary.data.Notes;
import com.zohaltech.app.essential504vocabulary.entities.Note;

import java.util.ArrayList;

import com.zohaltech.app.essential504vocabulary.R;

public class NotesFragment extends Fragment {
    public static final String VOCAB_ID = "VOCAB_ID";

    public static NotesFragment newInstance(int vocabId) {
        Bundle args = new Bundle();
        args.putInt(VOCAB_ID, vocabId);
        NotesFragment fragment = new NotesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_notes, container, false);
        RecyclerView recyclerNotes = (RecyclerView) view.findViewById(R.id.recyclerNotes);
        recyclerNotes.setHasFixedSize(true);
        recyclerNotes.setLayoutManager(new LinearLayoutManager(getActivity()));
        int vocabId = getArguments().getInt(VOCAB_ID);
        ArrayList<Note> notes = Notes.getNotes(vocabId);
        NoteAdapter adapter = new NoteAdapter(getActivity(), notes);
        recyclerNotes.setAdapter(adapter);

        return view;
    }
}
