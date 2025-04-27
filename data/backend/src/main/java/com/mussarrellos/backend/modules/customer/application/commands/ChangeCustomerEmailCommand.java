package com.mussarrellos.backend.modules.customer.application.commands;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;


import java.util.UUID;


public record ChangeCustomerEmailCommand(UUID clientId, String newEmail) implements ICommandWithoutResult { }