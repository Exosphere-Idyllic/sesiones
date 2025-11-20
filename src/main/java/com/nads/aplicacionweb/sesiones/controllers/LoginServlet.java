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
 * SERVLET DE LOGIN
 * ============================================
 * Descripción: Maneja la autenticación de usuarios
 * Autor: Pablo Aguilar
 * Fecha: 14/11/2025
 * ============================================
 */
@WebServlet({"/login", "/login.html"})
public class LoginServlet extends HttpServlet {

    // Constantes para las credenciales (en producción deberían estar en BD)
    final static String USERNAME = "admin";
    final static String PASSWORD = "12345";

    /**
     * Maneja las solicitudes GET
     * Redirige a la página de login JSP
     *
     * @param req Objeto HttpServletRequest con la solicitud
     * @param resp Objeto HttpServletResponse para la respuesta
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Verificar si el usuario ya está autenticado
        LoginService auth = new LoginServiceSessionImplement();
        Optional<String> username = auth.getUsername(req);

        // Si ya está autenticado, redirigir a productos
        if (username.isPresent()) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        // Si no está autenticado, mostrar página de login
        getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    /**
     * Maneja las solicitudes POST del formulario de login
     * Valida las credenciales y crea la sesión si son correctas
     *
     * @param req Objeto HttpServletRequest con la solicitud
     * @param resp Objeto HttpServletResponse para la respuesta
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Obtener los parámetros del formulario
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Validar que los campos no estén vacíos
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=empty");
            return;
        }

        // Verificar las credenciales
        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            // Credenciales correctas - crear sesión
            HttpSession session = req.getSession();
            session.setAttribute("username", username);

            // Redirigir a la página de productos
            resp.sendRedirect(req.getContextPath() + "/products");
        } else {
            // Credenciales incorrectas - redirigir con mensaje de error
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=invalid");
        }
    }
}