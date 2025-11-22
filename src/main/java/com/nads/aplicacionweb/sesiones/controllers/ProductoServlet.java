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
 * Autor: Pablo Aguilar
 * Fecha: 20/11/2025
 * ============================================
 */
@WebServlet({"/products.html", "/products"})
public class ProductoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println("=== PRODUCTOS SERVLET INICIADO ===");

        // Obtener la conexión desde el filtro
        Connection conn = (Connection) req.getAttribute("conn");
        System.out.println("Conexión obtenida: " + (conn != null));

        if (conn == null) {
            System.err.println("ERROR: La conexión es nula. Verificar el filtro de conexión.");
            req.setAttribute("error", "Error de conexión a la base de datos");
        } else {
            try {
                // Crear el servicio con la conexión
                ProductoService service = new ProductoServiceJdbcImplement(conn);

                // Obtener la lista de productos desde la base de datos
                List<Producto> productos = service.listar();

                System.out.println("Productos obtenidos en servlet: " + productos.size());

                // Pasar los productos como atributo al JSP
                req.setAttribute("productos", productos);

            } catch (Exception e) {
                System.err.println("ERROR en servlet: " + e.getMessage());
                e.printStackTrace();
                req.setAttribute("error", "Error al cargar productos: " + e.getMessage());
            }
        }

        // Forward a la vista JSP
        System.out.println("Forward a productos.jsp");
        getServletContext()
                .getRequestDispatcher("/productos.jsp")
                .forward(req, resp);
    }
}