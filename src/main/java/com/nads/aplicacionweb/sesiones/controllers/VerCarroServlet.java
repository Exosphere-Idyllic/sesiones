package com.nads.aplicacionweb.sesiones.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ============================================
 * SERVLET PARA VER EL CARRITO
 * ============================================
 * Descripción: Redirige a la página JSP del carrito de compras
 * Autor: Sistema
 * Fecha: 14/11/2025
 * ============================================
 */
@WebServlet("/ver-carro")
public class VerCarroServlet extends HttpServlet {

    /**
     * Maneja las solicitudes GET para mostrar el carrito
     * Utiliza el RequestDispatcher para forward a la página JSP
     *
     * @param req Objeto HttpServletRequest con la solicitud
     * @param resp Objeto HttpServletResponse para la respuesta
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Forward a la página JSP del carrito
        // Se usa forward en lugar de redirect para mantener los atributos de la sesión
        getServletContext().getRequestDispatcher("/carro.jsp").forward(req, resp);
    }
}