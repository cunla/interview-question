package com.payment.demo.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

 
@Repository
public interface UrlRepository 
        extends JpaRepository<URLEntity,Long> {
	
	@Query(value = "SELECT u FROM URLEntity u WHERE u.hashCode = ?1")
	Optional<URLEntity> findByHashCode(String hashCode);
	
	@Query(value = "SELECT u FROM URLEntity u WHERE u.url = ?1")
	Optional<URLEntity> findByURL(String url);
 }
