package com.se459.modules.models;

public interface Observer {
	
	public void update();
	
	public void sendNotification(String message);

}
