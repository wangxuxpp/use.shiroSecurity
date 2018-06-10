package use.shiroSecurity.cache;

/**
 * 
 * 项目名称:weixue
 * 类型名称:ShiroParameter
 * 类型描述:	
 * 作者: wx
 * 创建时间:2017年8月11日下午12:44:26
 * 
 * 
 * 
 * @version1.0
 */
public class ShiroParameter {

	// 0 - never expire
	private int expire = 0;

	public int getExpire() {
		return expire;
	}
	public void setExpire(int expire) {
		this.expire = expire;
	}
	
}
