package com.jsako.shiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsako.shiro.service.FilterChainService;

@RestController
@RequestMapping("/filterchain")
public class FilterChainController {
	
	@Autowired
	private FilterChainService filterChainService;
	
	@RequestMapping("add")
	public String add(@RequestParam String chainName,@RequestParam String chainDefinition){
		filterChainService.add(chainName, chainDefinition);
		return "success";
	}
}
