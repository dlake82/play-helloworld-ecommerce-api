package com.saysimple.users.clients;

import com.saysimple.users.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="orders")
public interface OrderClient {
    @GetMapping("/orders/{userId}/orders")
     List<ResponseOrder> get(@PathVariable("userId") String userId);
}
