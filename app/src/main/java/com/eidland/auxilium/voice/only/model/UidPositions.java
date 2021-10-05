package com.eidland.auxilium.voice.only.model;

import android.content.Context;
import android.view.View;

import com.eidland.auxilium.voice.only.helper.PortraitAnimator;

public class UidPositions {
    public int uid;
    public int position;
    public boolean isfill = false;
    private PortraitAnimator mAnimator;

    public UidPositions(int uid, int position) {
        this.uid = uid;
        this.position = position;
    }

    public void initAnimator(Context context, View layer1, View layer2) {
        mAnimator = new PortraitAnimator(context, layer1, layer2);
    }

    public void setIsfill(boolean isfill) {
        this.isfill = isfill;
    }


    public void startAnimation() {
        if (mAnimator != null) {
            mAnimator.startAnimation();
        }
    }

    public void stopAnimation() {
        if (mAnimator != null) {
            mAnimator.forceStop();
        }
    }
}
