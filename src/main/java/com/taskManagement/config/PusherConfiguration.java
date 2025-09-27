package com.taskManagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pusher.rest.Pusher;

@Configuration
public class PusherConfiguration {

	@Value("${pusher.app-id}")
	private String appId;

	@Value("${pusher.app-key}")
	private String appKey;

	@Value("${pusher.app-secret}")
	private String appSecret;

	@Value("${pusher.cluster:ap2}")
	private String cluster;

	@Bean
	public Pusher pusher() {
		Pusher pusher = new Pusher(appId, appKey, appSecret);
		pusher.setCluster(cluster);
		pusher.setEncrypted(true);
		return pusher;
	}
}