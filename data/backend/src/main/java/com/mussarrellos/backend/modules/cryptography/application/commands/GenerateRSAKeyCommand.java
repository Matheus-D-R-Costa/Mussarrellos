package com.mussarrellos.backend.modules.cryptography.application.commands;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.modules.cryptography.application.dtos.RSAKeyDto;

public record GenerateRSAKeyCommand(int timeout) implements ICommand<RSAKeyDto> {

}
