package com.fuap.alertas.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fuap.alertas.data.models.Resolution;

public interface ResolutionRepository extends JpaRepository<Resolution, Integer> {}
