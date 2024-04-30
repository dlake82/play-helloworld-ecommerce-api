package com.saysimple.supports.service;

import com.saysimple.supports.vo.RequestSupport;
import com.saysimple.supports.vo.ResponseSupport;

import java.util.List;

public interface SupportService {
    ResponseSupport create(RequestSupport support);

    List<ResponseSupport> list();

    ResponseSupport get(String productId);

    ResponseSupport update(SupportRequestUpdate product);

    void delete(String supportId);
}
