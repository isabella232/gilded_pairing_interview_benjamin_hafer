import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InventoryTest {
  @Test
  public void testEndOfDaySellByAndPriceAreReduced() {
    // This was timeboxed, so I was going prettyfast.
    // Ideally, these lines and the first assertion should have been
    // factored out to a method for each test to call.
    Inventory.Item item = new Inventory.Item("normal", 50, 50);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();
    Inventory.Item finalItemState = inventory.getItems().get(0);

    assertThat(finalItemState.name).isEqualTo("normal");
    assertThat(finalItemState.sellBy).isEqualTo(49);
    assertThat(finalItemState.price).isEqualTo(49);
  }

  @Test
  public void testEndOfDaySellByAndPriceIsReducedForFlowers() {
    Inventory.Item item = new Inventory.Item("Flowers", 48, 47);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();
    Inventory.Item finalItemState = inventory.getItems().get(0);

    assertThat(finalItemState.name).isEqualTo("Flowers");
    assertThat(finalItemState.sellBy).isEqualTo(47);
    assertThat(finalItemState.price).isEqualTo(45);
  }

  @Test
  public void TestFlowerPriceFloorSellByGreaterThanZero() {
    Inventory.Item item = new Inventory.Item("Flowers", 1, 1);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();

    Inventory.Item finalItemState = inventory.getItems().get(0);
    assertThat(finalItemState.name).isEqualTo("Flowers");
    assertThat(finalItemState.sellBy).isEqualTo(0);
    assertThat(finalItemState.price).isEqualTo(0);
  }

  @Test
  public void testFlowerPriceFloorSellByEqualsZero() {
    Inventory.Item item = new Inventory.Item("Flowers", 0, 1);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();

    Inventory.Item finalItemState = inventory.getItems().get(0);
    assertThat(finalItemState.name).isEqualTo("Flowers");
    assertThat(finalItemState.sellBy).isEqualTo(-1);
    assertThat(finalItemState.price).isEqualTo(0);
  }

  @Test
  public void testFlowerPriceFloorSellByIsLessThanZeroPriceIsGreaterThan1() {
    Inventory.Item item = new Inventory.Item("Flowers", -1, 5);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();

    Inventory.Item finalItemState = inventory.getItems().get(0);
    assertThat(finalItemState.name).isEqualTo("Flowers");
    assertThat(finalItemState.sellBy).isEqualTo(-2);
    assertThat(finalItemState.price).isEqualTo(1);
  }

  @Test
  public void testOnceTheSellByDateHasPassedPriceDegradesTwiceAsFast() {
    Inventory.Item item = new Inventory.Item("normal", 0, 50);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();
    Inventory.Item finalItemState = inventory.getItems().get(0);

    assertThat(finalItemState.name).isEqualTo("normal");
    assertThat(finalItemState.sellBy).isEqualTo(-1);
    assertThat(finalItemState.price).isEqualTo(48);
  }

  @Test
  public void testPriceIsNeverNegative() {
    Inventory.Item item = new Inventory.Item("normal", 0, 0);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();
    Inventory.Item finalItemState = inventory.getItems().get(0);

    assertThat(finalItemState.name).isEqualTo("normal");
    assertThat(finalItemState.sellBy).isEqualTo(-1);
    assertThat(finalItemState.price).isEqualTo(0);
  }

  @Test
  public void testFineArtIncreasesInPriceAsItAges() {
    Inventory.Item item = new Inventory.Item("Fine Art", 50, 40);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();
    Inventory.Item finalItemState = inventory.getItems().get(0);

    assertThat(finalItemState.name).isEqualTo("Fine Art");
    assertThat(finalItemState.sellBy).isEqualTo(49);
    assertThat(finalItemState.price).isEqualTo(41);
  }

  @Test
  public void testPriceDoesNotExceed50() {
    Inventory.Item item = new Inventory.Item("Fine Art", 10, 50);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();
    Inventory.Item finalItemState = inventory.getItems().get(0);

    assertThat(finalItemState.name).isEqualTo("Fine Art");
    assertThat(finalItemState.sellBy).isEqualTo(9);
    assertThat(finalItemState.price).isEqualTo(50);
  }

  @Test
  public void testGoldCoinsDoNotChangePrice() {
    Inventory.Item item = new Inventory.Item("Gold Coins", 10, 10);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();
    Inventory.Item finalItemState = inventory.getItems().get(0);

    assertThat(finalItemState.name).isEqualTo("Gold Coins");
    assertThat(finalItemState.sellBy).isEqualTo(10);
    assertThat(finalItemState.price).isEqualTo(10);
  }

  @Test
  public void testConcertTicketsIncreaseByOneWhenSellByIsGreaterThanTen() {
    Inventory.Item item = new Inventory.Item("Concert Tickets", 11, 10);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();
    Inventory.Item finalItemState = inventory.getItems().get(0);

    assertThat(finalItemState.name).isEqualTo("Concert Tickets");
    assertThat(finalItemState.sellBy).isEqualTo(10);
    assertThat(finalItemState.price).isEqualTo(11);
  }

  @Test
  public void testConcertTicketsIncreaseByTwoWhenSellByIsTen() {
    Inventory.Item item = new Inventory.Item("Concert Tickets", 10, 10);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();
    Inventory.Item finalItemState = inventory.getItems().get(0);

    assertThat(finalItemState.name).isEqualTo("Concert Tickets");
    assertThat(finalItemState.sellBy).isEqualTo(9);
    assertThat(finalItemState.price).isEqualTo(12);
  }

  @Test
  public void testConcertTicketsIncreaseByThreeWhenSellByIsFive() {
    Inventory.Item item = new Inventory.Item("Concert Tickets", 5, 10);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();
    Inventory.Item finalItemState = inventory.getItems().get(0);

    assertThat(finalItemState.name).isEqualTo("Concert Tickets");
    assertThat(finalItemState.sellBy).isEqualTo(4);
    assertThat(finalItemState.price).isEqualTo(13);
  }

  @Test
  public void testConcertTicketsDropToZeroAfterTheSellBy() {
    Inventory.Item item = new Inventory.Item("Concert Tickets", 0, 10);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();
    Inventory.Item finalItemState = inventory.getItems().get(0);

    assertThat(finalItemState.name).isEqualTo("Concert Tickets");
    assertThat(finalItemState.sellBy).isEqualTo(-1);
    assertThat(finalItemState.price).isEqualTo(0);
  }

  @Test
  public void testGoldCoinsMayHavePriceOf80() {
    Inventory.Item item = new Inventory.Item("Gold Coins", 10, 80);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();
    Inventory.Item finalItemState = inventory.getItems().get(0);

    assertThat(finalItemState.name).isEqualTo("Gold Coins");
    assertThat(finalItemState.sellBy).isEqualTo(10);
    assertThat(finalItemState.price).isEqualTo(80);
  }

  @Test
  public void testFlowerItemWithPriceAboveCeiling() {
    Inventory.Item item = new Inventory.Item("Flowers", 10, 80);
    Inventory inventory = new Inventory(Arrays.asList(item));
    inventory.updatePrice();
    Inventory.Item finalItemState = inventory.getItems().get(0);

    assertThat(finalItemState.name).isEqualTo("Flowers");
    assertThat(finalItemState.sellBy).isEqualTo(9);
    assertThat(finalItemState.price).isEqualTo(80);
  }
}
