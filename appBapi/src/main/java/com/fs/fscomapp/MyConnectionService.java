package com.fs.fscomapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telecom.ConnectionRequest;
import android.telecom.PhoneAccountHandle;

import androidx.annotation.Nullable;

public class MyConnectionService extends Service {
  //  onCreateOutgoingConnection(PhoneAccountHandle, ConnectionRequest)

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
