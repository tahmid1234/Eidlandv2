package com.eidland.auxilium.voice.only;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.eidland.auxilium.voice.only.model.CurrentUserSettings;
import com.eidland.auxilium.voice.only.model.WorkerThread;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import static android.content.ContentValues.TAG;

public class AGApplication extends Application {

    private WorkerThread mWorkerThread;
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;

    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(getApplicationContext());
            mWorkerThread.start();

            mWorkerThread.waitForReady();
        }
    }

    public synchronized WorkerThread getWorkerThread() {
        if (mWorkerThread == null) {
            initWorkerThread();
        } else return mWorkerThread;

        return mWorkerThread;
    }

    public synchronized void deInitWorkerThread() {
        mWorkerThread.exit();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerThread = null;
    }

    public static final CurrentUserSettings mAudioSettings = new CurrentUserSettings();

}
