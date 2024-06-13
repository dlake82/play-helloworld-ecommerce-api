package com.saysimple.supports.service;

import com.saysimple.supports.aop.SupportErrorEnum;
import com.saysimple.supports.entity.Support;
import com.saysimple.supports.enums.ContactEnumType;
import com.saysimple.supports.enums.ReturnEnumType;
import com.saysimple.supports.repository.SupportRepository;
import com.saysimple.supports.vo.ListSupport;
import com.saysimple.supports.vo.RequestSupport;
import com.saysimple.supports.vo.RequestUpdateSupport;
import com.saysimple.supports.vo.ResponseSupport;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.MappingException;
import org.saysimple.aop.exception.NotFoundException;
import org.saysimple.utils.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
<<<<<<< HEAD
import org.springframework.dao.DataAccessException;
=======
import org.springframework.data.jpa.domain.Specification;
>>>>>>> 3e3dff5c331a19347e114961cb540816e4f702e5
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public ResponseSupport create(RequestSupport support) {
        try {
            Support supportEntity = ModelUtils.strictMap(support, Support.class);
            supportRepository.save(supportEntity);
            return ModelUtils.map(supportEntity, ResponseSupport.class);
        } catch (DataAccessException dae) {
            // 데이터베이스 관련 예외 처리
            throw new RuntimeException("데이터베이스 오류로 인해 문의내역을 생성할 수 없습니다.", dae);
        } catch (MappingException me) {
            // 매핑 관련 예외 처리
            throw new RuntimeException("객체 매핑 중에 오류가 발생했습니다.", me);
        } catch (Exception e) {
            // 기타 예외 처리
            throw new RuntimeException("문의내역 생성 중에 알 수 없는 오류가 발생했습니다.", e);
        }
    }
    //전체 리스트 조회
    @Override
    public List<ListSupport> list() {
        List<Support> supportEntities = (List<Support>) supportRepository.findAll();

        return supportEntities.stream()
                .map(entity -> ModelUtils.map(entity, ListSupport.class))
                .toList();
    }

    //하나의 문의내역 조회
    @Override
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

        supportEntity.setContent(support.getContent());
        supportEntity.setReturnType(ReturnEnumType.valueOf(support.getReturnType()));
        supportEntity.setContactType(ContactEnumType.valueOf(support.getContactType()));
        supportEntity.setProductImages(support.getProductImages());

        return ModelUtils.map(supportRepository.save(supportEntity), ResponseSupport.class);
    }

    // 문의내역 D
    @Override
    public void delete(String supportId) {
        Support support = supportRepository.findBySupportId(supportId).orElseThrow(() ->
                new NotFoundException(com.saysimple.supports.aop.SupportErrorEnum.SUPPORT_NOT_FOUND.getMsg()));

        supportRepository.delete(support);
    }
}
