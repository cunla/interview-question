package com.payment.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.payment.demo.exception.RecordNotFoundException;
import com.payment.demo.persistence.URLEntity;
import com.payment.demo.service.PaymentService;

@RestController
public class PaymentController {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	
	@Value("${url.longerVersion}")
	private String longerVersion;

	@Value("${url.shorterVersion}")
	private  String shorterVersion;
	
	@Autowired
	PaymentService service;

	@GetMapping("/short")
	public String retrieveHashCodeByURL(@RequestParam(name = "url", required = true) String url)
			throws RecordNotFoundException {
		logger.info("PaymentController :: retrieveHashCodeByURL ");
		try {
			if (url != null && url.equals(longerVersion)) {
				// check on the server
				return shorterVersion;
			} else {
				
				// check on the persistence layer
				URLEntity urlEntity = service.getHashCodeByURL(url);
				return urlEntity.getHashCode();
			}
		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid URL");
		}
	}

	@GetMapping("/long")
	public String retrieveURLByHashCode(@RequestParam(name = "tiny", required = true) String hashCode)
			throws RecordNotFoundException {
		logger.info("PaymentController :: retrieveURLByHashCode ");
		try {
			if (hashCode != null && hashCode.equals(shorterVersion)) {
				// check on the server
				return longerVersion;
			} else {
				// check on the persistence layer
				URLEntity urlEntity = service.getURLByHashCode(hashCode);
				return urlEntity.getUrl();
			}
		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid hashCode");
		}
	}

}
