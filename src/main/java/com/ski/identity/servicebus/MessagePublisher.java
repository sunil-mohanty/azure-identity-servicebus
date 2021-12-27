package com.ski.identity.servicebus;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MessagePublisher {
	
	private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);
	
	@Value("${application.queueUri}")
	private String queueUri;
	
	public String publish(String accessToken) {
		logger.info("Publishing the message to topic");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/atom+xml;type=entry;charset=utf-8");
		headers.set("Authorization", "Bearer " +  accessToken);
	
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpEntity<String> entity = new HttpEntity<String>("Hello pos-transaction",headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(queueUri, HttpMethod.POST, entity, String.class);
		
		String response = responseEntity.getBody();
		logger.info("Message published");
		return response;
	}
}
