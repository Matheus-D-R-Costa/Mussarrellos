package com.mussarrellos.backend.modules.product.domain.entity;

import com.mussarrellos.backend.buildingblocks.Entity;
import com.mussarrellos.backend.modules.product.domain.Money;
import com.mussarrellos.backend.modules.product.domain.entity.types.ProductId;
import com.mussarrellos.backend.modules.product.domain.events.ProductCreatedDomainEvent;
import com.mussarrellos.backend.modules.product.domain.rules.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade de domínio que representa um produto.
 * Contém informações sobre um produto no catálogo.
 */
@Getter
public class Product extends Entity {

    private final ProductId id;
    private String name;
    private String description;
    private Money price;
    private String imageUrl;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Product(ProductId id, String name, String description, Money price, String imageUrl) {
        // Validações usando regras de negócio
        this.checkRule(new NameMustNotBeEmptyRule(name));
        this.checkRule(new DescriptionMustNotBeEmptyRule(description));
        this.checkRule(new NameAndDescriptionMustNotBeTheSameRule(name, description));
        this.checkRule(new MoneyMustBeValid(price));
        this.checkRule(new ImageUrlMustBeValidRule(imageUrl));

        // Inicialização dos campos
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Registro do evento de domínio
        this.addDomainEvent(new ProductCreatedDomainEvent(id.getValue(), name));
    }

    /**
     * Cria um novo produto.
     *
     * @param name        Nome do produto
     * @param description Descrição do produto
     * @param price       Preço do produto
     * @param imageUrl    URL da imagem do produto
     * @return Uma nova instância de Product
     */
    public static Product create(String name, String description, Money price, String imageUrl) {
        ProductId id = new ProductId(UUID.randomUUID());
        return new Product(id, name, description, price, imageUrl);
    }

    /**
     * Altera o nome do produto.
     *
     * @param newName Novo nome do produto
     */
    public void changeName(String newName) {
        this.checkRule(new NameMustNotBeEmptyRule(newName));
        this.checkRule(new NameAndDescriptionMustNotBeTheSameRule(newName, this.description));
        
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
        
        // Aqui poderíamos adicionar um evento de domínio para a mudança de nome
        // this.addDomainEvent(new ProductNameChangedDomainEvent(id.getValue(), newName));
    }

    /**
     * Altera a descrição do produto.
     *
     * @param newDescription Nova descrição do produto
     */
    public void changeDescription(String newDescription) {
        this.checkRule(new DescriptionMustNotBeEmptyRule(newDescription));
        this.checkRule(new NameAndDescriptionMustNotBeTheSameRule(this.name, newDescription));
        
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
        
        // Aqui poderíamos adicionar um evento de domínio para a mudança de descrição
        // this.addDomainEvent(new ProductDescriptionChangedDomainEvent(id.getValue(), newDescription));
    }

    /**
     * Altera o preço do produto.
     *
     * @param newPrice Novo preço do produto
     */
    public void changePrice(Money newPrice) {
        this.checkRule(new MoneyMustBeValid(newPrice));
        
        this.price = newPrice;
        this.updatedAt = LocalDateTime.now();
        
        // Aqui poderíamos adicionar um evento de domínio para a mudança de preço
        // this.addDomainEvent(new ProductPriceChangedDomainEvent(id.getValue(), newPrice));
    }

    /**
     * Altera a URL da imagem do produto.
     *
     * @param newImageUrl Nova URL da imagem do produto
     */
    public void changeImageUrl(String newImageUrl) {
        this.checkRule(new ImageUrlMustBeValidRule(newImageUrl));
        
        this.imageUrl = newImageUrl;
        this.updatedAt = LocalDateTime.now();
        
        // Aqui poderíamos adicionar um evento de domínio para a mudança da URL da imagem
        // this.addDomainEvent(new ProductImageChangedDomainEvent(id.getValue(), newImageUrl));
    }
}
