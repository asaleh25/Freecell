package cs3500.hw04;

import cs3500.hw02.FreecellModel;

/**
 * Provides a factory method to for creating freecell game depending on what version of the game
 * the user wants to play.
 */
public class FreecellModelCreator {
  public enum GameType { SINGLEMOVE, MULTIMOVE }

  /**
   * Factory method that allows the user to choose whether they want to play a game of freecell
   * that allows multicard moves or not.
   * @param type The GameType the user would like the model to be based off of.
   * @return The appropriate model representing the GameType.
   */
  public static FreecellModel create(GameType type) {
    if (type == GameType.SINGLEMOVE) {
      return new FreecellModel();
    }
    else if (type == GameType.MULTIMOVE) {
      return new MultimoveFreecellModel();
    }
    else {
      throw new IllegalArgumentException("Invalid game type");
    }
  }
}
