package com.mussarrellos.backend.modules.authentication.application.commands;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.modules.authentication.application.dtos.RSAKeyDto;


public record GenerateRSAKeyCommand(int timeout) implements ICommand<RSAKeyDto> {

}
