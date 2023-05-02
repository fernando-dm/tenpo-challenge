package com.tenpo.share.controller;

import com.tenpo.clients.share.ShareResponse;
import com.tenpo.share.services.ShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/share")
public class ShareController {

    private final ShareService shareService;

    public ShareController(ShareService shareService) {
        this.shareService = shareService;
    }

    @GetMapping
    public ResponseEntity<ShareResponse> getShare() {
        Double share = shareService.getShare();
        log.info("Share request for {}", share);
        return ResponseEntity.ok(new ShareResponse(share));
    }

}
