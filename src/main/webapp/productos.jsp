<%--
  ============================================
  PRODUCTOS.JSP - Listado completo de productos
  ============================================
  Descripción: Vista principal para gestión de productos
  Incluye: Listar, crear, editar, eliminar, agregar al carrito
  Autor: Sistema
  Fecha: 21/11/2025
  ============================================
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.nads.aplicacionweb.sesiones.models.*" %>
<%@ page import="java.util.List" %>
<%
    // Obtener datos del request
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");

    // Obtener datos de sesión
    String username = (String) session.getAttribute("username");
    DetalleCarro carro = (DetalleCarro) session.getAttribute("carro");
    int itemsEnCarro = (carro != null) ? carro.getItems().size() : 0;

    // Obtener mensajes de éxito o error
    String mensaje = (String) session.getAttribute("mensaje");
    String error = (String) session.getAttribute("error");

    // Limpiar mensajes de la sesión después de leerlos
    session.removeAttribute("mensaje");
    session.removeAttribute("error");
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
        /* Estilos adicionales para la tabla de productos */
        .product-table {
            font-size: 0.95rem;
        }

        .product-table th {
            font-weight: 600;
            white-space: nowrap;
        }

        .product-table td {
            vertical-align: middle;
        }

        .btn-group .btn {
            padding: 0.25rem 0.5rem;
        }

        .action-buttons {
            white-space: nowrap;
        }

        /* Animación para botones */
        .btn-action {
            transition: all 0.3s ease;
        }

        .btn-action:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
        }

        /* Badge personalizado para stock */
        .stock-badge {
            font-size: 0.85rem;
            padding: 0.35rem 0.65rem;
        }

        /* Tooltip personalizado */
        .tooltip-inner {
            background-color: #6f42c1;
        }

        .tooltip.bs-tooltip-auto[data-popper-placement^=top] .tooltip-arrow::before,
        .tooltip.bs-tooltip-top .tooltip-arrow::before {
            border-top-color: #6f42c1;
        }
    </style>
</head>
<body class="bg-light">

<!-- ============================================
     BARRA DE NAVEGACIÓN
     ============================================ -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container">
        <a class="navbar-brand" href="<%= request.getContextPath() %>/index.html">
            <i class="bi bi-shop"></i> Sistema de Gestión
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
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
                <!-- Usuario autenticado: Mostrar carrito y logout -->
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
                <!-- Usuario no autenticado: Mostrar login -->
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

<!-- ============================================
     CONTENEDOR PRINCIPAL
     ============================================ -->
