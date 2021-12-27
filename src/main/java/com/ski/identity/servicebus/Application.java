package com.ski.identity.servicebus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	ServiceBusManager serviceBusManager;
    
    //private final static Set<String> SCOPE = Collections.singleton("https://servicebus.azure.net/user_impersonation");
        
    public static void main(String args[]) throws Exception {
    	SpringApplication.run(Application.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		logger.info("Invoking serviceBusManager to publish message");
		serviceBusManager.publisMessage();
	}
}