package com.fuap.alertas.data.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Resolution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;
    private int dispositivoId;
    private int alertaId;
    @Nullable
    private String message;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDispositivoId() { return this.dispositivoId; }
    public void setDispositivoId(int id) { this.dispositivoId = id; }
    
    public int getAlertaId() { return this.alertaId; }
    public void setAlertaId(int id) { this.alertaId = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
