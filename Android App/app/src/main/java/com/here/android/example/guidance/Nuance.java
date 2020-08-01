package com.here.android.example.guidance;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.nuance.speechkit.Session;
import com.nuance.speechkit.Transaction;
import com.nuance.speechkit.TransactionException;

public class Nuance {

    private final String TAG = getClass().getSimpleName();
    private boolean isInitialized;
    private Session session;

    public Nuance(Context context) {
        Bundle bundle = null;
        String appKey = "";
        String appServer = "";
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            bundle = ai.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Exception: " + e.toString());
        }
        if (bundle != null) {
            try {
                appKey = bundle.getString("nuance.app.key");
                appServer = String.format("nmsps://%s@%s:%s",
                        bundle.getString("nuance.app.id"),
                        bundle.getString("nuance.app.host"),
                        String.valueOf(bundle.getInt("nuance.app.port", 443)));
            } catch (NullPointerException e) {
                Log.e(TAG, "Can't access Nuance credentials from AndroidManifest.xml: " + e.toString());
            }
        } else {
            Log.e(TAG, "Can't access Nuance credentials from AndroidManifest.xml");
        }
        // Create session for Nuance TTS engine
        try {
            session = Session.Factory.session(context, Uri.parse(appServer), appKey);
            isInitialized = true;
        } catch (IllegalArgumentException e) {
            isInitialized = false;
        }
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void speak(String text) {
        // set options
        Transaction.Options options = new Transaction.Options();
        // play text
        session.speakString(text, options, new Transaction.Listener() {
            @Override
            public void onSuccess(Transaction transaction, String suggestion) {
                super.onSuccess(transaction, suggestion);
                Log.i(TAG, "Success!");
            }

            @Override
            public void onError(Transaction transaction, String suggestion, TransactionException e) {
                super.onError(transaction, suggestion, e);
                Log.e(TAG, "Error: " + e.toString());
            }
        });
    }

}
