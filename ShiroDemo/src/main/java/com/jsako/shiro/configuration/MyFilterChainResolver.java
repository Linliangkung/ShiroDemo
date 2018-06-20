package com.jsako.shiro.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

public class MyFilterChainResolver extends PathMatchingFilterChainResolver {
	
	private String loginActionUrl;
	
	@Override
	public FilterChain getChain(ServletRequest request,
			ServletResponse response, FilterChain originalChain) {
		 	MyFilterChainManager filterChainManager = (MyFilterChainManager) getFilterChainManager();
	        if (!filterChainManager.hasChains()) {
	            return null;
	        }
	        String requestURI = getPathWithinApplication(request);
	        if(requestURI.equals(getLoginActionUrl())){
	        	return filterChainManager.proxy(originalChain, Arrays.asList(getLoginActionUrl()));
	        }
	        List<String> chainNames=new ArrayList<String>(); 
	        for(String pathPattern:filterChainManager.getChainNames()){
	        	if(pathMatches(pathPattern, requestURI)){
	        		chainNames.add(pathPattern);
	        		if(pathPattern.endsWith("css")||pathPattern.endsWith("js")){
	        			break;
	        		}
	        	}
	        }
	        if(chainNames.size()==0){
	        	return null;
	        }
	        return filterChainManager.proxy(originalChain, chainNames);
	}
	
	public String getLoginActionUrl() {
		return loginActionUrl;
	}
	public void setLoginActionUrl(String loginActionUrl) {
		this.loginActionUrl = loginActionUrl;
	}
	
}
