package com.nads.aplicacionweb.sesiones.repositories;

import com.nads.aplicacionweb.sesiones.models.Producto;

import java.sql.SQLException;
import java.util.List;

//Clase generica
public interface Repositorio <T> {
    //Listar
    List<Producto> listar() throws SQLException;
    //llamar por id
    T porid(long id) throws SQLException;
    //Guardar
    void guardar(T t) throws SQLException;
    //borrar
    void borrar(T t) throws SQLException;
    //activar
    void activar(T t) throws SQLException;


}
