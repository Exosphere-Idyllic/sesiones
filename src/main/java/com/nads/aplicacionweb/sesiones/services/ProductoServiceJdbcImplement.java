package com.nads.aplicacionweb.sesiones.services;

import com.nads.aplicacionweb.sesiones.models.Producto;
import com.nads.aplicacionweb.sesiones.repositories.ProductoRepositoryJdbcImplement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductoServiceJdbcImplement implements ProductoService {

    private ProductoRepositoryJdbcImplement repositorio;

    public ProductoServiceJdbcImplement(Connection connection) {
        this.repositorio = new ProductoRepositoryJdbcImplement(connection);
    }

    @Override
    public List<Producto> listar() {
        try {
            return repositorio.listar();
        } catch (SQLException e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Optional<Producto> porId(Long id) {
        try {
            return Optional.ofNullable(repositorio.porId(id));
        } catch (SQLException e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void guardar(Producto producto) {
        try {
            repositorio.guardar(producto);
        } catch (SQLException e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            repositorio.eliminar(id);
        } catch (SQLException e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    /**
     * NUEVO: Descontar stock de un producto
     * Lanza excepción si no hay stock suficiente
     */
    @Override
    public void descontarStock(Long id, int cantidad) {
        try {
            repositorio.descontarStock(id, cantidad);
        } catch (SQLException e) {
            throw new ServiceJdbcException("Error al descontar stock: " + e.getMessage(),
                    e.getCause());
        }
    }

    /**
     * NUEVO: Incrementar stock de un producto
     * Usado cuando se elimina un producto del carrito
     */
    @Override
    public void incrementarStock(Long id, int cantidad) {
        try {
            repositorio.incrementarStock(id, cantidad);
        } catch (SQLException e) {
            throw new ServiceJdbcException("Error al incrementar stock: " + e.getMessage(),
                    e.getCause());
        }
    }
}
