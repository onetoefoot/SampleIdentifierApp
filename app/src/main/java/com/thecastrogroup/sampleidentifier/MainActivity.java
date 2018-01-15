package com.thecastrogroup.sampleidentifier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.thecastrogroup.sampleidentifier.R;

public class MainActivity extends AppCompatActivity {

    Button btnLetUsBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButtonAction buttonAction = (ButtonAction) new ButtonAction();
    }

    /**
     *  manages all button activity
     */
    public class ButtonAction {
        ButtonAction() {
            btnLetUsBegin = (Button) findViewById(R.id.btnLetUsBegin);
            btnLetUsBegin.setOnClickListener(btnLetUsBeginOnClickListener);
        }
        private View.OnClickListener btnLetUsBeginOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        };
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_begin) {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        } else if (itemThatWasClickedId == R.id.action_view) {
            Intent intent = new Intent(MainActivity.this, ViewActivity.class);
            startActivity(intent);
        } else if (itemThatWasClickedId == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
