package com.noobit.model;

import javax.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    private String email;

    @Column(name = "puntos_ranked") // Mapeo exacto a SQL
    private int puntosRanked = 0;

    @Column(name = "puntos_market") // Mapeo exacto a SQL
    private int puntosMarket = 0;

    @Column(name = "es_admin")
    private boolean esAdmin = false;

    public Usuario() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getPuntosRanked() { return puntosRanked; }
    public void setPuntosRanked(int pts) { this.puntosRanked = pts; }
    public int getPuntosMarket() { return puntosMarket; }
    public void setPuntosMarket(int pts) { this.puntosMarket = pts; }
    public boolean isEsAdmin() { return esAdmin; }
    public void setEsAdmin(boolean admin) { this.esAdmin = admin; }
}