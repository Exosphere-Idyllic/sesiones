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

    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();

        // SQL actualizado con los nombres correctos de las columnas
        String sql = "SELECT p.id, p.nombreProducto, p.idCategoria, c.nombreCategoria, " +
                "p.stock, p.precio, p.descripcion, p.codigo, " +
                "p.fecha_elaboracion, p.fecha_caducidad, p.condicion " +
                "FROM producto p " +
                "LEFT JOIN categoria c ON p.idCategoria = c.id " +
                "WHERE p.condicion = 1 " +
                "ORDER BY p.id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto producto = crearProductoDesdeResultSet(rs);
                productos.add(producto);
            }
        }

        return productos;
    }

    @Override
    public Producto porId(Long id) throws SQLException {
        Producto producto = null;

        String sql = "SELECT p.id, p.nombreProducto, p.idCategoria, c.nombreCategoria, " +
                "p.stock, p.precio, p.descripcion, p.codigo, " +
                "p.fecha_elaboracion, p.fecha_caducidad, p.condicion " +
                "FROM producto p " +
                "LEFT JOIN categoria c ON p.idCategoria = c.id " +
                "WHERE p.id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    producto = crearProductoDesdeResultSet(rs);
                }
            }
        }

        return producto;
    }

    @Override
    public void guardar(Producto producto) throws SQLException {
        String sql;

        if (producto.getId() != null && producto.getId() > 0) {
            // UPDATE
            sql = "UPDATE producto SET nombreProducto = ?, idCategoria = ?, " +
                    "stock = ?, precio = ?, descripcion = ?, codigo = ?, " +
                    "fecha_elaboracion = ?, fecha_caducidad = ?, condicion = ? " +
                    "WHERE id = ?";
        } else {
            // INSERT
            sql = "INSERT INTO producto (nombreProducto, idCategoria, stock, precio, " +
                    "descripcion, codigo, fecha_elaboracion, fecha_caducidad, condicion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, producto.getNombreProducto());
            pstmt.setLong(2, producto.getIdCategoria());
            pstmt.setInt(3, producto.getStock());
            pstmt.setDouble(4, producto.getPrecio());
            pstmt.setString(5, producto.getDescripcion());
            pstmt.setString(6, producto.getCodigo());

            // Manejo de fechas
            if (producto.getFechaElaboracion() != null) {
                pstmt.setDate(7, new java.sql.Date(producto.getFechaElaboracion().getTime()));
            } else {
                pstmt.setNull(7, Types.DATE);
            }

            if (producto.getFechaCaducidad() != null) {
                pstmt.setDate(8, new java.sql.Date(producto.getFechaCaducidad().getTime()));
            } else {
                pstmt.setNull(8, Types.DATE);
            }

            pstmt.setInt(9, producto.getCondicion());

            if (producto.getId() != null && producto.getId() > 0) {
                pstmt.setLong(10, producto.getId());
            }

            pstmt.executeUpdate();

            // Si es INSERT, obtener el ID generado
            if (producto.getId() == null || producto.getId() == 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        producto.setId(generatedKeys.getLong(1));
                    }
                }
            }
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "UPDATE producto SET condicion = 0 WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void activar(Long id) throws SQLException {
        String sql = "UPDATE producto SET condicion = 1 WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Método auxiliar para crear un objeto Producto desde un ResultSet
     * ACTUALIZADO con nombres correctos de columnas
     */
    private Producto crearProductoDesdeResultSet(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getLong("id"));
        producto.setNombreProducto(rs.getString("nombreProducto"));
        producto.setIdCategoria(rs.getLong("idCategoria"));
        producto.setNombreCategoria(rs.getString("nombreCategoria"));
        producto.setStock(rs.getInt("stock"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setCodigo(rs.getString("codigo"));

        // Manejo de fechas con los nombres correctos
        Date fechaElab = rs.getDate("fecha_elaboracion");
        if (fechaElab != null) {
            producto.setFechaElaboracion(new java.util.Date(fechaElab.getTime()));
        }

        Date fechaCad = rs.getDate("fecha_caducidad");
        if (fechaCad != null) {
            producto.setFechaCaducidad(new java.util.Date(fechaCad.getTime()));
        }

        producto.setCondicion(rs.getInt("condicion"));

        return producto;
    }

    /**
     * Método adicional para buscar productos por categoría
     */
    public List<Producto> listarPorCategoria(Long idCategoria) throws SQLException {
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT p.id, p.nombreProducto, p.idCategoria, c.nombreCategoria, " +
                "p.stock, p.precio, p.descripcion, p.codigo, " +
                "p.fecha_elaboracion, p.fecha_caducidad, p.condicion " +
                "FROM producto p " +
                "LEFT JOIN categoria c ON p.idCategoria = c.id " +
                "WHERE p.condicion = 1 AND p.idCategoria = ? " +
                "ORDER BY p.nombreProducto";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, idCategoria);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Producto producto = crearProductoDesdeResultSet(rs);
                    productos.add(producto);
                }
            }
        }

        return productos;
    }
}
