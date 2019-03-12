package cs3500.hw04;

import java.util.ArrayList;
import java.util.Stack;

import cs3500.hw02.Card;
import cs3500.hw02.FreecellModel;
import cs3500.hw02.FreecellOperations;
import cs3500.hw02.PileType;

/**
 * Represents a model of the Freecell game that allows the user to move multiple cards from a
 * cascade to another cascade.
 */
public class MultimoveFreecellModel extends FreecellModel implements FreecellOperations<Card> {

  /**
   * Constructs an instance of the multimove model.
   */

  /**
   * Checks if the cards being moved form a valid build.
   *
   * @param source    The pile the card is being moved from.
   * @param cardIndex The location of that card in the pile.
   * @return True if the card forms a valid build.
   */
  private boolean validBuild(ArrayList<Card> source, int cardIndex) {
    for (int i = cardIndex; i < source.size() - 1; i++) {
      if (source.get(i).value() - 1 != source.get(i + 1).value() || source.get(i).sameColor(
              source.get(i + 1))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Moves multiple cards. Assumes the cards form a valid build.
   *
   * @param source    The source pile of the build.
   * @param dest      The destination pile for the build to go to.
   * @param cardIndex The index of the first card in the build
   */
  private void multiMoveCard(ArrayList<Card> source, ArrayList<Card> dest, int cardIndex) {
    Stack<Card> buildStack = new Stack<Card>();
    for (int i = source.size() - 1; i >= cardIndex; i--) {
      buildStack.push(source.get(i));
    }
    source.removeAll(buildStack);
    while (!buildStack.empty()) {
      dest.add(buildStack.pop());
    }
  }

  private int numEmpty(PileType type) {
    int i = 0;
    if (type == PileType.CASCADE) {
      for (ArrayList<Card> a : cascadePiles) {
        if (a.isEmpty()) {
          i++;
        }
      }
    }
    if (type == PileType.OPEN) {
      for (ArrayList<Card> a: openPiles) {
        if (a.isEmpty()) {
          i++;
        }
      }
    }
    return i;
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination, int
          destPileNumber) throws IllegalArgumentException, IllegalStateException {

    validateMove(source, destination, pileNumber, destPileNumber, cardIndex);



    // Move from cascade pile...
    if (source == PileType.CASCADE) {
      ArrayList<Card> sourcePile = cascadePiles.get(pileNumber);
      if (sourcePile.size() - cardIndex > ((numEmpty(PileType.OPEN) + 1) * 2 ^ (numEmpty(
              PileType.CASCADE)))) {
        throw new IllegalStateException("Not enough empty intermediate piles");
      }
      // ...to another cascade pile
      if (destination == PileType.CASCADE) {
        ArrayList<Card> destPile = cascadePiles.get(destPileNumber);
        // Fast path if the card is the last card in the pile
        if (cardIndex == sourcePile.size() - 1 && sourcePile.get(cardIndex).value() + 1
                == destPile.get(destPile.size() - 1).value() && !sourcePile.get(cardIndex)
                .sameColor(destPile.get(destPile.size() - 1))) {
          moveCard(sourcePile, destPile, cardIndex);
        } else if (validBuild(sourcePile, cardIndex) && sourcePile.get(cardIndex).value() + 1
                == destPile.get(destPile.size() - 1).value()) {
          multiMoveCard(sourcePile, destPile, cardIndex);
        } else {
          throw new IllegalArgumentException("The source card did not form a valid build");
        }
      }
      // ...to an open pile
      else if (destination == PileType.OPEN) {
        ArrayList<Card> destPile = openPiles.get(destPileNumber);
        // Only move the card if it is the last in cascade and open pile is empty
        if (cardIndex == sourcePile.size() - 1 && isEmpty(PileType.OPEN, destPileNumber)) {
          moveCard(sourcePile, destPile, cardIndex);
        }
      }
      // ...to a foundation pile
      else if (destination == PileType.FOUNDATION) {
        ArrayList<Card> destPile = foundationPiles.get(destPileNumber);
        if (cardIndex == sourcePile.size() - 1) {
          if (destPile.size() == 0 && sourcePile.get(cardIndex).value() == 1) {
            moveCard(sourcePile, destPile, cardIndex);
          } else if (!isEmpty(PileType.FOUNDATION, destPileNumber) && sourcePile.get(cardIndex)
                  .value() - 1 == destPile.get(destPile.size() - 1).value() && sourcePile.get(
                          cardIndex).sameSuit(destPile.get(destPile.size() - 1))) {
            moveCard(sourcePile, destPile, cardIndex);
          }
        }
      }
    }

    // Move from an open pile...
    else if (source == PileType.OPEN) {
      ArrayList<Card> sourcePile = openPiles.get(pileNumber);
      // ...to a cascade pile
      if (destination == PileType.CASCADE) {
        ArrayList<Card> destPile = cascadePiles.get(destPileNumber);
        if (isEmpty(PileType.CASCADE, destPileNumber)) {
          moveCard(sourcePile, destPile, cardIndex);
        } else if (!isEmpty(PileType.CASCADE, destPileNumber) && sourcePile.get(cardIndex).value()
                + 1 == destPile.get(destPile.size() - 1).value() && !sourcePile.get(
                        cardIndex).sameColor(destPile.get(destPile.size() - 1))) {
          moveCard(sourcePile, destPile, cardIndex);
        }
      }
      // ...to a foundation pile
      else if (destination == PileType.FOUNDATION) {
        ArrayList<Card> destPile = foundationPiles.get(destPileNumber);
        if (isEmpty(PileType.FOUNDATION, destPileNumber) && sourcePile.get(cardIndex).value()
                == 1) {
          moveCard(sourcePile, destPile, cardIndex);
        } else if (!isEmpty(PileType.FOUNDATION, destPileNumber) && sourcePile.get(cardIndex)
                .value() - 1 == destPile.get(destPile.size() - 1).value() && sourcePile.get(
                        cardIndex).sameSuit(destPile.get(destPile.size() - 1))) {
          moveCard(sourcePile, destPile, cardIndex);
        }
      }
    }
    // If the move isn't recognized
    else {
      throw new IllegalArgumentException("Unrecognized move");
    }
  }

}
