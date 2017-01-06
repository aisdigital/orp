package example.orp.activities;

import com.github.aistech.orp.activities.ORPActivity;
import com.github.aistech.orp.annotations.DestinationExtraObject;

import example.orp.model.User;

/**
 * Created by jonathan on 06/01/17.
 */

public class BaseActivity extends ORPActivity {

    @DestinationExtraObject("user")
    protected User user;
}
