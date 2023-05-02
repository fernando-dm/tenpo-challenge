package com.tenpo.apirest.controller;

import com.tenpo.apirest.dto.HistoryDto;
import com.tenpo.apirest.exceptions.ApplyPercentageException;
import com.tenpo.apirest.exceptions.CleanCacheException;
import com.tenpo.apirest.exceptions.HistoryNotFoundException;
import com.tenpo.apirest.service.ApiRestService;
import com.tenpo.clients.share.ShareResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/apirest")
@AllArgsConstructor
public class ApiRestController {

    private final ApiRestService apiRestService;
    private HttpServletRequest request;


    @PostMapping("/apply-percentage")
    @RateLimiter(name = "rateLimiter", fallbackMethod = "customFallback")//punto c
    public ResponseEntity<ShareResponse> applyPercentage(@RequestParam("number1") double number1,
                                                         @RequestParam("number2") double number2) throws ApplyPercentageException {

        if (number1 <= 0 || number2 <= 0)
            throw new ApplyPercentageException("Please provide valid numbers for applyPercentage operation: " + number2);

        log.info("appliying percentage to {}", number1 + number2);
        ShareResponse result = apiRestService.applyPercentage(number1, number2);
        apiRestService.saveHistory(result.shareValue(), request);
        return ResponseEntity.ok(result);
    }

    // Este endpoint es solo para la presentación del proyecto en caso de defenderlo.
    @PostMapping("/clean-cache")
    public ResponseEntity cleanCache() throws CleanCacheException {
        log.info("cleaning cache");
        apiRestService.saveHistory(null, request);
        apiRestService.cleanCache();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<List<HistoryDto>> getHistory(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws HistoryNotFoundException {
        log.info("Getting history (page {} with size {})", pageNo, pageSize);

        List<HistoryDto> history = apiRestService.getHistory();

        int fromIndex = pageNo * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, history.size());

        List<HistoryDto> page = history.subList(fromIndex, toIndex);

        return ResponseEntity.ok(page);
    }

    public ResponseEntity<?> customFallback(double number1, double number2, Exception ex) {
        log.error("Error occurred while processing the request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests. Please try again later.");
    }

}
