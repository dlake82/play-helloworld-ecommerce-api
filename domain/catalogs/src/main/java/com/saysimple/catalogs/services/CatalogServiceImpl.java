package com.saysimple.catalogs.services;

import com.saysimple.catalogs.dto.CatalogDto;
import com.saysimple.catalogs.jpa.CatalogEntity;
import com.saysimple.catalogs.jpa.CatalogRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
public class CatalogServiceImpl implements CatalogService {
    CatalogRepository catalogRepository;

    @Autowired
    public CatalogServiceImpl(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    // create catalog
    @Override
    public CatalogDto create(CatalogDto catalogDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        CatalogEntity catalogEntity = mapper.map(catalogDto, CatalogEntity.class);
        CatalogEntity catalog = catalogRepository.save(catalogEntity);

        return mapper.map(catalog, CatalogDto.class);
    }

    @Override
    public Iterable<CatalogEntity> list() {
        return catalogRepository.findAll();
    }

    @Override
    public CatalogDto get(String productId) {
        CatalogEntity catalogEntity = catalogRepository.findByProductId(productId);
        if (catalogEntity != null) {
            return new ModelMapper().map(catalogEntity, CatalogDto.class);
        } else {
            return null;
        }
    }
}
