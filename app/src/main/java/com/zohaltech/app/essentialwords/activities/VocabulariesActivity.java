package com.zohaltech.app.essentialwords.activities;


import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;


import com.zohaltech.app.essentialwords.adapters.VocabularyAdapter;
import com.zohaltech.app.essentialwords.data.Vocabularies;
import com.zohaltech.app.essentialwords.entities.Theme;
import com.zohaltech.app.essentialwords.entities.Vocabulary;

import java.util.ArrayList;

import com.zohaltech.app.essentialwords.R;

public class VocabulariesActivity extends EnhancedActivity {

    private Theme theme;

    @Override
    protected   void onCreated() {
        setContentView(R.layout.activity_vocabularies);
        RecyclerView recyclerVocabularies = (RecyclerView) findViewById(R.id.recyclerVocabularies);
        recyclerVocabularies.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerVocabularies.setLayoutManager(layoutManager);
        //recyclerVocabularies.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        theme = (Theme) getIntent().getSerializableExtra("THEME");

        //TODO handle themeId
        //ArrayList<Vocabulary> vocabularies = Vocabularies.selectByLesson(theme.getId());
        //VocabularyAdapter adapter = new VocabularyAdapter(this, vocabularies, true);
        //recyclerVocabularies.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected  void onToolbarCreated() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(theme.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
}
