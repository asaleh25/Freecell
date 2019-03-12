import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

import cs3500.hw02.FreecellModel;
import cs3500.hw03.FreecellController;

public class FreecellControllerTest {

  @Test
  public void TestController() throws IOException {
    FreecellModel testModel = new FreecellModel();
    StringBuffer output = new StringBuffer();
    Reader input = new StringReader("8 4 n C8 1 C4 q");
    FreecellController testController = new FreecellController(input, output);

    testController.playGame(testModel.getDeck(), testModel, 8, 4, false);

    String expectedOutput = "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 9♣, 4♦, Q♦, 7♥, 2♠, 10♠\n" +
            "C2: 2♣, 10♣, 5♦, K♦, 8♥, 3♠, J♠\n" +
            "C3: 3♣, J♣, 6♦, A♥, 9♥, 4♠, Q♠\n" +
            "C4: 4♣, Q♣, 7♦, 2♥, 10♥, 5♠, K♠\n" +
            "C5: 5♣, K♣, 8♦, 3♥, J♥, 6♠\n" +
            "C6: 6♣, A♦, 9♦, 4♥, Q♥, 7♠\n" +
            "C7: 7♣, 2♦, 10♦, 5♥, K♥, 8♠\n" +
            "C8: 8♣, 3♦, J♦, 6♥, A♠, 9♠\n" +
            "Source Pile: \n" +
            "Pile is invalid. Try again\n" +
            "Pile is invalid. Try again\n" +
            "Pile is invalid. Try again\n" +
            "Card Number: \n" +
            "Destination pile: \n" +
            "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 9♣, 4♦, Q♦, 7♥, 2♠, 10♠\n" +
            "C2: 2♣, 10♣, 5♦, K♦, 8♥, 3♠, J♠\n" +
            "C3: 3♣, J♣, 6♦, A♥, 9♥, 4♠, Q♠\n" +
            "C4: 4♣, Q♣, 7♦, 2♥, 10♥, 5♠, K♠\n" +
            "C5: 5♣, K♣, 8♦, 3♥, J♥, 6♠\n" +
            "C6: 6♣, A♦, 9♦, 4♥, Q♥, 7♠\n" +
            "C7: 7♣, 2♦, 10♦, 5♥, K♥, 8♠\n" +
            "C8: 8♣, 3♦, J♦, 6♥, A♠, 9♠\n" +
            "\n" +
            "Source Pile: \n" +
            "Game quit prematurely.";

    assertEquals(expectedOutput, output.toString());
  }
}