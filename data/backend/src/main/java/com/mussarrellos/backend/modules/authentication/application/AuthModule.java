package com.mussarrellos.backend.modules.authentication.application;

import com.mussarrellos.backend.buildingblocks.application.BaseModule;
import com.mussarrellos.backend.modules.authentication.application.contracts.IAuthModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthModule extends BaseModule<IAuthModule> implements IAuthModule {

    public AuthModule(ApplicationContext applicationContext) {
        super(applicationContext, IAuthModule.class);
    }

}
