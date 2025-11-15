<%--
  ============================================
  CARRO.JSP - Carrito de compras
  ============================================
  Descripción: Muestra los productos agregados al carrito
  con opción de imprimir factura en PDF
  Autor: Sistema
  Fecha: 14/11/2025
  ============================================
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.nads.aplicacionweb.sesiones.models.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%
    // Obtener el carrito de la sesión
    DetalleCarro detalleCarro = (DetalleCarro) session.getAttribute("carro");

    // Obtener el nombre de usuario de la sesión
    String username = (String) session.getAttribute("username");

    // Generar número de factura y fecha
    String numeroFactura = "FAC-" + System.currentTimeMillis();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    String fechaActual = sdf.format(new Date());
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Carrito de Compras</title>
    <!-- Bootstrap CSS -->
    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <!-- Estilos personalizados -->
    <link href="<%= request.getContextPath() %>/css/Styles.css" rel="stylesheet">

    <style>
        /* Estilos específicos para la impresión */
        @media print {
            .no-print {
                display: none !important;
            }
            body {
                background: white !important;
            }
            .card {
                border: 1px solid #ddd !important;
                box-shadow: none !important;
            }
        }
    </style>
</head>
<body class="bg-light">
<!-- Barra de navegación (no se imprime) -->
<nav class="navbar navbar-expand-lg navbar-dark no-print">
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
                    <a class="nav-link active" href="<%= request.getContextPath() %>/ver-carro">
                        <i class="bi bi-cart-fill"></i> Carrito
                        <% if (detalleCarro != null && !detalleCarro.getItems().isEmpty()) { %>
                        <span class="cart-count"><%= detalleCarro.getItems().size() %></span>
                        <% } %>
                    </a>
                </li>
                <% if (username != null) { %>
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/logout">
                        <i class="bi bi-box-arrow-right"></i> Cerrar Sesión
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
        <div class="col-md-10">
            <div class="card shadow fade-in">
                <div class="card-header text-white">
                    <h3 class="mb-0 text-center">
                        <i class="bi bi-cart-check-fill"></i> Carrito de Compras
                    </h3>
                </div>
                <div class="card-body">
                    <!-- Información del usuario -->
                    <% if (username != null) { %>
                    <div class="welcome-alert">
                        <i class="bi bi-person-circle"></i>
                        <strong>Cliente:</strong> <%= username %>
                    </div>
                    <% } %>

                    <!-- Verificar si el carrito está vacío -->
                    <% if (detalleCarro == null || detalleCarro.getItems().isEmpty()) { %>
                    <div class="text-center py-5">
                        <i class="bi bi-cart-x" style="font-size: 5rem; color: #ccc;"></i>
                        <h4 class="mt-3 text-muted">Tu carrito está vacío</h4>
                        <p class="text-muted">No hay productos en el carrito de compras</p>
                        <a href="<%= request.getContextPath() %>/products" class="btn btn-primary mt-3">
                            <i class="bi bi-bag-plus"></i> Ir a comprar
                        </a>
                    </div>
                    <% } else { %>
                    <!-- Información de la factura -->
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <p><strong><i class="bi bi-file-earmark-text"></i> Número de Factura:</strong> <%= numeroFactura %></p>
                        </div>
                        <div class="col-md-6 text-end">
                            <p><strong><i class="bi bi-calendar-event"></i> Fecha:</strong> <%= fechaActual %></p>
                        </div>
                    </div>

                    <!-- Tabla de productos -->
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th><i class="bi bi-hash"></i> ID</th>
                                <th><i class="bi bi-box"></i> Producto</th>
                                <th><i class="bi bi-tag"></i> Tipo</th>
                                <th class="text-end"><i class="bi bi-currency-dollar"></i> Precio</th>
                                <th class="text-center"><i class="bi bi-123"></i> Cantidad</th>
                                <th class="text-end"><i class="bi bi-calculator"></i> Subtotal</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (ItemCarro item : detalleCarro.getItems()) { %>
                            <tr>
                                <td><%= item.getProducto().getId() %></td>
                                <td><strong><%= item.getProducto().getNombre() %></strong></td>
                                <td><span class="badge bg-info"><%= item.getProducto().getTipo() %></span></td>
                                <td class="text-end">$<%= String.format("%.2f", item.getProducto().getPrecio()) %></td>
                                <td class="text-center">
                                    <span class="badge bg-secondary"><%= item.getCantidad() %></span>
                                </td>
                                <td class="text-end">
                                    <strong>$<%= String.format("%.2f", item.getSubtotal()) %></strong>
                                </td>
                            </tr>
                            <% } %>
                            </tbody>
                            <tfoot>
                            <tr class="table-active">
                                <td colspan="5" class="text-end">
                                    <h5 class="mb-0">
                                        <i class="bi bi-cash-stack"></i> <strong>TOTAL:</strong>
                                    </h5>
                                </td>
                                <td class="text-end">
                                    <h5 class="mb-0 text-primary">
                                        <strong>$<%= String.format("%.2f", detalleCarro.getTotal()) %></strong>
                                    </h5>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>

                    <!-- Botones de acción (no se imprimen) -->
                    <div class="d-flex justify-content-between mt-4 no-print">
                        <div>
                            <a href="<%= request.getContextPath() %>/products" class="btn btn-outline-primary">
                                <i class="bi bi-arrow-left-circle"></i> Seguir Comprando
                            </a>
                            <a href="<%= request.getContextPath() %>/index.html" class="btn btn-outline-secondary">
                                <i class="bi bi-house"></i> Volver al inicio
                            </a>
                        </div>
                        <div>
                            <button onclick="imprimirFactura()" class="btn btn-success">
                                <i class="bi bi-printer-fill"></i> Imprimir Factura
                            </button>
                            <button onclick="descargarPDF()" class="btn btn-danger">
                                <i class="bi bi-file-pdf-fill"></i> Descargar PDF
                            </button>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>

<!-- Scripts para impresión y generación de PDF -->
<script>
    /**
     * Función para imprimir la factura
     * Abre el diálogo de impresión del navegador
     */
    function imprimirFactura() {
        window.print();
    }

    /**
     * Función para descargar la factura como PDF
     * Utiliza la funcionalidad de impresión del navegador
     * con la opción de "Guardar como PDF"
     */
    function descargarPDF() {
        // Mostrar mensaje informativo
        alert('Se abrirá el diálogo de impresión.\n\n' +
            'Para guardar como PDF:\n' +
            '1. Selecciona "Guardar como PDF" en el destino\n' +
            '2. Haz clic en "Guardar"\n' +
            '3. Elige la ubicación donde guardar el archivo');

        // Abrir diálogo de impresión
        window.print();
    }

    /**
     * Evento que se ejecuta cuando se carga la página
     * Muestra un mensaje de bienvenida si hay productos en el carrito
     */
    window.addEventListener('load', function() {
        <% if (detalleCarro != null && !detalleCarro.getItems().isEmpty()) { %>
        console.log('Carrito cargado con <%= detalleCarro.getItems().size() %> producto(s)');
        <% } %>
    });
</script>
</body>
</html>