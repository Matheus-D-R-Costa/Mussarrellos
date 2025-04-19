package com.mussarrellos.backend.modules.product;

import lombok.Getter;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.UUID;

@Getter
public abstract class Product {

    private UUID id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;

    private static final UrlValidator URL_VALIDATOR = new UrlValidator(
            new String[] { "http", "https" }, UrlValidator.ALLOW_LOCAL_URLS
    );

    public void ChangeName(String newName) {
        this.name = newName;
    }

    public void ChangeDescription(String newDescription) {
        this.description = newDescription;
    }

    public void ChangePrice(double newPrice) {
        this.price = newPrice;
    }

    public void changeImageUrl(String newImageUrl) {
        this.imageUrl = newImageUrl;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {

        }
    }

    private void validateChange() {

    }



}
