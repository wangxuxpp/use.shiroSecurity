package use.shiroSecurity.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * shiro sesion操作对象，用户session的增删改查
 * 项目名称:weixue
 * 类型名称:ShiroSessionDAO
 * 类型描述:	
 * 作者: wx
 * 创建时间:2017年8月11日下午12:44:34
 * 
 * 
 * 
 * @version1.0
 */
public class ShiroSessionDAO extends AbstractSessionDAO {


	private static Logger logger = LoggerFactory.getLogger(ShiroSessionDAO.class);

	private SpringCatchToShiroCatchAdapter fCache = new SpringCatchToShiroCatchAdapter();
	private ShiroParameter shiroParameter = null;
	
	
	
	public SpringCatchToShiroCatchAdapter getfCache() {
		return fCache;
	}

	public void setfCache(SpringCatchToShiroCatchAdapter fCache) {
		this.fCache = fCache;
	}

	public ShiroParameter getShiroParameter() {
		return shiroParameter;
	}

	public void setShiroParameter(ShiroParameter shiroParameter) {
		this.shiroParameter = shiroParameter;
	}

	@Override
	public void update(Session session) throws UnknownSessionException {
		try
		{
			this.saveSession(session);
		}catch(Exception er)
		{
			throw new UnknownSessionException(er.getMessage());
		}
	}
	
	/**
	 * save session
	 * @param session
	 * @throws Exception 
	 */
	private void saveSession(Session session) throws Exception{
		if(session == null || session.getId() == null){
			logger.error("session or session id is null");
			return;
		}
		if(shiroParameter.getExpire()>0)
		{
			session.setTimeout(shiroParameter.getExpire()*1000);
		}
		fCache.set(getMemKey(session.getId()), session, shiroParameter.getExpire());
	}

	@Override
	public void delete(Session session) {
		if(session == null || session.getId() == null){
			logger.error("session or session id is null");
			return;
		}
		fCache.remove(getMemKey(session.getId()));

	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Session> getActiveSessions() {
		Set<Session> sessions = new HashSet<Session>();
		
		Set<byte[]> keys = fCache.keys(fCache.getKeyPrefix() + "*") ;
		if(keys != null && keys.size()>0){
			for(byte[] key:keys){
				Session s = (Session)fCache.get(key);
				sessions.add(s);
			}
		}
		
		return sessions;
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);  
        this.assignSessionId(session, sessionId);
        try {
			this.saveSession(session);
		} catch (Exception e) {
			throw new UnknownSessionException(e.getMessage());
		}
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		if(sessionId == null){
			logger.error("session id is null");
			return null;
		}
		Session s = (Session)fCache.get(getMemKey(sessionId));// SerializeUtils.deserialize(redisManager.get(this.getByteKey(sessionId)));
		return s;
	}
	

	
	private String getMemKey(Serializable sessionId){
		
		return fCache.getKeyPrefix() + sessionId;
	}
	
}
