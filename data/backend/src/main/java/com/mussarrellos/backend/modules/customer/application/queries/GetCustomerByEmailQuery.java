package com.mussarrellos.backend.modules.customer.application.queries;

import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import com.mussarrellos.backend.modules.customer.application.dtos.ClientDto;

/**
 * Query para buscar um cliente pelo email.
 */
public record GetClientByEmailQuery(String email) implements IQuery<ClientDto> { }