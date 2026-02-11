package com.noobit.model;

import javax.persistence.*;

@Entity
@Table(name = "recompensas")
public class Recompensa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String descripcion;
    private int precio;

    public Recompensa() {}

    public Recompensa(String nombre, String descripcion, int precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getPrecio() { return precio; }
    public void setPrecio(int precio) { this.precio = precio; }
}