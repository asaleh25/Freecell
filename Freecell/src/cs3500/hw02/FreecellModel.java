package cs3500.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a model of the Freecell game that only allows a single card to be moved at a time.
 */
public class FreecellModel implements FreecellOperations<Card> {
  protected ArrayList<ArrayList<Card>> cascadePiles;
  protected ArrayList<ArrayList<Card>> openPiles;
  protected ArrayList<ArrayList<Card>> foundationPiles;

  @Override
  public List<Card> getDeck() {
    List<Card> tempDeck = new ArrayList<Card>();
    for (SuitType s : SuitType.values()) {
      for (int i = 1; i < 14; i++) {
        tempDeck.add(new Card(s, i));
      }
    }
    return tempDeck;
  }

  /**
   * Validates the deck to ensure that it contains 52 unique cards.
   *
   * @param deck The deck to be validated
   * @return Boolean result determining if the deck is valid.
   */
  private boolean invalidDeck(List<Card> deck) {
    Set<Card> deckSet = new HashSet<Card>(deck);

    return deck == null || deck.size() != 52 || deckSet.size() < deck.size();

  }

  @Override
  public void startGame(List<Card> deck, int numCascadePiles, int numOpenPiles, boolean shuffle)
          throws IllegalArgumentException {
    if (invalidDeck(deck) || numCascadePiles < 4 || numCascadePiles > 8 || numOpenPiles < 1
            || numOpenPiles > 4) {
      throw new IllegalArgumentException("Please ensure that the deck is valid, there are at "
              + "least 4 cascade piles, and at least 1 open pile.");
    }

    this.cascadePiles = new ArrayList<ArrayList<Card>>();
    this.foundationPiles = new ArrayList<ArrayList<Card>>();
    this.openPiles = new ArrayList<ArrayList<Card>>();

    if (shuffle) {
      Collections.shuffle(deck);
    }

    for (int i = 0; i < numCascadePiles; i++) {
      this.cascadePiles.add(new ArrayList<Card>());
    }

    for (int i = 0; i < numOpenPiles; i++) {
      this.openPiles.add(new ArrayList<Card>());
    }

    for (int i = 0; i < 4; i++) {
      this.foundationPiles.add(new ArrayList<Card>());
    }

    for (int i = 0; i < deck.size(); i++) {
      int index = i % numCascadePiles;
      this.cascadePiles.get(index).add(deck.get(i));
    }
  }

  /**
   * Helper method, moves a card from one pile to another.
   *
   * @param source      the pile the card is taken from
   * @param destination the pile the card is moved to
   * @param cardIndex   the index of the card being moved
   */
  protected void moveCard(ArrayList<Card> source, ArrayList<Card> destination, int cardIndex) {
    Card movedCard = source.get(cardIndex);
    destination.add(movedCard);
    source.remove(movedCard);
  }

  protected boolean validPile(PileType pile, int pileNumber) {
    if (pile == PileType.OPEN) {
      return pileNumber < openPiles.size();
    } else if (pile == PileType.CASCADE) {
      return pileNumber < cascadePiles.size();
    } else {
      return pileNumber < 4;
    }
  }

  protected boolean isEmpty(PileType pile, int pileNumber) {
    if (pile == PileType.CASCADE) {
      return cascadePiles.get(pileNumber).size() == 0;
    } else if (pile == PileType.OPEN) {
      return openPiles.get(pileNumber).size() == 0;
    } else {
      return foundationPiles.get(pileNumber).size() == 0;
    }
  }

