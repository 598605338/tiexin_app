package com.linjia.web.thirdService.JGpush.push;

import cn.jpush.api.push.model.audience.AudienceTarget;

import com.linjia.tools.JSONUtil;
import com.linjia.web.model.JgPushCustomer;
import com.linjia.web.thirdService.JGpush.constant.DeviceConstant;
import com.linjia.web.thirdService.JGpush.constant.PushConstant;
import com.linjia.web.thirdService.JGpush.constant.ResultConstant;
import com.linjia.web.thirdService.JGpush.model.PushModel;
import com.linjia.web.thirdService.JGpush.model.UserPushToken;
import com.linjia.web.thirdService.JGpush.service.JgPushDeviceServiceImpl;

import java.util.List;

/**
 * 
 * @author xiangshouyi
 *
 */
public class PushClientFactory {

    private final int MAX_IOS_LEN = 256;
    /**
     * factory 根据不同客户端选择不同的pushclient
     */
	public static int push(UserPushToken userDevice , PushModel pushModel, boolean mute){
		PushClient  pushClient=null;
		if(userDevice.getOs()== DeviceConstant.ANDROID){
			pushClient= JPushPushClient.getInstance();
		}else if(userDevice.getOs()== DeviceConstant.IOS){
			pushClient=JPushPushClient.getInstance();
		}else{
			return ResultConstant.OP_FAIL;
		}
		int pushResult = pushClient.push(userDevice, pushModel,mute);
        int retries = 2;
        while (pushResult!=ResultConstant.OP_SUCC && retries>0){
            pushResult = pushClient.push(userDevice, pushModel,mute);
            retries --;
        }
        return pushResult;
	}
	
    //根据平台群发
    public static int push(int platformId, PushModel pushModel,boolean mute){
        PushClient  pushClient=null;
        if(platformId == DeviceConstant.ANDROID){
            pushClient= JPushPushClient.getInstance();
        }else if(platformId==DeviceConstant.IOS){
            pushClient=JPushPushClient.getInstance();
        }else{
            pushClient= JPushPushClient.getInstance();
        }
        int pushResult = pushClient.push(platformId, pushModel,mute);
        int retries = 2;
        while (pushResult!=ResultConstant.OP_SUCC && retries>0){
            pushResult = pushClient.push(platformId, pushModel,mute);
            retries--;
        }
        return pushResult;
    }

    //根据用户角色群发
    public static int push(List<String> registrationIds, PushModel pushModel,boolean mute){
        PushClient  pushClient=null;
        pushClient= JPushPushClient.getInstance();
        int pushResult = pushClient.push(registrationIds, pushModel,mute);
        int retries = 2;
        while (pushResult!=ResultConstant.OP_SUCC && retries>0){
            pushResult = pushClient.push(registrationIds, pushModel,mute);
            retries--;
        }
        return pushResult;
    }
    
    public PushModel trimForIOS(PushModel pushModel){
        String pushString = JSONUtil.toJson(pushModel);
        int diff = pushString.getBytes().length - MAX_IOS_LEN;
        String content = pushModel.getMessage();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<content.length();i++){
            char c = content.charAt(i);
            sb.append(c);
            if(sb.toString().getBytes().length>=MAX_IOS_LEN){
                sb.deleteCharAt(i);
                break;
            }
        }
        pushModel.setMessage(sb.toString());
        return pushModel;
    }


    public static void main(String[] args) {
    	JgPushDeviceServiceImpl jpush=new JgPushDeviceServiceImpl();
    	List<JgPushCustomer> list=jpush.selectJgPush("1005","");
    	JgPushCustomer jgPushCustomer=list.get(0);
    	System.out.println(jgPushCustomer);
        PushClientFactory factory = new PushClientFactory();
        PushModel model =new PushModel();
        model.setType("1");
        model.setMessage("[邻家便利店]你好你好你好你好，我很好我很好");
        String strPush = JSONUtil.toJson(model);
        System.out.println(strPush.getBytes().length);

//      PushModel model = JsonUtil.fromJson(s, PushModel.class);
        System.out.println(model);
        model =factory .trimForIOS(model);
        System.out.println(model);

        UserPushToken pushToken = new UserPushToken();
        pushToken.setJpushToken("aaa");
        pushToken.setOs(DeviceConstant.ANDROID);
        pushToken.setUserId(10000000);
        PushClientFactory.push(pushToken,model,false);
        AudienceTarget a;
        
        
        
        
        
        
        
        
        
        
        
        
        
/*
        UserPushToken iostoken=new UserPushToken();
        iostoken.setOs(2);
        iostoken.setToken("c747f9e82c9a6461bf1c6fb5ba0f2509");

        UserPushToken androidToken=new UserPushToken();
        androidToken.setOs(1);
        androidToken.setToken("bcfd40b023a8a5994fe1e36f9de7ed89");

        PushModel model=new PushModel();
        model.setOpLog("这是要显示的东西");
        model.setOs(99);
        model.setPayload(JsonUtil.toJson(new RemindPayload(100)));

        System.out.println(PushClientFactory.push(iostoken, model,false));
        */
//        System.out.println(client.push(androidToken,model));

    }
}
