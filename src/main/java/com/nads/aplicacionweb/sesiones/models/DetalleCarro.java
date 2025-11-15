package com.nads.aplicacionweb.sesiones.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetalleCarro {
    private List<ItemCarro> items;

    public DetalleCarro() {
        this.items = new ArrayList<>();
    }

    public void addItemCarro(ItemCarro itemCarro) {
        if (items.contains(itemCarro)) {
            Optional<ItemCarro> existingItem = items.stream()
                    .filter(i -> i.equals(itemCarro))
                    .findFirst();
            if (existingItem.isPresent()) {
                ItemCarro i = existingItem.get();
                i.setCantidad(i.getCantidad() + itemCarro.getCantidad());
            }
        } else {
            items.add(itemCarro);
        }
    }

    public List<ItemCarro> getItems() { return items; }

    public double getTotal() {
        return items.stream()
                .mapToDouble(ItemCarro::getSubtotal)
                .sum();
    }
}