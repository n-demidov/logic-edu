package com.demidovn.fruitbounty.game.services.game.rules.ending;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AddedScoreCalculatorTest {

  private static final int MIN_MODIFIER = 5;

  private final AddedScoreCalculator addedScoreCalculator = new AddedScoreCalculator();

  @Test
  public void whenScoreEquals() {
    int actual = addedScoreCalculator.findWinnerAddedScore(1000, 1000);

    assertThat(actual).isEqualTo(MIN_MODIFIER);
  }

  @Test
  public void whenWinnerAlmostLessAtOneLevel() {
    int actual = addedScoreCalculator.findWinnerAddedScore(1000, 1074);

    assertThat(actual).isEqualTo(MIN_MODIFIER);
  }

  @Test
  public void whenWinnerLessAtOneLevel() {
    int actual = addedScoreCalculator.findWinnerAddedScore(1000, 1075);

    assertThat(actual).isEqualTo(MIN_MODIFIER + 1);
  }

  @Test
  public void whenWinnerLessAtSomeDiff() {
    int actual = addedScoreCalculator.findWinnerAddedScore(1000, 1920);

    assertThat(actual).isEqualTo(MIN_MODIFIER + 12);
  }

  @Test
  public void whenWinnerAlmostBiggerAtOneLevel() {
    int actual = addedScoreCalculator.findWinnerAddedScore(1099, 1000);

    assertThat(actual).isEqualTo(MIN_MODIFIER);
  }

  @Test
  public void whenWinnerBiggerAtOneLevel() {
    int actual = addedScoreCalculator.findWinnerAddedScore(1100, 1000);

    assertThat(actual).isEqualTo(MIN_MODIFIER - 1);
  }

  @Test
  public void whenWinnerBiggerAtSomeDiff() {
    int actual = addedScoreCalculator.findWinnerAddedScore(1799, 1000);

    assertThat(actual).isEqualTo(0);
  }

}