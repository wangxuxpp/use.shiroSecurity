package use.shiroSecurity.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 项目名称:weixue
 * 类型名称:RedisCacheManager
 * 类型描述:	
 * 作者: wx
 * 创建时间:2017年8月11日下午12:43:57
 * 
 * 
 * 
 * @version1.0
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class RedisCacheManager implements CacheManager {

	private static final Logger logger = LoggerFactory
			.getLogger(RedisCacheManager.class);

	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		logger.debug("获取名称为: " + name + " 的Cache实例");
		Cache c = caches.get(name);
		if (c == null) {
			c = new ShiroCache();
			caches.put(name, c);
		}
		return c;
	}

}
