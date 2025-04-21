package com.mussarrellos.backend.modules.cryptography.application.contracts;

import com.mussarrellos.backend.buildingblocks.application.IModule;
import com.mussarrellos.backend.modules.cryptography.application.integration.CryptoIntegrationService;

/**
 * Interface do módulo de criptografia.
 * Além de estender IModule (para comandos/queries genéricos), 
 * expõe um método específico para obter o serviço de integração.
 */
public interface ICryptoModule extends IModule {
    
    /**
     * Obtém o serviço de integração que expõe operações criptográficas para outros módulos.
     * Este método expõe explicitamente os contratos que podem ser usados por outros módulos.
     *
     * @return O serviço de integração de criptografia
     */
    CryptoIntegrationService getCryptoIntegrationService();
}
