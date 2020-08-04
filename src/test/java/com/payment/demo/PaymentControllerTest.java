package com.payment.demo;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentControllerTest {

	@LocalServerPort
	private int port;
	private static String longerVersion = "https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json";
	private static String shorterVersion = "44C0173";
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	@Test
	public void testRetrieveHashCodeByURL() throws Exception {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/short?url=" + longerVersion),
				HttpMethod.GET, entity, String.class);
		System.out.println(response.getBody().toString());
		String expected = shorterVersion;
		Assert.assertEquals(expected, response.getBody().toString());
	}

	@Test
	public void testRetrieveURLByHashCode() throws Exception {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/long?tiny=44C0173"), HttpMethod.GET,
				entity, String.class);
		System.out.println(response.getBody().toString());
		String expected = longerVersion;
		Assert.assertEquals(expected, response.getBody().toString());
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
