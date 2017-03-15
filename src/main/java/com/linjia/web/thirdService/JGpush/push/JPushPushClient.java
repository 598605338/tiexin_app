package com.linjia.web.thirdService.JGpush.push;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.linjia.web.thirdService.JGpush.constant.DeviceConstant;
import com.linjia.web.thirdService.JGpush.constant.ResultConstant;
import com.linjia.web.thirdService.JGpush.model.PushModel;
import com.linjia.web.thirdService.JGpush.model.UserPushToken;
import com.linjia.web.thirdService.JGpush.constant.Configuration;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author xiangshouyi
 *
 */
public class JPushPushClient implements PushClient {

    private Logger logger = Logger.getLogger(JPushPushClient.class);
    private static final String APP_KEY= Configuration.genApiKey("jpush_app_key");
    private static final String MASTER_SECRET=Configuration.genApiKey("jpush_master_secret");
    private static final String MESSAGE_TITLE = "邻家便利店";

    private static JPushPushClient instance = new JPushPushClient();
    public static JPushPushClient getInstance(){
        return instance;
    }

    @Override
    public int push(UserPushToken userDevice, PushModel pushModel, boolean mute) {
        if(userDevice==null || StringUtils.isEmpty(userDevice.getJpushToken())){
            logger.warn("push failed : No Device Token found");
            return ResultConstant.OP_FAIL;
        }
        JPushClient client = new JPushClient(MASTER_SECRET, APP_KEY);
        PushPayload payload = null;
        HashMap<String,String> extraData = new HashMap<String,String>();
        extraData.put("type", pushModel.getType());
        extraData.put("orderSource", pushModel.getOrderSource());
        boolean isTestDev = Configuration.isTestDev();
        if(userDevice.getOs() == DeviceConstant.ANDROID){
            payload = PushPayload
                    .newBuilder()
                    .setPlatform(Platform.android())
                    .setAudience(Audience.registrationId(userDevice.getJpushToken()))
                    .setMessage(Message.content(pushModel.getType()))
                    .setNotification(Notification.android(pushModel.getMessage(),MESSAGE_TITLE, extraData))
                    .build();
        }else {
            payload = PushPayload
                    .newBuilder()
                    .setPlatform(Platform.ios())
                        .setAudience(Audience.registrationId(userDevice.getJpushToken()))
                    .setMessage(Message.content(pushModel.getMessage()))
                    .setNotification(Notification
                            .newBuilder().addPlatformNotification(IosNotification
                                    .newBuilder()
                                    .setAlert(pushModel.getMessage())
                                    .setBadge(1)
                                    .setSound("default.mp3")
                                    .addExtras(extraData)
                                    .build())
                            .build())
                    .setOptions(Options.newBuilder().setApnsProduction(!isTestDev).build())
                    .build();
        }
        System.out.println(payload.toJSON());
        try {
            PushResult pushResult = client.sendPush(payload);
            if(!pushResult.isResultOK()) {
                logger.warn("push failed :"+pushResult);
                return ResultConstant.OP_FAIL;
            }
            return ResultConstant.OP_SUCC;
        } catch (APIConnectionException e) {
            logger.warn("push fail 1", e);

            e.printStackTrace();
        } catch (APIRequestException e) {
            logger.warn("push fail 2", e);
            e.printStackTrace();
        }
        return ResultConstant.OP_FAIL;
    }
    @Override
    public int push(int platformId, PushModel pushModel, boolean mute) {
        JPushClient client = new JPushClient(MASTER_SECRET, APP_KEY);
        PushPayload payload = null;
        HashMap<String,String> extraData = new HashMap<String,String>();
        extraData.put("type", pushModel.getType());
        extraData.put("orderSource", pushModel.getOrderSource());
        boolean isTestDev = Configuration.isTestDev();
        if(platformId == DeviceConstant.ANDROID){
            payload = PushPayload
                    .newBuilder()
                    .setPlatform(Platform.android())
                    .setAudience(Audience.all())
                    .setMessage(Message.content(pushModel.getType()))
                    .setNotification(Notification.android(pushModel.getMessage(), MESSAGE_TITLE, extraData))
                    .build();
        }else if(platformId == DeviceConstant.IOS){//IOS
            payload = PushPayload
                    .newBuilder()
                    .setPlatform(Platform.ios())
                    .setAudience(Audience.all())
                    .setMessage(Message.content(pushModel.getMessage()))
                    .setNotification(Notification
                            .newBuilder().addPlatformNotification(IosNotification
                                    .newBuilder()
                                    .setAlert(pushModel.getMessage())
                                    .setBadge(1)
                                    .setSound("default.mp3")
                                    .addExtras(extraData)
                                    .build())
                            .build())
                    .setOptions(Options.newBuilder().setApnsProduction(!isTestDev).build())
                    .build();
        }else if(platformId == 3){//所有平台
            payload = PushPayload.newBuilder()
                    .setPlatform(Platform.android_ios())
                    .setAudience(Audience.all())
                    .setMessage(Message.content(pushModel.getMessage()))
                    .setNotification(Notification.newBuilder()
                            .setAlert(pushModel.getMessage())
                            .addPlatformNotification(AndroidNotification.newBuilder()
                                    .setTitle(MESSAGE_TITLE)
                                    .addExtras(extraData).build())
                            .addPlatformNotification(IosNotification.newBuilder()
                                    .incrBadge(1)
                                    .setSound("default.mp3")
                                    .addExtras(extraData).build())
                            .build())
                    .build();
        }

        try {
           PushResult pr = client.sendPush(payload);

            //为了方便测试，线下也发一遍
            if(!Configuration.isTestDev()) {
                payload.resetOptionsApnsProduction(false);
                PushResult pr2 = client.sendPush(payload);
            }

           if(pr.isResultOK()){
               return ResultConstant.OP_SUCC;
           }else{
               logger.warn("push fail");
               return ResultConstant.OP_FAIL;
           }

        } catch (APIConnectionException e) {
            logger.warn("push fail 1", e);
            e.printStackTrace();
        } catch (APIRequestException e) {
            logger.warn("push fail 2", e);
            e.printStackTrace();
        }
        return ResultConstant.OP_FAIL;
    }

