package com.example.elessar1992.friendsrating.Notifications;

/**
 * Created by elessar1992 on 6/20/19.
 */

public class RegistrationToken
{
    String registrationToken;

    public RegistrationToken(String registrationToken)
    {
        this.registrationToken = registrationToken;
    }

    public RegistrationToken()
    {

    }

    public String getRegistrationToken()
    {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken)
    {
        this.registrationToken = registrationToken;
    }
}
