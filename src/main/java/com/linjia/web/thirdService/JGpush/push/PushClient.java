package com.linjia.web.thirdService.JGpush.push;

import com.linjia.web.thirdService.JGpush.model.PushModel;
import com.linjia.web.thirdService.JGpush.model.UserPushToken;

import java.util.List;

/**
 * 
 * @author xiangshouyi
 *
 */
public interface PushClient {

	int push(UserPushToken userDevice, PushModel pushModel, boolean mute);

	int push(int platformId, PushModel pushModel, boolean mute);

	int push(List<String> registrationIds, PushModel pushModel, boolean mute);
}
