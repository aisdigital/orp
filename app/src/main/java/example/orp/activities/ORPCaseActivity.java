package example.orp.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.github.aistech.orp.activities.ORPActivity;
import com.github.aistech.orp.annotations.DestinationExtraObject;

import java.util.List;

import example.orp.R;
import example.orp.model.User;
import example.orp.util.CountdownUtils;

public class ORPCaseActivity extends ORPActivity {

    @DestinationExtraObject("users")
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Need to call this in order to load the parameters
        super.onCreate(savedInstanceState, this);

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CountdownUtils.getInstance().stop();
        finish();
    }
}
