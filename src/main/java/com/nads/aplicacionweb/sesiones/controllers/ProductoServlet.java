package com.nads.aplicacionweb.sesiones.controllers;

import com.nads.aplicacionweb.sesiones.models.Producto;
import com.nads.aplicacionweb.sesiones.services.ProductoService;
import com.nads.aplicacionweb.sesiones.services.ProductoServiceJdbcImplement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * ============================================
 * SERVLET DE PRODUCTOS ACTUALIZADO
 * ============================================
 * Descripción: Obtiene productos de BD y los
 * envía a la vista JSP
 * Autor: Sistema
 * Fecha: 20/11/2025
 * ============================================
 */
@WebServlet({"/products.html", "/products"})
public class ProductoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Obtener la conexión desde el filtro
        Connection conn = (Connection) req.getAttribute("conn");

        // Crear el servicio con la conexión
        ProductoService service = new ProductoServiceJdbcImplement(conn);

        // Obtener la lista de productos desde la base de datos
        List<Producto> productos = service.listar();

        // Pasar los productos como atributo al JSP
        req.setAttribute("productos", productos);

        // Forward a la vista JSP
        getServletContext()
                .getRequestDispatcher("/productos.jsp")
                .forward(req, resp);
    }
}