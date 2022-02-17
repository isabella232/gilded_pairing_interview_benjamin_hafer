import java.util.List;

public class Inventory {
  private List<Item> items;

  private Integer defaultPriceChange = 1;

  public Inventory(List<Item> items) {
    this.items = items;
  }

  public List<Item> getItems() {
    return items;
  }

  public void updatePrice() {
    items.forEach(item -> {
      if (item.name != "Fine Art" && item.name != "Concert Tickets") {
        if (item.price > 0) {
          if (item.name != "Gold Coins") {
            if (item.name.equals("Flowers")) {
              if (item.price > 1) {
                item.price -= (defaultPriceChange * 2);
              } else if (item.price == 1) {
                item.price = 0;
              }
            } else {
              item.price = item.price - defaultPriceChange;
            }
          }
        }
      } else {
        // this is fine art or concert tickets
        if (item.price < 50) {
          item.price = item.price + 1;
          if (item.name == "Concert Tickets") {
            if (item.sellBy < 11) {
              if (item.price < 50) {
                item.price = item.price + 1;
              }
            }
            if (item.sellBy < 6) {
              if (item.price < 50) {
                item.price = item.price + 1;
              }
            }
          }
        }
      }

      // descreasing the sell by date
      if (item.name != "Gold Coins") {
        item.sellBy = item.sellBy - defaultPriceChange;
      }

      // hasn't hit sellBy yet
      if (item.sellBy < 0) {
        if (item.name != "Fine Art") {
          if (item.name != "Concert Tickets") {
            // will hit in here for flowers
            if (item.price > 0) {
              if (item.name != "Gold Coins") {
                if (item.name.equals("Flowers")) {
                  item.price -= (defaultPriceChange * 2);
                } else {
                  item.price = item.price - defaultPriceChange;
                }
              }
            }

          } else {
            item.price = item.price - item.price;
          }
        } else {
          // at sell date or past
          if (item.price < 50) {
            item.price = item.price + 1;
          }
        }
      }
    });
  }


  static class Item {
    public String name;
    public Integer sellBy;
    public Integer price;

    Item(String name, Integer sellBy, Integer price) {
      this.name = name;
      this.sellBy = sellBy;
      this.price = price;
    }

    public String toString() {
      return String.format("%s, %d, %d", name, sellBy, price);
    }
  }
}
