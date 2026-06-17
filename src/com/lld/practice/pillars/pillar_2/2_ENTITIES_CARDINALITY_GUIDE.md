# Pillar 2: ENTITIES & CARDINALITY

> **Goal:** Master how to spot the *right* objects and their *right* relationships before writing a single class — in under 60 seconds, on any LLD problem.

---

## What it really means

Entities are the **nouns** of your system — the things you'll turn into classes. Cardinality is **how many of one entity relate to how many of another**.

The deeper meaning: **the interviewer is testing whether you can spot the right objects and their relationships before writing any code.** Get the entities wrong, and your whole class design collapses.

---

## The mental model: "The 4 Gates of Entities"

When you read any problem statement, your brain should fire these 4 gates **in this exact order**:

### Gate 1: NOUNS — What are the core entities?
**Why this matters:** Each noun usually becomes a class. Miss a noun, miss a class.

The trick: **read the problem statement and underline every noun**, just like you underlined verbs for Scope.

Example: *"Design a library where **users** borrow **books**. Each book has multiple **copies**."*

Visible nouns: `User`, `Book`, `Copy` → 3 classes minimum.

But probe deeper: are there hidden entities the interviewer didn't say?
- `Library` itself?
- `Loan` / `Issue` record?
- `Reservation`?

### Gate 2: RELATIONSHIPS — How do entities connect?
**Why this matters:** Relationships drive your data structures and method signatures.

For every pair of entities, ask:
- Does A know about B? Does B know about A? Both?
- Is the relationship temporary or permanent?
- Is it ownership, association, or composition?

Example:
- `User → Book`: a user borrows books (temporary, association)
- `Book → Copy`: a book has copies (permanent, composition)
- `User → Loan`: a user creates loans (ownership)

### Gate 3: CARDINALITY — How many of each?
**Why this matters:** Cardinality tells you which collection to use (`Set`, `List`, `Map`).

For every relationship, ask:
- 1-to-1? (one user has one profile)
- 1-to-many? (one user has many loans)
- many-to-many? (many users borrow many books over time)

Example mappings:
- 1-to-1 → direct field reference
- 1-to-many → `List<X>` or `Set<X>` on the parent
- many-to-many → join entity (`Loan`) or `Map<A, Set<B>>`

### Gate 4: IDENTITY — What uniquely identifies each entity?
**Why this matters:** Identity becomes the map key, the equals/hashCode contract, the database PK.

For every entity, ask:
- What field uniquely identifies it?
- Is identity assigned (UUID) or derived (composite key)?
- Can identity change over time?

Examples:
- `User` → `userId` (assigned)
- `Book` → `isbn` (derived from real world)
- `Loan` → `loanId` (assigned, system-generated)
- `Copy` → composite of `bookId + copyNumber`

---

## The Entity Question Template (memorize)

After you've nailed scope, fire these 4 questions:

```
Q1: I see entities [list nouns from statement]. Are there hidden ones (e.g., Loan, Reservation, Account)?
Q2: How do these entities relate? Who owns whom? Is the link temporary or permanent?
Q3: What's the cardinality between [A] and [B]? 1-to-1, 1-to-many, or many-to-many?
Q4: What uniquely identifies each entity? Is the ID assigned or derived?
```

That's **4 questions in ~45 seconds** — and you've fully covered Pillar 2.

---

## Worked Example: Watch me think on a problem

**Problem:** *"Design a ride-sharing app like Uber."*

**Step 1 — I read it. My brain underlines nouns: ride, app.**

**Step 2 — Gate 1 (NOUNS):**
Visible nouns: `Ride`. But Uber clearly has more:
- `Rider` (passenger)
- `Driver`
- `Vehicle`
- `Trip` (vs Ride request — different things!)
- `Payment`
- `Location` / `Coordinates`

I'd ask:
> *"I see core entities like Rider, Driver, Vehicle, and Trip. Are there others — Payment, Rating, RideRequest as separate from Trip?"*

**Step 3 — Gate 2 (RELATIONSHIPS):**
- `Rider → Trip`: a rider takes trips (1-to-many)
- `Driver → Trip`: a driver fulfills trips (1-to-many)
- `Driver → Vehicle`: a driver owns vehicles (1-to-many) OR drives one at a time (1-to-1 active)?
- `Trip → Payment`: each trip has one payment (1-to-1)

