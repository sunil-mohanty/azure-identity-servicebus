package com.ski.identity.servicebus;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.microsoft.aad.msal4j.DeviceCode;
import com.microsoft.aad.msal4j.DeviceCodeFlowParameters;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.PublicClientApplication;

@Service
public class ServiceBusManager {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceBusManager.class);
	
	@Autowired
	MessagePublisher messagePublisher;
	
	@Value("${application.clientId}")
	private String clientId;
    
	@Value("${application.authority}")
	private String authority;
    
	@Value("${application.serviceBusScope}")
    private String serviceBusScope;
    
    
	public IAuthenticationResult accuireAccessToken() throws Exception {
		logger.info("Accuring the access token");
        PublicClientApplication app = PublicClientApplication.builder(clientId)
                .authority(authority)
                .build();

        Consumer<DeviceCode> deviceCodeConsumer = (DeviceCode deviceCode) -> {
        	logger.info(deviceCode.message());
        };

        CompletableFuture<IAuthenticationResult> future = app.acquireToken(
                DeviceCodeFlowParameters.builder(
                        Collections.singleton(serviceBusScope),
                        deviceCodeConsumer)
                        .build());

        future.handle((res, ex) -> {
            if(ex != null) {
            	logger.error("There is a loose screw : " + ex.getMessage());
                return "SERVICE_FAILURE";
            }
           return res;
        });

        IAuthenticationResult result = future.join();
        return result;
    }
	
	
	
	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws Exception 
	 */
	public String publisMessage() throws Exception {
		return messagePublisher.publish(accuireAccessToken().accessToken());
	}

}
