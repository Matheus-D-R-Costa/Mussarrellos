package com.mussarrellos.backend.modules.user.application.queries;

import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import com.mussarrellos.backend.modules.user.application.dtos.ClientDto;
import lombok.Value;

/**
 * Query para buscar um cliente pelo email.
 */
public record GetClientByEmailQuery(String email) implements IQuery<ClientDto> { }