import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import cs3500.hw02.Card;
import cs3500.hw02.SuitType;

public class CardTest {
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCardThrowsError() {
    Card invalidCard = new Card(SuitType.HEARTS, 15);
  }

  @Test
  public void testToString() {
    Card cardFaceValue = new Card(SuitType.DIAMONDS, 11);
    Card cardNumValue = new Card(SuitType.CLUBS, 5);

    assertEquals("J♦", cardFaceValue.toString());
    assertEquals("5♣", cardNumValue.toString());
  }

  @Test
  public void testEquals() {
    assertEquals(new Card(SuitType.CLUBS, 5), new Card(SuitType.CLUBS, 5));
    assertNotEquals(new Card(SuitType.SPADES, 11), new Card(SuitType.HEARTS, 4));
  }

  @Test
  public void testGetColor() {
    Card redCard = new Card(SuitType.HEARTS, 5);
    Card blackCard = new Card(SuitType.SPADES, 8);

    assertEquals("RED", redCard.getColor());
    assertEquals("BLACK", blackCard.getColor());
  }

  @Test
  public void testSameColor() {
    Card redCard1 = new Card(SuitType.HEARTS, 9);
    Card redCard2 = new Card(SuitType.DIAMONDS, 2);
    Card blackCard = new Card(SuitType.SPADES, 7);

    assertEquals(true, redCard1.sameColor(redCard2));
    assertEquals(true, redCard1.sameColor(redCard1));
    assertEquals(false, blackCard.sameColor(redCard1));
  }

  @Test
  public void testValue() {
    Card faceCard = new Card(SuitType.CLUBS, 11);
    Card nonFaceCard = new Card(SuitType.DIAMONDS, 5);

    assertEquals(11, faceCard.value());
    assertEquals(5, nonFaceCard.value());
  }

  @Test
  public void testSameSuit() {
    Card club1 = new Card(SuitType.CLUBS, 6);
    Card club2 = new Card(SuitType.CLUBS, 9);
    Card heart = new Card(SuitType.HEARTS, 3);

    assertEquals(true, club1.sameSuit(club2));
    assertEquals(false, club1.sameSuit(heart));
  }
}
