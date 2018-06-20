package com.jsako.shiro.configuration;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;

import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.SimpleNamedFilterList;

public class MyFilterChainManager extends DefaultFilterChainManager {
	private String loginUrl;
	private String successUrl;
	private String unauthorizedUrl;

	private Map<String, String> filterChainDefinitionMap;

	public void setCustomFilters(Map<String, Filter> filters){
		for (Entry<String, Filter> filter: filters.entrySet()) {
			addFilter(filter.getKey(), filter.getValue());
		}
	}
	@PostConstruct
	private void init() {
		Map<String, Filter> filters = getFilters();
		/**
		 * 为所有的Filter设置loginUrl、successUrl、unauthorizedUrl等参数
		 */
		if (!CollectionUtils.isEmpty(filters)) {
			for (Map.Entry<String, Filter> entry : filters.entrySet()) {
				Filter filter = entry.getValue();
				applyGlobalPropertiesIfNecessary(filter);
			}
		}
		
		Map<String, String> filterChainDefinitionMap = getFilterChainDefinitionMap();
		if(!CollectionUtils.isEmpty(filters)){
			for(Entry<String, String> chain :filterChainDefinitionMap.entrySet()){
				String chainName=chain.getKey();
				String chainDefinition=chain.getValue();
				createChain(chainName, chainDefinition);
			}
		}
	}
	
	
	public FilterChain proxy(FilterChain original, List<String> chainNames) {
		NamedFilterList configured =new SimpleNamedFilterList(chainNames.toString());
		if(!CollectionUtils.isEmpty(chainNames)){
			for (String chainName : chainNames) {
				configured.addAll(getChain(chainName));
			}
		}
		return configured.proxy(original);
	}

	public Map<String, String> getFilterChainDefinitionMap() {
		return filterChainDefinitionMap;
	}

	public void setFilterChainDefinitionMap(
			Map<String, String> filterChainDefinitionMap) {
		this.filterChainDefinitionMap = filterChainDefinitionMap;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}

	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}


	private void applyLoginUrlIfNecessary(Filter filter) {
		String loginUrl = getLoginUrl();
		if (StringUtils.hasText(loginUrl)
				&& (filter instanceof AccessControlFilter)) {
			AccessControlFilter acFilter = (AccessControlFilter) filter;
			// only apply the login url if they haven't explicitly configured
			// one already:
			String existingLoginUrl = acFilter.getLoginUrl();
			if (AccessControlFilter.DEFAULT_LOGIN_URL.equals(existingLoginUrl)) {
				acFilter.setLoginUrl(loginUrl);
			}
		}
	}

	private void applySuccessUrlIfNecessary(Filter filter) {
		String successUrl = getSuccessUrl();
		if (StringUtils.hasText(successUrl)
				&& (filter instanceof AuthenticationFilter)) {
			AuthenticationFilter authcFilter = (AuthenticationFilter) filter;
			// only apply the successUrl if they haven't explicitly configured
			// one already:
			String existingSuccessUrl = authcFilter.getSuccessUrl();
			if (AuthenticationFilter.DEFAULT_SUCCESS_URL
					.equals(existingSuccessUrl)) {
				authcFilter.setSuccessUrl(successUrl);
			}
		}
	}

	private void applyUnauthorizedUrlIfNecessary(Filter filter) {
		String unauthorizedUrl = getUnauthorizedUrl();
		if (StringUtils.hasText(unauthorizedUrl)
				&& (filter instanceof AuthorizationFilter)) {
			AuthorizationFilter authzFilter = (AuthorizationFilter) filter;
			// only apply the unauthorizedUrl if they haven't explicitly
			// configured one already:
			String existingUnauthorizedUrl = authzFilter.getUnauthorizedUrl();
			if (existingUnauthorizedUrl == null) {
				authzFilter.setUnauthorizedUrl(unauthorizedUrl);
			}
		}
	}

	private void applyGlobalPropertiesIfNecessary(Filter filter) {
		applyLoginUrlIfNecessary(filter);
		applySuccessUrlIfNecessary(filter);
		applyUnauthorizedUrlIfNecessary(filter);
	}
}
