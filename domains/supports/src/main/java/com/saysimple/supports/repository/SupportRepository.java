package com.saysimple.supports.repository;

import com.saysimple.supports.entity.Support;
import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupportRepository extends CrudRepository<Support, Long> {
    Optional<Support> findBySupportId(String supportId);

//    @Query("SELECT b FROM Support b WHERE b.title LIKE %:keyword%")
//    List<Support> findByTitleContaining(@Param("keyword") String keyword);
//
//    @Query("SELECT b FROM Support b WHERE b.content LIKE %:keyword%")
//    List<Support> findByTextContaining(@Param("keyword") String keyword);
}