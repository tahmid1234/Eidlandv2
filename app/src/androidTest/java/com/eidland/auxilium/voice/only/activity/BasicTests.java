package com.eidland.auxilium.voice.only.activity;


import com.eidland.auxilium.voice.only.R;
import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

public class BasicTests {

    private Solo solo;

    public BasicTests() {
    }

    public String getString(int resId) {
        return solo.getString(resId);
    }

    public void testJoinAsBroadcaster() throws Exception {
        String AUTO_TEST_CHANNEL_NAME = "auto_test_" + System.currentTimeMillis();

        solo.unlockScreen();

        solo.assertCurrentActivity("Expected MainActivity activity", "MainActivity");
        solo.clearEditText(0);
        solo.enterText(0, AUTO_TEST_CHANNEL_NAME);
        solo.waitForText(AUTO_TEST_CHANNEL_NAME, 1, 2000L);

        solo.clickOnView(solo.getView(R.id.button_join));

        solo.waitForDialogToOpen(1000);

        solo.clickOnButton(getString(R.string.label_broadcaster));

        String targetActivity = LiveRoomActivity.class.getSimpleName();

        solo.waitForLogMessage("onJoinChannelSuccess " + AUTO_TEST_CHANNEL_NAME, JOIN_CHANNEL_SUCCESS_THRESHOLD + 1000);

        solo.assertCurrentActivity("Expected " + targetActivity + " activity", targetActivity);

        long firstRemoteAudioTs = System.currentTimeMillis();
        solo.waitForLogMessage("volume: ", FIRST_REMOTE_AUDIO_RECEIVED_THRESHOLD + 500);

        solo.waitForCondition(new Condition() { // stay at the channel for some time
            @Override
            public boolean isSatisfied() {
                return false;
            }
        }, FIRST_REMOTE_AUDIO_RECEIVED_THRESHOLD);
    }

    private static final int FIRST_REMOTE_AUDIO_RECEIVED_THRESHOLD = 50 * 1000;
    private static final int JOIN_CHANNEL_SUCCESS_THRESHOLD = 5000;

    public void testJoinAsAudience() throws Exception {
        String AUTO_TEST_CHANNEL_NAME = "for_auto_test";

        solo.unlockScreen();

        solo.assertCurrentActivity("Expected MainActivity activity", "MainActivity");
        solo.clearEditText(0);
        solo.enterText(0, AUTO_TEST_CHANNEL_NAME);
        solo.waitForText(AUTO_TEST_CHANNEL_NAME, 1, 2000L);

        solo.clickOnView(solo.getView(R.id.button_join));

        solo.waitForDialogToOpen(1000);

        solo.clickOnButton(getString(R.string.label_audience));

        String targetActivity = LiveRoomActivity.class.getSimpleName();

        solo.waitForLogMessage("onJoinChannelSuccess " + AUTO_TEST_CHANNEL_NAME, JOIN_CHANNEL_SUCCESS_THRESHOLD);

        solo.assertCurrentActivity("Expected " + targetActivity + " activity", targetActivity);

        long firstRemoteAudioTs = System.currentTimeMillis();
        solo.waitForLogMessage("volume: ", FIRST_REMOTE_AUDIO_RECEIVED_THRESHOLD + 500);

        solo.waitForCondition(new Condition() { // stay at the channel for some time
            @Override
            public boolean isSatisfied() {
                return false;
            }
        }, FIRST_REMOTE_AUDIO_RECEIVED_THRESHOLD);
    }
}
