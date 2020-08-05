package com.payment.demo.service;

import java.util.List;
import java.util.Objects;
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
	    
	    
	    public void purgeData(List<URLEntity> urls) throws RecordNotFoundException {
			logger.info("PaymentService :: getHashCodeByURL ");
			try {
				if (urls != null && urls.size() > 0) {
					{
						for (URLEntity url : urls) {
							System.out.println("inside delete" + url.getId());
							urlRepository.deleteById(url.getId());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RecordNotFoundException("Error occured while deleting URL data");
			}
		}

		public List<URLEntity> retrieveRecordsOlderThan30Mins() {
			logger.info("PaymentService :: getHashCodeByURL ");
			List<URLEntity> urls = urlRepository.findURLsOlderthan30mins();
			urls.stream().filter(Objects::nonNull).map(url -> url.getId()).forEach(System.out::println);
			return urls;
		}

}
