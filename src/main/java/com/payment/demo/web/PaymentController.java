package com.payment.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.payment.demo.exception.RecordNotFoundException;

@RestController
public class PaymentController {

	public static String longerVersion = "https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json";
	public static String shorterVersion = "44C0173";

	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	@GetMapping("/short")
	public String retrieveHashCodeByURL(@RequestParam(name = "url", required = true) String url)
			throws RecordNotFoundException {
		logger.info("PaymentController :: retrieveHashCodeByURL ");
		if (url != null && url.equals(longerVersion)) {
			return shorterVersion;
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid URL");
		}

	}

	@GetMapping("/long")
	public String retrieveURLByHashCode(@RequestParam(name = "tiny", required = true) String hashCode)
			throws RecordNotFoundException {
		logger.info("PaymentController :: retrieveURLByHashCode ");
		if (hashCode != null && hashCode.equals(shorterVersion)) {
			return longerVersion;
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid hashCode");
		}

	}

}
