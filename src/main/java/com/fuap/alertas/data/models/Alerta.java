package com.fuap.alertas.data.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;


@Entity
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int deviceId;
    private String message;
    private String nivel;
    private Boolean deviceExists;
    private String timestamp;
    @Nullable
    private int[] handlerUsers;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="resolution_id", referencedColumnName = "id")
    @Nullable
    private Resolution resolution;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDeviceId() { return this.deviceId; }
    public void setDeviceId(int deviceId) { this.deviceId = deviceId; }

    public String geNivel() { return this.nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getMessage() { return this.message; }
    public void setMessage(String message) { this.message = message; }

    public String getTimestamp() { return this.timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public Boolean getDeviceExists() { return this.deviceExists; }
    public void setDeviceExists(Boolean deviceExists) { this.deviceExists = deviceExists; }

    public int[] getHandlerUsers() { return this.handlerUsers; }
    public void setHandlerUsers(int[] handlerUsers) { this.handlerUsers = handlerUsers; }
}
