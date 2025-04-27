package com.mussarrellos.backend.modules.customer.application.commands;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;


import java.util.UUID;

/**
 * Comando para alterar o email de um cliente.
 */
public record ChangeClientEmailCommand(UUID clientId, String newEmail) implements ICommandWithoutResult { }