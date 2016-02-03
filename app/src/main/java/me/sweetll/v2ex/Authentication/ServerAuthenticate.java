package me.sweetll.v2ex.Authentication;

/**
 * Created by sweet on 2/2/16.
 */
public interface ServerAuthenticate {
    public String userSignUp(String user, String pass, String email, String secureCode, String authType) throws Exception;
    public String userSignIn(String user, String pass, String authType) throws Exception;
}
