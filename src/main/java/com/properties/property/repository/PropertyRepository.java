package com.properties.property.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.properties.property.model.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByAgentId(Integer agentId);
}
