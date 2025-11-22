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
        System.out.println("✓ ProductoService inicializado con conexión: " + (connection != null));
    }

    @Override
    public List<Producto> listar() {
        try {
            System.out.println("=== SERVICE: Solicitando lista de productos ===");
            List<Producto> productos = repositorio.listar();
            System.out.println("=== SERVICE: " + productos.size() + " productos obtenidos ===");
            return productos;
        } catch (SQLException e) {
            System.err.println("ERROR en service listar: " + e.getMessage());
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Optional<Producto> porId(Long id) {
        try {
            System.out.println("Service: Buscando producto por ID: " + id);
            return Optional.ofNullable(repositorio.porId(id));
        } catch (SQLException e) {
            System.err.println("ERROR en service porId: " + e.getMessage());
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void guardar(Producto producto) {
        try {
            System.out.println("Service: Guardando producto: " + producto.getNombreProducto());
            repositorio.guardar(producto);
            System.out.println("Service: Producto guardado exitosamente");
        } catch (SQLException e) {
            System.err.println("ERROR en service guardar: " + e.getMessage());
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            System.out.println("Service: Eliminando producto ID: " + id);
            repositorio.eliminar(id);
            System.out.println("Service: Producto eliminado exitosamente");
        } catch (SQLException e) {
            System.err.println("ERROR en service eliminar: " + e.getMessage());
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void descontarStock(Long id, int cantidad) {
        try {
            System.out.println("Service: Descontando stock - ID: " + id + ", Cantidad: " + cantidad);
            repositorio.descontarStock(id, cantidad);
            System.out.println("Service: Stock descontado exitosamente");
        } catch (SQLException e) {
            System.err.println("ERROR en service descontarStock: " + e.getMessage());
            throw new ServiceJdbcException("Error al descontar stock: " + e.getMessage(), e.getCause());
        }
    }

    @Override
    public void incrementarStock(Long id, int cantidad) {
        try {
            System.out.println("Service: Incrementando stock - ID: " + id + ", Cantidad: " + cantidad);
            repositorio.incrementarStock(id, cantidad);
            System.out.println("Service: Stock incrementado exitosamente");
        } catch (SQLException e) {
            System.err.println("ERROR en service incrementarStock: " + e.getMessage());
            throw new ServiceJdbcException("Error al incrementar stock: " + e.getMessage(), e.getCause());
        }
    }
}