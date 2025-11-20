package com.nads.aplicacionweb.sesiones.controllers;

import com.nads.aplicacionweb.sesiones.models.DetalleCarro;
import com.nads.aplicacionweb.sesiones.models.ItemCarro;
import com.nads.aplicacionweb.sesiones.models.Producto;
import com.nads.aplicacionweb.sesiones.services.ProductoService;
import com.nads.aplicacionweb.sesiones.services.ProductoServiceJdbcImplement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

/**
 * ============================================
 * SERVLET PARA AGREGAR PRODUCTOS AL CARRITO
 * ============================================
 * Descripción: Agrega productos al carrito desde BD
 * Autor: Sistema
 * Fecha: 20/11/2025
 * ============================================
 */
@WebServlet("/agregar-carro")
public class AgregarCarroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Verificar que el usuario esté autenticado
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("username") == null) {
                // Redirigir al login si no está autenticado
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            // Obtener los parámetros de la solicitud
            Long id = Long.parseLong(req.getParameter("id"));

            // Validar y obtener la cantidad
            int cantidad = 1;
            String cantidadParam = req.getParameter("cantidad");
            if (cantidadParam != null && !cantidadParam.trim().isEmpty()) {
                cantidad = Integer.parseInt(cantidadParam);
            }

            // Validar que la cantidad sea positiva
            if (cantidad <= 0) {
                cantidad = 1;
            }

            // Obtener la conexión desde el filtro
            Connection conn = (Connection) req.getAttribute("conn");

            // Buscar el producto en la base de datos
            ProductoService productoService = new ProductoServiceJdbcImplement(conn);
            Optional<Producto> productoOpt = productoService.porId(id);

            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();

                // Verificar que haya stock suficiente
                if (producto.getStock() < cantidad) {
                    // Stock insuficiente
                    req.getSession().setAttribute("error",
                            "Stock insuficiente. Solo hay " + producto.getStock() + " unidades disponibles");
                    resp.sendRedirect(req.getContextPath() + "/products");
                    return;
                }

                // Obtener el carrito de la sesión o crear uno nuevo
                DetalleCarro carro = (DetalleCarro) session.getAttribute("carro");

                if (carro == null) {
                    carro = new DetalleCarro();
                    session.setAttribute("carro", carro);
                }

                // Crear el item del carrito con el producto y la cantidad
                ItemCarro item = new ItemCarro(cantidad, producto);

                // Agregar el item al carrito
                carro.addItemCarro(item);

                // Mensaje de éxito
                session.setAttribute("mensaje",
                        "Producto agregado al carrito exitosamente");
            } else {
                // Producto no encontrado
                req.getSession().setAttribute("error",
                        "Producto no encontrado");
            }

            // Redirigir a la vista del carrito
            resp.sendRedirect(req.getContextPath() + "/ver-carro");

        } catch (NumberFormatException e) {
            // Error al parsear parámetros
            System.err.println("Error al parsear parámetros: " + e.getMessage());
            req.getSession().setAttribute("error",
                    "Parámetros inválidos");
            resp.sendRedirect(req.getContextPath() + "/products");
        } catch (Exception e) {
            // Error general
            System.err.println("Error al agregar producto al carrito: " + e.getMessage());
            e.printStackTrace();
            req.getSession().setAttribute("error",
                    "Error al agregar producto al carrito");
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }
}