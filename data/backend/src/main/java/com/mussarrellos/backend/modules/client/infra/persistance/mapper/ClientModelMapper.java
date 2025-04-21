package com.mussarrellos.backend.modules.client.infra.persistance.mapper;

import com.mussarrellos.backend.modules.client.domain.entity.Client;
import com.mussarrellos.backend.modules.client.infra.persistance.model.ClientModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientModelMapper {

    ClientModel toModel(Client client);
    Client toDomain(ClientModel clientModel);

}
