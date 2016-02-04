package com.andro.enigma.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andro.enigma.R;
import com.andro.enigma.database.CrosswordContract;
import com.andro.enigma.database.CrosswordDbHelper;
import com.andro.enigma.helper.Helper;
import com.andro.enigma.settings.MySettings;

import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {


    private EditText[] crossword;
    private TextView crosswordNumberTV;
    private TextView crosswordRecordTimeTV;
    private char[] letters;
    private int crosswordNumber;
    private int packageId;
    private String currentLocale;
    private String excludeString = "AEIOU";
    private Helper helper;

    public int getCrosswordNumber() {
        return crosswordNumber;
    }

    public void setCrosswordNumber(int crosswordNumber) {
        this.crosswordNumber = crosswordNumber;
    }

    public Chronometer myChrono;

    public EditText[] getCrossword() {
        return crossword;
    }

    public void setCrossword(EditText[] crossword) {
        this.crossword = crossword;
    }

    public void setLetters(char[] letters) {
        this.letters = letters;
    }

    public char getLetter(int position) {
        return letters[position];
    }

    public void setLetter(char letter, int position) {
        this.letters[position] = letter;
    }

    public void setField(EditText editText, int fieldNumber) {
        this.crossword[fieldNumber] = editText;
    }

    public EditText getField(int fieldNumber) {
        return crossword[fieldNumber];
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLetters(new char[36]);
        setCrossword(new EditText[36]);
        helper = new Helper();

        crosswordNumber = getIntent().getIntExtra("crosswordNumber",1);
        packageId = getIntent().getIntExtra("id",0);

        crosswordNumberTV = (TextView) findViewById(R.id.text_crossword_number);
        crosswordRecordTimeTV = (TextView) findViewById(R.id.text_record_time);
        Button next = (Button) findViewById(R.id.button_next);
        Button previous = (Button) findViewById(R.id.button_previous);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getCrosswordNumber() == 10){return;}
                clearCrossword();
                setCrosswordNumber(getCrosswordNumber()+1);
                initializeCrossword(getCrosswordNumber(), getLocale(), packageId);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Crossword", "" + getCrosswordNumber());
                if(getCrosswordNumber() == 1){return;}
                clearCrossword();
                setCrosswordNumber(getCrosswordNumber()-1);
                initializeCrossword(getCrosswordNumber(), getLocale(), packageId);
            }
        });

        myChrono = (Chronometer) findViewById(R.id.chronometer);

        TextWatcher myTextChanged = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){return;}
                for (int i = 0;i<=35;i++) {
                    if (getField(i).getText().hashCode() == s.hashCode()){
                            if (getField(i).getText().toString().charAt(0) == Character.toUpperCase(getLetter(i))){
                                getField(i).setEnabled(false);}
                            break;
                    }
                }
                boolean completed = true;
                for(EditText et : getCrossword()){
                    if(et.isEnabled()){
                        completed = false;
                    }
                }
                if(completed){finalizeCrossword();}
            }
        };


        for (int i = 0;i<=35;i++) {
            int resID = getResources().getIdentifier("Field" + i, "id", getPackageName());
            setField((EditText) findViewById(resID), i);
            getField(i).setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(1)});
            getField(i).addTextChangedListener(myTextChanged);
        }

        //initializeCrossword(crosswordNumber);
    }

    private void clearCrossword(){
        for (EditText item : getCrossword()){
            item.setEnabled(true);
            item.setBackgroundResource(R.drawable.edittext_drawable);
            item.setText("");
        }
        myChrono.setBase(SystemClock.elapsedRealtime());
    }

    private void finalizeCrossword() {
        myChrono.stop();
        Context context = getApplicationContext();
        CrosswordDbHelper mDbHelper = new CrosswordDbHelper(context);
        mDbHelper.updateCrosswordTime(getCrosswordNumber(), myChrono.getText().toString(), getLocale());
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.congratulations) + " " + myChrono.getText(), Toast.LENGTH_SHORT).show();
    }

    /**
     *
     * @param crosswordNumber
     * Number of the crossword to load
     */
    private void initializeCrossword(int crosswordNumber, String locale, int packageId) {

//        Log.d("MYTAG","initializeCrossword("+ crosswordNumber +", " + locale + ", " + packageId + ");");
        clearCrossword();
        Context context = getApplicationContext();
        CrosswordDbHelper mDbHelper = new CrosswordDbHelper(context);

        //TODO Figure out the best way to solve enigma localization
        Cursor c = null;
        String lang = PreferenceManager.getDefaultSharedPreferences(this).getString("listPref", "en_US");
        c = mDbHelper.getAllCrosswords(packageId);

//        UNCOMMENT TO ECHO DATABASE RECORDS
//        Log.d("DATABASE COUNT", "" + c.getCount());
//
//        if(c.moveToFirst()){
//           do{
//               Log.d("DATABASE RECORD", " ID " +c.getInt(0)+ " num "+ c.getInt(1) + " idpack" + c.getInt(2));
//           }while(c.moveToNext());
//        }

        assert c != null;
        c.moveToPosition(crosswordNumber-1);

        String inputString = c.getString(c.getColumnIndexOrThrow(CrosswordContract.CrosswordEntry.COLUMN_NAME_TEXT));
        String recordTime = c.getString(c.getColumnIndexOrThrow(CrosswordContract.CrosswordEntry.COLUMN_NAME_TIME));

        char[] charArray = inputString.toCharArray();
        setLetters(charArray);

        if(locale.equalsIgnoreCase("sr_RS")) {
            for (int i = 0; i <= 35; i++) {
                if (excludeString.contains(""+charArray[i])) {
                    getField(i).setEnabled(true);
                    getField(i).setBackgroundResource(R.drawable.edittext_drawable);
                } else if (helper.charMap.containsKey(""+charArray[i])) {
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(2);
                    getField(i).setFilters(FilterArray);
                    getField(i).setText(String.valueOf(helper.charMap.get(""+charArray[i])));
                    getField(i).setEnabled(false);
                    getField(i).setBackgroundResource(R.drawable.edittext_drawable);
                } else if (charArray[i] == '_') {
                    getField(i).setEnabled(false);
                    getField(i).setBackgroundResource(R.drawable.background_black);
                } else {
                    getField(i).setText(String.valueOf(charArray[i]));
                    getField(i).setEnabled(false);
                    getField(i).setBackgroundResource(R.drawable.edittext_drawable);
                }
            }
        } else if (locale.equalsIgnoreCase("en_US")){
            for (int i = 0; i <= 35; i++) {
                if (charArray[i] == '_') {
                    getField(i).setEnabled(false);
                    getField(i).setBackgroundResource(R.drawable.background_black);
                } else if (excludeString.contains(""+charArray[i])) {
                    getField(i).setEnabled(true);
                    getField(i).setBackgroundResource(R.drawable.edittext_drawable);
                } else {
                    getField(i).setText(String.valueOf(charArray[i]));
                    getField(i).setEnabled(false);
                    getField(i).setBackgroundResource(R.drawable.edittext_drawable);
                }
            }
        }
        c.close();
        crosswordNumberTV.setText("" + crosswordNumber);
        crosswordRecordTimeTV.setText(""+ recordTime);
        myChrono.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLocale();
        initializeCrossword(crosswordNumber, getLocale(),packageId);
    }

    private String getLocale() {
        return currentLocale;
    }

    public void setLocale() {
        SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(this);
        String languageToLoad  = shpref.getString("listPref", "sr_RS");
        Locale locale = new Locale(languageToLoad.split("_",2)[0],languageToLoad.split("_",2)[1]);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        ActionBar actionBar = getActionBar();
        if (actionBar != null){
            actionBar.setTitle(getResources().getString(R.string.app_name));
        }
        invalidateOptionsMenu();
        //Reset the TextView text to localized strings
        ((TextView)findViewById(R.id.label_running_time)).setText(getResources().getString(R.string.current_time));
        ((TextView)findViewById(R.id.label_record_time)).setText(getResources().getString(R.string.record_time));
        ((TextView)findViewById(R.id.label_enigma_number)).setText(getResources().getString(R.string.crossword_number));
        currentLocale = languageToLoad;
    }

    @Override
    protected void onPause() {
        super.onPause();
        CrosswordDbHelper mDbHelper = new CrosswordDbHelper(this);
        Cursor c = mDbHelper.getAllCrosswords(packageId);
        int solved = 0;
        if (c != null) {
            while(c.moveToNext()) {
                if(c.getString(4).equalsIgnoreCase("YES")){
                    solved++;
                }
            }
            c.close();
        }
        mDbHelper.updatePackageSolved(packageId,solved);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent set = new Intent(MainActivity.this, MySettings.class);
            startActivity(set);
        }
        else if(id == R.id.action_about){
            Intent ab = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(ab);
        }

        return super.onOptionsItemSelected(item);
    }
}
