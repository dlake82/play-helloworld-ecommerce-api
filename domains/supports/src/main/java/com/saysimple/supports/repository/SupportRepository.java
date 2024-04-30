package com.saysimple.supports.repository;

import com.saysimple.supports.entity.Support;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SupportRepository extends CrudRepository<Support, Long> {
    Optional<Support> findByContactId(String contactId);
}
