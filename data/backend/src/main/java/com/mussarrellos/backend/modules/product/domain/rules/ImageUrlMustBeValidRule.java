package com.mussarrellos.backend.modules.product.domain.rules;

import com.mussarrellos.backend.buildingblocks.IBusinessRule;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * Regra de negócio que verifica se a URL da imagem de um produto é válida.
 * A URL deve estar em formato válido e utilizar os protocolos http ou https.
 */
@RequiredArgsConstructor
public class ImageUrlMustBeValidRule implements IBusinessRule {

    private final String imageUrl;
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(
            new String[] { "http", "https" }, UrlValidator.ALLOW_LOCAL_URLS
    );

    @Override
    public boolean isBroken() {
        return imageUrl == null
                || imageUrl.isBlank()
                || !URL_VALIDATOR.isValid(imageUrl);
    }

    @Override
    public String getMessage() {
        return "Product image URL must be a valid URL using http or https protocol";
    }
} 