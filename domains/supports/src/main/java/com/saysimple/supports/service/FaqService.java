package com.saysimple.supports.service;

import com.saysimple.supports.vo.*;

import java.util.List;

public interface FaqService {
    ResponseFaq create(RequestFaq faq);

    List<ListFaq> list();

    ResponseFaq get(String faqId);

    ResponseFaq update(RequestUpdateFaq faq);

    void delete(String faqId);
}
