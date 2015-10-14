package com.zohaltech.app.essentialwords.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatSpinner;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zohaltech.app.essentialwords.R;
import com.zohaltech.app.essentialwords.classes.App;
import com.zohaltech.app.essentialwords.classes.DialogManager;
import com.zohaltech.app.essentialwords.classes.ReminderManager;
import com.zohaltech.app.essentialwords.data.SystemSettings;
import com.zohaltech.app.essentialwords.data.Themes;
import com.zohaltech.app.essentialwords.data.Vocabularies;
import com.zohaltech.app.essentialwords.entities.SystemSetting;
import com.zohaltech.app.essentialwords.entities.Theme;
import com.zohaltech.app.essentialwords.entities.Vocabulary;
import com.zohaltech.app.essentialwords.serializables.Reminder;
import com.zohaltech.app.essentialwords.serializables.ReminderSettings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import widgets.MyToast;

public class SchedulerActivity extends EnhancedActivity {
    CheckBox chkSa;
    CheckBox chkSu;
    CheckBox chkMo;
    CheckBox chkTu;
    CheckBox chkWe;
    CheckBox chkTh;
    CheckBox chkFr;

    AppCompatSpinner spinnerIntervals;
    AppCompatSpinner spinnerStartTheme;

    Button       btnStart;
    Button       btnStop;
    Button       btnPause;
    Button       btnRestart;
    Button       btnStartTime;
    LinearLayout layoutRingtone;
    Button       btnSelectTone;

    TextView txtStatus;

    @Override
    protected void onCreated() {
        setContentView(R.layout.activity_scheduler);
        initialise();
    }

    private void initialise() {
        //edtStartVocabularyNo = (EditText) findViewById(R.id.edtStartVocabularyNo);
        //edtAlarmIntervals = (EditText) findViewById(R.id.edtAlarmIntervals);
        spinnerIntervals = (AppCompatSpinner) findViewById(R.id.spinnerIntervals);
        spinnerStartTheme = (AppCompatSpinner) findViewById(R.id.spinnerStartTheme);
        btnStartTime = (Button) findViewById(R.id.btnStartTime);

        chkSa = (CheckBox) findViewById(R.id.chkSa);
        chkSu = (CheckBox) findViewById(R.id.chkSu);
        chkMo = (CheckBox) findViewById(R.id.chkMo);
        chkTu = (CheckBox) findViewById(R.id.chkTu);
        chkWe = (CheckBox) findViewById(R.id.chkWe);
        chkTh = (CheckBox) findViewById(R.id.chkTh);
        chkFr = (CheckBox) findViewById(R.id.chkFr);

        btnStop = (Button) findViewById(R.id.btnStop);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnRestart = (Button) findViewById(R.id.btnRestart);
        layoutRingtone = (LinearLayout) findViewById(R.id.layoutRingtone);
        btnSelectTone = (Button) findViewById(R.id.btnSelectTone);
        txtStatus = (TextView) findViewById(R.id.txtStatus);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            layoutRingtone.setVisibility(View.GONE);
        }

