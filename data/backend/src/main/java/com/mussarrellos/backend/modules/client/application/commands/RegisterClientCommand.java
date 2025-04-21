package com.mussarrellos.backend.modules.client.application.commands;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;

import java.util.UUID;

/**
 * Comando para registrar um novo cliente no sistema.
 * Contém todas as informações necessárias para o registro.
 */
public record RegisterClientCommand(String email, String password) implements ICommand<UUID> { }