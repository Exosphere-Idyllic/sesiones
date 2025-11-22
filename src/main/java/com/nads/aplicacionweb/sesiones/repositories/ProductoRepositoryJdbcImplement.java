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
        System.out.println("=== INICIANDO CONSULTA DE PRODUCTOS ===");

        String sql = "SELECT p.id, p.nombreProducto, p.idCategoria, c.nombreCategoria, " +
                "p.stock, p.precio, p.descripcion, p.codigo, " +
                "p.fecha_elaboracion, p.fecha_caducidad, p.condicion " +
                "FROM producto p " +
                "LEFT JOIN categoria c ON p.idCategoria = c.id " +
                "WHERE p.condicion = 1 " +
                "ORDER BY p.id";

        System.out.println("SQL ejecutado: " + sql);

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int count = 0;
            while (rs.next()) {
                Producto producto = crearProductoDesdeResultSet(rs);
                productos.add(producto);
                count++;
                System.out.println("Producto " + count + " encontrado: " + producto.getNombreProducto());
            }

            System.out.println("=== TOTAL PRODUCTOS ENCONTRADOS: " + count + " ===");
        } catch (SQLException e) {
            System.err.println("ERROR en listar productos: " + e.getMessage());
            e.printStackTrace();
            throw e;
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
                    System.out.println("Producto encontrado por ID " + id + ": " + producto.getNombreProducto());
                } else {
                    System.out.println("No se encontró producto con ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR en porId: " + e.getMessage());
            throw e;
        }

        return producto;
    }

    @Override
    public void guardar(Producto producto) throws SQLException {
        String sql;

        if (producto.getId() != null && producto.getId() > 0) {
            sql = "UPDATE producto SET nombreProducto = ?, idCategoria = ?, " +
                    "stock = ?, precio = ?, descripcion = ?, codigo = ?, " +
                    "fecha_elaboracion = ?, fecha_caducidad = ?, condicion = ? " +
                    "WHERE id = ?";
        } else {
            sql = "INSERT INTO producto (nombreProducto, idCategoria, stock, precio, " +
                    "descripcion, codigo, fecha_elaboracion, fecha_caducidad, condicion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, producto.getNombreProducto());

            if (producto.getIdCategoria() != null) {
                pstmt.setLong(2, producto.getIdCategoria());
            } else {
                pstmt.setNull(2, Types.BIGINT);
            }

            pstmt.setInt(3, producto.getStock());
            pstmt.setDouble(4, producto.getPrecio());
            pstmt.setString(5, producto.getDescripcion());
            pstmt.setString(6, producto.getCodigo());

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

            int affectedRows = pstmt.executeUpdate();
            System.out.println("Filas afectadas al guardar: " + affectedRows);

            if (producto.getId() == null || producto.getId() == 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        producto.setId(generatedKeys.getLong(1));
                        System.out.println("ID generado: " + producto.getId());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR en guardar producto: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "UPDATE producto SET condicion = 0 WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("Producto eliminado (condicion=0). ID: " + id + ", Filas afectadas: " + affectedRows);
        } catch (SQLException e) {
            System.err.println("ERROR en eliminar producto: " + e.getMessage());
            throw e;
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
     */
    private Producto crearProductoDesdeResultSet(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getLong("id"));
        producto.setNombreProducto(rs.getString("nombreProducto"));
        producto.setIdCategoria(rs.getLong("idCategoria"));

        // Manejar posible NULL en nombreCategoria
        String nombreCategoria = rs.getString("nombreCategoria");
        if (rs.wasNull()) {
            producto.setNombreCategoria("Sin categoría");
        } else {
            producto.setNombreCategoria(nombreCategoria);
        }

        producto.setStock(rs.getInt("stock"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setCodigo(rs.getString("codigo"));

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

    /**
     * ============================================
     * DESCONTAR STOCK DE UN PRODUCTO
     * ============================================
     */
    public void descontarStock(Long id, int cantidad) throws SQLException {
        // Primero verificar que hay stock suficiente
        Producto producto = porId(id);
        if (producto == null) {
            throw new SQLException("Producto no encontrado (ID: " + id + ")");
        }

        if (producto.getStock() < cantidad) {
            throw new SQLException("Stock insuficiente. Disponible: " + producto.getStock() + ", Solicitado: " + cantidad);
        }

        String sql = "UPDATE producto SET stock = stock - ? WHERE id = ? AND condicion = 1";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cantidad);
            pstmt.setLong(2, id);

            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas == 0) {
                throw new SQLException("No se pudo descontar el stock. Producto no encontrado o inactivo (ID: " + id + ")");
            }

            System.out.println("[REPOSITORY] ✓ Stock descontado - Producto ID: " + id + ", Cantidad: " + cantidad);
        } catch (SQLException e) {
            System.err.println("ERROR en descontarStock: " + e.getMessage());
            throw e;
        }
    }

    /**
     * ============================================
     * INCREMENTAR STOCK DE UN PRODUCTO
     * ============================================
     */
    public void incrementarStock(Long id, int cantidad) throws SQLException {
        String sql = "UPDATE producto SET stock = stock + ? WHERE id = ? AND condicion = 1";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cantidad);
            pstmt.setLong(2, id);

            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas == 0) {
                throw new SQLException("No se pudo incrementar el stock. Producto no encontrado o inactivo (ID: " + id + ")");
            }

            System.out.println("[REPOSITORY] ✓ Stock incrementado - Producto ID: " + id + ", Cantidad: " + cantidad);
        } catch (SQLException e) {
            System.err.println("ERROR en incrementarStock: " + e.getMessage());
            throw e;
        }
    }
}