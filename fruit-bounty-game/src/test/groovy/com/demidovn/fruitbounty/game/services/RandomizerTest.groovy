package com.demidovn.fruitbounty.game.services

import spock.lang.Specification
import spock.lang.Subject

class RandomizerTest extends Specification {

    @Subject
    Randomizer randomizer = new Randomizer()

    def "should 'generateFromRange' works correctly"() {
        setup:
        int inputMin = 2
        int inputMax = 4

        int iterations = 100_000
        Map<Integer, Integer> actualByInput = new HashMap<>()

        when:
        for (int i = 0; i < iterations; i++) {
            def actual = randomizer.generateFromRange(inputMin, inputMax)
            def result = actualByInput.getOrDefault(actual, 0)
            result++
            actualByInput.put(actual, result)
        }

        then:
        actualByInput.size() == 3
        actualByInput.get(2) > 0
        actualByInput.get(3) > 0
        actualByInput.get(4) > 0
    }

    def "should 'getRandomElement' works correctly"() {
        setup:
        List<String> input = new ArrayList<>(Arrays.asList("a", "b", "c"))
        int iterations = 100_000
        Map<String, Long> actualByInput = new HashMap<>()

        when:
        for (int i = 0; i < iterations; i++) {
            def actual = randomizer.getRandomElement(input)
            def result = actualByInput.getOrDefault(actual, 0)
            result++
            actualByInput.put(actual, result)
        }

        then:
        actualByInput.size() == 3
        actualByInput.get("a") > 0
        actualByInput.get("b") > 0
        actualByInput.get("c") > 0
    }

    def "should 'isPercentFired' works correctly"() {
        setup:
        int iterations = 100_000
        Map<Boolean, Long> actualByInput = new HashMap<>()

        when:
        for (int i = 0; i < iterations; i++) {
            def actual = randomizer.isPercentFired(percentChance)
            def result = actualByInput.getOrDefault(actual, 0)
            result++
            actualByInput.put(actual, result)
        }

        then:
        def actual = actualByInput.getOrDefault(true, 0L)
        isPassed(actual, percentChanceCompareMode, firedCount)

        where:
        percentChance  | firedCount | percentChanceCompareMode
        0              | 0          | "equals"
        100            | 100_000    | "equals"

        1              | 1100       | "less"
        10             | 11_000     | "less"
        90             | 81_000     | "greater"
        99             | 89_000     | "greater"
    }

    def "should 'isPercentFired' throw exceptions on wrong input"() {
        when:
        randomizer.isPercentFired(percentChance)

        then:
        def e = thrown(IllegalArgumentException)

        where:
        percentChance  | _
        -1             | _
        -10            | _
        101            | _
        110            | _
    }

    private boolean isPassed(long actual, String percentChanceCompareMode, long trueCount) {
        if ("equals".equals(percentChanceCompareMode)) {
            return actual == trueCount
        } else if ("less".equals(percentChanceCompareMode)) {
            return actual < trueCount
        } else if ("greater".equals(percentChanceCompareMode)) {
            return actual > trueCount
        } else {
            throw new IllegalStateException("Unknown command: " + percentChanceCompareMode)
        }
    }

}
