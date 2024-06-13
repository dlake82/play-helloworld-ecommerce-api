package com.saysimple.supports.service;


import com.saysimple.supports.aop.FaqErrorEnum;
import com.saysimple.supports.entity.Faq;
import com.saysimple.supports.repository.FaqRepository;
import com.saysimple.supports.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.saysimple.aop.exception.NotFoundException;
import org.saysimple.utils.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FaqServiceImpl implements FaqService {
    FaqRepository faqRepository;
    Environment env;

    @Autowired
    public FaqServiceImpl(FaqRepository faqRepository) {
        this.faqRepository = faqRepository; }

    // 문의내역 생성
    @Override
    public ResponseFaq create(RequestFaq faq) {
        try {
            Faq faqEntity = ModelUtils.strictMap(faq, Faq.class);
            faqRepository.save(faqEntity);
            return ModelUtils.map(faqEntity, ResponseFaq.class);
        } catch (Exception e) {
            // 예외 처리
            throw new RuntimeException("문의내역 생성 중에 오류가 발생했습니다.");
        }
    }
    //전체 리스트 조회
    @Override
    public List<ListFaq> list() {
        List<Faq> faqEntities = (List<Faq>) faqRepository.findAll();

        return faqEntities.stream()
                .map(entity -> ModelUtils.map(entity, ListFaq.class))
                .toList();
    }

    //하나의 문의내역 조회
    @Override
    public ResponseFaq get(String faqId) {
        Faq faq = faqRepository.findByFaqId(faqId).orElseThrow(() ->
                new NotFoundException(FaqErrorEnum.FAQ_NOT_FOUND.getMsg()));

        return ModelUtils.map(faq, ResponseFaq.class);
    }

    //문의내역 U
    @Override
    public ResponseFaq update(RequestUpdateFaq faq) {
        Faq faqEntity = faqRepository.findByFaqId(faq.getFaqId()).orElseThrow(() ->
                new NotFoundException((FaqErrorEnum.FAQ_NOT_FOUND.getMsg())));

        faqEntity.setFaqId(faq.getFaqId());
        faqEntity.setUserId(faq.getUserId());

        faqEntity.setTitle(faq.getTitle());
        faqEntity.setContent(faq.getContent());

        return ModelUtils.map(faqRepository.save(faqEntity), ResponseFaq.class);
    }

    // 문의내역 D
    @Override
    public void delete(String faqId) {
        Faq faq = faqRepository.findByFaqId(faqId).orElseThrow(() ->
                new NotFoundException(com.saysimple.supports.aop.FaqErrorEnum.FAQ_NOT_FOUND.getMsg()));

        faqRepository.delete(faq);
    }
}