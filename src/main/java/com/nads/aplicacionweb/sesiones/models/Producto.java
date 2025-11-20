package com.nads.aplicacionweb.sesiones.models;

import java.util.Date;

public class Producto {
    private Long id;
    private String nombreProducto;
    private Long idCategoria;
    private String nombreCategoria; // Para joins
    private int stock;
    private double precio;
    private String descripcion;
    private String codigo;
    private Date fechaElaboracion;  // Coincide con fecha_elaboracion
    private Date fechaCaducidad;    // Coincide con fecha_caducidad
    private int condicion;

    // ========================================
    // CONSTRUCTORES
    // ========================================

    public Producto() {
    }

    // Constructor para compatibilidad con código anterior
    public Producto(Long id, String nombreProducto, String tipo, double precio) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.descripcion = tipo;
        this.precio = precio;
        this.condicion = 1;
    }

    // Constructor completo
    public Producto(Long id, String nombreProducto, Long idCategoria, String nombreCategoria,
                    int stock, double precio, String descripcion, String codigo,
                    Date fechaElaboracion, Date fechaCaducidad, int condicion) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    // Método para compatibilidad con código anterior
    public String getNombre() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    // Método para compatibilidad con código anterior
    public String getTipo() {
        return nombreCategoria != null ? nombreCategoria : descripcion;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(Date fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public int getCondicion() {
        return condicion;
    }

    public void setCondicion(int condicion) {
        this.condicion = condicion;
    }
}