  protected void validateMove(PileType source, PileType destination, int pileNumber, int
          destPileNumber, int cardIndex) {
    if (pileNumber < 0 || cardIndex < 0 || destPileNumber < 0) {
      throw new IllegalArgumentException("Invalid pile or card index");
    }

    if (isGameOver()) {
      throw new IllegalStateException("Game has ended");
    }

    if (destination == PileType.OPEN && openPiles.get(destPileNumber).size() == 1) {
      throw new IllegalArgumentException("Destination open pile is full");
    }

    if (!validPile(source, pileNumber) || !validPile(destination, destPileNumber)) {
      throw new IllegalArgumentException("Invalid pile selected");
    }
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination, int
          destPileNumber) throws IllegalArgumentException, IllegalStateException {

    validateMove(source, destination, pileNumber, destPileNumber, cardIndex);

    // Deals with moving cards from cascade pile
    if (source == PileType.CASCADE) {
      // Only move the card if its the last card in the cascade.
      if (cardIndex == cascadePiles.get(pileNumber).size() - 1) {
        ArrayList<Card> sourcePile = cascadePiles.get(pileNumber);
        Card sourceCard = sourcePile.get(cardIndex);
        // Moves a card from cascade pile to open pile
        if (destination == PileType.OPEN && isEmpty(PileType.OPEN, destPileNumber)) {
          moveCard(sourcePile, openPiles.get(destPileNumber), cardIndex);
        }

        // Moves a card from the cascade pile to the foundation pile
        else if (destination == PileType.FOUNDATION) {
          ArrayList<Card> destPile = foundationPiles.get(destPileNumber);
          // if the foundation pile is empty
          if (isEmpty(PileType.FOUNDATION, destPileNumber)
                  && sourceCard.value() == 1) {
            moveCard(sourcePile, destPile, cardIndex);
          }
          // if the foundation pile isn't empty
          else if (!isEmpty(PileType.FOUNDATION, destPileNumber)
                  && sourceCard.value() - 1 == destPile.get(destPile.size() - 1).value()
                  && sourceCard.sameSuit(destPile.get(destPile.size() - 1))) {
            moveCard(sourcePile, destPile, cardIndex);
          }
        }

        // Moves a card from a cascade pile to another cascade pile
        else if (destination == PileType.CASCADE) {
          ArrayList<Card> destPile = cascadePiles.get(destPileNumber);
          Card destCard = destPile.get(destPile.size() - 1);
          if (destCard.value() - 1 == sourceCard.value() && !destCard.sameColor(sourceCard)) {
            moveCard(sourcePile, destPile, cardIndex);
          }
        }
      }
    }

    // Deals with moving from an open pile
    else if (source == PileType.OPEN) {
      ArrayList<Card> sourcePile = openPiles.get(pileNumber);
      // Only move the card if there is exactly one card in the open pile
      if (!isEmpty(PileType.OPEN, pileNumber) && cardIndex == sourcePile.size() - 1) {
        Card sourceCard = sourcePile.get(cardIndex);
        // Move from open to cascade pile
        if (destination == PileType.CASCADE) {
          ArrayList<Card> destPile = cascadePiles.get(destPileNumber);
          Card destCard = destPile.get(destPile.size() - 1);
          //if the cascade is empty
          if (destPile.size() == 0) {
            moveCard(sourcePile, destPile, cardIndex);
          }
          // If the cascade isn't empty
          else if (destCard.value() - 1 == sourceCard.value()
                  && !destCard.sameColor(sourceCard)) {
            moveCard(sourcePile, destPile, cardIndex);
          }
        }

        // Move from open to foundation
        else if (destination == PileType.FOUNDATION) {
          ArrayList<Card> destPile = foundationPiles.get(destPileNumber);
          // If the foundation is empty
          if (isEmpty(PileType.FOUNDATION, destPileNumber) && sourceCard.value() == 1) {
            moveCard(sourcePile, destPile, cardIndex);
          }
          // If the foundation isn't empty
          else if (destPile.size() > 0 && sourceCard.value() - 1 == destPile.get(destPile.size()
                  - 1).value() && sourceCard.sameSuit(destPile.get(destPile.size() - 1))) {
            moveCard(sourcePile, destPile, cardIndex);
          }
        }
      }
    }

    // If the move isn't in the above set of moves, then throw an exception
    else {
      throw new IllegalArgumentException("Invalid move");
    }
  }

  @Override
  public boolean isGameOver() {
    return (foundationPiles.get(0).size() == 13 && foundationPiles.get(1).size() == 13
            && foundationPiles.get(2).size() == 13 && foundationPiles.get(3).size() == 13);
  }

  @Override
  public String getGameState() {
    if (cascadePiles == null) {
      return "";
    }

    StringBuilder gameState = new StringBuilder();

    gameState.append(pileToString(this.foundationPiles, PileType.FOUNDATION));
    gameState.append("\n");
    gameState.append(pileToString(this.openPiles, PileType.OPEN));
    gameState.append("\n");
    gameState.append(pileToString(this.cascadePiles, PileType.CASCADE));

    return gameState.toString();
  }

  /**
   * Helper method, providing abstraction for the getGameState method Converts the given pile to its
   * string representation adding the appropriate symbols and spacing.
   *
   * @param pile The pile being operated on
   * @param type The type of the pile being operated on
   * @return A StringBuilder object representing the pile
   */
  private StringBuilder pileToString(ArrayList<ArrayList<Card>> pile, PileType type) {
    StringBuilder pileString = new StringBuilder();

    for (int i = 0; i < pile.size(); i++) {

      // Determines the appropriate prefix depending on the PileType.
      if (type == PileType.FOUNDATION) {
        pileString.append("F");
      } else if (type == PileType.OPEN) {
        pileString.append("O");
      } else if (type == PileType.CASCADE) {
        pileString.append("C");
      }
      pileString.append(i + 1);
      pileString.append(":");

      // Prints the cards in the given pile.
      for (int j = 0; j < pile.get(i).size(); j++) {
        pileString.append(" ");
        pileString.append(pile.get(i).get(j).toString());

        if (j < pile.get(i).size() - 1) {
          pileString.append(",");
        }
      }

      // Appends the last \n character if appropriate
      if (i != pile.size() - 1) {
        pileString.append("\n");
      }
    }

    return pileString;
  }
}
