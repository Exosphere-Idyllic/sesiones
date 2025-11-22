package com.nads.aplicacionweb.sesiones.services;

import com.nads.aplicacionweb.sesiones.models.Producto;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz ProductoService con métodos de gestión de stock
 */
public interface ProductoService {
    List<Producto> listar();
    Optional<Producto> porId(Long id);
    void guardar(Producto producto);
    void eliminar(Long id);

    // NUEVOS MÉTODOS PARA GESTIÓN DE STOCK
    void descontarStock(Long id, int cantidad);
    void incrementarStock(Long id, int cantidad);
}