I'd ask:
> *"Does a driver own multiple vehicles or just one active at a time? Does a trip always result in a payment, or can it be free/cancelled?"*

**Step 4 — Gate 3 (CARDINALITY):**
- Rider ↔ Trip: 1-to-many (one rider, many trips over time)
- Driver ↔ Trip: 1-to-many (same)
- Trip ↔ Vehicle: many-to-1 (each trip has one vehicle, vehicle has many trips)
- Rider ↔ Driver: many-to-many (any rider can be matched with any driver)

I'd ask:
> *"For a single trip, can there be multiple riders (carpool) or always one? Can multiple drivers be assigned to the same trip request before one accepts?"*

**Step 5 — Gate 4 (IDENTITY):**
- `Rider` → `riderId` (assigned)
- `Driver` → `driverId` (assigned)
- `Vehicle` → `licensePlate` (derived) or `vehicleId` (assigned)?
- `Trip` → `tripId` (assigned)

I'd ask:
> *"Is vehicle identified by license plate or an internal vehicleId? Is trip ID system-generated or derived from rider+timestamp?"*

**Total time:** ~60 seconds. **Total questions:** 4. **Pillar 2 fully covered.**

---

## Why this thinking process works

You don't memorize "what classes does Uber have". You memorize **4 gates**, and the entities + relationships fall out automatically based on the problem.

The same 4 gates apply to:
- Library system
- Parking lot
- Hotel reservation
- Anything else

**The pillar is the entity-discovery engine. You don't list nouns randomly — you derive them.**

---

## Common traps in Pillar 2

### Trap 1: Confusing entity with attribute
- ❌ "User has an email" — email is an attribute, not an entity
- ✅ "User has many Addresses" — Address is an entity (has its own ID, lifecycle)

**Rule:** If it has its own identity and lifecycle, it's an entity. Otherwise, it's an attribute.

### Trap 2: Missing the join entity in many-to-many
- ❌ "User borrows many Books, Book is borrowed by many Users" — and you stop there
- ✅ Realize you need a `Loan` entity to capture the relationship + metadata (issueDate, returnDate)

**Rule:** Many-to-many relationships often hide a third entity. Look for it.

### Trap 3: Treating snapshots and history as the same entity
- ❌ "Order has status PENDING/SHIPPED/DELIVERED" — single field
- ✅ "Order has many StatusEvents over time" — separate entity if history matters

**Rule:** If the interviewer cares about history/audit, you need an event entity.

---

## Cardinality → Data Structure Cheatsheet

| Cardinality | Java Choice | Example |
|---|---|---|
| 1-to-1 | direct field | `User.profile : Profile` |
| 1-to-many | `List<X>` (ordered) or `Set<X>` (unique) | `User.loans : List<Loan>` |
| many-to-1 | foreign-key field | `Loan.user : User` |
| many-to-many (no metadata) | `Map<A, Set<B>>` | `bookToUsers : Map<BookId, Set<UserId>>` |
| many-to-many (with metadata) | join entity | `Loan { user, book, issuedAt, returnedAt }` |

---

## Entity Cheatsheet (Visible vs Hidden, per domain)

> **Visible** = nouns the interviewer literally said in the problem statement.
> **Hidden** = nouns you must *infer* — usually transactional / relationship / state-history / policy entities.
>
> **Rule:** Visible alone gets you a 2/4. Hidden ones are where senior candidates score points.

