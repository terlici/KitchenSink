package com.terlici.kitchensink.injectadvanced;

import java.util.Random;

public class RandomService implements RandomServiceInterface {
    private Random random;
    private int max;

    public RandomService(long seed, int max) {
        random = new Random(seed);
        this.max = max;
    }

    public int nextInt() {
        return random.nextInt(max);
    }
}