<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-12">
            <div class="card shadow fade-in">

                <!-- ============================================
                     ENCABEZADO
                     ============================================ -->
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

                    <!-- ============================================
                         MENSAJES DE ÉXITO O ERROR
                         ============================================ -->
                    <% if (mensaje != null) { %>
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="bi bi-check-circle-fill"></i>
                        <strong>¡Éxito!</strong> <%= mensaje %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <% } %>

                    <% if (error != null) { %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="bi bi-exclamation-triangle-fill"></i>
                        <strong>Error:</strong> <%= error %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <% } %>

                    <!-- ============================================
                         MENSAJE DE BIENVENIDA O INFORMACIÓN
                         ============================================ -->
                    <% if (username != null) { %>
                    <div class="welcome-alert">
                        <i class="bi bi-person-check-fill"></i>
                        <strong>Bienvenido, <%= username %>!</strong>
                        Puedes gestionar productos y agregar al carrito
                    </div>

                    <!-- Botón para crear nuevo producto -->
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
                    <div class="alert alert-info">
                        <i class="bi bi-info-circle-fill"></i>
                        <strong>Inicia sesión</strong> para gestionar productos, ver precios y agregar al carrito
                        <a href="<%= request.getContextPath() %>/login" class="alert-link ms-2">
                            <i class="bi bi-box-arrow-in-right"></i> Iniciar Sesión Aquí
                        </a>
                    </div>
                    <% } %>

                    <!-- ============================================
                         VERIFICAR SI HAY PRODUCTOS
                         ============================================ -->
                    <% if (productos == null || productos.isEmpty()) { %>

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

                    <!-- ============================================
                         TABLA DE PRODUCTOS
                         ============================================ -->
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
                                <!-- Columnas visibles solo para usuarios autenticados -->
                                <th class="text-end" style="width: 10%;"><i class="bi bi-currency-dollar"></i> Precio</th>
                                <th class="text-center" style="width: 8%;"><i class="bi bi-123"></i> Cant.</th>
                                <th class="text-center" style="width: 8%;"><i class="bi bi-cart-plus"></i> Comprar</th>
                                <th class="text-center" style="width: 10%;"><i class="bi bi-gear"></i> Gestión</th>
                                <% } %>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                // Iterar sobre todos los productos
                                for (Producto p : productos) {
                            %>
                            <tr>
                                <!-- ID del producto -->
                                <td><strong><%= p.getId() %></strong></td>

                                <!-- Nombre del producto -->
                                <td>
                                    <strong><%= p.getNombreProducto() %></strong>
                                </td>

                                <!-- Código del producto -->
                                <td>
                                    <% if (p.getCodigo() != null && !p.getCodigo().isEmpty()) { %>
                                    <code><%= p.getCodigo() %></code>
                                    <% } else { %>
                                    <span class="text-muted">-</span>
                                    <% } %>
                                </td>

                                <!-- Categoría -->
                                <td>
                                        <span class="badge bg-info">
                                            <%= p.getNombreCategoria() != null ? p.getNombreCategoria() : "Sin categoría" %>
                                        </span>
                                </td>

                                <!-- Descripción -->
                                <td>
                                    <% if (p.getDescripcion() != null && !p.getDescripcion().isEmpty()) { %>
                                    <small><%= p.getDescripcion() %></small>
                                    <% } else { %>
                                    <span class="text-muted">-</span>
                                    <% } %>
                                </td>

                                <!-- Stock -->
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

                                <!-- Precio (solo si está autenticado) -->
                                <td class="text-end">
                                    <strong style="color: #6f42c1;">
                                        $<%= String.format("%.2f", p.getPrecio()) %>
                                    </strong>
                                </td>

                                <!-- Campo de cantidad (solo si está autenticado) -->
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

                                <!-- Botón para agregar al carrito -->
                                <td class="text-center action-buttons">
                                    <% if (p.getStock() > 0) { %>
                                    <button class="btn btn-primary btn-sm btn-action"
                                            onclick="agregarAlCarrito(<%= p.getId() %>)"
                                            data-bs-toggle="tooltip"
                                            data-bs-placement="top"
                                            title="Agregar al carrito">
                                        <i class="bi bi-cart-plus-fill"></i>
                                    </button>
                                    <% } else { %>
                                    <button class="btn btn-secondary btn-sm" disabled
                                            data-bs-toggle="tooltip"
                                            data-bs-placement="top"
                                            title="Sin stock disponible">
                                        <i class="bi bi-x-circle-fill"></i>
                                    </button>
                                    <% } %>
                                </td>

                                <!-- Botones de gestión (editar/eliminar) -->
                                <td class="text-center action-buttons">
                                    <div class="btn-group" role="group">
                                        <a href="<%= request.getContextPath() %>/producto/form?id=<%= p.getId() %>"
                                           class="btn btn-warning btn-sm btn-action"
                                           data-bs-toggle="tooltip"
                                           data-bs-placement="top"
                                           title="Editar producto">
                                            <i class="bi bi-pencil-square"></i>
                                        </a>
                                        <button class="btn btn-danger btn-sm btn-action"
                                                onclick="confirmarEliminar(<%= p.getId() %>, '<%= p.getNombreProducto().replace("'", "\\'") %>')"
                                                data-bs-toggle="tooltip"
                                                data-bs-placement="top"
                                                title="Eliminar producto">
                                            <i class="bi bi-trash-fill"></i>
                                        </button>
                                    </div>
                                </td>

                                <% } %>
                            </tr>
                            <% } %>
                            </tbody>

                            <!-- Pie de tabla con resumen -->
                            <tfoot>
                            <tr class="table-active">
                                <td colspan="<%= username != null ? "10" : "6" %>" class="text-center">
                                    <small class="text-muted">
                                        <i class="bi bi-info-circle"></i>
                                        Mostrando <%= productos.size() %> producto(s)
                                        <% if (username == null) { %>
                                        | Inicia sesión para ver precios y gestionar productos
                                        <% } %>
                                    </small>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>

                    <% } %>

                    <!-- ============================================
                         BOTONES DE NAVEGACIÓN
                         ============================================ -->
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

