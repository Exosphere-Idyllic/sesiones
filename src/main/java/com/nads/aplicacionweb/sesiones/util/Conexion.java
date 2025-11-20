package com.nads.aplicacionweb.sesiones.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ============================================
 * CLASE DE CONEXIÓN A BASE DE DATOS
 * ============================================
 * Descripción: Gestiona la conexión con la base de datos
 * mediante el patrón Singleton
 * Autor: Pablo Aguilar
 * Fecha: 18/11/2025
 * ============================================
 */
public class Conexion {
    // ACTUALIZADO PARA USAR LA BASE DE DATOS CORRECTA
    private static String url = "jdbc:mysql://localhost:3306/sistema_ventas?useTimezone=true&serverTimezone=UTC";
    private static String username = "root";
    private static String password = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}