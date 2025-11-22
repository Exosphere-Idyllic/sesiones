<%--
  ============================================
  FORM.JSP - Formulario para crear/editar productos
  ============================================
  Descripción: Formulario para agregar o modificar productos
  Autor: Pablo Aguilar
  Fecha: 21/11/2025
  ============================================
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.nads.aplicacionweb.sesiones.models.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
    Producto producto = (Producto) request.getAttribute("producto");
    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
    Map<String, String> errores = (Map<String, String>) request.getAttribute("errores");
    String username = (String) session.getAttribute("username");

    // Verificar autenticación
    if (username == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    // Determinar si es creación o edición
    boolean esEdicion = producto != null && producto.getId() != null && producto.getId() > 0;
    String titulo = esEdicion ? "Editar Producto" : "Nuevo Producto";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= titulo %> - Aplicación Web</title>
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
                    <a class="nav-link" href="<%= request.getContextPath() %>/products">
                        <i class="bi bi-box-seam"></i> Productos
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/logout">
                        <i class="bi bi-box-arrow-right"></i> Cerrar Sesión
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Contenedor principal -->
<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow fade-in">
                <div class="card-header text-white">
                    <h3 class="mb-0 text-center">
                        <i class="bi bi-<%= esEdicion ? "pencil-square" : "plus-circle" %>"></i>
                        <%= titulo %>
                    </h3>
                </div>
                <div class="card-body">

                    <!-- Mensaje de usuario logueado -->
                    <div class="welcome-alert">
                        <i class="bi bi-person-circle"></i>
                        <strong>Usuario:</strong> <%= username %>
                    </div>

                    <!-- Mostrar errores si existen -->
                    <% if (errores != null && !errores.isEmpty()) { %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <h5><i class="bi bi-exclamation-triangle-fill"></i> Errores en el formulario:</h5>
                        <ul class="mb-0">
                            <% for (Map.Entry<String, String> error : errores.entrySet()) { %>
                            <li><strong><%= error.getKey() %>:</strong> <%= error.getValue() %></li>
                            <% } %>
                        </ul>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <% } %>

                    <!-- Formulario -->
                    <form action="<%= request.getContextPath() %>/producto/form" method="post">

                        <!-- Campo oculto para ID (edición) -->
                        <input type="hidden" name="id" value="<%= producto != null && producto.getId() != null ? producto.getId() : "" %>">

                        <!-- Nombre del Producto -->
                        <div class="mb-3">
                            <label for="nombre" class="form-label">
                                <i class="bi bi-box-seam"></i> Nombre del Producto *
                            </label>
                            <input type="text"
                                   class="form-control <%= errores != null && errores.containsKey("nombre") ? "is-invalid" : "" %>"
                                   id="nombre"
                                   name="nombre"
                                   value="<%= producto != null && producto.getNombreProducto() != null ? producto.getNombreProducto() : "" %>"
                                   placeholder="Ej: Laptop HP Core i5"
                                    >
                            <% if (errores != null && errores.containsKey("nombre")) { %>
                            <div class="invalid-feedback"><%= errores.get("nombre") %></div>
                            <% } %>
                        </div>

                        <!-- Categoría -->
                        <div class="mb-3">
                            <label for="categoria" class="form-label">
                                <i class="bi bi-tag"></i> Categoría *
                            </label>
                            <select class="form-select <%= errores != null && errores.containsKey("categoria") ? "is-invalid" : "" %>"
                                    id="categoria"
                                    name="categoria"
                                    >
                                <option value="">Seleccione una categoría</option>
                                <% if (categorias != null) {
                                    for (Categoria cat : categorias) {
                                        boolean seleccionado = producto != null &&
                                                producto.getIdCategoria() != null &&
                                                producto.getIdCategoria().equals(cat.getId());
                                %>
                                <option value="<%= cat.getId() %>" <%= seleccionado ? "selected" : "" %>>
                                    <%= cat.getNombreCategoria() %>
                                </option>
                                <%
                                        }
                                    } %>
                            </select>
                            <% if (errores != null && errores.containsKey("categoria")) { %>
                            <div class="invalid-feedback"><%= errores.get("categoria") %></div>
                            <% } %>
                        </div>

                        <!-- Precio y Stock en la misma fila -->
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="precio" class="form-label">
                                        <i class="bi bi-currency-dollar"></i> Precio *
                                    </label>
                                    <input type="number"
                                           class="form-control <%= errores != null && errores.containsKey("precio") ? "is-invalid" : "" %>"
                                           id="precio"
                                           name="precio"
                                           step="0.01"
                                           min="0"
                                           value="<%= producto != null && producto.getPrecio() > 0 ? producto.getPrecio() : "" %>"
                                           placeholder="0.00"
                                           >
                                    <% if (errores != null && errores.containsKey("precio")) { %>
                                    <div class="invalid-feedback"><%= errores.get("precio") %></div>
                                    <% } %>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="stock" class="form-label">
                                        <i class="bi bi-boxes"></i> Stock *
                                    </label>
                                    <input type="number"
                                           class="form-control <%= errores != null && errores.containsKey("stock") ? "is-invalid" : "" %>"
                                           id="stock"
                                           name="stock"
                                           min="0"
                                           value="<%= producto != null && producto.getStock() > 0 ? producto.getStock() : "0" %>"
                                           >
                                    <% if (errores != null && errores.containsKey("stock")) { %>
                                    <div class="invalid-feedback"><%= errores.get("stock") %></div>
                                    <% } %>
                                </div>
                            </div>
                        </div>

                        <!-- Código -->
                        <div class="mb-3">
                            <label for="codigo" class="form-label">
                                <i class="bi bi-upc-scan"></i> Código del Producto *
                            </label>
                            <input type="text"
                                   class="form-control <%= errores != null && errores.containsKey("codigo") ? "is-invalid" : "" %>"
                                   id="codigo"
                                   name="codigo"
                                   value="<%= producto != null && producto.getCodigo() != null ? producto.getCodigo() : "" %>"
                                   placeholder="Ej: LAP-HP-001"
                                   >
                            <% if (errores != null && errores.containsKey("codigo")) { %>
                            <div class="invalid-feedback"><%= errores.get("codigo") %></div>
                            <% } %>
                        </div>

                        <!-- Descripción -->
                        <div class="mb-3">
                            <label for="descripcion" class="form-label">
                                <i class="bi bi-card-text"></i> Descripción
                            </label>
                            <textarea class="form-control"
                                      id="descripcion"
                                      name="descripcion"
                                      rows="3"
                                      placeholder="Descripción del producto (opcional)"><%= producto != null && producto.getDescripcion() != null ? producto.getDescripcion() : "" %></textarea>
                        </div>

                        <!-- Fechas de Elaboración y Caducidad -->
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="fecha_elaboracion" class="form-label">
                                        <i class="bi bi-calendar-check"></i> Fecha de Elaboración
                                    </label>
                                    <input type="date"
                                           class="form-control <%= errores != null && errores.containsKey("fecha_elaboracion") ? "is-invalid" : "" %>"
                                           id="fecha_elaboracion"
                                           name="fecha_elaboracion"
                                           value="<%= producto != null && producto.getFechaElaboracion() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(producto.getFechaElaboracion()) : "" %>">
                                    <% if (errores != null && errores.containsKey("fecha_elaboracion")) { %>
                                    <div class="invalid-feedback"><%= errores.get("fecha_elaboracion") %></div>
                                    <% } %>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="fecha_caducidad" class="form-label">
                                        <i class="bi bi-calendar-x"></i> Fecha de Caducidad
                                    </label>
                                    <input type="date"
                                           class="form-control <%= errores != null && errores.containsKey("fecha_caducidad") ? "is-invalid" : "" %>"
                                           id="fecha_caducidad"
                                           name="fecha_caducidad"
                                           value="<%= producto != null && producto.getFechaCaducidad() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(producto.getFechaCaducidad()) : "" %>">
                                    <% if (errores != null && errores.containsKey("fecha_caducidad")) { %>
                                    <div class="invalid-feedback"><%= errores.get("fecha_caducidad") %></div>
                                    <% } %>
                                </div>
                            </div>
                        </div>

                        <!-- Nota de campos requeridos -->
                        <div class="alert alert-info">
                            <small><i class="bi bi-info-circle"></i> Los campos marcados con (*) son obligatorios</small>
                        </div>

                        <!-- Botones -->
                        <div class="d-grid gap-2 d-md-flex justify-content-md-between">
                            <a href="<%= request.getContextPath() %>/products" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left"></i> Cancelar
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-save"></i> <%= esEdicion ? "Actualizar" : "Guardar" %> Producto
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>

</body>
</html>