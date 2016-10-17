package com.myspringway.secretmemory.memory;

import com.myspringway.secretmemory.model.thirdparty.Pastor;

/**
 * Created by ehowlsla on 2016. 10. 17..
 */

public class CurrentChurch {
    private volatile  static CurrentChurch uniqueInstance;
    public Pastor pastor;

    public static CurrentChurch getInstance() {
        if (uniqueInstance == null) {
            synchronized (CurrentChurch.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new CurrentChurch();
                    uniqueInstance.pastor = new Pastor();
                }
            }
        }
        return uniqueInstance;
    }
}
