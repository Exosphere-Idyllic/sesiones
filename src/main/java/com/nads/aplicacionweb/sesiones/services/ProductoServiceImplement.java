package com.nads.aplicacionweb.sesiones.services;

import com.nads.aplicacionweb.sesiones.models.Producto;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductoServiceImplement implements ProductoService {
    @Override
    public List<Producto> listar() {
        return Arrays.asList(
                new Producto(1L, "notebook", "HDD computacional", 175880),
                new Producto(2L, "mesa electrica", "HDD oficina", 100000),
                new Producto(3L, "impresora multifuncional", "HDD computacional", 40000)
        );
    }

    @Override
    public Optional<Producto> porId(long id) {
        return listar().stream()
                .filter(p -> p.getId().equals(id))
                .findAny();
    }
}