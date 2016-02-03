package me.sweetll.v2ex.Authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by sweet on 2/2/16.
 */
public class V2EXAuthenticatorService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        V2EXAuthenticator authenticator = new V2EXAuthenticator(this);
        return authenticator.getIBinder();
    }
}
