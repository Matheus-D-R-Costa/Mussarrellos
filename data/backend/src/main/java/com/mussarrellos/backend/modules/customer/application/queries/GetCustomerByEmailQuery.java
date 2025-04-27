package com.mussarrellos.backend.modules.customer.application.queries;

import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import com.mussarrellos.backend.modules.customer.application.dtos.CustomerDto;

public record GetCustomerByEmailQuery(String email) implements IQuery<CustomerDto> { }