package com.payment.demo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payment.demo.exception.RecordNotFoundException;
import com.payment.demo.persistence.URLEntity;
import com.payment.demo.persistence.UrlRepository;

	@Service
	public class PaymentService {
	   
		private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

	    @Autowired
	    UrlRepository urlRepository;
	     
	    public URLEntity getURLByHashCode(String hashCode) throws RecordNotFoundException 
	    {
			logger.info("PaymentService :: getURLByHashCode ");
	        Optional<URLEntity> url = urlRepository.findByHashCode(hashCode);
	         
	        if(url.isPresent()) {
	            return url.get();
	        } else {
	            throw new RecordNotFoundException("No URL record exist for given hashcode");
	        }
	    }

	    public URLEntity getHashCodeByURL(String url) throws RecordNotFoundException 
	    {
			logger.info("PaymentService :: getHashCodeByURL ");
	        Optional<URLEntity> urlEntity = urlRepository.findByURL(url);
	         
	        if(urlEntity.isPresent()) {
	            return urlEntity.get();
	        } else {
	            throw new RecordNotFoundException("No hashCode exist for given URL");
	        }
	    }

}
