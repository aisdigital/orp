package com.github.aistech.orp.singletons;

import com.github.aistech.orp.activities.ORPActivity;
import com.github.aistech.orp.exceptions.ORPExceptions;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The All Might Singleton that will handle all your sins and misbehavior...no...just kidding,
 * this will only help you recover the object reference while using the {@link com.github.aistech.orp.builder.ORPBuilder}.
 * <p>
 * For your sins, well, may God have mercy on your soul.
 * <p>
 * Created by Jonathan Nobre Ferreira on 07/12/16.
 */

public final class ORPSingleton {

    private static ORPSingleton instance = null;

    private Map<Integer, Map<String, Object>> parametersMap;

    public synchronized static ORPSingleton getInstance() {
        if (instance == null) {
            instance = new ORPSingleton();
        }
        return instance;
    }

    private ORPSingleton() {
        this.parametersMap = Collections.synchronizedMap(new LinkedHashMap<Integer, Map<String, Object>>());
    }

    /**
     * As the method name says..you should add the origin class, or, the source activity.
     *
     * @param originActivity
     * @param parameters
     */
    public void addOriginActivity(ORPActivity originActivity, Map<String, Object> parameters) {
        Integer hashCode = originActivity.hashCode();
        Map<String, Object> recoveredParameters = this.parametersMap.get(hashCode);
        if (recoveredParameters == null) {
            recoveredParameters = new LinkedHashMap<>();
        }
        recoveredParameters.putAll(parameters);
        this.parametersMap.put(hashCode, recoveredParameters);
    }

    /***
     * Try to guess what this method do ? Well, yes, it will get the object passed from the origin
     * using the ORIGIN Activity hashCode, try to use the {#link {@link ORPActivity#getActivityCallerHashCode()}}
     * and the parameterKey that is associated with the object.
     *
     * @param activityHashCode
     * @param parameterKey
     * @return
     */
    public Object getParametersForOriginActivity(Integer activityHashCode, String parameterKey) throws ORPExceptions {
        Map<String, Object> parameters = this.parametersMap.get(activityHashCode);
        if (parameters != null && parameters.containsKey(parameterKey)) {
            return parameters.get(parameterKey);
        }
        throw new ORPExceptions("Parameter with key '" + parameterKey + "' not found");
    }

    /**
     * Will turn water into wine... How I wish, but this method will only remove all the
     * parameters sent to the source activity who was the hashCode passed in the parameter.
     *
     * @param activityHashCode
     */
    public void clearCurrentActivityParameters(Integer activityHashCode) {
        if (activityHashCode == null) return;
        this.parametersMap.remove(activityHashCode);
    }
}
