package com.jsako.shiro.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilterChainService {

	@Autowired
	private DefaultFilterChainManager filterChainManager;

	private Map<String, String> datas = new LinkedHashMap<String, String>();

	private Map<String, NamedFilterList> defaultFilterChains;

	{
		// 初始化权限数据模拟数据库
		datas.put("/user/update", "roles[admin]");
		datas.put("/user/add", "roles[admin]");
	}

	@PostConstruct
	public void init() {
		defaultFilterChains = new LinkedHashMap<String, NamedFilterList>(filterChainManager.getFilterChains());
		// 初始化数据库的数据进filterChainManager
		for (Entry<String, String> data : datas.entrySet()) {
			filterChainManager.createChain(data.getKey(), data.getValue());
		}
		System.out.println("FilterChainService init end");
	}

	public void add(String chainName, String chainDefinition) {
		datas.put(chainName, chainDefinition);

		filterChainManager.getFilterChains().clear();
		if (defaultFilterChains != null && defaultFilterChains.size() > 0) {
			filterChainManager.getFilterChains().putAll(defaultFilterChains);
		}
		// 初始化数据库的数据进filterChainManager
		for (Entry<String, String> data : datas.entrySet()) {
			filterChainManager.createChain(data.getKey(), data.getValue());
		}
	}

}
