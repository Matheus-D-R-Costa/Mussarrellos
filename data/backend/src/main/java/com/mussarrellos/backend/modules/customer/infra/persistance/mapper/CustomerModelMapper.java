package com.mussarrellos.backend.modules.customer.infra.persistance.mapper;

import com.mussarrellos.backend.modules.customer.domain.entities.Customer;
import com.mussarrellos.backend.modules.customer.infra.persistance.model.CustomerModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerModelMapper {

    CustomerModel toModel(Customer customer);
    Customer toDomain(CustomerModel customerModel);

}
