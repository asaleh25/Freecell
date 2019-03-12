package cs3500.hw02;

import java.util.Objects;

/**
 * Represents a playing card.
 */
public class Card {
  private final SuitType suit;
  private final int value;

  /**
   * Constructs a card.
   *
   * @param suit      The suit of the card
   * @param value     The value of the card as an int
   */
  public Card(SuitType suit, int value) {
    this.suit = suit;
    // Check to ensure card value is valid
    if (value < 1 || value > 13) {
      throw new IllegalArgumentException("Invalid card value. Please ensure that the value is "
              + "between 1 and 13.");
    }
    else {
      this.value = value;
    }
  }

  private String valueToString(int cardValue) {
    switch (cardValue) {
      case 1: return "A";
      case 11: return "J";
      case 12: return "Q";
      case 13: return "K";
      default: return Integer.toString(value);
    }
  }

  @Override
  public String toString() {
    if (this.suit == SuitType.DIAMONDS) {
      return valueToString(this.value) + "♦";
    }
    else if (this.suit == SuitType.CLUBS) {
      return valueToString(this.value) + "♣";
    }
    else if (this.suit == SuitType.SPADES) {
      return valueToString(this.value) + "♠";
    }
    else {
      return valueToString(this.value) + "♥";
    }
    //return super.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Card)) {
      return false;
    }
    else {
      Card cardObj = (Card)obj;
      return this.suit == cardObj.suit && this.value == cardObj.value;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.suit, this.value);
  }

  /**
   * Determines the color of the card.
   *
   * @return  String representation of the card color
   */
  public String getColor() {
    if (this.suit == SuitType.DIAMONDS || this.suit == SuitType.HEARTS) {
      return "RED";
    }
    else {
      return "BLACK";
    }
  }

  /**
   * Determines if two colors are the same color.
   *
   * @param other   card whose color is to be compared
   * @return        true if cards are same color
   */
  public boolean sameColor(Card other) {
    return this.getColor().equals(other.getColor());
  }

  /**
   * Returns the value of this card.
   * @return    The numerical value of the card
   */
  public int value() {
    return this.value;
  }

  /**
   * Determines if two cards are the same suit.
   * @param other   Card to be compared to
   * @return        True if two cards are the same suit
   */
  public boolean sameSuit(Card other) {
    return this.suit == other.suit;
  }
}
