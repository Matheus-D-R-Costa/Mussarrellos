package com.mussarrellos.backend.modules.customer.application.queries;

import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import com.mussarrellos.backend.modules.customer.application.dtos.CustomerDto;

import java.util.UUID;

public record GetCustomerByIdQuery(UUID clientId) implements IQuery<CustomerDto> { }