package com.andro.enigma.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andro.enigma.R;

public class GetPackageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_package);

        TextView title = (TextView) findViewById(R.id.textView_package_title);

        Button button = (Button) findViewById(R.id.button_get_package);

        title.setText(getIntent().getStringExtra("title"));

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });

    }

}
