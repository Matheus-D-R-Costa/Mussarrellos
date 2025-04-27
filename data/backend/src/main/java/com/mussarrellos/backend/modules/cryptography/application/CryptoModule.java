package com.mussarrellos.backend.modules.cryptography.application;

import com.mussarrellos.backend.buildingblocks.application.BaseModule;
import com.mussarrellos.backend.modules.cryptography.application.contracts.ICryptoModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CryptoModule extends BaseModule<ICryptoModule> implements ICryptoModule {

    public CryptoModule(ApplicationContext applicationContext) {
        super(applicationContext, ICryptoModule.class);
    }
}
