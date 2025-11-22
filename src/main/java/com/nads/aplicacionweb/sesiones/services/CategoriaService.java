package com.nads.aplicacionweb.sesiones.services;

import com.nads.aplicacionweb.sesiones.models.Categoria;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el servicio de Categorías
 */
public interface CategoriaService {
    List<Categoria> listar();
    Optional<Categoria> porId(Long id);
    void guardar(Categoria categoria);
    void eliminar(Long id);
}
