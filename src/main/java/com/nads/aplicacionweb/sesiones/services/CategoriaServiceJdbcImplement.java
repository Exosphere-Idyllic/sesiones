package com.nads.aplicacionweb.sesiones.services;

import com.nads.aplicacionweb.sesiones.models.Categoria;
import com.nads.aplicacionweb.sesiones.repositories.CategoriaRepositoryJdbcImplement;
import com.nads.aplicacionweb.sesiones.repositories.Repositorio;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Categorías
 */
public class CategoriaServiceJdbcImplement implements CategoriaService {

    private Repositorio<Categoria> repositorio;

    public CategoriaServiceJdbcImplement(Connection connection) {
        this.repositorio = new CategoriaRepositoryJdbcImplement(connection);
    }

    @Override
    public List<Categoria> listar() {
        try {
            return repositorio.listar();
        } catch (SQLException e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Optional<Categoria> porId(Long id) {
        try {
            return Optional.ofNullable(repositorio.porId(id));
        } catch (SQLException e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void guardar(Categoria categoria) {
        try {
            repositorio.guardar(categoria);
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
}