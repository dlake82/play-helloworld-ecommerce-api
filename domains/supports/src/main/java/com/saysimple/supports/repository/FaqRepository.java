package com.saysimple.supports.repository;

import com.saysimple.supports.entity.Faq;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaqRepository extends CrudRepository<Faq, Long> {
    Optional<Faq> findByFaqId(String faqId);

//    @Query("SELECT b FROM Support b WHERE b.title LIKE %:keyword%")
//    List<Support> findByTitleContaining(@Param("keyword") String keyword);
//
//    @Query("SELECT b FROM Support b WHERE b.content LIKE %:keyword%")
//    List<Support> findByTextContaining(@Param("keyword") String keyword);
}