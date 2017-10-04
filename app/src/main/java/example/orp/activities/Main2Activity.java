package example.orp.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.aistech.orp.activities.ORPActivity;
import com.github.aistech.orp.annotations.Extra;
import com.github.aistech.orp.builder.ORPBuilder;

import example.orp.R;
import example.orp.model.User;

public class Main2Activity extends BaseActivity implements TextWatcher {

    @Extra("user_2")
    private User secondUser;

    private EditText firstUserNameEditText;
    private EditText secondUserNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Need to call this in order to load the parameters
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button button = (Button) findViewById(R.id.button_2);

        firstUserNameEditText = (EditText) findViewById(R.id.previous_user_edit_text);
        secondUserNameEditText = (EditText) findViewById(R.id.next_user_edit_text);

        firstUserNameEditText.addTextChangedListener(this);
        secondUserNameEditText.addTextChangedListener(this);

        if (secondUser == null) {
            this.secondUser = new User("Cortana");
        }

        if (firstUser == null) {
            firstUser = new User("Master Chief");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ORPBuilder(Main2Activity.this)
                        .withDestinationActivity(Main2Activity.class)
                        .withObject("user", firstUser)
                        .withObject("user_2", secondUser)
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
    protected void onExtrasRestored() {
        firstUserNameEditText.setText(this.firstUser.getName());
        secondUserNameEditText.setText(this.secondUser.getName());
    }

    @Override
    public ORPActivity getInstance() {
        return this;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (firstUserNameEditText.getEditableText() == s) {
            this.firstUser.setName(s.toString());
        } else if (secondUserNameEditText.getEditableText() == s) {
            this.secondUser.setName(s.toString());
        }
    }
}
