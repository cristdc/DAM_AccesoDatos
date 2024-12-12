package com.example.Gestor_De_Empleados.modelo;

import java.io.Serializable;

// Modelo de empleado
public class Empleado implements Serializable {
    private int id;
    private String nombre;
    private String apellidos;
    private String departamento;
    private double sueldo;

    // Constructor
    public Empleado(int id, String nombre, String apellidos, String departamento, double sueldo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.departamento = departamento;
        this.sueldo = sueldo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public double getSueldo() { return sueldo; }
    public void setSueldo(double sueldo) { this.sueldo = sueldo; }

    @Override
    public String toString() {
        return id + ": " + nombre + " " + apellidos + " (" + departamento + ") - $" + sueldo;
    }
}
