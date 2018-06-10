package use.shiroSecurity.common;

/**
 * 自定义token
 * 增加参数是否已经授权
 * 项目名称:use.shiroSecurity
 * 类型名称:UsernamePasswordToken
 * 类型描述:
 * 作者:wx
 * 创建时间:2018年6月10日
 * @version:
 */
public abstract class UsernamePasswordTokenAbstract extends org.apache.shiro.authc.UsernamePasswordToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7729567365286985311L;
	
	private boolean authorizationEd = false;		//增加参数是否已经授权

	public boolean getAuthorizationEd() {
		return authorizationEd;
	}
	public void setAuthorizationEd(boolean authorizationEd) {
		this.authorizationEd = authorizationEd;
	}
	public UsernamePasswordTokenAbstract() {
		super();
	}
	
	public UsernamePasswordTokenAbstract(String username, char[] password,
			boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}

	public UsernamePasswordTokenAbstract(String username, char[] password,
			boolean rememberMe, String host,boolean authorizationEd) {
		super(username, password, rememberMe, host);
		this.authorizationEd = authorizationEd;
	}

}
