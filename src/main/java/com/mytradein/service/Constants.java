package com.mytradein.service;

public class Constants {

    public static final int ITEMS_PER_PAGE = 10;

    public static final int TOKEN_VALIDITY_MIN = 30;

    public static final String REGISTRATION_SUBJECT = "Manager registration confirmation";

    public static final String REGISTRATION_MESSAGE = "Dear %s,\nCongratulation! You're registsred in our system as %s.\n Support team";

    public static final String REMINDER_SUBJECT = "Password reminder";

    public static final String REMINDER_MESSAGE = "Hi %s,\nPlease follow this link to recover your password %s\nLink is valid until %s";

    public static final String API_LOGIN = "extUser##";

    public static final String API_PASSWORD = "extUserPass!!";

    public static final String PATH_LOGIN = "/login";

    public static final String PATH_FAILED_LOGIN = "/login?error";

    public static final String PATH_LOGOUT = "/logout";

    public static final String PATH_REMINDER = "/remind/**";

    public static final String PATH_UNAUTHORIZED = "/unauthorized";

    public static final String PATH_DOLOGIN = "/dologin";

    public static final String PATH_DEFAULT_SUCCESS = "/offer/list";

    public static final String PATH_STYLES = "/styles/**";

    public static final String PATH_INIT = "/init";

    public static final String PATH_API = "/api/offer/**";

    public static final String PARAM_LOGIN = "username";

    public static final String PARAM_PASSWORD = "password";
}
