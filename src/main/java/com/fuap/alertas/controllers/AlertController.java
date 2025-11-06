package com.fuap.alertas.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuap.alertas.data.models.Alerta;
import com.fuap.alertas.services.AlertaService;



@RestController
@RequestMapping("/api/alertas")
public class AlertController {
    @Autowired
    private AlertaService alertaService;

    @GetMapping
    public List<Alerta> getAll() {
        return alertaService.getAll();
    }

    @GetMapping("/{id}")
    public Alerta getById(@PathVariable int id) {
        return alertaService.getById(id);
    }

    @GetMapping("/user/{id}")
    public List<Alerta> getByUser(@PathVariable int id) {
        return alertaService.getByUserId(id);
    }
}
