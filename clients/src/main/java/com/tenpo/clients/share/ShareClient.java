package com.tenpo.clients.share;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("share")
public interface ShareClient {

    @GetMapping(path = "api/v1/share")
    ShareResponse getShare();

}
