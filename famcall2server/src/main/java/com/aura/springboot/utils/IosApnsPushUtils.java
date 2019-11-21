package com.aura.springboot.utils;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.aura.springboot.utils.apns.IApnsService;
import com.aura.springboot.utils.apns.impl.ApnsServiceImpl;
import com.aura.springboot.utils.apns.model.ApnsConfig;
import com.aura.springboot.utils.apns.model.Payload;

/**
 * 
 * @author Micheal.chen
 *
 */
public class IosApnsPushUtils {

	private static Logger log = LoggerFactory.getLogger(IosApnsPushUtils.class);
	private final String TAG = IosApnsPushUtils.class.getSimpleName().toString();
//	private static Context context;
	private IApnsService apnsService;
//	private UserDao userDao;

	public static IosApnsPushUtils apnsPushUtils;

	private IosApnsPushUtils() {

	}

	public static IosApnsPushUtils getSingleInstance() {
		if (apnsPushUtils == null) {
			apnsPushUtils = new IosApnsPushUtils();
		}

		return apnsPushUtils;
	}

	public void init() {
//		this.context = cxt;
//		userDao = BaseApplication.getBaseApplication().getDaoSession().getUserDao();
		getApnsService();
	}

	private IApnsService getApnsService() {
		if (apnsService == null) {
			ApnsConfig config = new ApnsConfig();
//			InputStream is = Apns4jDemo.class.getClassLoader().getResourceAsStream("Push_Dev.p12");
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			InputStream is = null;
			try {
				Resource resources = resolver.getResource("pushKitTest.p12");
//				is = context.getAssets().open("SmartConnect.bks");
//				Resource resource = new ClassPathResource("FamCall2PushCer20190103.p12");
//				File file = resource.getFile();
				is = resources.getInputStream();
//				is = new FileInputStream(file);
				log.info("-----------is----->" + is);
//				is = context.getAssets().open("FamCall2PushCer20190103.p12");
//				is = getClassLoader().getResourceAsStream("Push_Dev.p12");
				// is = getAssets().open("dev.bks");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null == is) {
				log.info("-------is null-------");
				return null;
			}
			config.setKeyStore(is);
			config.setDevEnv(true);// micheal add 这里的开关关闭为正式上线证书和网关选择
			config.setPassword("123456");
			config.setPoolSize(30);
			apnsService = ApnsServiceImpl.createInstance(config);
		}
		return apnsService;
	}

	/**
	 * 发送消息
	 *
	 * ringTyte 2; /// setSound("default");;// 3; /// setSound("opening.mp3");//
	 */
	public void pushMsgiOS(String token, String callInNum, int news) {
		// String token =
		// "cacf02b6655bf5235f2afe15fd323b05ccde3ab3c5b120c2dd0f676f0321c522";
		IApnsService service = getApnsService();
		Payload payload = new Payload();
		// "New incoming call"
		log.info("-----------callInNum----->" + callInNum);
		payload.setAlert(callInNum);
		payload.setSound("default");
		// log.info("--------消息数量-------- ");

		payload.setBadge(news);
//		payload.addParam("callInNum", callInNum);

		long time = System.currentTimeMillis();
		payload.addParam("time", time);
		// PushNotification notification = new PushNotification();
		// notification.setToken(token);
		// notification.setPayload(payload);
		// notification.setId(324325);
		//
		// service.sendNotification(notification);
//	    Log.e(TAG,"pushMsgiOS:"+token+"   callInNum:"+callInNum);
		service.sendNotification(token, payload);
	}

	public void shutDownServer() {
		try {
			if (apnsService != null) {
				apnsService.shutdown();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
