package com.saysimple.supports.service;

import com.saysimple.supports.vo.ListSupport;
import com.saysimple.supports.vo.RequestSupport;
import com.saysimple.supports.vo.RequestUpdateSupport;
import com.saysimple.supports.vo.ResponseSupport;

import java.util.List;

public interface SupportService {
    ResponseSupport create(RequestSupport support);

    List<ListSupport> list();

    ResponseSupport get(String supportId);

    ResponseSupport update(RequestUpdateSupport support);

    void delete(String supportId);
}
