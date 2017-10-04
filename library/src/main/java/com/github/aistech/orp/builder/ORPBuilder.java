package com.github.aistech.orp.builder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.aistech.orp.activities.ORPActivity;

import org.parceler.Parcels;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builder used to create all the resources needed to start a new activity
 * and recover the object references passed in the {@link ORPBuilder#withObject} method.
 * Created by Jonathan Nobre Ferreira on 07/12/16.
 */

public class ORPBuilder {

    private Context context;
    private Class<? extends ORPActivity> destinationActivity;

    private Map<String, Object> parameters;

    public ORPBuilder(Context context) {
        this.context = context;
        parameters = new LinkedHashMap<>();
    }

    public ORPBuilder withDestinationActivity(Class<? extends ORPActivity> activity) {
        destinationActivity = activity;
        return this;
    }

    public ORPBuilder withObject(String key, Object object) {
        parameters.put(key, object);
        return this;
    }

    /**
     * In case you need the Intent already configured to use in other situations, I'll gonna be
     * nice with you, you can get the all set Intent using this method.
     */
    public Intent build() {
        Intent intent = new Intent(context, destinationActivity);
        Bundle bundle = new Bundle();

        for (String key : parameters.keySet()) {
            bundle.putParcelable(key, Parcels.wrap(parameters.get(key)));
        }

        intent.putExtras(bundle);

        return intent;
    }

    public void start() {
        Intent intent = build();
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, ORPActivity.REQUEST_CODE);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
