import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.hw02.FreecellModel;
import cs3500.hw02.Card;
import cs3500.hw02.PileType;
import cs3500.hw02.SuitType;

import static org.junit.Assert.assertEquals;

public class FreecellModelTest {
  @Test
  public void testGetDeck() {
    FreecellModel testModel = new FreecellModel();
    List<Card> testDeck = testModel.getDeck();

    assertEquals(52, testDeck.size());

    for (int i = 1; i < 14; i++) {
      assertEquals(new Card(SuitType.CLUBS, i), testDeck.get(i - 1));
    }

    for (int i = 1; i < 14; i++) {
      assertEquals(new Card(SuitType.DIAMONDS, i), testDeck.get(i + 12));
    }

    for (int i = 1; i < 14; i++) {
      assertEquals(new Card(SuitType.HEARTS, i), testDeck.get(i + 25));
    }

    for (int i = 1; i < 14; i++) {
      assertEquals(new Card(SuitType.SPADES, i), testDeck.get(i + 38));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidDeck() {
    List<Card> invalidDeck = new ArrayList<Card>();
    FreecellModel invalidModel = new FreecellModel();

    invalidModel.startGame(invalidDeck, 5, 2, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidNumCascade() {
    FreecellModel testModel = new FreecellModel();
    List<Card> testDeck = testModel.getDeck();

    testModel.startGame(testDeck, 0, 2, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidNumOpen() {
    FreecellModel testModel = new FreecellModel();
    List<Card> testDeck = testModel.getDeck();

    testModel.startGame(testDeck, 4, 11, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidIndexMove() {
    FreecellModel testModel = new FreecellModel();

    testModel.move(PileType.OPEN, -1, 4, PileType.CASCADE, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUnexpectedMove() {
    FreecellModel testModel = new FreecellModel();
    testModel.startGame(testModel.getDeck(), 8, 4, false);

    testModel.move(PileType.FOUNDATION, 3, 6, PileType.OPEN, 2);
  }

  @Test
  public void testGetGameState() {
    FreecellModel testModel = new FreecellModel();
    testModel.startGame(testModel.getDeck(), 8, 4, false);

    String expectedGameState =
            "F1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\n"
                    + "C1: A♣, 9♣, 4♦, Q♦, 7♥, 2♠, 10♠\n"
                    + "C2: 2♣, 10♣, 5♦, K♦, 8♥, 3♠, J♠\n"
                    + "C3: 3♣, J♣, 6♦, A♥, 9♥, 4♠, Q♠\n"
                    + "C4: 4♣, Q♣, 7♦, 2♥, 10♥, 5♠, K♠\n"
                    + "C5: 5♣, K♣, 8♦, 3♥, J♥, 6♠\n"
                    + "C6: 6♣, A♦, 9♦, 4♥, Q♥, 7♠\n"
                    + "C7: 7♣, 2♦, 10♦, 5♥, K♥, 8♠\n"
                    + "C8: 8♣, 3♦, J♦, 6♥, A♠, 9♠";

    assertEquals(expectedGameState, testModel.getGameState());
  }
}