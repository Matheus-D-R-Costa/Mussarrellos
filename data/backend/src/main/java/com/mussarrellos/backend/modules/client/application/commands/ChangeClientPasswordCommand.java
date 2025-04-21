package com.mussarrellos.backend.modules.client.application.commands;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;


import java.util.UUID;

/**
 * Comando para alterar a senha de um cliente.
 */
public record ChangeClientPasswordCommand(UUID clientId, String currentPassword,
                                          String newPassword) implements ICommandWithoutResult {
}