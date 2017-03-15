package com.linjia.web.thirdService.JGpush.push;

import com.linjia.web.thirdService.JGpush.model.PushModel;

/**
 * 
 * @author xiangshouyi
 *
 */
public class PlatformPushThread extends Thread {
    private final int platformId;
    private final PushModel pushModel;

    public PlatformPushThread(int platformId, PushModel pushModel){
        this.platformId = platformId;
        this.pushModel = pushModel;
    }

    @Override
    public void run() {
        int pushResult = PushClientFactory.push(platformId, pushModel, false);
    }
}
