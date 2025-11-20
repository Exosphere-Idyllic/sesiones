<%--
  ============================================
  PRODUCTOS JSP - Listado de productos
  ============================================
  Descripción: Muestra el catálogo de productos
  con funcionalidad de agregar al carrito
  Autor: Pablo Aguilar
  Fecha: 20/11/2025
  ============================================
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.nads.aplicacionweb.sesiones.models.*" %>
<%@ page import="java.util.List" %>
<%
    // Obtener datos del request
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    String username = (String) session.getAttribute("username");
    DetalleCarro carro = (DetalleCarro) session.getAttribute("carro");
    int itemsEnCarro = (carro != null) ? carro.getItems().size() : 0;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Productos - Aplicación Web</title>
    <!-- Bootstrap CSS -->
    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <!-- Estilos personalizados -->
    <link href="<%= request.getContextPath() %>/css/Styles.css" rel="stylesheet">
</head>
<body class="bg-light">

<!-- Barra de navegación -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container">
        <a class="navbar-brand" href="<%= request.getContextPath() %>/index.html">
            <i class="bi bi-shop"></i> Aplicación Web
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

<!-- Contenedor principal -->
<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-11">
            <div class="card shadow fade-in">
                <div class="card-header text-white">
                    <h3 class="mb-0 text-center">
                        <i class="bi bi-box-seam-fill"></i> Catálogo de Productos
                    </h3>
                </div>
                <div class="card-body">

                    <!-- Mensaje de bienvenida o información -->
                    <% if (username != null) { %>
                    <div class="welcome-alert">
                        <i class="bi bi-person-circle"></i>
                        <strong>Bienvenido, <%= username %>!</strong>
                        Puedes agregar productos al carrito
                    </div>
                    <% } else { %>
                    <div class="alert alert-info">
                        <i class="bi bi-info-circle"></i>
                        <strong>Inicia sesión</strong> para poder ver precios y agregar productos al carrito
                    </div>
                    <% } %>

                    <!-- Verificar si hay productos -->
                    <% if (productos == null || productos.isEmpty()) { %>
                    <div class="text-center py-5">
                        <i class="bi bi-box-seam" style="font-size: 5rem; color: #ccc;"></i>
                        <h4 class="mt-3 text-muted">No hay productos disponibles</h4>
                        <p class="text-muted">Por favor, intenta más tarde</p>
                    </div>
                    <% } else { %>

                    <!-- Tabla de productos -->
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th><i class="bi bi-hash"></i> ID</th>
                                <th><i class="bi bi-box"></i> Producto</th>
                                <th><i class="bi bi-tag"></i> Categoría</th>
                                <th><i class="bi bi-card-text"></i> Descripción</th>
                                <th class="text-center"><i class="bi bi-boxes"></i> Stock</th>
                                <% if (username != null) { %>
                                <th class="text-end"><i class="bi bi-currency-dollar"></i> Precio</th>
                                <th class="text-center"><i class="bi bi-123"></i> Cantidad</th>
                                <th class="text-center"><i class="bi bi-cart-plus"></i> Acción</th>
                                <% } %>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (Producto p : productos) { %>
                            <tr>
                                <td><%= p.getId() %></td>
                                <td><strong><%= p.getNombreProducto() %></strong></td>
                                <td>
                                        <span class="badge bg-info">
                                            <%= p.getNombreCategoria() != null ? p.getNombreCategoria() : "Sin categoría" %>
                                        </span>
                                </td>
                                <td>
                                    <%= p.getDescripcion() != null ? p.getDescripcion() : "" %>
                                </td>
                                <td class="text-center">
                                    <% if (p.getStock() > 0) { %>
                                    <span class="badge bg-success"><%= p.getStock() %></span>
                                    <% } else { %>
                                    <span class="badge bg-danger">Agotado</span>
                                    <% } %>
                                </td>

                                <% if (username != null) { %>
                                <td class="text-end">
                                    <strong>$<%= String.format("%.2f", p.getPrecio()) %></strong>
                                </td>
                                <td class="text-center">
                                    <% if (p.getStock() > 0) { %>
                                    <input type="number"
                                           class="form-control form-control-sm"
                                           id="cantidad-<%= p.getId() %>"
                                           value="1"
                                           min="1"
                                           max="<%= p.getStock() %>"
                                           style="width: 70px; display: inline-block;">
                                    <% } else { %>
                                    <span class="text-muted">-</span>
                                    <% } %>
                                </td>
                                <td class="text-center">
                                    <% if (p.getStock() > 0) { %>
                                    <button class="btn btn-primary btn-sm"
                                            onclick="agregarAlCarrito(<%= p.getId() %>)">
                                        <i class="bi bi-cart-plus"></i> Agregar
                                    </button>
                                    <% } else { %>
                                    <button class="btn btn-secondary btn-sm" disabled>
                                        <i class="bi bi-x-circle"></i> Agotado
                                    </button>
                                    <% } %>
                                </td>
                                <% } %>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                    <% } %>

                    <!-- Botones de navegación -->
                    <div class="mt-4 text-center">
                        <a href="<%= request.getContextPath() %>/index.html" class="btn btn-outline-secondary">
                            <i class="bi bi-house"></i> Volver al inicio
                        </a>
                        <% if (username != null && itemsEnCarro > 0) { %>
                        <a href="<%= request.getContextPath() %>/ver-carro" class="btn btn-primary">
                            <i class="bi bi-cart-check"></i> Ver Carrito (<%= itemsEnCarro %>)
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

<script>
    /**
     * Función para agregar productos al carrito
     */
    function agregarAlCarrito(idProducto) {
        var cantidadInput = document.getElementById('cantidad-' + idProducto);
        var cantidad = cantidadInput ? cantidadInput.value : 1;

        // Validar cantidad
        if (cantidad < 1) {
            alert('La cantidad debe ser mayor a 0');
            return;
        }

        // Redirigir al servlet de agregar carrito
        window.location.href = '<%= request.getContextPath() %>/agregar-carro?id=' + idProducto + '&cantidad=' + cantidad;
    }
</script>

</body>
</html>