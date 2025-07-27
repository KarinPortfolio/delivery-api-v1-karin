package com.deliverytech.api.model;

public class StatusEntregaCheck {
    public static void main(String[] args) {
        System.out.println("StatusEntrega values:");
        for (StatusEntrega status : StatusEntrega.values()) {
            System.out.println("- " + status.name());
        }
    }
}
