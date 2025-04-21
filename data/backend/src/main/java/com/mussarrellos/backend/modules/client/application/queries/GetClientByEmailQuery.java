package com.mussarrellos.backend.modules.client.application.queries;

import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import com.mussarrellos.backend.modules.client.application.dtos.ClientDto;

/**
 * Query para buscar um cliente pelo email.
 */
public record GetClientByEmailQuery(String email) implements IQuery<ClientDto> { }