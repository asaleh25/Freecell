package cs3500.hw03;

import java.io.IOException;
import java.util.List;

import cs3500.hw02.FreecellOperations;

/**
 * Interface of the FreeCellController parametrized over the card type. Handles the actual
 * running of a game of free cell.
 */
public interface IFreecellController<K> {

  /**
   * Plays a new game of free cell using the model provided. If shuffle is set to false the game
   * runs with the deck as-is, otherwise the deck is shuffled.
   * @param deck The deck that starts the game.
   * @param model The model that represents the state of the FreeCell game.
   * @param numCascades The number of cascade piles in the game, between 4 and 8.
   * @param numOpens The number of open piles in the game, between 1 and 4.
   * @param shuffle Boolean value to determine whether the deck should be shuffled before dealing.
   */
  public void playGame(List<K> deck, FreecellOperations<K> model, int numCascades, int numOpens,
                       boolean shuffle) throws IllegalArgumentException, IOException;
}
