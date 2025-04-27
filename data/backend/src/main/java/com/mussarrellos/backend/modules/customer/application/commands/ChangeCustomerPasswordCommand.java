package com.mussarrellos.backend.modules.customer.application.commands;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;


import java.util.UUID;


public record ChangeCustomerPasswordCommand(UUID clientId, String currentPassword,
                                            String newPassword) implements ICommandWithoutResult { }