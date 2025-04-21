package com.mussarrellos.backend.modules.authentication.application.commands;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.modules.authentication.application.dtos.TokenDto;

public record AuthenticateClientCommand(String email,
                                        String password,
                                        String inst,
                                        String fingerprint,
                                        String deviceId) implements ICommand<TokenDto> {
}
