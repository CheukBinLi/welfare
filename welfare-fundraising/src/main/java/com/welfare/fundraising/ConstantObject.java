package com.welfare.fundraising;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.cheuks.bin.original.common.cache.redis.RedisExcecption;
import com.cheuks.bin.original.common.cache.redis.RedisFactory;
import com.cheuks.bin.original.common.cache.redis.RedisLua;
import com.cheuks.bin.original.common.util.Scan;
import com.cheuks.bin.original.common.util.SoftConcurrentHashMap;
import com.common.api.weixin.js.JsSdkSignUtil;
import com.common.api.weixin.js.JsSdkTicket;
import com.common.api.weixin.token.AccessTokenUtil;

@Component
public class ConstantObject {

	public static RedisFactory redisFactory;

	public static FreeMarkerViewResolver freeMarkerViewResolver;

	public static DefaultListableBeanFactory defaultListableBeanFactory;

	public static RedisLua redisLua;

	public static Scan scan;

	private static Object isDevelop;

	private SoftConcurrentHashMap<String, Map<String, String>> cache = new SoftConcurrentHashMap<String, Map<String, String>>();

	private ReentrantLock refreshSignLock = new ReentrantLock();
	private ReentrantLock refreshTokenLock = new ReentrantLock();

	private static ConstantObject newInstance = new ConstantObject();

	public static ConstantObject newInstance() {
		return newInstance;
	}

	@PostConstruct
	private void init() {
		// 装载lua
		try {
			redisLua.initLoader(scan.getResource("lua.*$lua").toArray(new String[0]));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private String getfreeMarkerViewResolverAttributes(String key) {
		Map<String, Object> attrs = freeMarkerViewResolver.getAttributesMap();
		Object attr = attrs.get(key);
		return null == attr ? null : attr.toString();
	}

	public String getAppId() {
		return getfreeMarkerViewResolverAttributes("weixinAppId");
	}

	public String getAppSecret() {
		return getfreeMarkerViewResolverAttributes("weixinAppSecret");
	}

	public String getMchId() {
		return getfreeMarkerViewResolverAttributes("weixinMchId");
	}

	public boolean isDevelop() {
		if (null == isDevelop) {
			isDevelop = getfreeMarkerViewResolverAttributes("develop");
			if (null == isDevelop) {
				isDevelop = false;
			} else
				isDevelop = Boolean.valueOf(isDevelop.toString());
		}
		return (Boolean) isDevelop;
	}

	public Map<String, String> getSign(String url) throws Throwable {
		return getSign(url, 7200, false);
	}

	public String getAipKey() {
		return getfreeMarkerViewResolverAttributes("weixinAipKey");
	}

	/***
	 * 
	 * @param url
	 *            当前URL
	 * @param expire
	 *            时效
	 * @param oneTime
	 *            是否一次性
	 * @return
	 * @throws Throwable
	 */
	public Map<String, String> getSign(String url, long expire, boolean oneTime) throws Throwable {
		Map<String, String> sign = null;
		String ticket;
		while (null == sign) {
			sign = cache.get(url);
			if (null == sign || !oneTime) {
				sign = redisFactory.getMap(url);
				cache.put(url, sign);
			}
			if (oneTime || null == sign || sign.size() < 1 || Long.valueOf(sign.get("expireAt")) <= System.currentTimeMillis()) {
				if (refreshSignLock.tryLock()) {
					try {
						// sign = redisFactory.getMap(url);
						ticket = getTicket();
						// 生成签名
						sign = JsSdkSignUtil.sign(ticket, url);
						sign.put("expireAt", Long.toString(System.currentTimeMillis() + (expire * 1000)));
						if (!oneTime) {
							synchronized (cache) {
								cache.put(url, sign);
							}
							redisFactory.setMap(url, sign);
							redisFactory.expire(url, (int) (expire));
						}
					} finally {
						refreshSignLock.unlock();
					}
					return sign;
				}
				Thread.sleep(500);
			}
		}
		return sign;
	}

	public String getTicket() throws RedisExcecption, InterruptedException {
		return getToken("ticket");
	}

	public String getApiAccessToken() throws RedisExcecption, InterruptedException {
		return getToken("apiAccessToken");
	}

	public String getToken(String key) throws RedisExcecption, InterruptedException {
		Map<String, String> token = null;
		int count = 3;
		while (count-- > 0 && null == token) {
			if (null != (token = getToken()))
				break;
			Thread.sleep(300);
		}
		return null == token ? null : token.get(key);
	}

	public Map<String, String> getToken() throws RedisExcecption {
		String apiAccessToken;
		String ticket;
		Map<String, String> token = cache.get("token");
		if (null == token) {
			token = redisFactory.getMap("token");
			cache.put("token", token);
		}
		if (null == token || token.size() < 1 || Long.valueOf(token.get("expireAt")) <= System.currentTimeMillis()) {
			if (refreshTokenLock.tryLock()) {
				try {
					apiAccessToken = AccessTokenUtil.getAccessToken(getAppId(), getAppSecret());
					ticket = JsSdkTicket.getJsSdkTicket(apiAccessToken);
					token.put("apiAccessToken", apiAccessToken);
					token.put("ticket", ticket);
					token.put("expireAt", Long.toString(System.currentTimeMillis() + 7200000));
					synchronized (cache) {
						cache.put("token", token);
					}
					redisFactory.setMap("token", token);
					redisFactory.expire("token", 7100);
				} finally {
					refreshTokenLock.unlock();
				}
			}
		}
		return token;
	}

	@Autowired
	public ConstantObject setRedisFactory(RedisFactory redisFactory) {
		ConstantObject.redisFactory = redisFactory;
		return this;
	}

	@Autowired
	public ConstantObject setFreeMarkerViewResolver(FreeMarkerViewResolver freeMarkerViewResolver) {
		ConstantObject.freeMarkerViewResolver = freeMarkerViewResolver;
		return this;
	}

	@Autowired
	public ConstantObject setDefaultListableBeanFactory(DefaultListableBeanFactory defaultListableBeanFactory) {
		ConstantObject.defaultListableBeanFactory = defaultListableBeanFactory;
		return this;
	}

	@Autowired
	public ConstantObject setRedisLua(RedisLua redisLua) {
		ConstantObject.redisLua = redisLua;
		return this;
	}

	@Autowired
	public ConstantObject setScan(Scan scan) {
		ConstantObject.scan = scan;
		return this;
	}

}
