package com.mussarrellos.backend.modules.customer.application.commands;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;

import java.util.UUID;

public record RegisterCustomerCommand(String email, String password) implements ICommand<UUID> { }