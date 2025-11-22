package com.nads.aplicacionweb.sesiones.controllers;

import com.nads.aplicacionweb.sesiones.models.Categoria;
import com.nads.aplicacionweb.sesiones.models.Producto;
import com.nads.aplicacionweb.sesiones.services.CategoriaService;
import com.nads.aplicacionweb.sesiones.services.CategoriaServiceJdbcImplement;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ============================================
 * PRODUCTO FORM SERVLET
 * ============================================
 * Descripción: Servlet para gestionar el formulario
 * de creación y edición de productos
 *
 * ERRORES CORREGIDOS:
 * 1. Faltaba importar Map y HashMap
 * 2. Variable 'codigo' no estaba declarada
 * 3. LocalDate incompatible con java.util.Date de la BD
 * 4. Método setNombre() debe ser setNombreProducto()
 * 5. Método setCategoriaId() debe ser setIdCategoria()
 * 6. Faltaba método listarCategoria() en el servicio
 *
 * Autor: Pablo Aguilar
 * Fecha: 21/11/2025
 * ============================================
 */
@WebServlet("/producto/form")
public class ProductoFormServlet extends HttpServlet {

    /**
     * Método GET - Muestra el formulario para crear o editar
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verificar autenticación
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Obtener la conexión desde el filtro
        Connection conn = (Connection) req.getAttribute("conn");

        // Crear servicios
        ProductoService productoService = new ProductoServiceJdbcImplement(conn);
        CategoriaService categoriaService = new CategoriaServiceJdbcImplement(conn);

        // Obtener el ID del producto (si existe)
        Long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            id = 0L;
        }

        // Buscar el producto si es edición
        Producto producto = new Producto();
        if (id > 0) {
            Optional<Producto> productoOpt = productoService.porId(id);
            if (productoOpt.isPresent()) {
                producto = productoOpt.get();
            }
        }

        // Obtener lista de categorías
        List<Categoria> categorias = categoriaService.listar();

        // Pasar datos al JSP
        req.setAttribute("categorias", categorias);
        req.setAttribute("producto", producto);

        // Forward al JSP
        getServletContext()
                .getRequestDispatcher("/form.jsp")
                .forward(req, resp);
    }

    /**
     * Método POST - Procesa el formulario (crear o actualizar)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verificar autenticación
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Obtener la conexión desde el filtro
        Connection conn = (Connection) req.getAttribute("conn");

        // Crear servicios
        ProductoService productoService = new ProductoServiceJdbcImplement(conn);
        CategoriaService categoriaService = new CategoriaServiceJdbcImplement(conn);

        // ========================================
        // CAPTURAR PARÁMETROS DEL FORMULARIO
        // ========================================

        String nombre = req.getParameter("nombre");

        // ERROR CORREGIDO: Capturar el parámetro 'codigo' que faltaba
        String codigo = req.getParameter("codigo");

        Long categoriaId;
        try {
            categoriaId = Long.parseLong(req.getParameter("categoria"));
        } catch (NumberFormatException e) {
            categoriaId = 0L;
        }

        Integer stock;
        try {
            stock = Integer.valueOf(req.getParameter("stock"));
        } catch (NumberFormatException e) {
            stock = 0;
        }

        String precioParam = req.getParameter("precio");
        Double precio = 0.0;

        String descripcion = req.getParameter("descripcion");
        String fechaElaboracionStr = req.getParameter("fecha_elaboracion");
        String fechaCaducidadStr = req.getParameter("fecha_caducidad");

        // ========================================
        // VALIDACIÓN DE DATOS
        // ========================================

        // ERROR CORREGIDO: Usar HashMap con tipos correctos
        Map<String, String> errores = new HashMap<>();

        // Validar nombre
        if (nombre == null || nombre.isBlank()) {
            errores.put("nombre", "El nombre no puede estar vacío");
        }

        // Validar categoría
        if (categoriaId.equals(0L)) {
            errores.put("categoria", "Debe seleccionar una categoría");
        }

        // Validar precio
        if (precioParam != null && !precioParam.isBlank()) {
            try {
                precio = Double.valueOf(precioParam);
                if (precio < 0) {
                    errores.put("precio", "El precio no puede ser negativo");
                }
            } catch (NumberFormatException e) {
                errores.put("precio", "El precio debe ser un número válido");
            }
        } else {
            errores.put("precio", "El precio es obligatorio");
        }

        // Validar stock
        if (stock < 0) {
            errores.put("stock", "El stock no puede ser negativo");
        }

        // Validar código
        if (codigo == null || codigo.isBlank()) {
            errores.put("codigo", "El código no puede estar vacío");
        }

        // ========================================
        // CONVERTIR FECHAS
        // ========================================

        // ERROR CORREGIDO: Usar java.util.Date en lugar de LocalDate
        // porque la base de datos y el modelo usan java.util.Date
        Date fechaElaboracion = null;
        Date fechaCaducidad = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (fechaElaboracionStr != null && !fechaElaboracionStr.isBlank()) {
                fechaElaboracion = dateFormat.parse(fechaElaboracionStr);
            }
        } catch (ParseException e) {
            errores.put("fecha_elaboracion", "Formato de fecha inválido");
        }

        try {
            if (fechaCaducidadStr != null && !fechaCaducidadStr.isBlank()) {
                fechaCaducidad = dateFormat.parse(fechaCaducidadStr);
            }
        } catch (ParseException e) {
            errores.put("fecha_caducidad", "Formato de fecha inválido");
        }

        // ========================================
        // PROCESAR EL ID DEL PRODUCTO
        // ========================================

        Long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            id = 0L;
        }

        // ========================================
        // CREAR OBJETO PRODUCTO
        // ========================================

        Producto producto = new Producto();
        producto.setId(id);

        // ERROR CORREGIDO: Usar setNombreProducto() en lugar de setNombre()
        producto.setNombreProducto(nombre);

        producto.setPrecio(precio);

        // ERROR CORREGIDO: Usar setIdCategoria() en lugar de setCategoriaId()
        producto.setIdCategoria(categoriaId);

        producto.setStock(stock);
        producto.setDescripcion(descripcion);
        producto.setCodigo(codigo);
        producto.setFechaElaboracion(fechaElaboracion);
        producto.setFechaCaducidad(fechaCaducidad);
        producto.setCondicion(1); // Activo por defecto

        // ========================================
        // GUARDAR O MOSTRAR ERRORES
        // ========================================

        if (errores.isEmpty()) {
            // No hay errores, guardar el producto
            try {
                productoService.guardar(producto);

                // Mensaje de éxito en la sesión
                session.setAttribute("mensaje",
                        id > 0 ? "Producto actualizado exitosamente" : "Producto creado exitosamente");

                // Redirigir a la lista de productos
                resp.sendRedirect(req.getContextPath() + "/products");
            } catch (Exception e) {
                // Error al guardar
                errores.put("general", "Error al guardar el producto: " + e.getMessage());
                req.setAttribute("errores", errores);
                req.setAttribute("categorias", categoriaService.listar());
                req.setAttribute("producto", producto);
                getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);
            }
        } else {
            // Hay errores, volver a mostrar el formulario
            req.setAttribute("errores", errores);
            req.setAttribute("categorias", categoriaService.listar());
            req.setAttribute("producto", producto);
            getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);
        }
    }
}