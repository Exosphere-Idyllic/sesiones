package com.nads.aplicacionweb.sesiones.repositories;

import com.nads.aplicacionweb.sesiones.models.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositoryJdbcImplement implements Repositorio<Producto> {

    private Connection conn;

    public ProductoRepositoryJdbcImplement(Connection conn) {
        this.conn = conn;
    }

    // ... métodos existentes (listar, porId, guardar, eliminar, activar)

    /**
     * ============================================
     * DESCONTAR STOCK DE UN PRODUCTO
     * ============================================
     * Descuenta la cantidad especificada del stock
     * SOLO si hay stock suficiente (validación atómica en SQL)
     *
     * @param id ID del producto
     * @param cantidad Cantidad a descontar
     * @throws SQLException Si no hay stock suficiente o producto no existe
     */
    public void descontarStock(Long id, int cantidad) throws SQLException {
        // SQL con validación atómica de stock
        String sql = "UPDATE producto " +
                "SET stock = stock - ? " +
                "WHERE id = ? " +
                "AND stock >= ? " +      // CRÍTICO: Solo descuenta si hay suficiente
                "AND condicion = 1";      // Solo productos activos

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cantidad);      // Cantidad a descontar
            pstmt.setLong(2, id);           // ID del producto
            pstmt.setInt(3, cantidad);      // Validación: stock >= cantidad

            int filasActualizadas = pstmt.executeUpdate();

            // Si no se actualizó ninguna fila, determinar la causa
            if (filasActualizadas == 0) {
                // Verificar si el producto existe y obtener su stock actual
                String verificarSql = "SELECT stock, nombreProducto " +
                        "FROM producto " +
                        "WHERE id = ? AND condicion = 1";

                try (PreparedStatement verificarStmt = conn.prepareStatement(verificarSql)) {
                    verificarStmt.setLong(1, id);

                    try (ResultSet rs = verificarStmt.executeQuery()) {
                        if (rs.next()) {
                            // Producto existe pero no hay stock suficiente
                            int stockActual = rs.getInt("stock");
                            String nombreProducto = rs.getString("nombreProducto");

                            throw new SQLException(
                                    "Stock insuficiente para el producto '" + nombreProducto + "'. " +
                                            "Stock disponible: " + stockActual + ", " +
                                            "cantidad solicitada: " + cantidad);
                        } else {
                            // Producto no existe o está inactivo
                            throw new SQLException(
                                    "Producto no encontrado o inactivo (ID: " + id + ")");
                        }
                    }
                }
            }

            // Log exitoso
            System.out.println("[REPOSITORY] ✓ Stock descontado - " +
                    "Producto ID: " + id + ", Cantidad: " + cantidad);
        }
    }

    /**
     * ============================================
     * INCREMENTAR STOCK DE UN PRODUCTO
     * ============================================
     * Devuelve stock cuando se elimina un producto del carrito
     *
     * @param id ID del producto
     * @param cantidad Cantidad a incrementar
     * @throws SQLException Si el producto no existe
     */
    public void incrementarStock(Long id, int cantidad) throws SQLException {
        String sql = "UPDATE producto " +
                "SET stock = stock + ? " +
                "WHERE id = ? " +
                "AND condicion = 1";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cantidad);
            pstmt.setLong(2, id);

            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas == 0) {
                throw new SQLException(
                        "No se pudo incrementar el stock. " +
                                "Producto no encontrado o inactivo (ID: " + id + ")");
            }

            // Log exitoso
            System.out.println("[REPOSITORY] ✓ Stock incrementado - " +
                    "Producto ID: " + id + ", Cantidad: " + cantidad);
        }
    }

    @Override
    public List<Producto> listar() throws SQLException {
        return List.of();
    }

    @Override
    public Producto porId(Long id) throws SQLException {
        return null;
    }

    @Override
    public void guardar(Producto producto) throws SQLException {

    }

    @Override
    public void eliminar(Long id) throws SQLException {

    }

    @Override
    public void activar(Long id) throws SQLException {

    }

    // ... resto de métodos (listar, porId, guardar, eliminar, activar)
    // mantienen su implementación original
}
