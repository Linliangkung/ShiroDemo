package com.jsako.shiro.configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jsako.shiro.filter.MyRolesAuthorizationFilter;

@Configuration
public class ShiroConfiguration {
	@Value("${shiro.rememberme.secret}")
	private String secret;
	
	
	@Bean
	public FilterChainManager filterChainManager(){
		MyFilterChainManager filterChainManager=new MyFilterChainManager();
		filterChainManager.setLoginUrl("/login.jsp");
		filterChainManager.setUnauthorizedUrl("/unauthorized.jsp");
		Map<String, String> filterChainDefinitionMap=new LinkedHashMap<String,String>();
		filterChainDefinitionMap.put("/**/*.css", "anon");
		filterChainDefinitionMap.put("/user/login", "anon");
		filterChainDefinitionMap.put("/user/delete", "roles[admin,user-admin]");
		filterChainDefinitionMap.put("/addFilterChain.jsp", "roles[admin]");
		filterChainDefinitionMap.put("/**", "user");
		//配置自定义Filter
		Map<String, Filter> filters=new HashMap<String, Filter>();
		filters.put("roles",new MyRolesAuthorizationFilter());
		filterChainManager.setCustomFilters(filters);
		filterChainManager.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return filterChainManager;
	}
	
	@Bean
	public Cookie rememberMeCookie(){
		SimpleCookie rememberMeCookie=new SimpleCookie("rememberMe");
		rememberMeCookie.setHttpOnly(true);
		rememberMeCookie.setMaxAge(2592000);
		return rememberMeCookie;
	}
	
	@Bean
	public FilterChainResolver filterChainResolver(@Qualifier("filterChainManager")FilterChainManager filterChainManager){
		MyFilterChainResolver filterChainResolver=new MyFilterChainResolver();
		filterChainResolver.setFilterChainManager(filterChainManager);
		filterChainResolver.setLoginActionUrl("/user/login");
		return filterChainResolver;
	}

	
	
	@Bean
	public RememberMeManager rememberMeManager(@Qualifier("rememberMeCookie")Cookie rememberMeCookie){
		CookieRememberMeManager rememberMeManager=new CookieRememberMeManager();
		//设置AES算法的秘钥
		rememberMeManager.setCipherKey(Base64.decode(secret));
		rememberMeManager.setCookie(rememberMeCookie);
		return rememberMeManager;
	}
	
	
	
	@Bean
	public SecurityManager securityManager(@Qualifier("userRealm")Realm userRealm,
			@Qualifier("rememberMeManager")RememberMeManager rememberMeManager){
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		defaultWebSecurityManager.setRealm(userRealm);
		defaultWebSecurityManager.setRememberMeManager(rememberMeManager);
		return defaultWebSecurityManager;
	}
	
	@Bean
	public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager")SecurityManager securityManager){
		ShiroFilterFactoryBean shiroFilter=new ShiroFilterFactoryBean();
//		shiroFilter.setLoginUrl("/login.jsp");
//		shiroFilter.setUnauthorizedUrl("/unauthorized.jsp");
//		Map<String, String> filterChainDefinitionMap=new LinkedHashMap<String,String>();
//		filterChainDefinitionMap.put("/**/*.css", "anon");
//		filterChainDefinitionMap.put("/user/login", "anon");
//		filterChainDefinitionMap.put("/user/delete", "roles[admin,user-admin]");
//		filterChainDefinitionMap.put("/**", "user");
//		//配置自定义Filter
//		Map<String, Filter> filters=new HashMap<String, Filter>();
//		filters.put("roles",new MyRolesAuthorizationFilter());
//		shiroFilter.setFilters(filters);
//		shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
		shiroFilter.setSecurityManager(securityManager);
		return shiroFilter;
	}
	
/*	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent()==null){
			Object bean = event.getApplicationContext().getBean("shiroFilter");
			try {
				Method method = bean.getClass().getMethod("setFilterChainResolver", FilterChainResolver.class);
				method.invoke(bean, event.getApplicationContext().getBean("filterChainResolver"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/
}
