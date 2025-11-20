package com.nads.aplicacionweb.sesiones.repositories;

import com.nads.aplicacionweb.sesiones.models.Categoria;
import com.nads.aplicacionweb.sesiones.models.Producto;

import java.sql.SQLException;
import java.util.List;


public class CategoriaRepositorieJdbcImplement implements Repositorio <Categoria>{
    @Override
    public List<Producto> listar() throws SQLException {
        return null;
    }

    @Override
    public Categoria porid(long id) throws SQLException {
        return null;
    }

    @Override
    public void guardar(Categoria categoria) throws SQLException {

    }

    @Override
    public void borrar(Categoria categoria) throws SQLException {

    }

    @Override
    public void activar(Categoria categoria) throws SQLException {

    }
}
