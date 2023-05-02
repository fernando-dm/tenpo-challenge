package com.tenpo.apirest.service;

import com.tenpo.apirest.domain.History;
import com.tenpo.apirest.dto.HistoryDto;
import com.tenpo.apirest.exceptions.ApplyPercentageException;
import com.tenpo.apirest.exceptions.HistoryNotFoundException;
import com.tenpo.apirest.repository.ApiRestRepository;
import com.tenpo.clients.share.ShareClient;
import com.tenpo.clients.share.ShareResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tenpo.apirest.utils.RetryMethod.retryMethod;

@Slf4j
@Service
public class ApiRestService {

    private static ShareResponse response = null;
    private final ApiRestRepository apiRestRepository;
    private final ShareClient shareClient;

    public ApiRestService(ApiRestRepository apiRestRepository, ShareClient shareClient) {
        this.apiRestRepository = apiRestRepository;
        this.shareClient = shareClient;
    }

    public ShareResponse applyPercentage(double number1, double number2) throws ApplyPercentageException {

        ShareResponse percentage = getShareFromCache();
        double result = (number1 + number2) + percentage.shareValue(); //punto a.0
        return new ShareResponse(result);
    }

    @Cacheable("randomValue")
    public ShareResponse getShareFromCache() throws ApplyPercentageException {
        try {
            response = retryMethod(3, "getShare", shareClient::getShare); // punto a.iv
            if (response == null || response.shareValue() == 0) {
                throw new ApplyPercentageException("Share Response is null or 0"); // punto a.iii
            }
        } catch (ApplyPercentageException e) {
            throw new ApplyPercentageException("There is no data, please check share-ms" + e.getMessage());
        } finally {
            return response; //punto a.iii ultimo valor retornado
        }
    }

    @CacheEvict(value = "randomValue", allEntries = true)
    public void cleanCache() {
        log.info("cache clear");
    }

    // punto b
    public void saveHistory(Double result, HttpServletRequest request) throws ApplyPercentageException {
        String resultShareValue = result != null ? result.toString() : getShareFromCache().shareValue().toString();
        History history = History.builder()
                .endpointUrl(getParsedUrl(request))
                .response(resultShareValue)
                .status("OK")
                .build();

        try {
            apiRestRepository.save(history);
        } catch (ApplyPercentageException e) {
            throw new ApplyPercentageException("cant save history" + e.getMessage());
        }
    }

    private String getParsedUrl(HttpServletRequest request) {
        String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
        String uri = request.getRequestURI() + "/";
        String parameters = Optional.ofNullable(request.getParameterMap())
                .map(Map::entrySet)
                .orElse(Collections.emptySet())
                .stream()
                .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                .collect(Collectors.joining("&"));

        String url = baseUrl + uri + parameters;
        return url;
    }

    public List<HistoryDto> getHistory() {

        try {
            List<HistoryDto> history = apiRestRepository.findHistory()
                    .stream().map(it -> new HistoryDto(it.getId(),
                            it.getEndpointUrl(),
                            it.getResponse(),
                            it.getStatus())
                    ).collect(Collectors.toList());
            return history;
        } catch (HistoryNotFoundException e) {
            throw new HistoryNotFoundException("Cant get history" + e.getMessage());
        }
    }
}
