package me.sweetll.v2ex.Authentication;

/**
 * Created by sweet on 2/2/16.
 */
public class V2EXServerAuthenticate implements ServerAuthenticate {
    @Override
    public String userSignUp(String user, String pass, String email, String secureCode, String authType) throws Exception {
        return "Sign Up OK";
    }

    @Override
    public String userSignin(String user, String pass, String authType) throws Exception {
        return "Sign In OK";
    }
}
