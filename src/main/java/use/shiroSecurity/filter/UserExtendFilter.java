package use.shiroSecurity.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.UserFilter;

import use.common.json.JSONResult;
import use.common.util.Util;

/**
 * 登录用户过滤器
 * 主要用户过滤ajax访问，如果是ajax访问则返回json错误对象
 * 项目名称:use.shiroSecurity
 * 类型名称:UserExtendFilter
 * 类型描述:
 * 作者:wx
 * 创建时间:2018年6月10日
 * @version:
 */
public class UserExtendFilter extends UserFilter {

	private final String ajaxHeadKey = "X-Requested-With";
	private final String ajaxHeadValue = "XMLHttpRequest";
	private final String userAjaxHeadValue = "XMLHttpRequestclient";
	
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
    	HttpServletRequest hr = (HttpServletRequest) request;
    	if(isAjax(hr) || isUserAjax(hr))
    	{
    		JSONResult json = new JSONResult();
    		json.setJsonType("error");
    		json.setJsonMessage("连接认证信息过期");
    		json.write((HttpServletResponse)response);
    		return;
    	}
        super.redirectToLogin(request, response);
    }
    
    private boolean isAjax(HttpServletRequest hr)
    {
    	String head = hr.getHeader(ajaxHeadKey);
    	if(head == null)
    	{
    		return false;
    	}
    	return ajaxHeadValue.equals(head) ? true : false;
    }
    private boolean isUserAjax(HttpServletRequest hr)
    {
    	String head = hr.getHeader(ajaxHeadKey);
    	if(head == null)
    	{
    		head = Util.getRequestValue(hr, ajaxHeadKey);
    		return userAjaxHeadValue.equals(head) ? true : false;
    	}
    	return userAjaxHeadValue.equals(head) ? true : false;
    }
}
