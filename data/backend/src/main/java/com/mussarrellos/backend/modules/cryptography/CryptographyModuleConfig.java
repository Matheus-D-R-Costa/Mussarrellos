package com.mussarrellos.backend.modules.cryptography;

import com.mussarrellos.backend.modules.cryptography.application.CryptoModule;
import com.mussarrellos.backend.modules.cryptography.application.commands.handler.GenerateRSAKeyCommandHandler;
import com.mussarrellos.backend.modules.cryptography.application.contracts.ICryptoModule;
import com.mussarrellos.backend.modules.cryptography.domain.repository.CryptoRepository;
import com.mussarrellos.backend.modules.cryptography.domain.services.CryptoService;
import com.mussarrellos.backend.modules.cryptography.infra.CryptoRepositoryAdapterImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptographyModuleConfig {

    @Bean
    public CryptoRepository cryptoRepository() {
        return new CryptoRepositoryAdapterImpl();
    }

    @Bean
    CryptoService cryptoService(CryptoRepository cryptoRepository) {
        return new CryptoService(cryptoRepository);
    }

    @Bean
    GenerateRSAKeyCommandHandler generateRSAKeyCommandHandler(CryptoService cryptoService) {
        return new GenerateRSAKeyCommandHandler(cryptoService);
    }

    @Bean
    public ICryptoModule cryptoModule(ApplicationContext applicationContext) {
        return new CryptoModule(applicationContext);
    }

}
