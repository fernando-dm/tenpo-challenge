package com.tenpo.apirest.repository;

import com.tenpo.apirest.domain.History;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ApiRestRepositoryShould {


    @Autowired
    private ApiRestRepository apiRestRepository;

    @Test
    @DisplayName("FindHistory method returns list of History")
    public void findHistoryReturnsListOfHistory() {
        // Given
        apiRestRepository.save(new History(1L, "http://localhost:8080/api/v1/apirest/apply-percentage/number1=10.5&number2=10.2", "1", "OK"));
        apiRestRepository.save(new History(2L, "http://localhost:8080/api/v1/apirest/apply-percentage/number1=10.5&number2=10.2", "2", "OK"));
        apiRestRepository.save(new History(3L, "http://localhost:8080/api/v1/apirest/apply-percentage/number1=10.5&number2=10.2", "3", "OK"));

        // When Retrieve history records
        List<History> historyList = apiRestRepository.findHistory();

        // Then Verify that the correct number of history records was returned
        assertEquals(3, historyList.size());
    }
}