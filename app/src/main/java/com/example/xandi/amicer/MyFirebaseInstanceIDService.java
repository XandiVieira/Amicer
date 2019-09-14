package com.example.xandi.amicer;

public class MyFirebaseInstanceIDService /*extends FirebaseInstanceIdService*/ {

    public static final String TOKEN_BROADCAST = "myfcmtokenbroadcast";

    /*@Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("myFirebaseId", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {
        SharePrefManager.getmInstance(getApplicationContext()).storeToken(token);
    }*/
}
