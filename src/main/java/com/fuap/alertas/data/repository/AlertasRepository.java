package com.fuap.alertas.data.repository;


import com.fuap.alertas.data.models.Alerta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlertasRepository extends JpaRepository<Alerta, Integer> {
    @Query(value = "SELECT * FROM alerta WHERE :userId = ANY(handler_users)", nativeQuery = true)
    List<Alerta> findByUserId(@Param("userId") int userId);
}
