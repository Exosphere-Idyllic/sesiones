package com.nads.aplicacionweb.sesiones.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * ============================================
 * CHECK SESSION SERVLET
 * ============================================
 * Descripción: Verifica si existe una sesión activa
 * y devuelve un JSON con el estado de autenticación
 *
 * Funcionalidad:
 * - Consulta si hay un usuario en sesión
 * - Retorna JSON con el estado (authenticated: true/false)
 * - Usado por index.html para ocultar tarjeta de login
 *
 * Autor: Pablo Aguilar
 * Fecha: 21/11/2025
 * ============================================
 */
@WebServlet("/check-session")
public class CheckSessionServlet extends HttpServlet {

    /**
     * Maneja peticiones GET para verificar estado de sesión
     *
     * @param req HttpServletRequest con la petición
     * @param resp HttpServletResponse para la respuesta JSON
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de I/O
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ========================================
        // CONFIGURAR RESPUESTA COMO JSON
        // ========================================
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Prevenir caché en el navegador
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        // ========================================
        // VERIFICAR SESIÓN EXISTENTE
        // ========================================
        // Obtener la sesión sin crear una nueva (false)
        HttpSession session = req.getSession(false);

        // Variables para almacenar información de sesión
        boolean authenticated = false;
        String username = null;

        // Verificar si existe sesión y tiene usuario
        if (session != null) {
            username = (String) session.getAttribute("username");
            authenticated = (username != null && !username.isEmpty());
        }

        // ========================================
        // CONSTRUIR RESPUESTA JSON
        // ========================================
        PrintWriter out = resp.getWriter();

        // Construir objeto JSON manualmente
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"authenticated\": ").append(authenticated);

        // Agregar información adicional si está autenticado
        if (authenticated && username != null) {
            // Escapar comillas en el username por seguridad
            String safeUsername = username.replace("\"", "\\\"");
            json.append(", \"username\": \"").append(safeUsername).append("\"");
        }

        json.append("}");

        // Enviar respuesta
        out.print(json.toString());
        out.flush();

        // ========================================
        // LOG PARA DEBUGGING (OPCIONAL)
        // ========================================
        System.out.println("[CheckSession] Autenticado: " + authenticated +
                (username != null ? " | Usuario: " + username : ""));
    }

    /**
     * Método POST redirige a GET
     * Para mantener consistencia en el manejo de peticiones
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}