package com.github.aistech.orp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.aistech.orp.annotations.Extra;
import com.github.aistech.orp.interfaces.ORProtocol;
import com.github.aistech.orp.utils.ExtraIterator;

import org.parceler.Parcels;

import java.lang.reflect.Field;

/**
 * The base Activity that will handle the object reference passing between activities.
 * Created by Jonathan Nobre Ferreira on 07/12/16.
 */

public abstract class ORPActivity extends AppCompatActivity implements ORProtocol {

    public static final Integer REQUEST_CODE = 2393;
    public static final String TAG = ORPActivity.class.getSimpleName();

    public Boolean didLoadedExtras = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras() == null ? new Bundle() : getIntent().getExtras();

        if (savedInstanceState != null) {
            bundle.putAll(savedInstanceState);
        }

        parseFields(bundle, getInstance().getClass());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (didLoadedExtras) {
            didLoadedExtras = false;
            onExtrasRestored();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            final Bundle bundle = data == null ? new Bundle() : data.getExtras();
            if (!bundle.isEmpty()) {
                new ExtraIterator(getInstance().getClass())
                        .iterate(new ExtraIterator.ExtraFieldIterator() {
                            @Override
                            public void onFieldIterate(Field field, Extra extra, String parameterKey) {
                                try {
                                    Parcelable parcelable = bundle.getParcelable(parameterKey);
                                    if (parcelable != null) {
                                        Object object = Parcels.unwrap(parcelable);
                                        field.set(getInstance(), object);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .execute();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (shouldSaveInstanceState()) {
            saveInstanceStateForFields(outState, getInstance().getClass());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (shouldSaveInstanceState()) {
            onRestoringState(savedInstanceState, getInstance().getClass());
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void finish() {
        onFinishWithResult(new Intent(), new Bundle(), getInstance().getClass());
        super.finish();
    }

    private void parseFields(final Bundle bundle, Class<? extends ORPActivity> clazz) {

        if (bundle == null) {
            Log.i(TAG, "bundle in parseFields() is null" + " | " + clazz.getName());
            return;
        }

        if (clazz.getSuperclass() != ORPActivity.class) {
            parseFields(bundle, (Class<? extends ORPActivity>) clazz.getSuperclass());
        }

        Log.i(TAG, "parseFields() for " + clazz.getName() + "with Bundle: " + bundle.toString());

        new ExtraIterator(clazz)
                .iterate(new ExtraIterator.ExtraFieldIterator() {
                    @Override
                    public void onFieldIterate(Field field, Extra extra, String parameterKey) {
                        try {
                            Parcelable parcelable = bundle.getParcelable(parameterKey);
                            if (parcelable != null) {
                                Object object = Parcels.unwrap(parcelable);
                                field.set(getInstance(), object);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onFinish(new ExtraIterator.ExtraFieldIteratorFinished() {
                    @Override
                    public void onFinished() {
                        didLoadedExtras = true;
                    }
                })
                .execute();
    }

    private void saveInstanceStateForFields(final Bundle outState, Class<? extends ORPActivity> clazz) {

        if (outState == null) {
            Log.i(TAG, "bundle in saveInstanceStateForFields() is null" + " | " + clazz.getName());
            return;
        }

        if (clazz.getSuperclass() != ORPActivity.class) {
            saveInstanceStateForFields(outState, (Class<? extends ORPActivity>) clazz.getSuperclass());
        }

        new ExtraIterator(clazz)
                .iterate(new ExtraIterator.ExtraFieldIterator() {
                    @Override
                    public void onFieldIterate(Field field, Extra extra, String parameterKey) {
                        try {
                            Object object = field.get(getInstance());
                            if (object != null)
                                outState.putParcelable(parameterKey, Parcels.wrap(object));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .execute();
    }

    private void onRestoringState(final Bundle savedInstanceState, Class<? extends ORPActivity> clazz) {

        if (savedInstanceState == null) {
            Log.i(TAG, " bundle in onRestoringState() is null" + " | " + clazz.getName());
            return;
        }

        if (clazz.getSuperclass() != ORPActivity.class) {
            onRestoringState(savedInstanceState, (Class<? extends ORPActivity>) clazz.getSuperclass());
        }

        Log.i(TAG, "onRestoringState() | " + clazz.getName());

        new ExtraIterator(clazz)
                .iterate(new ExtraIterator.ExtraFieldIterator() {
                    @Override
                    public void onFieldIterate(Field field, Extra extra, String parameterKey) {
                        try {
                            Parcelable parcelable = savedInstanceState.getParcelable(parameterKey);
                            if (parcelable != null) {
                                Object object = Parcels.unwrap(parcelable);
                                field.set(getInstance(), object);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onFinish(new ExtraIterator.ExtraFieldIteratorFinished() {
                    @Override
                    public void onFinished() {
                        didLoadedExtras = true;
                    }
                })
                .execute();
    }

    private void onFinishWithResult(final Intent intent, final Bundle bundle, Class<? extends ORPActivity> clazz) {

        if (clazz.getSuperclass() != ORPActivity.class) {
            onFinishWithResult(intent, bundle, (Class<? extends ORPActivity>) clazz.getSuperclass());
        }

        new ExtraIterator(clazz)
                .iterate(new ExtraIterator.ExtraFieldIterator() {
                    @Override
                    public void onFieldIterate(Field field, Extra extra, String parameterKey) {
                        try {
                            Object object = field.get(getInstance());
                            if (object != null) {
                                bundle.putParcelable(parameterKey, Parcels.wrap(object));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onFinish(new ExtraIterator.ExtraFieldIteratorFinished() {
                    @Override
                    public void onFinished() {
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                    }
                })
                .execute();
    }

    protected Boolean shouldSaveInstanceState() {
        return true;
    }

    protected abstract void onExtrasRestored();
}
