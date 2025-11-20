package com.nads.aplicacionweb.sesiones.repositories;

import com.nads.aplicacionweb.sesiones.models.Producto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorieJdbcImplement implements Repositorio<T> {

    private Connection conexion;
    public ProductoRepositorieJdbcImplement(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<Producto> listar() throws SQLException {
        List <Producto> productos = new ArrayList<>();
        try(Statement statement = conexion.createStatement();) {
        ResultSet rs = statement.executeQuery("select p.* , c.nombreCategoria");
        while(rs.next()) {
            Producto producto = new Producto();
            producto.setId(rs.getInt("id"));
            producto.setNombreCategoria(rs.getString("nombreCategoria"));
            productos.add(producto);
        }

        }
    }

    @Override
    public T porid(long id) throws SQLException {
        return null;
    }

    @Override
    public void guardar(T producto) throws SQLException {

    }

    @Override
    public void borrar(T producto) throws SQLException {

    }

    @Override
    public void activar(T producto) throws SQLException {

    }
}
