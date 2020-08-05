package com.payment.demo.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.payment.demo.exception.RecordNotFoundException;
import com.payment.demo.persistence.URLEntity;
import com.payment.demo.service.PaymentService;

@Configuration
@EnableScheduling
public class ScheduledJobs {
	
  @Autowired
  PaymentService service;
	
  @Value("${jobs.enabled:false}")
  private boolean isEnabled;
 
	/**
	 * Scheduler 
	 * invoked every 2 mins 
	 * with initial delay of 1 min for server startup
	 */
	@Scheduled(initialDelay=60000, fixedDelay = 120000)
	public void purgeData() throws RecordNotFoundException {
		System.out.println("isEnabled"+ isEnabled);
		if (isEnabled) {
			List<URLEntity> urls = service.retrieveRecordsOlderThan30Mins();
			service.purgeData(urls);
		}
	}
}
