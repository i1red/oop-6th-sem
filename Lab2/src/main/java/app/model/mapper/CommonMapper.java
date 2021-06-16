package app.model.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommonMapper<R, W, E> {
    E fromDTO(W writeDTO);
    R toDTO(E entity);
}
