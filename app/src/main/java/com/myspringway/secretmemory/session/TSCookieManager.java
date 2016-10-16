package com.myspringway.secretmemory.session;

import android.content.Context;

import com.myspringway.secretmemory.helper.SharedPreferenceHelper;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.util.List;
import java.util.Map;


/**
 * Created by yoontaesup on 15. 9. 18..
 */
public class TSCookieManager extends CookieManager {

    public static final String SESSION_NAME = "_firebase_secretmemory_session"; //not work

    Context context;
    public TSCookieManager(Context context) {
        this.context = context;
        super.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    }

    @Override
    public void put(URI uri, Map<String, List<String>> stringListMap) throws IOException {
        super.put(uri, stringListMap);
        if (stringListMap != null && stringListMap.get("Set-Cookie") != null) {
            for (String string : stringListMap.get("Set-Cookie")) {
                String[] cookies = string.split(";");
                for (int i = 0; i < cookies.length; i++) {
                    String[] cookie = cookies[i].split("=", 2);
                    if(cookie[0].equals(SESSION_NAME)) {
                        if(cookie.length > 1) {
                            SharedPreferenceHelper.setValue(this.context, cookie[0], cookie[1]);
                        }
                    }
                }
            }
        }
    }
}