| Domain | Visible entities (stated) | Hidden entities (you must probe) |
|---|---|---|
| Library | `User`, `Book`, `Copy` | `Loan`, `Reservation`, `Fine`, `Catalog`, `Librarian` |
| Parking lot | `Vehicle`, `ParkingLot`, `Floor` | `Slot`, `Ticket`, `Payment`, `Gate`, `RateCard` |
| Hotel | `Hotel`, `Room`, `Guest` | `Reservation`, `Invoice`, `RoomType`, `Payment`, `Stay` |
| Ride-sharing | `Rider`, `Driver` | `Trip`, `Vehicle`, `Payment`, `Rating`, `RideRequest`, `Location` |
| Movie booking | `Movie`, `Theatre`, `Seat` | `Show`, `Booking`, `Hold`, `Payment`, `Screen`, `Pricing` |
| ATM | `User`, `ATM`, `Money` | `Account`, `Card`, `Transaction`, `Session`, `CashInventory` |
| KV store | `Key`, `Value` | `Entry`, `EvictionPolicy`, `Snapshot`, `ExpiryPolicy`, `Stats` |
| URL shortener | `LongURL`, `ShortURL` | `ShortLink`, `Owner`, `ClickEvent`, `ExpiryPolicy`, `Alias` |
| Tic-Tac-Toe | `Player`, `Board` | `Game`, `Move`, `Cell`, `GameResult`, `Symbol` |
| Online learning | `Student`, `Course`, `Instructor` | `Enrollment`, `Lesson`, `Progress`, `Quiz`, `Certificate`, `Payment` |
| Twitter/X | `User`, `Tweet` | `Follow`, `Like`, `Feed`, `Retweet`, `Notification`, `Timeline` |
| Food delivery | `Customer`, `Restaurant`, `MenuItem` | `Order`, `Cart`, `DeliveryAgent`, `Payment`, `OrderStatusEvent`, `Address` |
| E-commerce / Cart | `User`, `Product`, `Cart` | `Order`, `Inventory`, `Payment`, `Shipment`, `Discount`, `Review` |
| Chess | `Player`, `Board`, `Piece` | `Game`, `Move`, `MoveValidator`, `GameResult`, `Clock` |
| Snake & Ladder | `Player`, `Board`, `Dice` | `Game`, `Snake`, `Ladder`, `Cell`, `MoveResult` |
| Elevator | `Elevator`, `Floor`, `Building` | `Request`, `Direction`, `DispatchPolicy`, `ElevatorState` |
| Vending Machine | `Item`, `Machine`, `User` | `Inventory`, `Slot`, `Coin`/`Note`, `Transaction`, `Refund` |
| Splitwise | `User`, `Group`, `Expense` | `Split`, `Settlement`, `Balance`, `BalanceSheet` |
| Notification System | `User`, `Notification` | `Channel` (email/SMS/push), `Template`, `Subscription`, `DeliveryAttempt` |
| Chat / Messaging | `User`, `Message` | `Conversation`, `Group`, `Participant`, `ReadReceipt`, `Attachment` |
| File Storage (Dropbox) | `User`, `File`, `Folder` | `Version`, `ShareLink`, `Permission`, `SyncEvent`, `Quota` |
| Stock Exchange | `User`, `Stock`, `Order` | `OrderBook`, `Trade`, `Portfolio`, `Position`, `MatchingEngine` |
| Logger | `Message` | `LogEvent`, `LogLevel`, `Appender`/`Sink`, `Formatter`, `Filter` |
| Rate Limiter | `Request`, `Client` | `Bucket`, `Quota`, `Window`, `Rule` |
| Calendar | `User`, `Event` | `Calendar`, `Invitee`, `RSVP`, `Recurrence`, `Reminder` |
| Music streaming | `User`, `Song`, `Artist` | `Playlist`, `PlayEvent`, `Subscription`, `Album`, `Recommendation` |
| Video streaming | `User`, `Video` | `Subscription`, `WatchHistory`, `Recommendation`, `Episode`, `Season` |
| Airbnb | `Host`, `Guest`, `Listing` | `Booking`, `Review`, `Pricing`, `Availability`, `Payment` |
| Banking | `Customer`, `Account` | `Transaction`, `Card`, `Loan`, `Beneficiary`, `Statement` |

### How to spot hidden entities (quick filter)
Ask yourself these 4 questions about each visible entity pair:
1. **Is there a transaction between them?** → likely hidden entity (`Loan`, `Booking`, `Trip`, `Transaction`).
2. **Does the relationship carry metadata (timestamp, amount, status)?** → join entity needed (`Enrollment`, `Reservation`).
3. **Does the system have a policy/rule that varies?** → policy entity (`EvictionPolicy`, `RateCard`, `Pricing`).
4. **Does history/audit matter?** → event entity (`ClickEvent`, `Move`, `Notification`, `Transaction`).

If yes to any → that's a hidden entity to call out in clarification.

---

## Probing Questions Per Domain (memorize the *shape*, not the words)

> **How to use:** When the interviewer says "Design X", these are the **exact 3 questions** that uncover the hidden entities for that domain. Each question targets either a *transaction*, *metadata*, or *policy*.

