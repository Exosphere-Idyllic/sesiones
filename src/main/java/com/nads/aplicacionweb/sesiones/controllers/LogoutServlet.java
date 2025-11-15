package com.nads.aplicacionweb.sesiones.controllers;

import com.nads.aplicacionweb.sesiones.services.LoginService;
import com.nads.aplicacionweb.sesiones.services.LoginServiceSessionImplement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * ============================================
 * SERVLET DE LOGOUT
 * ============================================
 * Descripción: Cierra la sesión del usuario
 * Autor: Sistema
 * Fecha: 14/11/2025
 * ============================================
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Maneja las solicitudes GET para cerrar sesión
     * Invalida la sesión actual y redirige al login
     *
     * @param req Objeto HttpServletRequest con la solicitud
     * @param resp Objeto HttpServletResponse para la respuesta
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Crear instancia del servicio de autenticación
        LoginService auth = new LoginServiceSessionImplement();

        // Verificar si existe un usuario en la sesión
        Optional<String> username = auth.getUsername(req);

        // Si existe un usuario autenticado, cerrar la sesión
        if (username.isPresent()) {
            HttpSession session = req.getSession(false); // No crear nueva sesión

            if (session != null) {
                // Invalidar la sesión (elimina todos los atributos)
                session.invalidate();
            }
        }

        // Redirigir a la página de login
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    /**
     * Maneja las solicitudes POST (igual que GET)
     *
     * @param req Objeto HttpServletRequest con la solicitud
     * @param resp Objeto HttpServletResponse para la respuesta
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Reutilizar la lógica del método GET
        doGet(req, resp);
    }
}