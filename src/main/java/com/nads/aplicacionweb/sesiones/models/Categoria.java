package com.nads.aplicacionweb.sesiones.models;

public class Categoria {
    private int id;
    private String descripcion;
    private String nombreCategoria;
    private int condicion;


    public Categoria(){

    }

    public Categoria(int id, String descripcion, String nombreCategoria, int condicion) {
        this.id = id;
        this.descripcion = descripcion;
        this.nombreCategoria = nombreCategoria;
        this.condicion = condicion;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public int getCondicion() {
        return condicion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public void setCondicion(int condicion) {
        this.condicion = condicion;
    }
}
