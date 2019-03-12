import org.junit.Test;

import cs3500.hw02.FreecellModel;
import cs3500.hw04.FreecellModelCreator;
import cs3500.hw04.MultimoveFreecellModel;

import static org.junit.Assert.assertEquals;


public class FreecellModelCreatorTest {
  @Test
  public void testCreate() {
    FreecellModelCreator modelCreator = new FreecellModelCreator();

    assertEquals(FreecellModel.class,
            modelCreator.create(FreecellModelCreator.GameType.SINGLEMOVE).getClass());

    assertEquals(MultimoveFreecellModel.class,
            modelCreator.create(FreecellModelCreator.GameType.MULTIMOVE).getClass());
  }
}