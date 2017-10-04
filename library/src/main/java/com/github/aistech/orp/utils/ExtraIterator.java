package com.github.aistech.orp.utils;

import com.github.aistech.orp.activities.ORPActivity;
import com.github.aistech.orp.annotations.Extra;

import java.lang.reflect.Field;

/**
 * Created by Jonathan Nobre Ferreira on 03/10/17.
 */

public class ExtraIterator {

    private Class<? extends ORPActivity> clazz;

    private ExtraFieldIterator extraFieldIterator;
    private ExtraFieldIteratorFinished extraFieldIteratorFinished;

    public ExtraIterator(Class<? extends ORPActivity> clazz) {
        this.clazz = clazz;
    }

    public ExtraIterator iterate(ExtraFieldIterator extraFieldIterator) {
        this.extraFieldIterator = extraFieldIterator;
        return this;
    }

    public ExtraIterator onFinish(ExtraFieldIteratorFinished extraFieldIteratorFinished) {
        this.extraFieldIteratorFinished = extraFieldIteratorFinished;
        return this;
    }

    public void execute() {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Extra.class)) {

                Extra extra = field.getAnnotation(Extra.class);
                String parameterKey = field.getName();

                if (!extra.value().isEmpty()) {
                    parameterKey = extra.value();
                }

                extraFieldIterator.onFieldIterate(field, extra, parameterKey);
            }
        }

        if (extraFieldIteratorFinished != null) {
            this.extraFieldIteratorFinished.onFinished();
        }
    }

    public interface ExtraFieldIterator {
        void onFieldIterate(Field field, Extra extra, String parameterKey);
    }

    public interface ExtraFieldIteratorFinished {
        void onFinished();
    }
}
