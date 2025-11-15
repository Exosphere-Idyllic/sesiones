<%--
  ============================================
  LOGIN JSP - Página de inicio de sesión
  ============================================
  Descripción: Formulario de autenticación
  Autor: Sistema
  Fecha: 14/11/2025
  ============================================
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Obtener mensaje de error si existe
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Aplicación Web</title>
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
            </ul>
        </div>
    </div>
</nav>

<!-- Contenedor principal -->
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow fade-in">
                <div class="card-header text-white">
                    <h4 class="text-center mb-0">
                        <i class="bi bi-lock-fill"></i> Iniciar Sesión
                    </h4>
                </div>
                <div class="card-body">
                    <!-- Mostrar mensaje de error si existe -->
                    <% if (error != null) { %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="bi bi-exclamation-triangle-fill"></i>
                        <strong>Error:</strong> Usuario o contraseña incorrectos.
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <% } %>

                    <!-- Información de credenciales de prueba -->
                    <div class="alert alert-info" role="alert">
                        <i class="bi bi-info-circle-fill"></i>
                        <strong>Credenciales de prueba:</strong><br>
                        Usuario: <code>admin</code><br>
                        Contraseña: <code>12345</code>
                    </div>

                    <!-- Formulario de login -->
                    <form action="<%= request.getContextPath() %>/login" method="post">
                        <div class="mb-3">
                            <label for="username" class="form-label">
                                <i class="bi bi-person-fill"></i> Usuario
                            </label>
                            <input type="text"
                                   class="form-control"
                                   id="username"
                                   name="username"
                                   placeholder="Ingrese su usuario"
                                   required
                                   autofocus>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">
                                <i class="bi bi-key-fill"></i> Contraseña
                            </label>
                            <input type="password"
                                   class="form-control"
                                   id="password"
                                   name="password"
                                   placeholder="Ingrese su contraseña"
                                   required>
                        </div>
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="bi bi-box-arrow-in-right"></i> Ingresar
                            </button>
                            <a href="<%= request.getContextPath() %>/index.html" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left"></i> Volver al inicio
                            </a>
                        </div>
                    </form>
                </div>
                <div class="card-footer text-center text-muted">
                    <small>
                        <i class="bi bi-shield-lock"></i>
                        Sistema de gestión de sesiones seguro
                    </small>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>
</body>
</html>