package com.nads.aplicacionweb.sesiones.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.nads.aplicacionweb.sesiones.models.DetalleCarro;
import com.nads.aplicacionweb.sesiones.models.ItemCarro;
import com.nads.aplicacionweb.sesiones.models.Producto;
import com.nads.aplicacionweb.sesiones.services.ProductoService;
import com.nads.aplicacionweb.sesiones.services.ProductoServiceImplement;
import java.io.IOException;

/**
 * ============================================
 * SERVLET PARA AGREGAR PRODUCTOS AL CARRITO
 * ============================================
 * Descripción: Agrega productos al carrito de compras
 * Autor: Sistema
 * Fecha: 14/11/2025
 * ============================================
 */
@WebServlet("/agregar-carro")
public class AgregarCarroServlet extends HttpServlet {

    /**
     * Maneja las solicitudes GET para agregar productos al carrito
     *
     * @param req Objeto HttpServletRequest con la solicitud
     * @param resp Objeto HttpServletResponse para la respuesta
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Obtener los parámetros de la solicitud
            Long id = Long.parseLong(req.getParameter("id"));

            // Validar y obtener la cantidad (con valor por defecto de 1)
            int cantidad = 1;
            String cantidadParam = req.getParameter("cantidad");
            if (cantidadParam != null && !cantidadParam.trim().isEmpty()) {
                cantidad = Integer.parseInt(cantidadParam);
            }

            // Validar que la cantidad sea positiva
            if (cantidad <= 0) {
                cantidad = 1;
            }

            // Buscar el producto en el servicio
            ProductoService productoService = new ProductoServiceImplement();
            Producto producto = productoService.porId(id).orElse(null);

            // Verificar si el producto existe
            if (producto != null) {
                // Obtener o crear la sesión
                HttpSession session = req.getSession();

                // Obtener el carrito de la sesión o crear uno nuevo
                DetalleCarro carro = (DetalleCarro) session.getAttribute("carro");

                if (carro == null) {
                    // Si no existe el carrito, crear uno nuevo
                    carro = new DetalleCarro();
                    session.setAttribute("carro", carro);
                }

                // Crear el item del carrito con el producto y la cantidad
                ItemCarro item = new ItemCarro(cantidad, producto);

                // Agregar el item al carrito (se suman cantidades si ya existe)
                carro.addItemCarro(item);
            }

            // Redirigir a la vista del carrito
            resp.sendRedirect(req.getContextPath() + "/ver-carro");

        } catch (NumberFormatException e) {
            // Manejar error de formato de número
            System.err.println("Error al parsear parámetros: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/products");
        } catch (Exception e) {
            // Manejar cualquier otro error
            System.err.println("Error al agregar producto al carrito: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }
}