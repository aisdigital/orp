package example.orp.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.github.aistech.orp.activities.ORPActivity;
import com.github.aistech.orp.annotations.Extra;
import com.github.aistech.orp.builder.ORPBuilder;

import example.orp.R;
import example.orp.model.User;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends ORPActivity {

    @Extra("user")
    private User user;

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fabric.with(this, new Crashlytics());

        this.user = new User("Noble Six");
        this.text = (TextView) findViewById(R.id.text);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ORPBuilder(MainActivity.this)
                        .withDestinationActivity(Main2Activity.class)
                        .withObject("user", user)
                        .start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // But as soon as the activity is resumed from the Main2Activity
        // and since we changed his name on the other activity, guess what? Changed :)
    }

    @Override
    public ORPActivity getInstance() {
        return this;
    }

    @Override
    protected void onExtrasRestored() {
        text.setText(this.user.getName());
    }
}
