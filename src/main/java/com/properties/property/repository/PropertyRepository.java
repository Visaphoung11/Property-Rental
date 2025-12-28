package com.properties.property.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.properties.property.model.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    Page<Property> findByAgentId(Long agentId, Pageable pageable);
}