<!-- ============================================
     BOOTSTRAP JS
     ============================================ -->
<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>

<!-- ============================================
     SCRIPTS PERSONALIZADOS
     ============================================ -->
<script>
    /**
     * ============================================
     * FUNCIÓN PARA AGREGAR PRODUCTOS AL CARRITO
     * ============================================
     */
    function agregarAlCarrito(idProducto) {
        // Obtener el input de cantidad
        var cantidadInput = document.getElementById('cantidad-' + idProducto);
        var cantidad = cantidadInput ? cantidadInput.value : 1;

        // Validar que la cantidad sea válida
        if (cantidad < 1) {
            alert('La cantidad debe ser mayor a 0');
            return;
        }

        // Obtener el valor máximo (stock disponible)
        var stockDisponible = cantidadInput ? cantidadInput.max : 0;

        if (parseInt(cantidad) > parseInt(stockDisponible)) {
            alert('La cantidad no puede ser mayor al stock disponible (' + stockDisponible + ')');
            return;
        }

        // Redirigir al servlet de agregar carrito
        window.location.href = '<%= request.getContextPath() %>/agregar-carro?id=' + idProducto + '&cantidad=' + cantidad;
    }

    /**
     * ============================================
     * FUNCIÓN PARA CONFIRMAR ELIMINACIÓN
     * ============================================
     */
    function confirmarEliminar(id, nombre) {
        // Mostrar diálogo de confirmación
        var mensaje = '¿Está seguro de que desea eliminar el producto?\n\n' +
            'ID: ' + id + '\n' +
            'Nombre: ' + nombre + '\n\n' +
            'Esta acción no se puede deshacer.';

        if (confirm(mensaje)) {
            // Si confirma, redirigir al servlet de eliminar
            window.location.href = '<%= request.getContextPath() %>/producto/eliminar?id=' + id;
        }
    }

    /**
     * ============================================
     * INICIALIZACIÓN DE TOOLTIPS DE BOOTSTRAP
     * ============================================
     */
    document.addEventListener('DOMContentLoaded', function() {
        // Inicializar todos los tooltips
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });

        // Auto-cerrar alertas después de 5 segundos
        setTimeout(function() {
            var alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                var bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            });
        }, 5000);
    });

    /**
     * ============================================
     * VALIDACIÓN DE CANTIDAD EN TIEMPO REAL
     * ============================================
     */
    document.addEventListener('DOMContentLoaded', function() {
        // Obtener todos los inputs de cantidad
        var cantidadInputs = document.querySelectorAll('input[type="number"][id^="cantidad-"]');

        cantidadInputs.forEach(function(input) {
            input.addEventListener('change', function() {
                var valor = parseInt(this.value);
                var min = parseInt(this.min);
                var max = parseInt(this.max);

                // Validar que esté dentro del rango
                if (valor < min) {
                    this.value = min;
                    alert('La cantidad mínima es ' + min);
                } else if (valor > max) {
                    this.value = max;
                    alert('La cantidad máxima disponible es ' + max);
                }
            });
        });
    });

    /**
     * ============================================
     * LOG DE PRODUCTOS CARGADOS (CONSOLA)
     * ============================================
     */
    <% if (productos != null && !productos.isEmpty()) { %>
    console.log('Productos cargados: <%= productos.size() %>');
    console.log('Usuario autenticado: <%= username != null %>');
    console.log('Items en carrito: <%= itemsEnCarro %>');
    <% } %>
</script>

</body>
</html>