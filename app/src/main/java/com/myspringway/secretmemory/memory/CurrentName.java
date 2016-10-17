package com.myspringway.secretmemory.memory;

import com.myspringway.secretmemory.model.thirdparty.Name;

/**
 * Created by ehowlsla on 2016. 10. 17..
 */

public class CurrentName {
    private volatile  static CurrentName uniqueInstance;
    public Name name;

    public static CurrentName getInstance() {
        if (uniqueInstance == null) {
            synchronized (CurrentName.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new CurrentName();
                    uniqueInstance.name = new Name();
                }
            }
        }
        return uniqueInstance;
    }
}
