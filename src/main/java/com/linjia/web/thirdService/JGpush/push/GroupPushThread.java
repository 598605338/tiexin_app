package com.linjia.web.thirdService.JGpush.push;

import com.linjia.web.thirdService.JGpush.model.PushModel;

import java.util.List;

/**
 * 
 * @author xiangshouyi
 *
 */
public class GroupPushThread extends Thread {
    private final List<String> registrationIds;
    private final PushModel pushModel;
    private final boolean mute;

    public GroupPushThread(List<String> registrationIds, PushModel pushModel,boolean mute){
        this.registrationIds = registrationIds;
        this.pushModel = pushModel;
        this.mute = mute;
    }

    @Override
    public void run() {
        int pushResult = PushClientFactory.push(registrationIds, pushModel, mute);

    }
}
