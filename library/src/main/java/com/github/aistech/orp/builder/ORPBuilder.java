package com.github.aistech.orp.builder;

import android.content.Intent;

import java.util.LinkedHashMap;
import java.util.Map;

import com.github.aistech.orp.activities.ORPActivity;
import com.github.aistech.orp.singletons.ORPSingleton;

/**
 * Builder used to create all the resources needed to start a new activity
 * and recover the object references passed in the {@link ORPBuilder#passingObject} method.
 * Created by Jonathan Nobre Ferreira on 07/12/16.
 */

public class ORPBuilder {

    private ORPActivity originActivity;
    private Class<? extends ORPActivity> destinationActivity;

    private Map<String, Object> parameters;

    /**
     * You shall init this builder passing the origin activity, a.k.a the source Activity.
     * @param originActivity
     */
    public ORPBuilder(ORPActivity originActivity) {
        this.originActivity = originActivity;
        this.parameters = new LinkedHashMap<>();
    }

    /**
     * Well, we want to send those objects to somewhere, so pass then destination
     * activity class here and I'll handle for you :)
     * @param activity
     * @return
     */
    public ORPBuilder withDestinationActivity(Class<? extends ORPActivity> activity) {
        this.destinationActivity = activity;
        return this;
    }

    /**
     * Using the same behavior while using the {#link {@link Intent#putExtra} method,
     * you should pass the key associeated with the object that you want to send to the
     * destination activity.
     * @param key
     * @param object
     * @return
     */
    public ORPBuilder passingObject(String key, Object object) {
        this.parameters.put(key, object);
        return this;
    }

    /**
     * In case you need the Intent already configured to use in other situations, I'll gonna be
     * good with you, you can get the all set Intent using this method.
     * @return
     */
    public Intent build() {
        ORPSingleton.getInstance().addOriginActivity(this.originActivity, this.parameters);
        Intent intent = new Intent(this.originActivity, this.destinationActivity);
        intent.putExtra(ORPActivity.HASH_CODE_EXTRA, this.originActivity.hashCode());
        return intent;
    }

    /**
     * But if you are lazy and you don't want to write the goddammit
     * {#link {@link android.content.Context#startActivity} method, don't worry, here it is.
     */
    public void start() {
        this.originActivity.startActivity(build());
    }
}
