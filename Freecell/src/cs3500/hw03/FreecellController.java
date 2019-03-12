package cs3500.hw03;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import cs3500.hw02.Card;
import cs3500.hw02.FreecellModel;
import cs3500.hw02.FreecellOperations;
import cs3500.hw02.PileType;

/**
 * Implementation of the Freecell Controller interface. Provides functionality to play a game of
 * freecell independent of the model used.
 */
public class FreecellController implements IFreecellController<Card> {

  private Appendable ap;

  private int numCascade;
  private int numOpen;

  private boolean shuffle;
  private String shuffleAsString;

  Scanner sc;

  /**
   * Constructor, Builds a new instance of the FreecellConstroller.
   *
   * @param rd Input from the user
   * @param ap Output to the user
   */
  public FreecellController(Readable rd, Appendable ap) throws IllegalArgumentException {
    if (rd == null || ap == null) {
      throw new IllegalArgumentException("Invalid input or output.");
    }

    FreecellModel model = new FreecellModel();
    this.ap = ap;
    this.numCascade = 0;
    this.numOpen = 0;
    this.shuffleAsString = "";
    this.shuffle = false;


    sc = new Scanner(rd);
  }

  @Override
  public void playGame(List<Card> deck, FreecellOperations<Card> model, int numCascades,
                       int numOpens, boolean shuffle) throws IllegalArgumentException, IOException {
    if (deck == null || model == null || deck.isEmpty()) {
      throw new IllegalArgumentException("Invalid deck of model. Ensure they are not null");
    }

    try {
      model.startGame(deck, numCascades, numOpens, shuffle);
    } catch (IllegalArgumentException e) {
      try {
        ap.append("Unable to start game.");
        return;
      } catch (IOException io) {
        return;
      }
    }

    try {
      ap.append(model.getGameState());
    } catch (IOException io) {
      return;
    }

    while (!model.isGameOver()) {
      String source;
      String dest;
      String readableCardNumber;

      PileType sourcePileType;
      PileType destPileType;

      int sourcePileIndex;
      int destPileIndex;
      int cardNumber;

      try {
        ap.append("\n");
        ap.append("Source Pile: ");
        while (!validPileFromInput(source = sc.next())) {
          ap.append("\n");
          ap.append("Pile is invalid. Try again");
        }

        if (quit(source)) {
          return;
        }

        sourcePileType = getPileType(source.charAt(0));
        sourcePileIndex = Integer.parseInt(source.substring(1)) - 1;

        ap.append("\n");
        ap.append("Card Number: ");
        while (!validIndex(readableCardNumber = sc.next())) {
          ap.append("Invalid card number. Try again");
        }

        if (quit(readableCardNumber)) {
          return;
        }

        cardNumber = Integer.parseInt(readableCardNumber) - 1;

        ap.append("\n");
        ap.append("Destination pile: ");
        while (!validPileFromInput(dest = sc.next())) {
          ap.append("Pile is invalid. Try again");
        }

        if (quit(dest)) {
          return;
        }

        destPileType = getPileType(dest.charAt(0));
        destPileIndex = Integer.parseInt(dest.substring(1)) - 1;

        try {
          model.move(sourcePileType, sourcePileIndex, cardNumber, destPileType, destPileIndex);
        } catch (IllegalArgumentException e) {
          ap.append(String.format("\nThe following error occured: %s. Try again", e.getMessage()));
        }

        try {
          ap.append("\n");
          ap.append(model.getGameState());
          ap.append("\n");
        } catch (IOException io) {
          return;
        }
      } catch (IOException io) {
        return;
      }
    }

    try {
      ap.append("\n");
      ap.append(model.getGameState());
      ap.append("\n");
      ap.append("Game Over!");
    } catch (IOException io) {
      return;
    } finally {
      return;
    }
  }

