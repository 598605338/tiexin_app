package com.linjia.web.thirdService.JGpush.push;

import com.linjia.web.thirdService.JGpush.model.PushModel;
import com.linjia.web.thirdService.JGpush.model.UserPushToken;

/**
 * 
 * @author xiangshouyi
 *
 */
public class P2PPushThread extends Thread {

    private final PushModel pushModel;
    private final UserPushToken userDevice;
    private final boolean mute;

    public P2PPushThread(UserPushToken userDevice, PushModel pushModel, boolean mute){
        this.userDevice = userDevice;
        this.pushModel = pushModel;
        this.mute = mute;
    }
    @Override
    public void run() {
        PushClientFactory.push(userDevice,pushModel,mute);
    }
}
