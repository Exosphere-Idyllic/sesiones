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
 * AGREGAR CARRO SERVLET
 * ============================================
 * Descripción: Agrega productos al carrito y
 * DESCUENTA el stock inmediatamente de la BD
 *
 * FLUJO:
 * 1. Verificar autenticación
 * 2. Obtener producto de BD
 * 3. Validar stock disponible
 * 4. Descontar stock en BD
 * 5. Agregar al carrito en sesión
 *
 * Autor: Pablo Aguilar
 * Fecha: 21/11/2025
 * ============================================
 */
@WebServlet("/agregar-carro")
public class AgregarCarroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // ========================================
            // 1. VERIFICAR AUTENTICACIÓN
            // ========================================
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("username") == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            // ========================================
            // 2. CAPTURAR Y VALIDAR PARÁMETROS
            // ========================================
            Long id = Long.parseLong(req.getParameter("id"));

            int cantidad = 1;
            String cantidadParam = req.getParameter("cantidad");
            if (cantidadParam != null && !cantidadParam.trim().isEmpty()) {
                cantidad = Integer.parseInt(cantidadParam);
            }

            // Validar que la cantidad sea positiva
            if (cantidad <= 0) {
                cantidad = 1;
            }

            // ========================================
            // 3. OBTENER CONEXIÓN Y SERVICIO
            // ========================================
            Connection conn = (Connection) req.getAttribute("conn");
            ProductoService productoService = new ProductoServiceJdbcImplement(conn);

            // ========================================
            // 4. BUSCAR PRODUCTO EN BASE DE DATOS
            // ========================================
            Optional<Producto> productoOpt = productoService.porId(id);

            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();

                // ========================================
                // 5. VERIFICAR STOCK DISPONIBLE
                // ========================================
                if (producto.getStock() < cantidad) {
                    // No hay stock suficiente
                    session.setAttribute("error",
                            "Stock insuficiente. Solo hay " + producto.getStock() +
                                    " unidad(es) disponible(s)");
                    resp.sendRedirect(req.getContextPath() + "/products");
                    return;
                }

                // ========================================
                // 6. OBTENER O CREAR CARRITO
                // ========================================
                DetalleCarro carro = (DetalleCarro) session.getAttribute("carro");

                if (carro == null) {
                    carro = new DetalleCarro();
                    session.setAttribute("carro", carro);
                }

                // ========================================
                // 7. VERIFICAR SI EL PRODUCTO YA ESTÁ EN EL CARRITO
                // ========================================
                ItemCarro itemExistente = carro.buscarItem(id);

                if (itemExistente != null) {
                    // El producto YA está en el carrito
                    // Verificar si hay stock para la cantidad adicional

                    // IMPORTANTE: El stock actual en BD ya fue descontado
                    // por la cantidad que está en el carrito
                    if (producto.getStock() < cantidad) {
                        session.setAttribute("error",
                                "Ya tienes " + itemExistente.getCantidad() +
                                        " en el carrito. Solo hay " + producto.getStock() +
                                        " unidad(es) adicional(es) disponible(s)");
                        resp.sendRedirect(req.getContextPath() + "/ver-carro");
                        return;
                    }
                }

                // ========================================
                // 8. DESCONTAR STOCK EN LA BASE DE DATOS
                // ========================================
                try {
                    productoService.descontarStock(id, cantidad);

                    // Log para debugging
                    System.out.println("[STOCK DESCONTADO] Producto ID: " + id +
                            " | Stock anterior: " + producto.getStock() +
                            " | Cantidad descontada: " + cantidad +
                            " | Nuevo stock: " + (producto.getStock() - cantidad));

                } catch (Exception e) {
                    // Error al descontar stock (ej: stock insuficiente por concurrencia)
                    session.setAttribute("error",
                            "Error al descontar stock: " + e.getMessage());
                    resp.sendRedirect(req.getContextPath() + "/products");
                    return;
                }

                // ========================================
                // 9. ACTUALIZAR STOCK DEL PRODUCTO EN MEMORIA
                // ========================================
                // Esto actualiza el objeto en memoria, NO la BD
                producto.setStock(producto.getStock() - cantidad);

                // ========================================
                // 10. CREAR ITEM Y AGREGAR AL CARRITO
                // ========================================
                ItemCarro item = new ItemCarro(cantidad, producto);
                carro.addItemCarro(item);

                // ========================================
                // 11. MENSAJE DE ÉXITO
                // ========================================
                session.setAttribute("mensaje",
                        "✓ Producto agregado al carrito. Stock actualizado.");

            } else {
                // Producto no encontrado
                session.setAttribute("error", "Producto no encontrado");
            }

            // ========================================
            // 12. REDIRIGIR AL CARRITO
            // ========================================
            resp.sendRedirect(req.getContextPath() + "/ver-carro");

        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "Parámetros inválidos");
            resp.sendRedirect(req.getContextPath() + "/products");
        } catch (Exception e) {
            System.err.println("Error al agregar producto al carrito: " + e.getMessage());
            e.printStackTrace();
            req.getSession().setAttribute("error",
                    "Error al procesar la solicitud");
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }
}