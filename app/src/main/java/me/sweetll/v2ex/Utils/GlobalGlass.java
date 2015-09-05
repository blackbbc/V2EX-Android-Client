package me.sweetll.v2ex.Utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import me.sweetll.v2ex.MainActivity;

/**
 * Created by sweet on 15-9-5.
 */
public class GlobalGlass {
    public static RequestQueue queue;

    public static void Initialize(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static RequestQueue getQueue() {
        return queue;
    }
}
