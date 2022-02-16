import unittest

from main import Inventory, Item

NAME_FLOWERS = 'Flowers'
NAME_FINE_ART = 'Fine Art'
NAME_GOLD_COINS = 'Gold Coins'
NAME_CONCERT_TICKETS = 'Concert Tickets'
NAMES_TICKETS_AND_ART = [NAME_CONCERT_TICKETS, NAME_FINE_ART]
ALL_NAMES = [NAME_FLOWERS, NAME_FINE_ART, NAME_GOLD_COINS, NAME_CONCERT_TICKETS]

class UnitTests(unittest.TestCase):

    def test_1_sell_by_lowers_each_day(self):
        SELL_BY = 10
        items = []
        for name in ALL_NAMES:
            items.append(Item(name=name, sell_by=SELL_BY, price=0))
        Inventory(items=items).update_price()
        for item in items:
            if item.name == NAME_GOLD_COINS:
                # Gold coins never have to be sold, so sell_by doesn't change
                self.assertEqual(item.sell_by, SELL_BY)
            else:
                self.assertEqual(item.sell_by, SELL_BY - 1)

    def test_2_price_lowers_each_day(self):
        PRICE = 10
        items = []
        for name in ALL_NAMES:
            items.append(Item(name=name, sell_by=100, price=PRICE))
        Inventory(items=items).update_price()
        for item in items:
            if item.name == NAME_GOLD_COINS:
                # Gold coins never decrease in price
                self.assertEqual(item.price, PRICE)
            elif item.name in NAMES_TICKETS_AND_ART:
                # tested in a different test
                pass
            else:
                self.assertEqual(item.price, PRICE - 1, f'item.name=[{item.name}]')


    def test_3_price_degrades_twice_as_fast_after_sell_by_date_passed(self):
        PRICE = 100
        SELL_BY_PASSED = 0
        items = []
        for name in ALL_NAMES:
            items.append(Item(name=name, sell_by=SELL_BY_PASSED, price=PRICE))
        Inventory(items=items).update_price()
        for item in items:
            if item.name == NAME_GOLD_COINS:
                self.assertEqual(item.price, PRICE, f'item.name=[{item.name}]')
            elif item.name in NAMES_TICKETS_AND_ART:
                # tested in a different test
                pass
            else:
                self.assertEqual(item.price, PRICE - 2)

    def test_4_price_is_never_negative(self):
        PRICE = 0
        SELL_BY_PASSED = 0
        items = []
        for name in ALL_NAMES:
            items.append(Item(name=name, sell_by=SELL_BY_PASSED, price=PRICE))
        Inventory(items=items).update_price()
        for item in items:
            if item.name in NAMES_TICKETS_AND_ART:
                # tested in other test(s)
                pass
            else:
                self.assertEqual(item.price, PRICE)

    def test_5_fine_art_increases_in_price(self):
        PRICE = 10
        item = Item(name=NAME_FINE_ART, sell_by=5, price=PRICE)
        Inventory(items=[item]).update_price()
        self.assertEqual(item.price, PRICE + 1)

    def test_6_price_never_more_than_50(self):
        PRICE = 50
        SELL_BY_INCREASE_BY_3 = 5
        items = []
        for name in ALL_NAMES:
            items.append(Item(name=name, sell_by=SELL_BY_INCREASE_BY_3, price=PRICE))
        Inventory(items=items).update_price()
        for item in items:
            if item.name in NAMES_TICKETS_AND_ART:
                self.assertEqual(item.price, PRICE)
            else:
                # not applicable
                pass

    def test_7_gold_coins_never_decrease_in_price(self):
        PRICE = 80
        SELL_BY = 100
        item = (Item(name=NAME_GOLD_COINS, sell_by=SELL_BY, price=PRICE))
        Inventory(items=[item]).update_price()
        self.assertEqual(item.price, PRICE)
        self.assertEqual(item.sell_by, SELL_BY)


    def test_8_concert_tickets_increase_in_price(self):
        SELL_BY_DAYS = [11, 10, 5, 0]
        PRICE = 10
        items = []
        for sell_by in SELL_BY_DAYS:
            items.append(Item(name=NAME_CONCERT_TICKETS, sell_by=sell_by, price=PRICE))
        Inventory(items=items).update_price()
        for item in items:
            orig_sell_by = item.sell_by + 1
            if orig_sell_by == 11:
                # decrease by 1 if > 10 days
                self.assertEqual(item.price, PRICE + 1)
            elif orig_sell_by == 10:
                # decrease by 2 when 10 days or less
                self.assertEqual(item.price, PRICE + 2, item)
            elif orig_sell_by == 5:
                # decrease by 3 when 5 days or less
                self.assertEqual(item.price, PRICE + 3, item)
            elif orig_sell_by == 0:
                # price drops to 0 after the concert
                self.assertEqual(item.price, 0)
            else:
                self.assertTrue(False, f'Error: unexpected orig_sell_by=[{orig_sell_by}]')

unittest.main()
