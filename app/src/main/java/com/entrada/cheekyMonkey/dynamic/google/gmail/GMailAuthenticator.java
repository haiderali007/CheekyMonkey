package com.entrada.cheekyMonkey.dynamic.google.gmail;

/**
 * Created by ACER on 07/05/2017.
 */

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


class GMailAuthenticator extends Authenticator {
    String user;
    String pw;
    public GMailAuthenticator(String username, String password)
    {
        super();
        this.user = username;
        this.pw = password;
    }
    public PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(user, pw);
    }
}