### Library
1. *"When a user borrows a book, do we just flip a flag, or do we record a `Loan` with timestamps and due date?"* → uncovers `Loan`
2. *"Can a user **reserve** a book that's currently issued, so they're next in queue?"* → uncovers `Reservation`
3. *"Are late returns penalized? Do we track fines per user?"* → uncovers `Fine`

### Parking Lot
1. *"When a vehicle enters, do we just assign a slot, or do we issue a `Ticket` that tracks entry/exit time?"* → uncovers `Ticket`
2. *"Are different slot types priced differently — bike vs car vs truck? Hourly or per-minute?"* → uncovers `RateCard`
3. *"Is payment in scope? Cash, card, or both? Pre-paid or pay-on-exit?"* → uncovers `Payment`

### Hotel
1. *"Is `Reservation` a separate entity from `Room`, with check-in/check-out dates and status?"* → uncovers `Reservation`
2. *"Are rooms grouped by type (Deluxe, Suite) for pricing/availability, or is each room individually priced?"* → uncovers `RoomType`
3. *"Do we generate an `Invoice` per stay, or just charge once on checkout?"* → uncovers `Invoice`

### Ride-Sharing
1. *"Is a `RideRequest` (rider taps button) the same entity as a `Trip` (driver accepted), or two separate entities?"* → uncovers `RideRequest` vs `Trip`
2. *"Does a driver own one vehicle or many? Can they switch vehicles?"* → uncovers `Vehicle` cardinality
3. *"After the trip, do both rider and driver leave ratings? Is that a separate entity?"* → uncovers `Rating`

### Movie Booking
1. *"Is `Show` (movie + theatre + time) a separate entity from `Movie`?"* → uncovers `Show`
2. *"When a user picks a seat but hasn't paid, do we put a temporary `Hold` on it?"* → uncovers `Hold`
3. *"Is pricing per seat category (Gold/Silver) static, or does it vary by show/time?"* → uncovers `Pricing`

### ATM
1. *"Does a user have one account or many? Is `Card` separate from `Account`?"* → uncovers `Card` vs `Account`
2. *"Do we keep a `Session` from card-insert to eject, with multiple operations inside?"* → uncovers `Session`
3. *"Do we record every `Transaction` with type (withdraw/deposit/balance), amount, timestamp?"* → uncovers `Transaction`

### KV Store
1. *"Is each key-value pair just a map entry, or do we wrap it in an `Entry` with TTL and metadata?"* → uncovers `Entry`
2. *"Is eviction (LRU/LFU/TTL) configurable per cache, or hardcoded?"* → uncovers `EvictionPolicy`
3. *"Do we support snapshots/persistence, or pure in-memory?"* → uncovers `Snapshot`

### URL Shortener
1. *"Is the short URL bound to an `Owner` (user account), or anonymous?"* → uncovers `Owner`
2. *"Do we track click analytics — count, timestamp, source?"* → uncovers `ClickEvent`
3. *"Do short URLs expire? Custom aliases allowed?"* → uncovers `ExpiryPolicy`, `Alias`

### Tic-Tac-Toe
1. *"Is `Game` a separate entity from `Board`? Can one game span multiple boards (best-of-3)?"* → uncovers `Game`
2. *"Do we record each `Move` with player, position, timestamp — or just mutate the board?"* → uncovers `Move`
3. *"Do we persist `GameResult` (winner, loser, draw) for history/leaderboard?"* → uncovers `GameResult`

### Online Learning
1. *"When a student joins a course, do we record an `Enrollment` with date, status, payment ref?"* → uncovers `Enrollment`
2. *"Do we track lesson-level `Progress` (% complete, last position) per student?"* → uncovers `Progress`
3. *"Are quizzes/assignments separate entities from lessons? Do we issue certificates?"* → uncovers `Quiz`, `Certificate`

### Twitter/X
1. *"Is `Follow` a separate entity (with timestamp), or just an edge in a graph?"* → uncovers `Follow`
2. *"Is `Feed` (the timeline shown to a user) computed on read, or stored as an entity per user?"* → uncovers `Feed` / `Timeline`
3. *"Are `Like`, `Retweet`, `Reply` separate entities, or attributes on `Tweet`?"* → uncovers engagement entities

