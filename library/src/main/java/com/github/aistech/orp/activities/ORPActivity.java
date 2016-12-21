package com.github.aistech.orp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Field;

import com.github.aistech.orp.annotations.DestinationExtraObject;
import com.github.aistech.orp.exceptions.ORPExceptions;
import com.github.aistech.orp.singletons.ORPSingleton;

/**
 * The base Activity that will handle the object reference passing between activities.
 * Created by Jonathan Nobre Ferreira on 07/12/16.
 */

public class ORPActivity extends AppCompatActivity {

    /**
     * This constant is used recover the Caller (or if you want, origin) activity's hashCode who started
     * the current activity. This code is used to recover the extra parameters sent to the current activity.
     */
    public static final String HASH_CODE_EXTRA = ORPActivity.class.getName().concat("originActivityHashCode");

    private Integer activityCallerHashCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recoverOriginHashCode();
    }

    protected void onCreate(@Nullable Bundle savedInstanceState, ORPActivity thisActivity) {
        super.onCreate(savedInstanceState);

        recoverOriginHashCode();
        parseParameters(thisActivity);
    }

    private void recoverOriginHashCode() {
        // God damn it Android, why don't you at least initialize the extras ¬¬
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(HASH_CODE_EXTRA)) {
            this.activityCallerHashCode = getIntent().getIntExtra(HASH_CODE_EXTRA, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
            As soon as this activity is destroyed, we remove the extras parameters from the singleton
            that was sent to this one, because it won't need anymore.
        */
        ORPSingleton.getInstance().clearCurrentActivityParameters(this.activityCallerHashCode);
    }

    /* Utils */

    /**
     * Well, as soon as the current activity is started, preferably on the
     * {@link ORPActivity#onCreate(Bundle)} method if you call this method
     * passing the current {@link ORPActivity} instance, it will initialize
     * all the variables that is annotated with {@link DestinationExtraObject}
     * with the respective object reference recovered from {@link ORPSingleton}
     * that matches with the value passed in the annotation.
     *
     * @param orpActivity
     */
    public static void parseParameters(ORPActivity orpActivity) {
        for (Field field : orpActivity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(DestinationExtraObject.class)) {
                try {
                    DestinationExtraObject destinationExtraObject = field.getAnnotation(DestinationExtraObject.class);
                    String parameterKey = field.getName();
                    if (!destinationExtraObject.value().isEmpty()) {
                        parameterKey = destinationExtraObject.value();
                    }
                    Object object = ORPSingleton.getInstance().getParametersForOriginActivity(orpActivity.getActivityCallerHashCode(), parameterKey);
                    field.set(orpActivity, object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ORPExceptions orpExceptions) {
                    orpExceptions.printStackTrace();
                }
            } else {

            }
        }
    }

    /* getters and Setters */

    public Integer getActivityCallerHashCode() {
        return activityCallerHashCode;
    }
}
