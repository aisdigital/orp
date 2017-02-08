package example.orp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.github.aistech.orp.activities.ORPActivity;
import com.github.aistech.orp.annotations.DestinationExtraObject;
import com.github.aistech.orp.builder.ORPBuilder;

import example.orp.R;
import example.orp.model.User;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends ORPActivity {

    private User user1;

    // Set the parameter name is optional
    @DestinationExtraObject()
    private User cortana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        // Look, at First the user name is Master Chief
        if (user1 == null) {
            this.user1 = new User("Master Chief", 117);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ORPBuilder(MainActivity.this)
                        .withDestinationActivity(Main2Activity.class)
                        .passingObject("user", user1)
                        .start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // But as soon as the activity is resumed from the Main2Activity
        // and since we changed his name on the other activity, guess what? Changed :)
        Toast.makeText(this, this.user1.getName(), Toast.LENGTH_SHORT).show();

        // This is for the second time that this activity is launched from the Main2Activity.
        // Showing not only that is possible to use more than one instance of the same activity class
        // but there is no problem being the activity that send and recover the parameters.
        //
        // Cool isn't ? :D
        if (this.cortana != null) {
            Toast.makeText(this, this.cortana.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public ORPActivity getInstance() {
        return this;
    }
}
