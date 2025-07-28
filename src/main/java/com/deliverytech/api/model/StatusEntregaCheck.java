package com.deliverytech.api.model;
import jakarta.annotation.Generated;

@Generated(value = "StatusEntregaCheck.class")
public class StatusEntregaCheck {
    public static void main(String[] args) {
        System.out.println("StatusEntrega values:");
        for (StatusEntrega status : StatusEntrega.values()) {
            System.out.println("- " + status.name());
        }
    }
}