    /**
     *
     * @param registrationIds:设备标识。一次推送最多 1000 个,需要批量发送
     * @param pushModel
     * @param mute
     * @return
     */
    @Override
    public int push(List<String> registrationIds, PushModel pushModel, boolean mute) {
        JPushClient client = new JPushClient(MASTER_SECRET, APP_KEY);
        PushPayload payload = null;
        HashMap<String,String> extraData = new HashMap<String,String>();
        extraData.put("type", pushModel.getType());
        extraData.put("orderSource", pushModel.getOrderSource());
        int limit = 1000;
        if(registrationIds.size() >0){//同时发送给多个用户
            int registrationIdsSize = registrationIds.size();
            do {
                List<String> setRegistrationIds = null;
                if(registrationIds.size() >limit){
                    setRegistrationIds = registrationIds.subList(0,limit);
                }else{
                    setRegistrationIds = registrationIds;
                }
                payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.registrationId(setRegistrationIds))
                .setMessage(Message.content(pushModel.getType()))
                 .setNotification(Notification.android(pushModel.getMessage(),MESSAGE_TITLE, extraData))
                        .build();
                try {
                    PushResult pr = client.sendPush(payload);
                    if(pr.isResultOK()){
                        return ResultConstant.OP_SUCC;
                    }else{
                        logger.warn("push fail");
                        return ResultConstant.OP_FAIL;
                    }
                } catch (APIConnectionException e) {
                    logger.warn("push fail 1", e);
                    e.printStackTrace();
                } catch (APIRequestException e) {
                    logger.warn("push fail 2", e);
                    e.printStackTrace();
                }
                registrationIdsSize = registrationIds.size()-limit;
                registrationIds = registrationIds.subList(registrationIdsSize,registrationIds.size());
            }while(registrationIdsSize>0);

        }

        return ResultConstant.OP_FAIL;
    }
    
}
