package me.sweetll.v2ex.Utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import me.sweetll.v2ex.MainActivity;

/**
 * Created by sweet on 15-9-5.
 */
public class GlobalClass {
    public static final int REQUEST_SIGN_IN = 10;
    public static final int REQUEST_SIGN_UP = 20;

    public static final int RESULT_SUCCESS = 10;
    public static final int RESULT_FAILURE = 20;

    public static RequestQueue queue;

    public static void Initialize(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static RequestQueue getQueue() {
        return queue;
    }
}
