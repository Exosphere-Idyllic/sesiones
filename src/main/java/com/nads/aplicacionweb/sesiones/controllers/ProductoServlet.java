package com.nads.aplicacionweb.sesiones.controllers;

import com.nads.aplicacionweb.sesiones.models.DetalleCarro;
import com.nads.aplicacionweb.sesiones.models.Producto;
import com.nads.aplicacionweb.sesiones.services.LoginService;
import com.nads.aplicacionweb.sesiones.services.LoginServiceSessionImplement;
import com.nads.aplicacionweb.sesiones.services.ProductoService;
import com.nads.aplicacionweb.sesiones.services.ProductoServiceImplement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

/**
 * ============================================
 * SERVLET DE PRODUCTOS
 * ============================================
 * Descripción: Muestra el listado de productos disponibles
 * Autor: Sistema
 * Fecha: 14/11/2025
 * ============================================
 */
@WebServlet({"/products.html", "/products"})
public class ProductoServlet extends HttpServlet {

    /**
     * Maneja las solicitudes GET para mostrar productos
     * Incluye estilos Bootstrap y paleta morada/celeste
     *
     * @param req Objeto HttpServletRequest con la solicitud
     * @param resp Objeto HttpServletResponse para la respuesta
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Obtener el servicio de productos (CORREGIDO: estaba usando LoginServiceSessionImplement)
        ProductoService service = new ProductoServiceImplement();
        List<Producto> productos = service.listar();

        // Verificar si el usuario está autenticado
        LoginService auth = new LoginServiceSessionImplement();
        Optional<String> usernameOptional = auth.getUsername(req);

        // Obtener información del carrito
        HttpSession session = req.getSession();
        DetalleCarro carro = (DetalleCarro) session.getAttribute("carro");
        int itemsEnCarro = (carro != null) ? carro.getItems().size() : 0;

        // Configurar el tipo de contenido de la respuesta
        resp.setContentType("text/html; charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            // Inicio del documento HTML
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("  <meta charset='UTF-8'>");
            out.println("  <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("  <title>Listado de Productos</title>");

            // Incluir Bootstrap CSS
            out.println("  <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");

            // Incluir Bootstrap Icons
            out.println("  <link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css'>");

            // Incluir estilos personalizados
            out.println("  <link href='" + req.getContextPath() + "/css/Styles.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");

            // Barra de navegación
            out.println("<nav class='navbar navbar-expand-lg navbar-dark'>");
            out.println("  <div class='container'>");
            out.println("    <a class='navbar-brand' href='" + req.getContextPath() + "/index.html'>");
            out.println("      <i class='bi bi-shop'></i> Aplicación Web");
            out.println("    </a>");
            out.println("    <button class='navbar-toggler' type='button' data-bs-toggle='collapse' data-bs-target='#navbarNav'>");
            out.println("      <span class='navbar-toggler-icon'></span>");
            out.println("    </button>");
            out.println("    <div class='collapse navbar-collapse' id='navbarNav'>");
            out.println("      <ul class='navbar-nav ms-auto'>");
            out.println("        <li class='nav-item'>");
            out.println("          <a class='nav-link' href='" + req.getContextPath() + "/index.html'>");
            out.println("            <i class='bi bi-house-door'></i> Inicio");
            out.println("          </a>");
            out.println("        </li>");

            // Mostrar enlace al carrito con contador
            if (usernameOptional.isPresent()) {
                out.println("        <li class='nav-item'>");
                out.println("          <a class='nav-link cart-link' href='" + req.getContextPath() + "/ver-carro'>");
                out.println("            <i class='bi bi-cart-fill'></i> Carrito");
                if (itemsEnCarro > 0) {
                    out.println("            <span class='cart-count'>" + itemsEnCarro + "</span>");
                }
                out.println("          </a>");
                out.println("        </li>");
                out.println("        <li class='nav-item'>");
                out.println("          <a class='nav-link' href='" + req.getContextPath() + "/logout'>");
                out.println("            <i class='bi bi-box-arrow-right'></i> Cerrar Sesión");
                out.println("          </a>");
                out.println("        </li>");
            } else {
                out.println("        <li class='nav-item'>");
                out.println("          <a class='nav-link' href='" + req.getContextPath() + "/login'>");
                out.println("            <i class='bi bi-box-arrow-in-right'></i> Iniciar Sesión");
                out.println("          </a>");
                out.println("        </li>");
            }

            out.println("      </ul>");
            out.println("    </div>");
            out.println("  </div>");
            out.println("</nav>");

            // Contenedor principal
            out.println("<div class='container mt-4'>");
            out.println("  <div class='row justify-content-center'>");
            out.println("    <div class='col-md-10'>");
            out.println("      <div class='card shadow fade-in'>");
            out.println("        <div class='card-header text-white'>");
            out.println("          <h3 class='mb-0 text-center'>");
            out.println("            <i class='bi bi-box-seam-fill'></i> Catálogo de Productos");
            out.println("          </h3>");
            out.println("        </div>");
            out.println("        <div class='card-body'>");

            // Mensaje de bienvenida si está autenticado
            if (usernameOptional.isPresent()) {
                out.println("          <div class='welcome-alert'>");
                out.println("            <i class='bi bi-person-circle'></i>");
                out.println("            <strong>Bienvenido, " + usernameOptional.get() + "!</strong>");
                out.println("            Puedes agregar productos al carrito");
                out.println("          </div>");
            } else {
                out.println("          <div class='alert alert-info'>");
                out.println("            <i class='bi bi-info-circle'></i>");
                out.println("            <strong>Inicia sesión</strong> para poder agregar productos al carrito");
                out.println("          </div>");
            }

            // Tabla de productos con Bootstrap
            out.println("          <div class='table-responsive'>");
            out.println("            <table class='table table-hover'>");
            out.println("              <thead>");
            out.println("                <tr>");
            out.println("                  <th><i class='bi bi-hash'></i> ID</th>");
            out.println("                  <th><i class='bi bi-box'></i> Nombre</th>");
            out.println("                  <th><i class='bi bi-tag'></i> Tipo</th>");

            if (usernameOptional.isPresent()) {
                out.println("                  <th class='text-end'><i class='bi bi-currency-dollar'></i> Precio</th>");
                out.println("                  <th class='text-center'><i class='bi bi-123'></i> Cantidad</th>");
                out.println("                  <th class='text-center'><i class='bi bi-cart-plus'></i> Acción</th>");
            }

            out.println("                </tr>");
            out.println("              </thead>");
            out.println("              <tbody>");

            // Iterar sobre los productos
            productos.forEach(p -> {
                out.println("                <tr>");
                out.println("                  <td>" + p.getId() + "</td>");
                out.println("                  <td><strong>" + p.getNombre() + "</strong></td>");
                out.println("                  <td><span class='badge bg-info'>" + p.getTipo() + "</span></td>");

                if (usernameOptional.isPresent()) {
                    out.println("                  <td class='text-end'>$" + String.format("%.2f", p.getPrecio()) + "</td>");
                    out.println("                  <td class='text-center'>");
                    out.println("                    <input type='number' class='form-control form-control-sm' ");
                    out.println("                           id='cantidad-" + p.getId() + "' value='1' min='1' max='99' ");
                    out.println("                           style='width: 70px; display: inline-block;'>");
                    out.println("                  </td>");
                    out.println("                  <td class='text-center'>");
                    out.println("                    <button class='btn btn-primary btn-sm' ");
                    out.println("                            onclick='agregarAlCarrito(" + p.getId() + ")'>");
                    out.println("                      <i class='bi bi-cart-plus'></i> Agregar");
                    out.println("                    </button>");
                    out.println("                  </td>");
                }

                out.println("                </tr>");
            });

            out.println("              </tbody>");
            out.println("            </table>");
            out.println("          </div>");
            out.println("        </div>");
            out.println("      </div>");
            out.println("    </div>");
            out.println("  </div>");
            out.println("</div>");

            // Scripts de Bootstrap y función para agregar al carrito
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("<script>");
            out.println("  function agregarAlCarrito(idProducto) {");
            out.println("    var cantidad = document.getElementById('cantidad-' + idProducto).value;");
            out.println("    window.location.href = '" + req.getContextPath() + "/agregar-carro?id=' + idProducto + '&cantidad=' + cantidad;");
            out.println("  }");
            out.println("</script>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}
