package me.sweetll.v2ex.Authentication;

/**
 * Created by sweet on 2/2/16.
 */
public class AccountGeneral {
    public static final String ACCOUNT_TYPE = "me.sweetll.v2ex";
    public static final String ACCOUNT_NAME = "V2EX";
    public static final String AUTH_TOKEN_TYPE = "FULL access";
    public static final String AUTH_TOKEN_TYPE_LABEL = "FULL access to v2ex account";

    public static final ServerAuthenticate mServerAuthenticate = new V2EXServerAuthenticate();
}
