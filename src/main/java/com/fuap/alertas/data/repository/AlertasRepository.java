package com.fuap.alertas.data.repository;


import com.fuap.alertas.data.models.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertasRepository extends JpaRepository<Alerta, Integer> {}
