package com.mussarrellos.backend.modules.cryptography.application;

import com.mussarrellos.backend.buildingblocks.application.BaseModule;
import com.mussarrellos.backend.modules.cryptography.application.contracts.ICryptoModule;
import com.mussarrellos.backend.modules.cryptography.application.integration.CryptoIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Implementação do módulo de criptografia.
 * Fornece acesso ao serviço de integração para outros módulos.
 */
@Slf4j
@Service
public class CryptoModule extends BaseModule<ICryptoModule> implements ICryptoModule {

    private final CryptoIntegrationService cryptoIntegrationService;

    public CryptoModule(
            ApplicationContext applicationContext,
            CryptoIntegrationService cryptoIntegrationService) {
        super(applicationContext, ICryptoModule.class);
        this.cryptoIntegrationService = cryptoIntegrationService;
        log.debug("CryptoModule inicializado com serviço de integração");
    }

    @Override
    public CryptoIntegrationService getCryptoIntegrationService() {
        return cryptoIntegrationService;
    }
}
