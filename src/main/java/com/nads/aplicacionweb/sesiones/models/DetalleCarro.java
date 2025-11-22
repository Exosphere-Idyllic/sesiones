package com.nads.aplicacionweb.sesiones.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Modelo DetalleCarro con métodos para gestión de items
 */
public class DetalleCarro {
    private List<ItemCarro> items;

    public DetalleCarro() {
        this.items = new ArrayList<>();
    }

    /**
     * Agrega un item al carrito
     * Si el producto ya existe, suma las cantidades
     */
    public void addItemCarro(ItemCarro itemCarro) {
        if (items.contains(itemCarro)) {
            // Producto ya existe, sumar cantidades
            Optional<ItemCarro> existingItem = items.stream()
                    .filter(i -> i.equals(itemCarro))
                    .findFirst();

            if (existingItem.isPresent()) {
                ItemCarro i = existingItem.get();
                i.setCantidad(i.getCantidad() + itemCarro.getCantidad());
            }
        } else {
            // Producto nuevo, agregarlo
            items.add(itemCarro);
        }
    }

    /**
     * NUEVO: Buscar un item por ID de producto
     * @param productoId ID del producto a buscar
     * @return ItemCarro si existe, null si no existe
     */
    public ItemCarro buscarItem(Long productoId) {
        return items.stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst()
                .orElse(null);
    }

    /**
     * NUEVO: Eliminar un item del carrito
     * @param productoId ID del producto a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminarItem(Long productoId) {
        return items.removeIf(item -> item.getProducto().getId().equals(productoId));
    }

    /**
     * NUEVO: Limpiar todo el carrito
     */
    public void limpiar() {
        items.clear();
    }

    /**
     * NUEVO: Obtener cantidad total de items
     */
    public int getTotalItems() {
        return items.stream()
                .mapToInt(ItemCarro::getCantidad)
                .sum();
    }

    public List<ItemCarro> getItems() {
        return items;
    }

    /**
     * Calcular total del carrito
     */
    public double getTotal() {
        return items.stream()
                .mapToDouble(ItemCarro::getSubtotal)
                .sum();
    }
}