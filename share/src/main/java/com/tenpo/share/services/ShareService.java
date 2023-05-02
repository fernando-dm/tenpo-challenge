package com.tenpo.share.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
public class ShareService {
    private static final Random random = new Random();
    private double currentValue = 0.0;

    public Double getShare() {
        return currentValue;
    }

    // Change value every x miliseconds
    // punto a.ii
    @Scheduled(fixedRate = 5000)
    public void randomizeValue() {
        // increase by a minimu,m of 0.01 every 30 sec
        double randomIncrement = 0.01 + (0.01 * random.nextDouble());

        currentValue += randomIncrement;
    }

}
