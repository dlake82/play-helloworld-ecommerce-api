package com.saysimple.supports.service;


import com.saysimple.supports.entity.Support;
import com.saysimple.supports.repository.SupportRepository;
import com.saysimple.supports.vo.ResponseSupport;
import lombok.extern.slf4j.Slf4j;
import org.saysimple.utils.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SupportServiceImpl {
    SupportRepository supportRepository;
    Environment env;

    @Autowired
    public SupportServiceImpl(SupportRepository supportRepository) {
        this.supportRepository = supportRepository;
    }

    public ResponseSupport create(RequestSupport support) {
        Support supportEntity = ModelUtils.strictMap(support, Support.class);

        supportRepository.save(supportEntity);

        return ModelUtils.map(supportEntity, ResponseSupport.class);
    }
}
