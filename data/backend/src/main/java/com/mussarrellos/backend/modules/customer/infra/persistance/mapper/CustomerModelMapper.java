package com.mussarrellos.backend.modules.customer.infra.persistance.mapper;

import com.mussarrellos.backend.modules.customer.domain.entities.Customer;
import com.mussarrellos.backend.modules.customer.infra.persistance.model.ClientModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientModelMapper {

    ClientModel toModel(Customer customer);
    Customer toDomain(ClientModel clientModel);

}
