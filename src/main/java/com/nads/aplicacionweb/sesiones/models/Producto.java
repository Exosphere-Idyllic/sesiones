package com.nads.aplicacionweb.sesiones.models;

import java.util.Date;

/**
 * ============================================
 * MODELO DE PRODUCTO
 * ============================================
 * Descripción: Representa un producto del sistema
 * con todos sus atributos según la BD
 * Autor: Pablo Aguilar
 * Fecha: 18/11/2025
 * ============================================
 */
public class Producto {

    private int id;
    private String nombreProducto;
    private int idCategoria;
    private String nombreCategoria; // Para joins
    private int stock;
    private double precio;
    private String descripcion;
    private String codigo;
    private Date fechaElaboracion;
    private Date fechaCaducidad;
    private int condicion;

    // ========================================
    // CONSTRUCTORES
    // ========================================

    /**
     * Constructor vacío
     */
    public Producto() {
    }



    /**
     * Constructor completo
     */
    public Producto(int id, String nombreProducto, int stock, double precio, String descripcion, String codigo, Date fechaElaboracion, Date fechaCaducidad, int condicion) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        Categoria categoria = new Categoria();
        Categoria.setNombreCategoria(idCategoria);
        this.stock = stock;
        this.precio = precio;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.fechaElaboracion = fechaElaboracion;
        this.fechaCaducidad = fechaCaducidad;
        this.condicion = condicion;
    }

    // ========================================
    // GETTERS Y SETTERS
    // ========================================

    public int getId() {
        return id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public int getStock() {
        return stock;
    }

    public double getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public Date getFechaElaboracion() {
        return fechaElaboracion;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public int getCondicion() {
        return condicion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setFechaElaboracion(Date fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public void setCondicion(int condicion) {
        this.condicion = condicion;
    }
    }
