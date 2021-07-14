package com.marketing.rewards.util;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class Utility {

    /**
     * Get Random {@link Instant} between two given dates <code>startInclusive</code>
     * and <code>endExclusive</code>
     * @param startInclusive {@link Instant} representing the start Date
     * @param endExclusive {@link Instant} representing the end Date
     * @return an {@link Instant} between <code>startInclusive</code> and <code>endExclusive</code>
     */
    private static Instant getDateBetween(Instant startInclusive, Instant endExclusive) {
        long startSeconds = startInclusive.getEpochSecond();
        long endSeconds = endExclusive.getEpochSecond();
        long random = ThreadLocalRandom
                .current()
                .nextLong(startSeconds, endSeconds);

        return Instant.ofEpochSecond(random);
    }

    /**
     *
     * @return a random {@link Instant} in the past 90 days
     */
    public static Instant getRandomDateInPastThreeMonths(){
        Instant ninetyDaysAgo = Instant.now().minus(Duration.ofDays(90));
        Instant tenDaysAgo = Instant.now().minus(Duration.ofDays(10));
        return Utility.getDateBetween(ninetyDaysAgo, Instant.now());
    }
}
