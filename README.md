# creditsuisse
I've assumed that the library will be used by an application that has already implemented a trading or exchange logic.
I'm assuming also that is a client responsibility to keep the library updated with full-filled or canceled orders.
Therefore the purpose of the library is to collect a report of the live orders in the system.
The main "ready to use" facade class exposed to the client is LiveOrderClient which configures the service.
Alternatively a client application msy decide to setup the LiveOrderService in a different way, custom validations, different datasources.

OrderDataStore is the data structure needed to hold the live orders. The transactionality and thread-safeness is delegated to the implementor of this interface.
For this test i've provided an implementation of an OrderDataStore: InMemoryOrderDataStore.
In this test are used two different datastore configured to be iterated in opposite directions; BUY fist, SELL after. for buy higher price first, for sell lower price first.

The InMemoryOrderDataStore should guarantee thread-safeness (reader, writers and atomic operations), linear (prices) time to return the summaries while
for insert and cancel operation the cost depends on the data structure used to store the prices.
In this example, since we wanted to access prices in a specific order i've used a TreeMap (with a reverseOrder comparator for the buy)
that takes logn (n=prices) to insert/cancel/update the quantity for price.
A different approach was also possible, using an HashMap to store prices (O(1) for insert/cancel/update) and sorting the map whilst generating the summaries (nlogn).

The further HashMap provided to the InMemoryOrderDataStore is to count the references of each order.
I'm assuming here, in absence of a primary key (or a timestamp), that an user can place two or more equal orders.
This logic is needed to avoid to remove from the store quantities for a not existing order.


