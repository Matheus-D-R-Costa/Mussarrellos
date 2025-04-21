package com.mussarrellos.backend.modules.client.application.queries;

import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import com.mussarrellos.backend.modules.client.application.dtos.ClientDto;

import java.util.UUID;

/**
 * Query para buscar um cliente pelo ID.
 */
public record GetClientByIdQuery(UUID clientId) implements IQuery<ClientDto> { }