package com.kingh.core.timer.task;

import com.kingh.core.bundle.BundleManagerFace;

import java.util.TimerTask;

public class ReloadExtLibTimerTask extends TimerTask {

    @Override
    public void run() {
        BundleManagerFace.buildBundle();
    }
}
