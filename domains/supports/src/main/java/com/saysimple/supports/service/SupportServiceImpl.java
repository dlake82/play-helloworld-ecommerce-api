package com.saysimple.supports.service;

import com.saysimple.supports.aop.SupportErrorEnum;
import com.saysimple.supports.entity.Support;
import com.saysimple.supports.enums.ContactEnumType;
import com.saysimple.supports.enums.ProductEnumType;
import com.saysimple.supports.repository.SupportRepository;
import com.saysimple.supports.vo.RequestSupport;
import com.saysimple.supports.vo.RequestUpdateSupport;
import com.saysimple.supports.vo.ResponseSupport;
import lombok.extern.slf4j.Slf4j;
import org.saysimple.aop.exception.NotFoundException;
import org.saysimple.utils.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SupportServiceImpl implements SupportService {
    SupportRepository supportRepository;
    Environment env;

    @Autowired
    public SupportServiceImpl(SupportRepository supportRepository) {
        this.supportRepository = supportRepository; }

    // 문의내역 생성
    @Override
    public ResponseSupport create(RequestSupport support) {
        try {
            Support supportEntity = ModelUtils.strictMap(support, Support.class);
            supportRepository.save(supportEntity);
            return ModelUtils.map(supportEntity, ResponseSupport.class);
        } catch (Exception e) {
            // 예외 처리
            throw new RuntimeException("문의내역 생성 중에 오류가 발생했습니다.");
        }
    }
    //전체 리스트 조회
    @Override
    public List<ResponseSupport> list() {
        List<Support> supportEntities = (List<Support>) supportRepository.findAll();

        return supportEntities.stream()
                .map(entity -> ModelUtils.map(entity, ResponseSupport.class))
                .toList();
    }

    //하나의 문의내역 조회
    public ResponseSupport get(String supportId) {
        Support support = supportRepository.findBySupportId(supportId).orElseThrow(() ->
                new NotFoundException(SupportErrorEnum.SUPPORT_NOT_FOUND.getMsg()));

        return ModelUtils.map(support, ResponseSupport.class);
    }

    //문의내역 U
    @Override
    public ResponseSupport update(RequestUpdateSupport support) {
        Support supportEntity = supportRepository.findBySupportId(support.getSupportId()).orElseThrow(() ->
                new NotFoundException((SupportErrorEnum.SUPPORT_NOT_FOUND.getMsg())));

        supportEntity.setSupportId(support.getSupportId());
        supportEntity.setProductId(support.getProductId());
        supportEntity.setUserId(support.getUserId());

        supportEntity.setTitle(support.getTitle());
        supportEntity.setContent(support.getContent());
        supportEntity.setReturnChoice(support.getReturnChoice());
        supportEntity.setContactChoice(ContactEnumType.valueOf(support.getContactChoice()));
        supportEntity.setProductImages(ProductEnumType.valueOf(support.getProductImages()));

        return ModelUtils.map(supportRepository.save(supportEntity), ResponseSupport.class);
    }

    // 문의내역 D
    @Override
    public void delete(String supportId) {
        Support support = supportRepository.findBySupportId(supportId).orElseThrow(() ->
                new NotFoundException(com.saysimple.supports.aop.SupportErrorEnum.SUPPORT_NOT_FOUND.getMsg()));

        supportRepository.delete(support);
    }

    // 문의내역 검색 R

}
