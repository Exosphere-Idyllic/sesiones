package com.nads.aplicacionweb.sesiones.repositories;

import com.nads.aplicacionweb.sesiones.models.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepositoryJdbcImplement implements Repositorio<Categoria> {

    private Connection conn;

    public CategoriaRepositoryJdbcImplement(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT id, nombreCategoria, descripcion, condicion " +
                "FROM categoria " +
                "WHERE condicion = 1 " +
                "ORDER BY nombreCategoria";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categoria categoria = crearCategoriaDesdeResultSet(rs);
                categorias.add(categoria);
            }
        }

        return categorias;
    }

    @Override
    public Categoria porId(Long id) throws SQLException {
        Categoria categoria = null;

        String sql = "SELECT id, nombreCategoria, descripcion, condicion " +
                "FROM categoria " +
                "WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    categoria = crearCategoriaDesdeResultSet(rs);
                }
            }
        }

        return categoria;
    }

    @Override
    public void guardar(Categoria categoria) throws SQLException {
        String sql;

        if (categoria.getId() != null && categoria.getId() > 0) {
            // UPDATE
            sql = "UPDATE categoria SET nombreCategoria = ?, descripcion = ?, " +
                    "condicion = ? WHERE id = ?";
        } else {
            // INSERT
            sql = "INSERT INTO categoria (nombreCategoria, descripcion, condicion) " +
                    "VALUES (?, ?, ?)";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, categoria.getNombreCategoria());
            pstmt.setString(2, categoria.getDescripcion());
            pstmt.setInt(3, categoria.getCondicion());

            if (categoria.getId() != null && categoria.getId() > 0) {
                pstmt.setLong(4, categoria.getId());
            }

            pstmt.executeUpdate();

            // Si es INSERT, obtener el ID generado
            if (categoria.getId() == null || categoria.getId() == 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        categoria.setId(generatedKeys.getLong(1));
                    }
                }
            }
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "UPDATE categoria SET condicion = 0 WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void activar(Long id) throws SQLException {
        String sql = "UPDATE categoria SET condicion = 1 WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    private Categoria crearCategoriaDesdeResultSet(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getLong("id"));
        categoria.setNombreCategoria(rs.getString("nombreCategoria"));
        categoria.setDescripcion(rs.getString("descripcion"));
        categoria.setCondicion(rs.getInt("condicion"));
        return categoria;
    }
}