  /**
   * Checks whether the pile received from input is valid.
   *
   * @param s The input string representing the pile
   * @return Boolean determining whether the pile is valid
   */
  private boolean validPileFromInput(String s) {
    if (s.equals("q") || s.equals("Q")) {
      return true;
    }

    if (s.length() < 2) {
      return false;
    }

    char pileType = s.charAt(0);
    String pileNum = s.substring(1);

    int pileNumAsInt;

    try {
      pileNumAsInt = Integer.parseInt(pileNum);
    } catch (NumberFormatException nfe) {
      return false;
    }

    if (pileNumAsInt < 0) {
      return false;
    }

    return pileType == 'F' || pileType == 'C' || pileType == 'O';
  }

  /**
   * Appends the final game state on the proper input and boolean result stating whether game has
   * been quit.
   *
   * @param s The input string to check
   * @return A boolean result determining whether game has been quit
   */
  private boolean quit(String s) {
    if (s.equals("q") || s.equals("Q")) {
      try {
        ap.append("\n");
        ap.append("Game quit prematurely.");
      } catch (IOException io) {
        return false;
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * Checks if the given input is a valid index.
   *
   * @param s Input from the user
   * @return Boolean result ensuring a positive index.
   */
  private boolean validIndex(String s) {
    if (s.equals("q") || s.equals("Q")) {
      return true;
    }

    int index;

    try {
      index = Integer.parseInt(s);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return index > 0;
  }

  private PileType getPileType(char s) {
    if (s == 'C') {
      return PileType.CASCADE;
    } else if (s == 'F') {
      return PileType.FOUNDATION;
    } else if (s == 'O') {
      return PileType.OPEN;
    } else {
      throw new IllegalArgumentException("Invalid pile type.");
    }
  }

  /**
   * Writes information to output for user to read.
   *
   * @throws IOException If parameter is unrecognized.
   */
  private void writeOutput() throws IOException {
    ap.append("\n");
    ap.append("Number of cascade piles: ").append(Integer.toString(this.numCascade));
    ap.append("\n");
    ap.append("Number of open piles: ").append(Integer.toString(this.numOpen));
    ap.append("\n");

    switch (this.shuffleAsString) {
      case "y":
        ap.append("Shuffling deck.\n");
        break;
      case "n":
        ap.append("Deck dealt as is.\n");
        break;
      default:
        throw new IllegalArgumentException("Unrecognized response to shuffle. Ensure that either "
                + "'y' or 'n' has been entered (case-sensitive)");
    }
  }

  /**
   * Reads in required parameters from user.
   *
   * @throws IOException If input was not recognized.
   */
  private void readInput() throws IOException {
    int tempNumPiles;
    do {
      ap.append("How many cascade piles would you like (4-8): ");
      while (!sc.hasNextInt()) {
        ap.append("That number is not valid. Remember you can have between 4 and 8 cascade piles."
                + " Input another number");
      }
      tempNumPiles = sc.nextInt();
    }
    while (tempNumPiles < 0);
    this.numCascade = tempNumPiles;

    do {
      ap.append("How many open piles would you like (1-4): ");
      while (!sc.hasNextInt()) {
        ap.append("That number is invalid. Remember you can have between 1 and 4 open piles. "
                + "Input another number");
      }
      tempNumPiles = sc.nextInt();
    }
    while (tempNumPiles < 0);
    this.numOpen = tempNumPiles;

    ap.append("Would you like to shuffle the deck (y/n): ");
    String tempShuffle = sc.next();
    while (!tempShuffle.equals("y") || !tempShuffle.equals("n")) {
      ap.append("That input was not recognized. 'y' and 'n' are the two valid responses (case "
              + "sensitive). Try again");
    }
    shuffleAsString = tempShuffle;

    switch (shuffleAsString) {
      case "y":
        this.shuffle = true;
        break;
      case "n":
        this.shuffle = false;
        break;
      default:
        throw new IllegalArgumentException("Unrecognized shuffle parameter.");
    }
  }
}
