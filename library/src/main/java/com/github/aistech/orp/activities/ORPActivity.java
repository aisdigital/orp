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

    public static final String TAG = ORPActivity.class.getSimpleName();

    /**
     * This constant is used recover the Caller (or if you want, origin) activity's hashCode who started
     * the current activity. This code is used to recover the extra parameters sent to the current activity.
     */
    public static final String HASH_CODE_EXTRA = ORPActivity.class.getName().concat("originActivityHashCode");

    private Integer activityCallerHashCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initializingParsing(savedInstanceState);

        super.onCreate(savedInstanceState);
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
        initializingParsing(savedInstanceState);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveInstanceStateForFields(outState, getInstance(), getInstance().getClass());

        super.onSaveInstanceState(outState);
    }

    private void saveInstanceStateForFields(Bundle outState, ORPActivity thisActivity, Class<? extends ORPActivity> clazz) {

        if (clazz.getSuperclass() != ORPActivity.class) {
            saveInstanceStateForFields(outState, thisActivity, (Class<? extends ORPActivity>) clazz.getSuperclass());
        }

        Log.i(TAG, "saveInstanceStateForFields() | " + clazz.getName());

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
                        Log.i(TAG, "Saved (" + parameterKey + "): " + field.get(getInstance()).toString() + " | " + clazz.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void onRestoringState(@Nullable Bundle savedInstanceState, ORPActivity thisActivity, Class<? extends ORPActivity> clazz) {

        if (clazz.getSuperclass() != ORPActivity.class) {
            onRestoringState(savedInstanceState, thisActivity, (Class<? extends ORPActivity>) clazz.getSuperclass());
        }

        Log.i(TAG, "onRestoringState() | " + clazz.getName());

        if (savedInstanceState == null) {
            Log.i(TAG, "savedInstanceState is null" + " | " + clazz.getName());
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

                Log.i(TAG, "Trying to restore (" + parameterKey + ") | " + clazz.getName());

                try {
                    Parcelable parcelable = savedInstanceState.getParcelable(parameterKey);

                    if (parcelable != null) {
                        Log.i(TAG, "Recovered Parcelable (" + parcelable.toString() + ") | " + clazz.getName());

                        Object object = Parcels.unwrap(parcelable);

                        Log.i(TAG, "Recovered Object (" + object.toString() + ") | " + clazz.getName());

                        field.set(thisActivity, object);

                        Log.i(TAG, "Set object (" + object.toString() + ") in Field: " + field.getName());
                    } else {
                        Log.i(TAG, "parcelable for (" + parameterKey + ") is null" + " | " + clazz.getName());
                    }

                } catch (Exception e) {
                    Log.i(TAG, "Trying to restore | Exception: (" + e.getMessage() + ")");
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

        if (clazz.getSuperclass() != ORPActivity.class) {
            parseFields((Class<? extends ORPActivity>) clazz.getSuperclass());
        }

        Log.i(TAG, "parseFields() | " + clazz.getName());

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(DestinationExtraObject.class)) {
                try {
                    DestinationExtraObject destinationExtraObject = field.getAnnotation(DestinationExtraObject.class);
                    String parameterKey = field.getName();

                    if (!destinationExtraObject.value().isEmpty()) {
                        parameterKey = destinationExtraObject.value();
                    }

                    Log.i(TAG, "Trying to parse field (" + parameterKey + ") | " + clazz.getName());

                    Object object = ORPSingleton.getInstance().getParametersForOriginActivity(getInstance().getActivityCallerHashCode(), parameterKey);

                    if (object != null) {

                        Log.i(TAG, "Object to set (" + object.toString() + ") | " + clazz.getName());

                        field.set(getInstance(), object);

                        Log.i(TAG, "Object set (" + object.toString() + ") in Field: " + field.getName());
                    } else {
                        Log.i(TAG, "Object for (" + parameterKey + ") is null" + " | " + clazz.getName());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ORPExceptions orpExceptions) {
                    Log.i(TAG, "Trying to parse object | Exception: (" + orpExceptions.getMessage() + ")");
                }
            }
        }
    }

    /* getters and Setters */

    public Integer getActivityCallerHashCode() {
        return activityCallerHashCode;
    }
}