### Food Delivery
1. *"When a customer places an order, is `Cart` a separate entity from `Order`, or do we promote cart → order on checkout?"* → uncovers `Cart` vs `Order`
2. *"Do we record every status change (PLACED → PREPARED → PICKED_UP → DELIVERED) as an event, or just a single status field?"* → uncovers `OrderStatusEvent`
3. *"Is `DeliveryAgent` a separate actor with assignment logic, or just a field on the order?"* → uncovers `DeliveryAgent`

### E-commerce / Cart
1. *"Is `Cart` persisted across sessions, and how does it become an `Order`?"* → uncovers `Cart` vs `Order`
2. *"Do we track `Inventory` per product per warehouse, or just a global stock count?"* → uncovers `Inventory`
3. *"Are discounts/coupons separate entities with rules, or hardcoded?"* → uncovers `Discount`

### Chess
1. *"Do we record each `Move` (from-to-piece-timestamp), or just mutate the board?"* → uncovers `Move`
2. *"Is move validation a separate component (per-piece rules), or inline in `Game`?"* → uncovers `MoveValidator`
3. *"Do we track game clock per player (timed games)?"* → uncovers `Clock`

### Snake & Ladder
1. *"Are `Snake` and `Ladder` separate entities mapped to board cells, or just attributes on cells?"* → uncovers `Snake`, `Ladder`
2. *"Do we record every dice roll + resulting move for replay, or just current position?"* → uncovers `MoveResult`
3. *"Is `Game` separate from `Board` (multi-game with same board)?"* → uncovers `Game`

### Elevator
1. *"Is a button press a transient event, or do we persist it as a `Request` until served?"* → uncovers `Request`
2. *"Is the dispatch policy (nearest-elevator, SCAN, LOOK) pluggable?"* → uncovers `DispatchPolicy`
3. *"Does each elevator have a state (IDLE/MOVING_UP/MOVING_DOWN/MAINTENANCE)?"* → uncovers `ElevatorState`

### Vending Machine
1. *"Is `Inventory` per slot (slot → item + count), or a global pool?"* → uncovers `Slot`, `Inventory`
2. *"Do we record each purchase as a `Transaction` with item, amount, timestamp?"* → uncovers `Transaction`
3. *"How do we handle refunds/insufficient-change — separate `Refund` entity?"* → uncovers `Refund`

### Splitwise
1. *"When an `Expense` is added, do we materialize per-person `Split` entities, or compute splits on the fly?"* → uncovers `Split`
2. *"Do we track running `Balance` per (user, user) pair, or compute from all expenses each time?"* → uncovers `Balance`
3. *"Is `Settlement` (one user pays another to clear debt) a separate entity from `Expense`?"* → uncovers `Settlement`

### Notification System
1. *"Is `Channel` (email/SMS/push) an entity with config, or hardcoded?"* → uncovers `Channel`
2. *"Do users `Subscribe` to topics? Is subscription per-channel?"* → uncovers `Subscription`
3. *"Do we track each `DeliveryAttempt` (success/fail/retry) for audit?"* → uncovers `DeliveryAttempt`

### Chat / Messaging
1. *"Is `Conversation` a separate entity from `Message`, holding participants and metadata?"* → uncovers `Conversation`
2. *"Do we track per-user `ReadReceipt` per message?"* → uncovers `ReadReceipt`
3. *"Are `Attachment`s separate entities (with storage refs) or inline blobs?"* → uncovers `Attachment`

### File Storage (Dropbox)
1. *"Do we keep file `Version` history, or overwrite on update?"* → uncovers `Version`
2. *"Is sharing modeled as `ShareLink` + `Permission`, or just a flag on the file?"* → uncovers `ShareLink`, `Permission`
3. *"Do we track per-user `Quota` (storage limit)?"* → uncovers `Quota`

### Stock Exchange
1. *"Is `OrderBook` (buy/sell queues per stock) a separate entity from `Stock`?"* → uncovers `OrderBook`
2. *"When two orders match, do we record a `Trade` entity (separate from the orders)?"* → uncovers `Trade`
3. *"Do we track per-user `Portfolio`/`Position` (stock → quantity)?"* → uncovers `Portfolio`

