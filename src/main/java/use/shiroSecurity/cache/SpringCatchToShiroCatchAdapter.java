package use.shiroSecurity.cache;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.SimplePrincipalCollection;

import use.cache.springDataRedis.initial.CacheManagerOperation;
import use.cache.springDataRedis.initial.SpringRedisFactoryUtil;
import use.cache.springDataShiro.memStorage.IMemoryStorage;
import use.cache.springDataShiro.memStorage.MemoryStorageFactory;

/**
 * shiro cache to spring cache 适配器，
 * 用于缓存对象转换
 * 项目名称:use.shiroSecurity
 * 类型名称:SpringCatchToShiroCatchAdapter
 * 类型描述:
 * 作者:wx
 * 创建时间:2018年6月10日
 * @version:
 */
@SuppressWarnings({"rawtypes"})
public class SpringCatchToShiroCatchAdapter implements Cache{

	
	private IMemoryStorage fMem = null;

	public String getKeyPrefix() {
		return SpringRedisFactoryUtil.getConfigure().getShiroPreFix();
	}


	private String getCacheName(int index)
	{
		switch(index)
		{
			case 0: return CacheManagerOperation.defaultCache;
			case 1: return CacheManagerOperation.secondCache;
			case 2: return CacheManagerOperation.limitsCache;
			default: return CacheManagerOperation.defaultCache;
		}
	}
	public IMemoryStorage getSpringCache()
	{
		if(fMem == null)
		{
			String key = getCacheName(SpringRedisFactoryUtil.getConfigure().getShiroDataBaseIndex());
			org.springframework.cache.Cache fCache = CacheManagerOperation.getCacheManager().getCache(key);
			fMem = MemoryStorageFactory.getMemoryStorage(fCache);
		}
		
		return fMem;
	}
	
	public Object get(Object key) throws CacheException {
		return getSpringCache().get(key);
	}

	public Object put(Object key, Object value) throws CacheException {
		return getSpringCache().put(key, value);
	}

	public Object remove(Object key) throws CacheException {
		if(key instanceof SimplePrincipalCollection)
		{
			return key;
		}
		return getSpringCache().remove(key);
	}

	public void clear() throws CacheException {
		getSpringCache().clear();
	}

	public int size() {
		return getSpringCache().size();
	}
	public Set keys(String key) {
		return getSpringCache().keys(key);
	}
	public Collection values(String key) {
		return getSpringCache().values(key);
	}
	public Set keys() {
		return keys(getKeyPrefix());
	}
	public Collection values() {
		return values(getKeyPrefix());
	}
	public Object set(Object key,Object value,int expire) throws Exception
	{
		return getSpringCache().set(key, value, expire);
	}
	

}
