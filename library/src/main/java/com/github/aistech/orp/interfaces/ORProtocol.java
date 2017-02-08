package com.github.aistech.orp.interfaces;

import com.github.aistech.orp.activities.ORPActivity;

/**
 * Created by jonathan on 08/02/17.
 */

public interface ORProtocol<T extends ORPActivity> {
    T getInstance();
}
