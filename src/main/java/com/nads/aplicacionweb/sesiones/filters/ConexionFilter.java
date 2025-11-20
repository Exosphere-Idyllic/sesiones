package com.nads.aplicacionweb.sesiones.filters;

import com.nads.aplicacionweb.sesiones.services.ServiceJdbcException;
import com.nads.aplicacionweb.sesiones.util.Conexion;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * ============================================
 * FILTRO DE CONEXIÓN A BASE DE DATOS
 * ============================================
 * Descripción: Intercepta todas las peticiones para
 * proporcionar una conexión a BD disponible durante
 * toda la petición. La conexión se cierra automáticamente
 * al finalizar.
 *
 * Autor: Pablo Aguilar
 * Fecha: 18/11/2025
 * ============================================
 */
@WebFilter("/*")
public class ConexionFilter implements Filter {

    /*
     * Una clase Filter en Java es un objeto que realiza tareas
     * de filtrado en las solicitudes cliente servidor
     * y respuesta a un recurso: los filtros se puedes ejecutar
     * en servidores compatibles con Jakarta EE
     * Los filtros interceptan solicitudes y respuestas de manera
     * dinámica para transformar o utilizar la información
     * que contienen. El filtrado se realiza mediante el método doFilter
     */

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        /*
         * request: petición que hace el cliente
         * response: respuesta del servidor
         * filterChain: Es una clase de filtro que representa el flujo
         * de procesamiento. Este método llama al método chain.doFilter(request, response)
         * El filtro pasa la solicitud, el siguiente paso la clase
         * filtro o devuelve el recurso destino que puede ser un servlet
         * jsp
         */

        // Obtenemos la conexión
        try (Connection conn = Conexion.getConnection()) {
            // Verificamos que la conexión realizada o se cambien a autocommit
            // configuración automática a la base de datos y cada instrucción (sql)
            if (conn.getAutoCommit()) {
                conn.setAutoCommit(false);
            }

            try {
                // Agregamos la conexión como un atributo en la solcitus
                // Esto nos permite que otros componentes como servlet o DAOS
                // puedan acceder a la conexión
                request.setAttribute("conn", conn);

                // Pasamos la solicitud y la respuesta al siguiente filtro o al recurso destino
                filterChain.doFilter(request, response);

                /*
                 * Si el procesamiento se realizó correctamente sin lanzar excepciones,
                 * se confirma la transacción, y se aplica todos los cambios a la base de datos
                 */
                conn.commit();

            } catch (SQLException e) {
                /*
                 * Si ocurre algún error durante el procesamiento (dentro del doFilter),
                 * se deshace los cambios en la conexión
                 */
                conn.rollback();

                // Enviamos un código de error Http 500 al cliente
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        e.getMessage());
                e.printStackTrace();
            }

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