### Logger
1. *"Is each log call a `LogEvent` (level, message, timestamp, source)?"* → uncovers `LogEvent`
2. *"Are `Appender`s/`Sink`s pluggable (file, console, network)?"* → uncovers `Appender`
3. *"Do we support `Filter`s (level threshold, regex, sampling)?"* → uncovers `Filter`

### Rate Limiter
1. *"Is the limit per-user, per-IP, or per-API-key — and is `Bucket` a per-client entity?"* → uncovers `Bucket`
2. *"Is the algorithm (token bucket / fixed window / sliding window) pluggable?"* → uncovers algorithm policy
3. *"Are different APIs governed by different `Rule`s (quota, refill rate)?"* → uncovers `Rule`

### Calendar
1. *"Is `Event` shared across multiple users (invitees), or owned by one?"* → uncovers `Invitee`, `RSVP`
2. *"Do recurring events store one `Recurrence` rule, or N expanded events?"* → uncovers `Recurrence`
3. *"Is `Reminder` a separate entity per event per user?"* → uncovers `Reminder`

### Music Streaming
1. *"Are `Playlist`s user-owned entities, or just collections of song IDs?"* → uncovers `Playlist`
2. *"Do we record every `PlayEvent` (user, song, timestamp) for recommendations?"* → uncovers `PlayEvent`
3. *"Is `Subscription` (free/premium/family) a separate entity governing access?"* → uncovers `Subscription`

### Video Streaming (Netflix)
1. *"Is per-user `WatchHistory` (with resume position) tracked per video?"* → uncovers `WatchHistory`
2. *"Are `Episode` and `Season` separate entities under a `Show`?"* → uncovers `Episode`, `Season`
3. *"Is `Subscription` separate from `User`, with plan tier and device limit?"* → uncovers `Subscription`

### Airbnb
1. *"Is `Booking` separate from `Listing`, with check-in/check-out and status?"* → uncovers `Booking`
2. *"Is `Pricing` per listing per night, or does it vary by date (peak/off-peak)?"* → uncovers `Pricing`, `Availability`
3. *"Are `Review`s left by both guest and host, as separate entities?"* → uncovers `Review`

### Banking
1. *"Is each `Account` linked to one customer or many (joint accounts)?"* → uncovers ownership cardinality
2. *"Do we record every `Transaction` (debit/credit/transfer) with reference to source/dest accounts?"* → uncovers `Transaction`
3. *"Are `Card`, `Loan`, `Beneficiary` separate entities tied to an account?"* → uncovers `Card`, `Loan`, `Beneficiary`

---

### The universal probing pattern (works on any new domain)

If the domain isn't listed above, fall back to **these 3 universal questions**:

```
Q1 (transaction): "When [actor A] does X with [entity B], 
                  do we record a separate entity capturing that event?"

Q2 (metadata):    "Does that interaction carry timestamps, status, 
                  amount, or expiry? If yes → it's a join entity."

Q3 (policy):      "Is there a rule (price, eviction, expiry, allocation) 
                  that varies? If yes → it's a policy entity."
```

These 3 questions surface 80% of hidden entities in any LLD problem.

---


## Mini-Check (do this before drills)

**Problem:** *"Design a hotel room reservation system."*

Apply the **4 gates of Entities**. Write **just 4 answers** (one per gate) in 3 minutes.

Format:
```
Gate 1 [NOUNS]: List entities you see + ask about hidden ones
Gate 2 [RELATIONSHIPS]: Pick 2 key relationships and describe them
Gate 3 [CARDINALITY]: State cardinality for those 2 relationships
Gate 4 [IDENTITY]: What uniquely identifies each entity
```

Pass criteria: hits all 4 gates, names ≥1 hidden entity, picks correct cardinality.

---

## Quick Reference Card

```
┌────────────────────────────────────────────────┐
│  PILLAR 2 — 4 GATES OF ENTITIES                │
├────────────────────────────────────────────────┤
│  G1. NOUNS         → visible + hidden          │
│  G2. RELATIONSHIPS → who owns whom?            │
│  G3. CARDINALITY   → 1-1 / 1-N / N-N           │
│  G4. IDENTITY      → assigned vs derived       │
├────────────────────────────────────────────────┤
│  Q-template (≤45s):                            │
│  "Hidden entities? Ownership? Cardinality?     │
│   Identity assigned or derived?"               │
└────────────────────────────────────────────────┘
```

