package com.github.aistech.orp.exceptions;

import android.annotation.TargetApi;
import android.os.Build;

/**
 * Created by Jonathan Nobre Ferreira on 09/12/16.
 */

public class ORPExceptions extends Exception {

    public ORPExceptions() {
    }

    public ORPExceptions(String message) {
        super(message);
    }

    public ORPExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public ORPExceptions(Throwable cause) {
        super(cause);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public ORPExceptions(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