        bind();
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                setViewsStatus();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderManager.pause();
                bind();
                setViewsStatus();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderManager.stop();
                bind();
                setViewsStatus();
            }
        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                setViewsStatus();
            }
        });

        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnStartTime.getText().length() > 0) {
                    int hour = Integer.valueOf(btnStartTime.getText().toString().substring(0, 2));
                    int minute = Integer.valueOf(btnStartTime.getText().toString().substring(3, 5));
                    DialogManager.showTimePickerDialog(App.currentActivity, "", hour, minute, new Runnable() {
                        @Override
                        public void run() {
                            btnStartTime.setText(DialogManager.timeResult);
                        }
                    });
                } else {
                    DialogManager.showTimePickerDialog(App.currentActivity, "", 12, 0, new Runnable() {
                        @Override
                        public void run() {
                            btnStartTime.setText(DialogManager.timeResult);
                        }
                    });
                }
            }
        });

        btnSelectTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                startActivityForResult(intent, 5);
            }
        });

        setViewsStatus();
    }

    private void start() {
        ReminderSettings settings = ReminderManager.getReminderSettings();

        boolean paused = settings.getStatus() == ReminderSettings.Status.PAUSE;

        boolean[] days = {
                chkSu.isChecked(),
                chkMo.isChecked(),
                chkTu.isChecked(),
                chkWe.isChecked(),
                chkTh.isChecked(),
                chkFr.isChecked(),
                chkSa.isChecked()};

        int selectedThemeId = spinnerStartTheme.getSelectedItemPosition() + 1;
      //  int startVocabId = Vocabularies.selectByLesson(selectedThemeId).get(0).getId();
        //TODO handle themeId
        int startVocabId=0;

        Date reminderTime = Calendar.getInstance().getTime();
        Reminder garbage = settings.getReminder();
        if (garbage != null) {
            if (garbage.getTime() != null) {
                reminderTime = garbage.getTime();
            }

            startVocabId = garbage.getVocabularyId();
        }
        Vocabulary vocabulary = Vocabularies.select(startVocabId);
        if (vocabulary == null) {
            return;
        }


        settings.setReminder(new Reminder(vocabulary.getId(), reminderTime, vocabulary.getVocabulary(), vocabulary.getVocabEnglishDef(), true));
        settings.setStartTime(btnStartTime.getText().toString());
        //settings.setIntervals(Integer.parseInt(edtAlarmIntervals.getText().toString()));
        //  settings.setIntervals();
        settings.setIntervals((Integer) spinnerIntervals.getSelectedItem());
        settings.setWeekdays(days);
        settings.setStatus(ReminderSettings.Status.RUNNING);
        ReminderManager.setReminderSettings(settings);

        ReminderManager.start(paused);

        bind();

        settings = ReminderManager.getReminderSettings();
        Date time = settings.getReminder().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm", Locale.getDefault());
        MyToast.show("First vocabulary will be notified on " + sdf.format(time), Toast.LENGTH_LONG);
    }

    private void bind() {
        ReminderSettings settings = ReminderManager.getReminderSettings();

        btnRestart.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnStart.setVisibility(View.GONE);

        if (settings.getStatus() == ReminderSettings.Status.STOP) {
            btnStart.setVisibility(View.VISIBLE);
        } else if (settings.getStatus() == ReminderSettings.Status.RUNNING) {
            btnStop.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.VISIBLE);
        } else if (settings.getStatus() == ReminderSettings.Status.PAUSE) {
            btnStop.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.VISIBLE);
        } else if (settings.getStatus() == ReminderSettings.Status.FINISHED) {
            btnRestart.setVisibility(View.VISIBLE);
        }

        btnStartTime.setText(settings.getStartTime());
        //edtAlarmIntervals.setText(String.valueOf(settings.getIntervals()));

        ArrayList<Integer> intervals = new ArrayList<>();

        intervals.add(15);
        intervals.add(30);
        intervals.add(45);
        intervals.add(60);
        intervals.add(90);
        ArrayAdapter<Integer> intervalAdapter = new ArrayAdapter<>(this, R.layout.spinner_current_item, intervals);
        intervalAdapter.setDropDownViewResource(R.layout.spinner_item_center);
        spinnerIntervals.setAdapter(intervalAdapter);
        spinnerIntervals.setSelection(intervalAdapter.getPosition(settings.getIntervals()));

        ArrayList<String> themeNames = new ArrayList<>();
        ArrayList<Theme> themes = Themes.select();

        for (Theme theme : themes) {
            themeNames.add(theme.getName());
        }

        ArrayAdapter<String> themesAdapter = new ArrayAdapter<>(this, R.layout.spinner_current_item, themeNames);
        themesAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerStartTheme.setAdapter(themesAdapter);
        spinnerStartTheme.setSelection(0);

        if (settings.getReminder() != null) {
            Vocabulary vocabulary = Vocabularies.select(settings.getReminder().getVocabularyId());
            assert vocabulary != null;

            //TODO handle themeId
          //  spinnerStartTheme.setSelection(vocabulary.getThemeId() - 1);
            //edtStartVocabularyNo.setText(String.valueOf(settings.getReminder().getVocabularyId()));
        }
        boolean[] days = settings.getWeekdays();

        chkSu.setChecked(days[0]);
        chkMo.setChecked(days[1]);
        chkTu.setChecked(days[2]);
        chkWe.setChecked(days[3]);
        chkTh.setChecked(days[4]);
        chkFr.setChecked(days[5]);
        chkSa.setChecked(days[6]);

        SystemSetting setting = SystemSettings.getCurrentSettings();
        btnSelectTone.setText(setting.getAlarmRingingTone());
    }

    private void setViewsStatus() {
        switch (ReminderManager.getReminderSettings().getStatus()) {
            case RUNNING:
                spinnerStartTheme.setEnabled(false);
                btnStartTime.setEnabled(false);
                spinnerIntervals.setEnabled(false);
                chkSu.setEnabled(false);
                chkMo.setEnabled(false);
                chkTu.setEnabled(false);
                chkWe.setEnabled(false);
                chkTh.setEnabled(false);
                chkFr.setEnabled(false);
                chkSa.setEnabled(false);
                btnSelectTone.setEnabled(false);
                txtStatus.setText("Status : Running");
                break;
            case PAUSE:
                spinnerStartTheme.setEnabled(false);
                btnStartTime.setEnabled(true);
                spinnerIntervals.setEnabled(true);
                chkSu.setEnabled(true);
                chkMo.setEnabled(true);
                chkTu.setEnabled(true);
                chkWe.setEnabled(true);
                chkTh.setEnabled(true);
                chkFr.setEnabled(true);
                chkSa.setEnabled(true);
                btnSelectTone.setEnabled(true);
                txtStatus.setText("Status : Paused");
                break;
            case STOP:
                spinnerStartTheme.setEnabled(true);
                btnStartTime.setEnabled(true);
                spinnerIntervals.setEnabled(true);
                chkSu.setEnabled(true);
                chkMo.setEnabled(true);
                chkTu.setEnabled(true);
                chkWe.setEnabled(true);
                chkTh.setEnabled(true);
                chkFr.setEnabled(true);
                chkSa.setEnabled(true);
                btnSelectTone.setEnabled(true);
                txtStatus.setText("Status : Stopped");
                break;
            case FINISHED:
                spinnerStartTheme.setEnabled(true);
                btnStartTime.setEnabled(true);
                spinnerIntervals.setEnabled(true);
                chkSu.setEnabled(true);
                chkMo.setEnabled(true);
                chkTu.setEnabled(true);
                chkWe.setEnabled(true);
                chkTh.setEnabled(true);
                chkFr.setEnabled(true);
                chkSa.setEnabled(true);
                btnSelectTone.setEnabled(true);
                txtStatus.setText("Status : Finished");
                break;
        }
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
    protected void onToolbarCreated() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Scheduler");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == 5) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            SystemSetting setting = SystemSettings.getCurrentSettings();
            if (uri != null) {
                Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
                String title = ringtone.getTitle(this);
                setting.setRingingToneUri(uri.toString());
                setting.setAlarmRingingTone(title);
                btnSelectTone.setText(title);
            } else {
                setting.setRingingToneUri(Settings.System.DEFAULT_NOTIFICATION_URI.getPath());
                Ringtone ringtone = RingtoneManager.getRingtone(App.context, Settings.System.DEFAULT_NOTIFICATION_URI);
                setting.setAlarmRingingTone(ringtone.getTitle(this));
                btnSelectTone.setText(ringtone.getTitle(this));
            }
            SystemSettings.update(setting);
        }
    }
}
