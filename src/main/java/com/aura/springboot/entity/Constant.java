package com.aura.springboot.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component 
public class Constant {
  	@Value("${aura.carry.server}")
   String  server;
  	double index=0;
  	
	public double getIndex() {
		return index;
	}

	public void setIndex(double index) {
		this.index = index;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
  	
  	
	
}
