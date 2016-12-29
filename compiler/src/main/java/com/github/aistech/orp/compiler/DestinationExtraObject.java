package com.github.aistech.orp.compiler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be used if you want to recover the object reference
 * sent by the caller activity.
 * Created by Jonathan Nobre Ferreira on 07/12/16.
 */

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface DestinationExtraObject {

    String value() default "";
}