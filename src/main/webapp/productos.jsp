<%--
  ============================================
  PRODUCTOS.JSP - Listado completo de productos
  ============================================
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.nads.aplicacionweb.sesiones.models.*" %>
<%@ page import="java.util.List" %>
<%
    // Obtener datos del request
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    String error = (String) request.getAttribute("error");

    // Obtener datos de sesión
    String username = (String) session.getAttribute("username");
    DetalleCarro carro = (DetalleCarro) session.getAttribute("carro");
    int itemsEnCarro = (carro != null) ? carro.getItems().size() : 0;

    // Obtener mensajes de éxito o error de sesión
    String mensaje = (String) session.getAttribute("mensaje");
    String errorSesion = (String) session.getAttribute("error");

    // Limpiar mensajes de la sesión después de leerlos
    session.removeAttribute("mensaje");
    session.removeAttribute("error");

    System.out.println("JSP - Productos recibidos: " + (productos != null ? productos.size() : "null"));
    System.out.println("JSP - Error recibido: " + error);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Productos - Sistema de Gestión</title>

    <!-- Bootstrap CSS -->
    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>

    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">

    <!-- Estilos personalizados -->
    <link href="<%= request.getContextPath() %>/css/Styles.css" rel="stylesheet">

    <style>
        .product-table { font-size: 0.95rem; }
        .product-table th { font-weight: 600; white-space: nowrap; }
        .product-table td { vertical-align: middle; }
        .btn-action { transition: all 0.3s ease; }
        .btn-action:hover { transform: translateY(-2px); box-shadow: 0 4px 8px rgba(0,0,0,0.2); }
        .stock-badge { font-size: 0.85rem; padding: 0.35rem 0.65rem; }
        .error-container { background: #f8d7da; border: 1px solid #f5c6cb; }
    </style>
</head>
<body class="bg-light">

<!-- Barra de Navegación -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container">
        <a class="navbar-brand" href="<%= request.getContextPath() %>/index.html">
            <i class="bi bi-shop"></i> Sistema de Gestión
        </a>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/index.html">
                        <i class="bi bi-house-door"></i> Inicio
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="<%= request.getContextPath() %>/products">
                        <i class="bi bi-box-seam-fill"></i> Productos
                    </a>
                </li>
                <% if (username != null) { %>
                <li class="nav-item">
                    <a class="nav-link cart-link" href="<%= request.getContextPath() %>/ver-carro">
                        <i class="bi bi-cart-fill"></i> Carrito
                        <% if (itemsEnCarro > 0) { %>
                        <span class="cart-count"><%= itemsEnCarro %></span>
                        <% } %>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/logout">
                        <i class="bi bi-box-arrow-right"></i> Cerrar Sesión
                    </a>
                </li>
                <% } else { %>
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/login">
                        <i class="bi bi-box-arrow-in-right"></i> Iniciar Sesión
                    </a>
                </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>

<!-- Contenedor Principal -->
<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-12">
            <div class="card shadow fade-in">

                <!-- Encabezado -->
                <div class="card-header text-white">
                    <div class="d-flex justify-content-between align-items-center">
                        <h3 class="mb-0">
                            <i class="bi bi-box-seam-fill"></i> Catálogo de Productos
                        </h3>
                        <% if (username != null) { %>
                        <span class="badge bg-light text-primary">
                            <i class="bi bi-person-circle"></i> <%= username %>
                        </span>
                        <% } %>
                    </div>
                </div>

                <div class="card-body">

                    <!-- Mensajes de Error -->
                    <% if (error != null) { %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="bi bi-exclamation-triangle-fill"></i>
                        <strong>Error de Conexión:</strong> <%= error %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <% } %>

                    <% if (errorSesion != null) { %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="bi bi-exclamation-triangle-fill"></i>
                        <strong>Error:</strong> <%= errorSesion %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <% } %>

                    <% if (mensaje != null) { %>
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="bi bi-check-circle-fill"></i>
                        <strong>¡Éxito!</strong> <%= mensaje %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <% } %>

                    <!-- Mensaje de Bienvenida -->
                    <% if (username != null) { %>
                    <div class="alert alert-info">
                        <i class="bi bi-person-check-fill"></i>
                        <strong>Bienvenido, <%= username %>!</strong>
                        Puedes gestionar productos y agregar al carrito
                    </div>

                    <div class="mb-3">
                        <a href="<%= request.getContextPath() %>/producto/form"
                           class="btn btn-success btn-action">
                            <i class="bi bi-plus-circle-fill"></i> Nuevo Producto
                        </a>

                        <% if (itemsEnCarro > 0) { %>
                        <a href="<%= request.getContextPath() %>/ver-carro"
                           class="btn btn-primary btn-action ms-2">
                            <i class="bi bi-cart-check-fill"></i>
                            Ver Carrito (<%= itemsEnCarro %>)
                        </a>
                        <% } %>
                    </div>
                    <% } else { %>
                    <div class="alert alert-warning">
                        <i class="bi bi-info-circle-fill"></i>
                        <strong>Inicia sesión</strong> para gestionar productos, ver precios y agregar al carrito
                        <a href="<%= request.getContextPath() %>/login" class="alert-link ms-2">
                            <i class="bi bi-box-arrow-in-right"></i> Iniciar Sesión Aquí
                        </a>
                    </div>
                    <% } %>

                    <!-- Verificar si hay productos -->
                    <% if (error != null) { %>
                    <!-- Mostrar mensaje de error de conexión -->
                    <div class="text-center py-5 error-container rounded">
                        <i class="bi bi-database-x" style="font-size: 4rem; color: #dc3545;"></i>
                        <h4 class="mt-3 text-danger">Error de Conexión</h4>
                        <p class="text-muted"><%= error %></p>
                        <p class="text-muted">Por favor, contacte al administrador del sistema.</p>
                    </div>
                    <% } else if (productos == null || productos.isEmpty()) { %>
                    <!-- No hay productos -->
                    <div class="text-center py-5">
                        <i class="bi bi-box-seam" style="font-size: 5rem; color: #ccc;"></i>
                        <h4 class="mt-3 text-muted">No hay productos disponibles</h4>
                        <p class="text-muted">
                            <% if (username != null) { %>
                            Comienza agregando tu primer producto
                            <% } else { %>
                            Por favor, intenta más tarde o inicia sesión para gestionar productos
                            <% } %>
                        </p>
                        <% if (username != null) { %>
                        <a href="<%= request.getContextPath() %>/producto/form"
                           class="btn btn-primary mt-3">
                            <i class="bi bi-plus-circle"></i> Crear Primer Producto
                        </a>
                        <% } %>
                    </div>
                    <% } else { %>
                    <!-- Tabla de Productos -->
                    <div class="table-responsive">
                        <table class="table table-hover product-table">
                            <thead>
                            <tr>
                                <th style="width: 5%;"><i class="bi bi-hash"></i> ID</th>
                                <th style="width: 20%;"><i class="bi bi-box"></i> Producto</th>
                                <th style="width: 12%;"><i class="bi bi-upc-scan"></i> Código</th>
                                <th style="width: 12%;"><i class="bi bi-tag"></i> Categoría</th>
                                <th style="width: 20%;"><i class="bi bi-card-text"></i> Descripción</th>
                                <th class="text-center" style="width: 8%;"><i class="bi bi-boxes"></i> Stock</th>

                                <% if (username != null) { %>
                                <th class="text-end" style="width: 10%;"><i class="bi bi-currency-dollar"></i> Precio</th>
                                <th class="text-center" style="width: 8%;"><i class="bi bi-123"></i> Cant.</th>
                                <th class="text-center" style="width: 8%;"><i class="bi bi-cart-plus"></i> Comprar</th>
                                <th class="text-center" style="width: 10%;"><i class="bi bi-gear"></i> Gestión</th>
                                <% } %>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (Producto p : productos) { %>
                            <tr>
                                <td><strong><%= p.getId() %></strong></td>
                                <td><strong><%= p.getNombreProducto() %></strong></td>
                                <td>
                                    <% if (p.getCodigo() != null && !p.getCodigo().isEmpty()) { %>
                                    <code><%= p.getCodigo() %></code>
                                    <% } else { %>
                                    <span class="text-muted">-</span>
                                    <% } %>
                                </td>
                                <td>
                                    <span class="badge bg-info">
                                        <%= p.getNombreCategoria() != null ? p.getNombreCategoria() : "Sin categoría" %>
                                    </span>
                                </td>
                                <td>
                                    <% if (p.getDescripcion() != null && !p.getDescripcion().isEmpty()) { %>
                                    <small><%= p.getDescripcion() %></small>
                                    <% } else { %>
                                    <span class="text-muted">-</span>
                                    <% } %>
                                </td>
                                <td class="text-center">
                                    <% if (p.getStock() > 10) { %>
                                    <span class="badge stock-badge bg-success">
                                        <i class="bi bi-check-circle"></i> <%= p.getStock() %>
                                    </span>
                                    <% } else if (p.getStock() > 0) { %>
                                    <span class="badge stock-badge bg-warning text-dark">
                                        <i class="bi bi-exclamation-circle"></i> <%= p.getStock() %>
                                    </span>
                                    <% } else { %>
                                    <span class="badge stock-badge bg-danger">
                                        <i class="bi bi-x-circle"></i> Agotado
                                    </span>
                                    <% } %>
                                </td>

                                <% if (username != null) { %>
                                <td class="text-end">
                                    <strong style="color: #6f42c1;">
                                        $<%= String.format("%.2f", p.getPrecio()) %>
                                    </strong>
                                </td>
                                <td class="text-center">
                                    <% if (p.getStock() > 0) { %>
                                    <input type="number"
                                           class="form-control form-control-sm text-center"
                                           id="cantidad-<%= p.getId() %>"
                                           value="1"
                                           min="1"
                                           max="<%= p.getStock() %>"
                                           style="width: 60px;">
                                    <% } else { %>
                                    <span class="text-muted">-</span>
                                    <% } %>
                                </td>
                                <td class="text-center action-buttons">
                                    <% if (p.getStock() > 0) { %>
                                    <button class="btn btn-primary btn-sm btn-action"
                                            onclick="agregarAlCarrito(<%= p.getId() %>)"
                                            data-bs-toggle="tooltip"
                                            title="Agregar al carrito">
                                        <i class="bi bi-cart-plus-fill"></i>
                                    </button>
                                    <% } else { %>
                                    <button class="btn btn-secondary btn-sm" disabled
                                            title="Sin stock disponible">
                                        <i class="bi bi-x-circle-fill"></i>
                                    </button>
                                    <% } %>
                                </td>
                                <td class="text-center action-buttons">
                                    <div class="btn-group">
                                        <a href="<%= request.getContextPath() %>/producto/form?id=<%= p.getId() %>"
                                           class="btn btn-warning btn-sm btn-action"
                                           title="Editar producto">
                                            <i class="bi bi-pencil-square"></i>
                                        </a>
                                        <button class="btn btn-danger btn-sm btn-action"
                                                onclick="confirmarEliminar(<%= p.getId() %>, '<%= p.getNombreProducto().replace("'", "\\'") %>')"
                                                title="Eliminar producto">
                                            <i class="bi bi-trash-fill"></i>
                                        </button>
                                    </div>
                                </td>
                                <% } %>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                    <% } %>

                    <!-- Botones de Navegación -->
                    <div class="mt-4 text-center">
                        <a href="<%= request.getContextPath() %>/index.html"
                           class="btn btn-outline-secondary">
                            <i class="bi bi-house"></i> Volver al Inicio
                        </a>
                        <% if (username != null && itemsEnCarro > 0) { %>
                        <a href="<%= request.getContextPath() %>/ver-carro"
                           class="btn btn-primary">
                            <i class="bi bi-cart-check-fill"></i>
                            Ver Carrito (<%= itemsEnCarro %> items)
                        </a>
                        <% } %>
                        <% if (username == null) { %>
                        <a href="<%= request.getContextPath() %>/login"
                           class="btn btn-success">
                            <i class="bi bi-box-arrow-in-right"></i> Iniciar Sesión
                        </a>
                        <% } %>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>

<!-- Scripts Personalizados -->
<script>
    function agregarAlCarrito(idProducto) {
        var cantidadInput = document.getElementById('cantidad-' + idProducto);
        var cantidad = cantidadInput ? cantidadInput.value : 1;

        if (cantidad < 1) {
            alert('La cantidad debe ser mayor a 0');
            return;
        }

        var stockDisponible = cantidadInput ? cantidadInput.max : 0;
        if (parseInt(cantidad) > parseInt(stockDisponible)) {
            alert('La cantidad no puede ser mayor al stock disponible (' + stockDisponible + ')');
            return;
        }

        window.location.href = '<%= request.getContextPath() %>/agregar-carro?id=' + idProducto + '&cantidad=' + cantidad;
    }

    function confirmarEliminar(id, nombre) {
        var mensaje = '¿Está seguro de que desea eliminar el producto?\n\n' +
            'ID: ' + id + '\n' +
            'Nombre: ' + nombre + '\n\n' +
            'Esta acción no se puede deshacer.';

        if (confirm(mensaje)) {
            window.location.href = '<%= request.getContextPath() %>/producto/eliminar?id=' + id;
        }
    }

    document.addEventListener('DOMContentLoaded', function() {
        // Inicializar tooltips
        var tooltips = document.querySelectorAll('[data-bs-toggle="tooltip"]');
        tooltips.forEach(function(tooltip) {
            new bootstrap.Tooltip(tooltip);
        });

        // Auto-cerrar alertas
        setTimeout(function() {
            var alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                var bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            });
        }, 5000);
    });
</script>

</body>
</html>