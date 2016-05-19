package com.github.koushikr.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by koushikr on 19/05/16.
 */
public enum Status {

    FAILED(0), PROCESSED(1), PROCESSING(null);   //As they are in DB

    private static final Map<Integer, Status> lookup = new HashMap<Integer, Status>();    //Reverse map from code to ENUM

    static {
        for (Status s : EnumSet.allOf(Status.class))
            lookup.put(s.getCode(), s);
    }

    private Integer code = null;

    Status(Integer code) {
        this.code = code;
    }

    public static Status get(Integer code) {
        return lookup.get(code);
    }

    public Integer getCode() {
        return code;
    }

}
