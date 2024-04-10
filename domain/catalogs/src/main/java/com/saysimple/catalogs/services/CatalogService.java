package com.saysimple.catalogs.services;

import com.saysimple.catalogs.jpa.CatalogEntity;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}
