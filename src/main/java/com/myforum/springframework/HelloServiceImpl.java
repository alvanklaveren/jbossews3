package com.myforum.springframework;

import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {

	@Override
	public String getHelloWorldMessage() {
		return "Spring : hello world";
	}
}
