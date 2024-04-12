package com.saysimple.catalogs.services;

import com.saysimple.catalogs.dto.CatalogDto;
import com.saysimple.catalogs.jpa.CatalogEntity;

public interface CatalogService {
    CatalogDto create(CatalogDto catalogDto);
    Iterable<CatalogEntity> list();
    CatalogDto get(String productId);
}
