package com.github.aistech.orp.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.aistech.orp.annotations.DestinationExtraObject;
import com.github.aistech.orp.exceptions.ORPExceptions;
import com.github.aistech.orp.interfaces.ORProtocol;
import com.github.aistech.orp.singletons.ORPSingleton;

import org.parceler.Parcels;

import java.lang.reflect.Field;

/**
 * The base Activity that will handle the object reference passing between activities.
 * Created by Jonathan Nobre Ferreira on 07/12/16.
 */

public abstract class ORPActivity extends AppCompatActivity implements ORProtocol {

    /**
     * This constant is used recover the Caller (or if you want, origin) activity's hashCode who started
     * the current activity. This code is used to recover the extra parameters sent to the current activity.
     */
    public static final String HASH_CODE_EXTRA = ORPActivity.class.getName().concat("originActivityHashCode");

    private Integer activityCallerHashCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializingParsing(savedInstanceState);
    }

    private void initializingParsing(@Nullable Bundle savedInstanceState) {
        recoverOriginHashCode();

        parseFields(getInstance().getClass());

        onRestoringState(savedInstanceState, getInstance(), getInstance().getClass());
    }

    /**
     * @param savedInstanceState
     * @deprecated Because now we get the instance using {@link ORProtocol#getInstance()}
     */
    @Deprecated
    protected void onCreate(@Nullable Bundle savedInstanceState, ORPActivity thisActivity) {
        super.onCreate(savedInstanceState);
        initializingParsing(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveInstanceStateForFields(outState, getInstance(), getInstance().getClass());
    }

    private void saveInstanceStateForFields(Bundle outState, ORPActivity thisActivity, Class<? extends ORPActivity> clazz) {

        try {
            clazz.getSuperclass().asSubclass(ORPActivity.class);
            saveInstanceStateForFields(outState, thisActivity, (Class<? extends ORPActivity>) clazz.getSuperclass());
        } catch (ClassCastException e) {
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(DestinationExtraObject.class)) {
                DestinationExtraObject destinationExtraObject = field.getAnnotation(DestinationExtraObject.class);
                String parameterKey = field.getName();

                if (!destinationExtraObject.value().isEmpty()) {
                    parameterKey = destinationExtraObject.value();
                }

                try {
                    Object o = field.get(thisActivity);
                    if (o != null) {
                        outState.putParcelable(parameterKey, Parcels.wrap(o));
                        Log.e(getInstance().getLocalClassName(), "SAVED: " + field.get(getInstance()).toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void onRestoringState(@Nullable Bundle savedInstanceState, ORPActivity thisActivity, Class<? extends ORPActivity> clazz) {

        try {
            clazz.getSuperclass().asSubclass(ORPActivity.class);
            onRestoringState(savedInstanceState, thisActivity, (Class<? extends ORPActivity>) clazz.getSuperclass());
        } catch (ClassCastException e) {
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(DestinationExtraObject.class)) {
                DestinationExtraObject destinationExtraObject = field.getAnnotation(DestinationExtraObject.class);
                String parameterKey = field.getName();

                if (!destinationExtraObject.value().isEmpty()) {
                    parameterKey = destinationExtraObject.value();
                }

                try {
                    if (savedInstanceState != null) {
                        Parcelable parcelable = savedInstanceState.getParcelable(parameterKey);

                        Log.e(getInstance().getLocalClassName(), "RESTORING: " + field.get(getInstance()).toString());

                        if (parcelable != null) {
                            Object o = Parcels.unwrap(parcelable);
                            field.set(thisActivity, o);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
     * {@link ORPActivity#onCreate(Bundle)} method if you call,
     * it will initialize all the variables that is annotated with {@link DestinationExtraObject}
     * with the respective object reference recovered from {@link ORPSingleton}
     * that matches with the value passed in the annotation.
     */
    private void parseFields(Class<? extends ORPActivity> clazz) {

        try {
            clazz.getSuperclass().asSubclass(ORPActivity.class);
            parseFields((Class<? extends ORPActivity>) clazz.getSuperclass());
        } catch (ClassCastException e) {
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(DestinationExtraObject.class)) {
                try {
                    DestinationExtraObject destinationExtraObject = field.getAnnotation(DestinationExtraObject.class);
                    String parameterKey = field.getName();

                    if (!destinationExtraObject.value().isEmpty()) {
                        parameterKey = destinationExtraObject.value();
                    }

                    Object object = ORPSingleton.getInstance().getParametersForOriginActivity(getInstance().getActivityCallerHashCode(), parameterKey);
                    field.set(getInstance(), object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ORPExceptions orpExceptions) {
                    orpExceptions.printStackTrace();
                }
            }
        }
    }

    /* getters and Setters */

    public Integer getActivityCallerHashCode() {
        return activityCallerHashCode;
    }
}
