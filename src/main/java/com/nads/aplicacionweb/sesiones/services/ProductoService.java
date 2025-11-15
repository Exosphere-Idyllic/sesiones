package com.nads.aplicacionweb.sesiones.services;

import com.nads.aplicacionweb.sesiones.models.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> listar();
    Optional<Producto> porId(long id);
}