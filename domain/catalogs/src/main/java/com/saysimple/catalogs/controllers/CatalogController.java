package com.saysimple.catalogs.controllers;

import com.saysimple.catalogs.dto.CatalogDto;
import com.saysimple.catalogs.jpa.CatalogEntity;
import com.saysimple.catalogs.services.CatalogService;
import com.saysimple.catalogs.vo.RequestCatalog;
import com.saysimple.catalogs.vo.ResponseCatalog;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class CatalogController {
    Environment env;
    CatalogService catalogService;

    DiscoveryClient discoveryClient;

    @Autowired
    public CatalogController(Environment env, CatalogService catalogService,DiscoveryClient discoveryClient) {
        this.env = env;
        this.catalogService = catalogService;
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/health-check")
    public String status() {
        List<ServiceInstance> serviceList = getApplications();
        for (ServiceInstance instance : serviceList) {
            System.out.println(String.format("instanceId:%s, serviceId:%s, host:%s, scheme:%s, uri:%s",
                    instance.getInstanceId(), instance.getServiceId(), instance.getHost(), instance.getScheme(), instance.getUri()));
        }

        return String.format("It's Working in Catalog Service on LOCAL PORT %s (SERVER PORT %s)",
                env.getProperty("local.server.port"),
                env.getProperty("server.port"));
    }

    @PostMapping("/catalogs")
    public ResponseEntity<ResponseCatalog> create(@RequestBody RequestCatalog requestCatalog) {
        log.info("create catalog: {}", requestCatalog.getProductId());
        ModelMapper mapper = new ModelMapper();
        CatalogDto catalogDto = mapper.map(requestCatalog, CatalogDto.class);
        CatalogDto createdCatalog = catalogService.create(catalogDto);
        ResponseCatalog responseCatalog = mapper.map(createdCatalog, ResponseCatalog.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseCatalog);
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> list() {
        Iterable<CatalogEntity> catalogList = catalogService.list();

        List<ResponseCatalog> result = new ArrayList<>();
        catalogList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseCatalog.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ResponseCatalog> get(@PathVariable("productId") String productId) {
        CatalogDto catalogDto = catalogService.get(productId);
        if (catalogDto != null) {
            ResponseCatalog responseCatalog = new ModelMapper().map(catalogDto, ResponseCatalog.class);
            return ResponseEntity.status(HttpStatus.OK).body(responseCatalog);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private List<ServiceInstance> getApplications() {

        List<String> services = this.discoveryClient.getServices();
        List<ServiceInstance> instances = new ArrayList<>();
        services.forEach(serviceName -> {
            this.discoveryClient.getInstances(serviceName).forEach(instance ->{
                instances.add(instance);
            });
        });
        return instances;
    }
}
