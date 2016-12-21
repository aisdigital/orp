package example.orp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.aistech.orp.activities.ORPActivity;
import com.github.aistech.orp.annotations.DestinationExtraObject;
import com.github.aistech.orp.builder.ORPBuilder;

import example.orp.R;
import example.orp.model.User;

public class Main2Activity extends ORPActivity {

    @DestinationExtraObject("user")
    private User user;

    private User anotherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Need to call this in order to load the parameters
        super.onCreate(savedInstanceState, this);

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.anotherUser = new User("Cortana", 18);

        this.user.setName("Jonathan Nobre Ferreira");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ORPBuilder(Main2Activity.this)
                        .withDestinationActivity(MainActivity.class)
                        .passingObject("cortana", anotherUser)
                        .start();
            }
        });
    }

}
