package com.tenpo.share;

import com.tenpo.share.services.ShareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ShareServiceTest {

    private ShareService shareService;

    @Mock
    private ThreadPoolTaskScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shareService = new ShareService();
    }

    @Test
    public void testRandomizeValue() throws InterruptedException {
        ShareService shareService = new ShareService();
        double expected = 0.0;

        Thread.sleep(6000L);

        double updatedValue = shareService.getShare();

        assertEquals(expected, updatedValue);
    }


}