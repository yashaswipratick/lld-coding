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

## 🟢 BEGINNER FOUNDATION — The 3-Tier Entity Sweep

> **Read this first if entity-spotting feels overwhelming.** Once you internalize this, the per-domain table below becomes obvious instead of memorization.

### The core insight

Most LLD problems hide entities in **3 predictable layers**. For *any* problem, sweep in this exact order:

```
┌───────────────────────────────────────────────────────────────┐
│ TIER 1 — STATED            "What words did the interviewer    │
│  (the obvious actors)       literally say?"                   │
│                             → Usually: people + the "thing"   │
│                                                               │
│ TIER 2 — CONTAINER          "What physical/logical space      │
│  (capacity / location)      holds or houses something?"       │
│                             → Has capacity, position, state   │
│                                                               │
│ TIER 3 — TRANSACTION        "What event has timestamp,        │
│  (the receipt / record)     status, amount, or money?"        │
│                             → Created when actor uses         │
│                               container; lives, then closes   │
└───────────────────────────────────────────────────────────────┘
```

### Why this works (the mental shortcut)

- **Tier 1** = the *who* and *what* (nouns spoken).
- **Tier 2** = the *where* (the slot/room/seat/cell that gets occupied).
- **Tier 3** = the *when + how much* (the loan/booking/ticket/order that records the act).

> 90% of LLD problems are: **Actor → uses → Container → producing → Transaction**.

### Worked example — Library (read this slowly)

**Problem:** *"Design a library where users borrow books. Each book has multiple copies."*

| Tier | Question | Answer |
|---|---|---|
| **T1 — Stated** | What was literally said? | `User`, `Book`, `Copy` |
| **T2 — Container** | What physical thing holds/houses something with limited capacity? | `Copy` is the physical object on the shelf (the "container of borrow-ability"). Each copy can be held by **at most one user at a time**. |
| **T3 — Transaction** | When user takes copy, what record is created with timestamp + status? | `Loan` (issuedAt, dueDate, returnedAt, status). And if the copy is unavailable → `Reservation` (queue with timestamp). Late return → `Fine` (amount). |

**Wiring it together:**
```
User  ──borrows──▶  Loan  ──occupies──▶  Copy  (belongs to)  Book
 (T1)               (T3)                  (T2)                 (T1)
```

That's the whole picture. Same pattern for every domain below.

### How "physical space" applies even when it's not a building

Many people get stuck thinking "container = room/floor only". It's broader:

| Domain | Container (T2) is… | Why it's a "container" |
|---|---|---|
| Library | `Copy` | Holds the *state of being borrowable*; capacity = 1 user at a time |
| Parking lot | `Slot` | Holds 1 vehicle; has location (floor + number) |
| Hotel | `Room` | Holds 1 reservation per date range |
| Movie booking | `Seat` | Holds 1 booking per show |
| Vending machine | `Slot` (A1, B2…) | Holds N units of one item |
| Elevator | `Floor` (current) + cabin | Cabin holds people; floor is position |
| ATM | `CashInventory` | Holds notes; has capacity |
| KV store | `Entry` | Holds 1 value per key |
| File storage | `Folder` | Holds files (tree structure) |
| Chat | `Conversation` | Holds messages + participants |
| Stock exchange | `OrderBook` | Holds buy/sell queues per stock |
| Calendar | `Event` slot (date+time) | Holds 1 booking on a calendar grid |
| Rate limiter | `Bucket` | Holds N tokens for a client |

> **Rule of thumb:** A container has **capacity, position, or state** — and only one transaction can "own" it at a time (or N units, in inventory cases).

### The 3-Tier Sweep Applied to All Domains

| Domain | T1 — Stated (actors + thing) | T2 — Container (capacity / location) | T3 — Transaction (timestamp / status / money) |
|---|---|---|---|
| Library | `User`, `Book` | `Copy` (1 user at a time) | `Loan`, `Reservation`, `Fine` |
| Parking lot | `Vehicle`, `ParkingLot` | `Slot` (on `Floor`) | `Ticket`, `Payment` |
| Hotel | `Guest`, `Hotel` | `Room` (of `RoomType`) | `Reservation`, `Invoice`, `Payment` |
| Ride-sharing | `Rider`, `Driver` | `Vehicle` (1 driver active) | `RideRequest`, `Trip`, `Payment`, `Rating` |
| Movie booking | `Movie`, `Theatre` | `Seat` (in `Screen`, per `Show`) | `Hold`, `Booking`, `Payment` |
| ATM | `User`, `ATM` | `Account`, `Card`, `CashInventory` | `Session`, `Transaction` |
| KV store | `Key`, `Value` | `Entry` (in `Cache`) | (no money) `ExpiryEvent`, `EvictionEvent` |
| URL shortener | `LongURL`, `ShortURL` | `ShortLink` (1 alias slot) | `ClickEvent`, `ExpiryPolicy` |
| Tic-Tac-Toe | `Player` | `Cell` (on `Board`, 1 symbol) | `Move`, `GameResult` |
| Online learning | `Student`, `Instructor`, `Course` | `Lesson` (slot in course) | `Enrollment`, `Progress`, `Quiz`, `Certificate`, `Payment` |
| Twitter/X | `User`, `Tweet` | `Feed`/`Timeline` (per user) | `Follow`, `Like`, `Retweet`, `Reply` |
| Food delivery | `Customer`, `Restaurant`, `MenuItem` | `Cart` (1 active per customer) | `Order`, `OrderStatusEvent`, `Payment` |
| E-commerce | `User`, `Product` | `Cart`, `Inventory` (per warehouse) | `Order`, `Payment`, `Shipment`, `Discount` |
| Chess | `Player` | `Cell` (8x8), `Piece` position | `Move`, `GameResult`, `Clock` tick |
| Snake & Ladder | `Player`, `Dice` | `Cell` (1-100 on `Board`) | `MoveResult`, `GameResult` |
| Elevator | `Building` | `Elevator` (cabin) at `Floor` | `Request` (queued button press) |
| Vending Machine | `Item`, `Machine`, `User` | `Slot` (A1, B2 — holds N units) | `Transaction`, `Refund` |
| Splitwise | `User`, `Group` | (no physical container — `Group` is the logical container) | `Expense`, `Split`, `Settlement`, `Balance` |
| Notification | `User` | `Channel` (email/SMS/push pipe) | `Notification`, `DeliveryAttempt` |
| Chat | `User`, `Message` | `Conversation` (holds participants + messages) | `ReadReceipt`, `Attachment` upload |
| File storage | `User`, `File` | `Folder` (tree), `Quota` (per user) | `Version`, `ShareLink`, `SyncEvent` |
| Stock exchange | `User`, `Stock` | `OrderBook` (buy queue + sell queue) | `Order`, `Trade`, `Position` snapshot |
| Logger | (system) | `Appender`/`Sink` (output pipe) | `LogEvent` |
| Rate limiter | `Client`, `Request` | `Bucket` (per client, N tokens) | `AllowDecision` (with timestamp) |
| Calendar | `User`, `Event` | `Calendar` (date×time grid) | `Invitee`+`RSVP`, `Reminder` fire |
| Music streaming | `User`, `Song`, `Artist` | `Playlist` (ordered slot list) | `PlayEvent`, `Subscription` |
| Video streaming | `User`, `Video` | `Episode` slot in `Season`/`Show` | `WatchHistory`, `Subscription` |
| Airbnb | `Host`, `Guest`, `Listing` | `Listing` per date (availability calendar) | `Booking`, `Review`, `Payment` |
| Banking | `Customer` | `Account`, `Card` | `Transaction` (debit/credit), `Statement` |

### How to use this in an interview (15-second drill)

When you hear "Design X", say to yourself:
1. **"Who/what was named?"** → write Tier 1.
2. **"Where does the action *land* — what fills up?"** → write Tier 2.
3. **"What gets recorded with a timestamp/amount/status?"** → write Tier 3.

That's your entity list. Now you can confidently ask the interviewer:
> *"I see Tier-1 entities `[X, Y]`. I'd model `[Z]` as the container with capacity, and `[W]` as the transaction record with timestamps. Sound right?"*

This single sentence demonstrates senior-level entity thinking.

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

If yes → that's a hidden entity to call out in clarification.

---


## 🟢 BEGINNER FOUNDATION — How to *Identify* Relationships (before memorizing them)

> **The problem most beginners face:** they look at the Gate 2 table and feel like the relationships were pulled out of thin air. They weren't. Each one comes from applying **3 simple questions** to the 3-tier entities you already found.

### The 3 questions that uncover every relationship

Once you have your **T1 (actors), T2 (container), T3 (transaction)** entities, walk the data flow:

```
   ACTOR  ──[Q1: who creates?]──▶  TRANSACTION  ──[Q2: what does it occupy?]──▶  CONTAINER
                                          │
                                          └──[Q3: what does it produce/settle?]──▶  TERMINAL ENTITY
                                                                                    (Payment, Invoice, Rating, Result…)
```

| # | Question | What it reveals | Relationship type |
|---|---|---|---|
| **Q1** | *"Who **creates / owns** the transaction?"* | Actor → Transaction | **Ownership**, transactional, 1-to-N (over time) |
| **Q2** | *"What container does the transaction **occupy / target**?"* | Transaction → Container | **Assignment**, temporary, 1-to-1 *active* / 1-to-N historical |
| **Q3** | *"What does the transaction **produce / settle into** at the end?"* | Transaction → Terminal entity | **Lifecycle terminal**, 1-to-1 (Payment, Rating, Invoice, Result) |

> **Bonus Q4 (structural):** *"How is the container itself **composed**?"* → Parent → Child (Building → Floor → Slot). This is **composition**, permanent, 1-to-N.

### Worked example — Library

T1: `User`, `Book` · T2: `Copy` (composes from `Book`) · T3: `Loan` (terminal: `Fine` if late)

| Question | Answer | Relationship |
|---|---|---|
| Q1: who creates Loan? | User | `User → Loan` (owns, transactional, 1-N) |
| Q2: what does Loan occupy? | Copy | `Loan → Copy` (assigns, temporary, 1-1 active) |
| Q3: what does Loan settle into? | Fine (sometimes) | `Loan → Fine` (terminal, 0..1) |
| Q4: container composition? | Book has many Copies | `Book → Copy` (composition, permanent, 1-N) |

Notice: you didn't memorize anything. You **derived** all 4 relationships from the entity tiers.

### The 4 Relationship Flavors (vocabulary you'll reuse)

| Flavor | Meaning | When | Lifetime |
|---|---|---|---|
| **Composition** | Parent literally *contains* child; child can't exist alone | Container hierarchy (Floor → Slot) | Permanent |
| **Ownership** | Actor *creates and owns* a record | Actor → Transaction | Transactional |
| **Assignment** | Transaction *temporarily occupies* a container | Transaction → Container | Temporary (1-1 active) |
| **Terminal / Settlement** | Transaction *produces* a final record | Transaction → Payment / Result / Invoice | 1-1, fires on close |

### Applying the 3 Questions to All Domains

> **Read this table as:** "Given my T1/T2/T3 entities, here's what Q1/Q2/Q3 produces." This is the *derivation* of the Gate 2 table below.

| Domain | Q1: Actor → Transaction (owns) | Q2: Transaction → Container (occupies) | Q3: Transaction → Terminal (settles) | Q4: Container composition |
|---|---|---|---|---|
| Library | `User → Loan` | `Loan → Copy` | `Loan → Fine` (if late) | `Book → Copy` |
| Parking lot | `Vehicle → Ticket` | `Ticket → Slot` | `Ticket → Payment` | `ParkingLot → Floor → Slot` |
| Hotel | `Guest → Reservation` | `Reservation → Room` | `Reservation → Invoice/Payment` | `Hotel → Floor → Room` |
| Ride-sharing | `Rider → RideRequest → Trip` | `Trip → Vehicle` (driver's) | `Trip → Payment + Rating` | `Driver → Vehicle` |
| Movie booking | `User → Booking` | `Booking → Seat(s)` (per Show) | `Booking → Payment` (via Hold) | `Theatre → Screen → Seat`; `Show = Movie+Screen+Time` |
| ATM | `Customer → Session → Transaction` | `Transaction → Account` (debits/credits) | `Transaction → Receipt` | `ATM → CashInventory` |
| KV store | `Client → put/get` (no actor record) | `Entry → Key` (in `Cache`) | `Entry → ExpiryEvent / EvictionEvent` | `Cache → Entry` |
| URL shortener | `Owner → ShortLink` | `ShortLink → shortCode slot` | `ShortLink → ClickEvent(s)` | (none) |
| Tic-Tac-Toe | `Player → Move` | `Move → Cell` | `Game → GameResult` | `Board → Cell` (3×3) |
| Online learning | `Student → Enrollment` | `Enrollment → Course/Lesson` | `Enrollment → Certificate/Payment` | `Course → Lesson` |
| Twitter/X | `User → Tweet` (and `User → Follow`) | `Tweet → Feed` (of followers) | `Tweet → Like/Reply/Retweet` | (none) |
| Food delivery | `Customer → Cart → Order` | `Order → Restaurant + DeliveryAgent` | `Order → Payment + OrderStatusEvent(s)` | `Restaurant → MenuItem` |
| E-commerce | `User → Cart → Order` | `Order → Inventory` (decrements) | `Order → Payment + Shipment` | `Warehouse → Inventory → Product` |
| Chess | `Player → Move` | `Move → Cell` (from→to) | `Game → GameResult` | `Board → Cell` (8×8) |
| Snake & Ladder | `Player → MoveResult` (via Dice) | `MoveResult → Cell` | `Game → GameResult` | `Board → Cell` (1-100) |
| Elevator | `Person → Request` | `Request → Elevator → Floor` | `Request → completed event` | `Building → Elevator(s) + Floor(s)` |
| Vending Machine | `User → Transaction` | `Transaction → Slot` (decrements count) | `Transaction → Refund?` | `Machine → Slot` |
| Splitwise | `User → Expense` (in `Group`) | `Expense → Split(s)` (per user) | `Expense → Settlement` (clears Balance) | `Group → User(s)` |
| Notification | `System → Notification` | `Notification → Channel` (via Subscription) | `Notification → DeliveryAttempt(s)` | `User → Subscription(s)` |
| Chat | `User → Message` | `Message → Conversation` | `Message → ReadReceipt(s)` | `Conversation → Participant(s)` |
| File storage | `User → File/Version` | `File → Folder` (path) | `File → ShareLink` (with Permission) | `Folder → File(s)` (tree) |
| Stock exchange | `User → Order` | `Order → OrderBook` (per Stock) | `Order ⊕ Order → Trade` | `Stock → OrderBook` |
| Logger | `Code → LogEvent` | `LogEvent → Appender` (via Filter) | `Appender → Formatted output` | `Logger → Appender(s)` |
| Rate limiter | `Client → Request` | `Request → Bucket` (per Client+Rule) | `Request → AllowDecision` | `Rule → Bucket(s)` |
| Calendar | `User → Event` | `Event → Calendar slot` (date+time) | `Event → Reminder(s) + RSVP(s)` | `User → Calendar` |
| Music streaming | `User → PlayEvent` | `PlayEvent → Song` (in Playlist) | `PlayEvent → contributes to Recommendation` | `User → Playlist → Song(s)` |
| Video streaming | `User → WatchHistory entry` | `WatchHistory → Episode` (in Season/Show) | `WatchHistory → resume position` | `Show → Season → Episode` |
| Airbnb | `Guest → Booking` | `Booking → Listing` (per date range) | `Booking → Payment + Review(s)` | `Host → Listing(s)` |
| Banking | `Customer → Transaction` | `Transaction → Account` (debit/credit) | `Transaction → Statement entry` | `Customer → Account → Card` |

### How to use this in an interview (45-second drill)

For *any* domain, after stating Tier 1/2/3 entities, recite:
1. **"Who owns the transaction?"** → state Q1 (actor → transaction).
2. **"What container does it occupy?"** → state Q2 (transaction → container, flag *active vs historical*).
3. **"What does it produce when it closes?"** → state Q3 (transaction → terminal).
4. **"How's the container composed?"** → state Q4 (parent → child).

You've now identified every key relationship — *without* memorizing the table below. The Gate 2 table is just the *result*; this section is the *recipe*.

---

### Per-Domain Derivation Walkthroughs (Relationships + Cardinality)

> **How to use:** For each domain, walk Q1→Q4 and the cardinality follows. Format per domain:
> - **Q1** (Actor → Transaction): ownership + 1-N over time
> - **Q2** (Transaction → Container): ⚠️ flag *active* (1-1) vs *historical* (1-N)
> - **Q3** (Transaction → Terminal): 1-1 settlement
> - **Q4** (Container composition): 1-N permanent
>
> *Parking Lot is intentionally skipped — already worked through above. Identity (Gate 4) has its own cheatsheet.*

#### Library
- **Q1** `User → Loan` — owns, transactional → **1-N** historical
- **Q2** `Loan → Copy` — assigns, temporary → **1-1 active**, **N-1 historical**
- **Q3** `Loan → Fine` (if late) — terminal → **0..1**
- **Q4** `Book → Copy` — composition → **1-N**

#### Hotel
- **Q1** `Guest → Reservation` → **1-N**
- **Q2** `Reservation → Room` → **1-1 per date range**, **1-N over time** (need date-range index)
- **Q3** `Reservation → Invoice/Payment` → **1-1**
- **Q4** `Hotel → Floor → Room`; `RoomType → Room` → **1-N each**

#### Ride-Sharing
- **Q1** `Rider → RideRequest → Trip` — state evolution → **1-N** historical, **1-1 active**
- **Q2** `Trip → Vehicle` — driver's vehicle → **1-1 per trip**, **N-1 historical**
- **Q3** `Trip → Payment + Rating` — terminal events → **1-1 each**
- **Q4** `Driver → Vehicle` → **1-1 active** OR **1-N** (clarify)

#### Movie Booking
- **Q1** `User → Booking` → **1-N**
- **Q2** `Booking → Seat(s)` per `Show` → **1-N seats per booking**, **1-1 booking per (seat, show)**
- **Q3** `Booking → Hold → Payment` — lifecycle → **1-1**
- **Q4** `Theatre → Screen → Seat`; `Show = Movie + Screen + Time` (composite)

#### ATM
- **Q1** `Customer → Session → Transaction` → **1-N transactions per session**
- **Q2** `Transaction → Account` (debits/credits) → **N-1**
- **Q3** `Transaction → Receipt` → **1-1**
- **Q4** `Customer → Account → Card`; `ATM → CashInventory` → **1-N**

#### KV Store
- **Q1** Client `put/get` (no actor entity persisted)
- **Q2** `Entry → Key` (in `Cache`) → **1-N entries in cache**, **1-1 entry per key**
- **Q3** `Entry → ExpiryEvent / EvictionEvent` → terminal
- **Q4** `Cache → Entry` → **1-N**; `Cache → EvictionPolicy` → **1-1 strategy**

#### URL Shortener
- **Q1** `Owner → ShortLink` → **1-N**
- **Q2** `ShortLink → shortCode` (alias slot) → **1-1**; `LongURL ↔ ShortLink` → **1-1 OR 1-N** (custom alias — clarify)
- **Q3** `ShortLink → ClickEvent(s)` → **1-N**; `ShortLink → ExpiryPolicy` → **N-1**
- **Q4** none (flat)

#### Tic-Tac-Toe
- **Q1** `Player → Move` → **1-N ordered**
- **Q2** `Move → Cell` → **1-1 per game** (each cell takes 1 symbol)
- **Q3** `Game → GameResult` → **1-1**
- **Q4** `Board → Cell` (3×3) → **1-N**; `Game → Player` → **1-2**

#### Online Learning
- **Q1** `Student → Enrollment` → **1-N**; `Student ↔ Course` = **N-N via Enrollment**
- **Q2** `Enrollment → Lesson` (tracks) → `Enrollment → Progress` = **1-1 per lesson**
- **Q3** `Enrollment → Certificate / Payment` → **0..1 / 1-1**
- **Q4** `Course → Lesson` → **1-N ordered**

#### Twitter/X
- **Q1** `User → Tweet` → **1-N**; `User → Follow → User` → **N-N self-referential**
- **Q2** `Tweet → Feed` (of followers) → fan-out, **N-N**
- **Q3** `Tweet → Like + Reply + Retweet` → **1-N each**
- **Q4** none (graph-shaped)

#### Food Delivery
- **Q1** `Customer → Cart → Order` — state evolution → **1-N orders historical**, **1-1 cart active**
- **Q2** `Order → Restaurant` (N-1); `Order → DeliveryAgent` (N-1 active); `Order ↔ MenuItem` = **N-N via LineItem**
- **Q3** `Order → Payment + OrderStatusEvent(s)` → **1-1 payment, 1-N events**
- **Q4** `Restaurant → MenuItem` → **1-N**

#### E-commerce
- **Q1** `User → Cart → Order` → **1-1 cart active**, **1-N orders**
- **Q2** `Order → Inventory` (decrements) → **N-N via OrderItem**
- **Q3** `Order → Payment + Shipment` → **1-1 payment**, **1-N shipments** (split)
- **Q4** `Warehouse → Inventory → Product` → **1-N each**

#### Chess
- **Q1** `Player → Move` → **1-N ordered ledger**
- **Q2** `Move → Cell` (from→to) → **1-1 per move**
- **Q3** `Game → GameResult` → **1-1**
- **Q4** `Board → Cell` (8×8); `Board → Piece` → **1-N (max 32)**; `Game → Player` → **1-2**

#### Snake & Ladder
- **Q1** `Player → MoveResult` (via Dice) → **1-N**
- **Q2** `MoveResult → Cell` → **N-1**
- **Q3** `Game → GameResult` → **1-1**
- **Q4** `Board → Cell` (1-100); `Board → Snake/Ladder` → **1-N each**; `Game → Player` → **1-N**

#### Elevator
- **Q1** `Person → Request` → **1-N**
- **Q2** `Request → Elevator → Floor` — assignment → **1-N pending queue**, **1-1 current floor**
- **Q3** `Request → completed event` → **1-1 terminal**
- **Q4** `Building → Elevator(s) + Floor(s)` → **1-N each**; `Building → DispatchPolicy` → **1-1**

#### Vending Machine
- **Q1** `User → Transaction` → **1-N**
- **Q2** `Transaction → Slot` (decrements count) → **N-1**
- **Q3** `Transaction → Refund?` → **0..1**
- **Q4** `Machine → Slot` → **1-N (Map<SlotId, Slot>)**; `Slot → Item + Count` (composite)

#### Splitwise
- **Q1** `User → Expense` (in `Group`) → **1-N**
- **Q2** `Expense → Split(s)` (per participant) → **1-N**; `User ↔ User → Balance` = **N-N pair-wise**
- **Q3** `Expense → Settlement` (clears Balance) → terminal
- **Q4** `Group → User(s)` = **N-N**; `Group → Expense` → **1-N**

#### Notification
- **Q1** `System → Notification` → **1-N**
- **Q2** `Notification → Channel` (via Subscription) → **N-N via Subscription**
- **Q3** `Notification → DeliveryAttempt(s)` → **1-N retries**
- **Q4** `User → Subscription(s)`; `Channel → Template` → **1-N**

#### Chat
- **Q1** `User → Message` → **1-N**
- **Q2** `Message → Conversation` → **N-1**; `Conversation ↔ User` = **N-N via Participant**
- **Q3** `Message → ReadReceipt(s)` → **1-N (one per recipient)**
- **Q4** `Conversation → Message` → **1-N ordered**

#### File Storage
- **Q1** `User → File / Version` → **1-N owned**, **N-N shared**
- **Q2** `File → Folder` (path) → **N-1**
- **Q3** `File → ShareLink → Permission` → composite terminal
- **Q4** `Folder → File(s)` → **1-N tree**; `File → Version(s)` → **1-N**

#### Stock Exchange
- **Q1** `User → Order` → **1-N**
- **Q2** `Order → OrderBook` (per Stock) → **N-1**; `OrderBook → Order(s)` = **1-N buy + 1-N sell queues**
- **Q3** `Order ⊕ Order → Trade` (matches produce) → **1-N partial fills**
- **Q4** `Stock → OrderBook` → **1-1**; `User → Position` → **1-N (one per stock)**

#### Logger
- **Q1** `Code → LogEvent` → **1-N**
- **Q2** `LogEvent → Appender` (via Filter pipeline) → **N-N via Filter**
- **Q3** `Appender → Formatted output` → **1-1 via Formatter**
- **Q4** `Logger → Appender(s)` → **1-N**; `Appender → Filter` → **1-N**

#### Rate Limiter
- **Q1** `Client → Request` → **1-N**
- **Q2** `Request → Bucket` (per Client+Rule) → **N-1**; `Client ↔ Bucket` = **1-1 active**
- **Q3** `Request → AllowDecision` → **1-1**
- **Q4** `Rule → Bucket(s)` → **1-N**; `Rule → Window + Quota` (composite)

#### Calendar
- **Q1** `User → Event` → **1-N organized**
- **Q2** `Event → Calendar slot` (date+time) → **1-1 per slot**; `Event ↔ User` (attendees) = **N-N via Invitee/RSVP**
- **Q3** `Event → Reminder(s)` → **1-N per user**
- **Q4** `User → Calendar`; `Event → Recurrence` → **1-1**

#### Music Streaming
- **Q1** `User → PlayEvent` → **1-N history**
- **Q2** `PlayEvent → Song` (in Playlist) → **N-1**; `User ↔ Playlist ↔ Song` = **N-N via PlaylistItem**
- **Q3** `PlayEvent → contributes to Recommendation` → derived
- **Q4** `User → Playlist → Song(s)` → **1-N → N-N ordered**; `User → Subscription` → **1-1 active**

#### Video Streaming
- **Q1** `User → WatchHistory entry` → **1-N (one per video)**
- **Q2** `WatchHistory → Episode` (in Season/Show) → **N-1**
- **Q3** `WatchHistory → resume position` → **1-1** (snapshot)
- **Q4** `Show → Season → Episode` → **1-N → 1-N tree**; `User → Subscription` → **1-1 active**

#### Airbnb
- **Q1** `Guest → Booking` → **1-N**
- **Q2** `Booking → Listing` (per date range) → **1-N over time, non-overlapping per date**
- **Q3** `Booking → Payment + Review(s)` → **1-1 payment, 2 reviews (guest + host)**
- **Q4** `Host → Listing(s)` → **1-N**

#### Banking
- **Q1** `Customer → Transaction` → **1-N**
- **Q2** `Transaction → Account` (debit/credit) → **N-1** (or 2-1 for transfer: src + dest)
- **Q3** `Transaction → Statement entry` → **1-1 ledger entry**
- **Q4** `Customer → Account` → **1-N or N-N (joint)**; `Account → Card(s) + Beneficiary(s)` → **1-N each**

> **Pattern recap:** every domain follows **Actor → Transaction → Container + Terminal**. Cardinality nuance always lives at Q2 (1-1 *active* vs 1-N *historical*). If you nail Q2's temporal trap, you score senior-level on Gate 3.

---

## Gate 2 Cheatsheet — Key Relationships (per domain)

> **How to use:** For each domain, these are the **2–3 most important relationships** you must surface in clarification. Memorize the *shape*, not the words. Each row tells you: who owns whom, and whether the link is permanent or transactional.

| Domain | Key Relationship 1 | Key Relationship 2 | Key Relationship 3 |
|---|---|---|---|
| Library | `User → Loan` (owns, transactional) | `Book → Copy` (composition, permanent) | `Loan → Copy` (association, temporary) |
| Parking lot | `Vehicle → Ticket` (1 active ticket per vehicle) | `Slot → Vehicle` (occupies, temporary) | `Ticket → Payment` (settles, 1-1) |
| Hotel | `Guest → Reservation` (owns, transactional) | `Reservation → Room` (assigns, temporary) | `Reservation → Invoice` (generates, 1-1) |
| Ride-sharing | `Rider → RideRequest → Trip` (state evolution) | `Driver → Vehicle` (drives, 1 active) | `Trip → Payment + Rating` (terminal events) |
| Movie booking | `Show = Movie + Screen + Time` (composite) | `Booking → Seat(s)` (reserves, temporary) | `Booking → Hold → Payment` (lifecycle) |
| ATM | `Card → Account(s)` (accesses, 1-N) | `Session → Transaction(s)` (contains, 1-N) | `Account → Transaction` (records on, ledger) |
| KV store | `Entry → Key + Value` (composition) | `Cache → EvictionPolicy` (uses, strategy) | `Entry → ExpiryPolicy` (governed by) |
| URL shortener | `Owner → ShortLink` (creates, 1-N) | `ShortLink → ClickEvent` (logs, 1-N) | `ShortLink → ExpiryPolicy` (governed by) |
| Tic-Tac-Toe | `Game → Board` (has, 1-1) | `Game → Move(s)` (records, 1-N ordered) | `Game → GameResult` (terminates with, 1-1) |
| Online learning | `Student → Enrollment → Course` (join entity) | `Enrollment → Progress` (tracks, 1-1 per lesson) | `Course → Lesson(s)` (composition, ordered) |
| Twitter/X | `User → Follow → User` (self-referential, N-N) | `User → Tweet(s)` (authors, 1-N) | `Tweet → Like + Reply + Retweet` (engagement, 1-N) |
| Food delivery | `Customer → Cart → Order` (state evolution) | `Order → DeliveryAgent` (assigned to, N-1) | `Order → OrderStatusEvent(s)` (audit trail, 1-N) |
| E-commerce | `User → Cart → Order` (state evolution) | `Order → Payment + Shipment` (terminal, 1-1 each) | `Product → Inventory` (per-warehouse, 1-N) |
| Chess | `Game → Player(s)` (1-2) | `Game → Move(s)` (ordered ledger, 1-N) | `Move → Piece + From + To` (composite VO) |
| Snake & Ladder | `Game → Player(s)` (1-N) | `Board → Snake(s) + Ladder(s)` (1-N each) | `Game → MoveResult(s)` (history, 1-N) |
| Elevator | `Building → Elevator(s)` (1-N) | `Elevator → Request(s)` (queue, 1-N) | `Building → DispatchPolicy` (1-1 strategy) |
| Vending Machine | `Machine → Slot(s)` (composition, 1-N) | `Slot → Item + Count` (composite) | `Transaction → Item + Refund?` (1-1, optional) |
| Splitwise | `Group → Expense(s)` (1-N) | `Expense → Split(s)` (composition, 1-N per user) | `User ↔ User → Balance` (N-N pair-wise) |
| Notification | `User → Subscription(s) → Channel` (N-N via join) | `Notification → DeliveryAttempt(s)` (1-N retry) | `Notification → Template` (rendered from, N-1) |
| Chat | `Conversation → Participant(s)` (N-N via join) | `Conversation → Message(s)` (1-N ordered) | `Message → ReadReceipt(s)` (1-N per user) |
| File Storage | `User → File/Folder` (owns, tree) | `File → Version(s)` (history, 1-N) | `File → ShareLink → Permission` (composite) |
| Stock Exchange | `Stock → OrderBook` (1-1) | `OrderBook → Order(s)` (queue, 1-N buy + 1-N sell) | `Order ⊕ Order → Trade` (matches produce, N-N) |
| Logger | `Logger → Appender(s)` (1-N) | `LogEvent → Filter → Appender` (pipeline) | `Appender → Formatter` (1-1) |
| Rate Limiter | `Client → Bucket` (1-1 active) | `Bucket → Rule` (governed by, N-1) | `Rule → Window + Quota` (composite) |
| Calendar | `User → Event(s)` (organizes, 1-N) | `Event → Invitee(s) → RSVP` (N-N via join) | `Event → Recurrence + Reminder(s)` (1-1, 1-N) |
| Music streaming | `User → Playlist(s) → Song(s)` (N-N via join) | `User → PlayEvent(s)` (history, 1-N) | `User → Subscription` (1-1 active) |
| Video streaming | `User → WatchHistory → Video` (join with resume pos) | `Show → Season(s) → Episode(s)` (composition tree) | `User → Subscription` (1-1 active) |
| Airbnb | `Host → Listing(s)` (1-N) | `Guest → Booking → Listing` (N-N via join) | `Booking → Review(s)` (2: guest + host) |
| Banking | `Customer → Account(s)` (1-N or N-N for joint) | `Account → Transaction(s)` (ledger, 1-N) | `Account → Card(s) + Beneficiary(s)` (1-N each) |

> **Pattern to spot:** "state evolution" chains (Cart → Order → Shipment) and "ledger" relationships (Account → Transaction) appear in *every* transactional system. If you see one, expect both.

---

## Gate 3 Cheatsheet — Cardinality (per domain)

> **How to use:** For each domain, these are the cardinality calls you must lock down. Each cell tells you: cardinality + the data structure it implies.

| Domain | Critical cardinality decisions |
|---|---|
| Library | `User ↔ Loan` = 1-N (`List<Loan>`); `Book ↔ Copy` = 1-N (`List<Copy>`); `Loan ↔ Copy` = 1-1 active, N-1 historical (need `status`) |
| Parking lot | `Vehicle ↔ Ticket` = 1-1 *active*, 1-N historical; `Slot ↔ Vehicle` = 1-1 active; `Floor ↔ Slot` = 1-N |
| Hotel | `Guest ↔ Reservation` = 1-N; `Room ↔ Reservation` = 1-N over time, 1-1 per date range; `RoomType ↔ Room` = 1-N |
| Ride-sharing | `Rider ↔ Trip` = 1-N historical, 1-1 active; `Driver ↔ Vehicle` = 1-1 active OR 1-N if owns multiple; `Trip ↔ Rider` = 1-1 (or 1-N for carpool — clarify) |
| Movie booking | `Show ↔ Booking` = 1-N; `Booking ↔ Seat` = 1-N (group); `Seat ↔ Show` = 1-1 booking per show (composite key) |
| ATM | `Customer ↔ Account` = 1-N; `Account ↔ Card` = 1-N (multiple cards per account); `Session ↔ Transaction` = 1-N |
| KV store | `Cache ↔ Entry` = 1-N (`Map<K,V>`); `Entry ↔ ExpiryPolicy` = N-1; only one EvictionPolicy per cache |
| URL shortener | `Owner ↔ ShortLink` = 1-N; `LongURL ↔ ShortLink` = 1-1 OR 1-N (custom aliases — clarify); `ShortLink ↔ ClickEvent` = 1-N |
| Tic-Tac-Toe | `Game ↔ Player` = 1-2 (exactly); `Game ↔ Move` = 1-N ordered; `Cell ↔ Symbol` = 1-1 per game |
| Online learning | `Student ↔ Course` = N-N (via `Enrollment`); `Course ↔ Lesson` = 1-N ordered; `Enrollment ↔ Progress` = 1-1 per lesson |
| Twitter/X | `User ↔ Follow ↔ User` = N-N self-referential; `User ↔ Tweet` = 1-N; `Tweet ↔ Like` = 1-N; `User ↔ Feed` = 1-1 |
| Food delivery | `Customer ↔ Order` = 1-N; `Order ↔ MenuItem` = N-N (via `LineItem`); `Order ↔ DeliveryAgent` = N-1 active |
| E-commerce | `User ↔ Cart` = 1-1 active; `Cart ↔ Product` = N-N (via `CartItem`); `Order ↔ Shipment` = 1-N (split shipments) |
| Chess | `Game ↔ Player` = 1-2; `Game ↔ Move` = 1-N ordered ledger; `Board ↔ Piece` = 1-N (max 32) |
| Snake & Ladder | `Game ↔ Player` = 1-N (typically 2-4); `Board ↔ Snake` = 1-N; `Board ↔ Ladder` = 1-N |
| Elevator | `Building ↔ Elevator` = 1-N; `Elevator ↔ Request` = 1-N pending queue; `Elevator ↔ Floor` = 1-1 current position |
| Vending Machine | `Machine ↔ Slot` = 1-N (`Map<SlotId, Slot>`); `Slot ↔ Item` = 1-1 + count; `User ↔ Transaction` = 1-N |
| Splitwise | `Group ↔ User` = N-N; `Group ↔ Expense` = 1-N; `Expense ↔ Split` = 1-N (one per participant); `User ↔ User Balance` = N-N pair-wise |
| Notification | `User ↔ Subscription` = N-N (via subscription); `Notification ↔ DeliveryAttempt` = 1-N (retries); `Channel ↔ Template` = 1-N |
| Chat | `Conversation ↔ User` = N-N via `Participant`; `Conversation ↔ Message` = 1-N ordered; `Message ↔ ReadReceipt` = 1-N (per recipient) |
| File Storage | `User ↔ File` = 1-N owned, N-N shared; `File ↔ Version` = 1-N; `Folder ↔ File` = 1-N (tree) |
| Stock Exchange | `Stock ↔ OrderBook` = 1-1; `User ↔ Order` = 1-N; `Order ↔ Trade` = 1-N partial fills; `User ↔ Position` = 1-N (one per stock) |
| Logger | `Logger ↔ Appender` = 1-N; `Appender ↔ Filter` = 1-N; `Appender ↔ Formatter` = 1-1 |
| Rate Limiter | `Client ↔ Bucket` = 1-1 active; `API ↔ Rule` = N-1 (many APIs share rule); `Rule ↔ Bucket` = 1-N |
| Calendar | `User ↔ Event` = 1-N organized, N-N attended (via `Invitee`); `Event ↔ Recurrence` = 1-1; `Event ↔ Reminder` = 1-N per user |
| Music streaming | `User ↔ Playlist` = 1-N; `Playlist ↔ Song` = N-N ordered; `User ↔ Subscription` = 1-1 active |
| Video streaming | `Show ↔ Season ↔ Episode` = 1-N → 1-N; `User ↔ WatchHistory` = 1-N (one per video); `User ↔ Profile` = 1-N (multi-profile) |
| Airbnb | `Host ↔ Listing` = 1-N; `Guest ↔ Booking` = 1-N; `Listing ↔ Booking` = 1-N over time, non-overlapping per date |
| Banking | `Customer ↔ Account` = 1-N or N-N (joint); `Account ↔ Transaction` = 1-N ledger; `Account ↔ Card` = 1-N |

> **Critical rule:** "1-1 active" vs "1-N historical" is the most missed cardinality nuance. Always ask: "many at once, or many over time?"

---

## Gate 4 Cheatsheet — Identity (per domain)

> **How to use:** For each entity, identity becomes the map key, equals/hashCode contract, and DB primary key. Each row shows: entity → ID field → assigned (system-generated) vs derived (composite/natural key).

| Domain | Entity → ID (assigned `A` / derived `D`) |
|---|---|
| Library | `User → userId (A)`; `Book → isbn (D)`; `Copy → bookId+copyNumber (D)`; `Loan → loanId (A)` |
| Parking lot | `Vehicle → licensePlate (D)`; `Slot → floorId+slotNumber (D)`; `Ticket → ticketId (A)`; `Payment → paymentId (A)` |
| Hotel | `Guest → guestId (A)`; `Room → roomNumber (D)`; `Reservation → reservationId (A)`; `Invoice → invoiceId (A)` |
| Ride-sharing | `Rider → riderId (A)`; `Driver → driverId (A)`; `Vehicle → vehicleId (A) or licensePlate (D)`; `Trip → tripId (A)` |
| Movie booking | `Movie → movieId (A)`; `Show → movieId+screenId+startTime (D)`; `Seat → showId+row+col (D)`; `Booking → bookingId (A)` |
| ATM | `Customer → customerId (A)`; `Account → accountNumber (A)`; `Card → cardNumber (A)`; `Transaction → txnId (A)`; `Session → sessionId (A)` |
| KV store | `Entry → key (D, user-supplied)`; cache itself is singleton |
| URL shortener | `ShortLink → shortCode (A or D for custom alias)`; `Owner → userId (A)`; `ClickEvent → eventId (A)` |
| Tic-Tac-Toe | `Game → gameId (A)`; `Player → playerId (A)`; `Move → gameId+sequenceNumber (D)`; `Cell → row+col (D)` |
| Online learning | `Student → studentId (A)`; `Course → courseId (A)`; `Lesson → courseId+lessonNumber (D)`; `Enrollment → studentId+courseId (D) or enrollmentId (A)` |
| Twitter/X | `User → userId (A) or handle (D)`; `Tweet → tweetId (A)`; `Follow → followerId+followeeId (D)`; `Like → userId+tweetId (D)` |
| Food delivery | `Customer → customerId (A)`; `Restaurant → restaurantId (A)`; `Order → orderId (A)`; `MenuItem → restaurantId+itemId (D)` |
| E-commerce | `User → userId (A)`; `Product → productId (A) or SKU (D)`; `Cart → userId (D, 1-1)`; `Order → orderId (A)` |
| Chess | `Game → gameId (A)`; `Player → playerId (A)`; `Move → gameId+moveNumber (D)`; `Piece → gameId+pieceId (D)` |
| Snake & Ladder | `Game → gameId (A)`; `Player → playerId (A)`; `Cell → cellNumber (D, 1-100)` |
| Elevator | `Elevator → elevatorId (A)`; `Floor → floorNumber (D)`; `Request → requestId (A)`; `Building → buildingId (A)` |
| Vending Machine | `Machine → machineId (A)`; `Slot → slotCode (D, e.g., A1, B2)`; `Item → itemId (A) or barcode (D)`; `Transaction → txnId (A)` |
| Splitwise | `User → userId (A)`; `Group → groupId (A)`; `Expense → expenseId (A)`; `Split → expenseId+userId (D)`; `Settlement → settlementId (A)` |
| Notification | `User → userId (A)`; `Notification → notificationId (A)`; `Subscription → userId+topic+channel (D)`; `Template → templateId (A)` |
| Chat | `User → userId (A)`; `Conversation → conversationId (A)`; `Message → messageId (A)`; `ReadReceipt → messageId+userId (D)` |
| File Storage | `User → userId (A)`; `File → fileId (A)`; `Version → fileId+versionNumber (D)`; `ShareLink → token (A, opaque)` |
| Stock Exchange | `User → userId (A)`; `Stock → ticker (D, e.g., AAPL)`; `Order → orderId (A)`; `Trade → tradeId (A)`; `Position → userId+ticker (D)` |
| Logger | `LogEvent → eventId (A) or implicit (no ID needed)`; `Appender → appenderName (D)`; `Logger → loggerName (D, hierarchical)` |
| Rate Limiter | `Bucket → clientId+ruleId (D)`; `Rule → ruleId (A)`; `Client → apiKey or IP (D)` |
| Calendar | `User → userId (A)`; `Event → eventId (A)`; `Invitee → eventId+userId (D)`; `Recurrence → eventId (D, 1-1)` |
| Music streaming | `User → userId (A)`; `Song → songId (A)`; `Playlist → playlistId (A)`; `Artist → artistId (A)`; `PlayEvent → eventId (A)` |
| Video streaming | `User → userId (A)`; `Video → videoId (A)`; `Episode → showId+seasonNum+episodeNum (D)`; `WatchHistory → userId+videoId (D)` |
| Airbnb | `Host → hostId (A)`; `Listing → listingId (A)`; `Booking → bookingId (A)`; `Review → reviewId (A)` |
| Banking | `Customer → customerId (A)`; `Account → accountNumber (A)`; `Card → cardNumber (A)`; `Transaction → txnId (A)`; `Beneficiary → customerId+accountNumber (D)` |

> **Identity rules of thumb:**
> - **Use derived IDs** when the natural key is stable & unique (ISBN, license plate, ticker, SKU, handle).
> - **Use assigned IDs** when no natural key exists, or when the natural key can change (email, name).
> - **Composite keys** appear naturally in *join entities* (Enrollment, Like, ReadReceipt, Split).
> - **Opaque tokens** (ShareLink token, sessionId) are assigned but designed to be unguessable — clarify if security matters.

---

## 🟢 Per-Domain Walkthroughs (4-Step Pattern)

> **How to use:** For each domain, apply the same 4 steps in order. This is the *full drill* — Tiers → Relationships → Cardinality → Identity. Mimic this pattern in interviews.
>
> **Pattern (memorize):**
> 1. **3-Tier Sweep** — T1 (stated) · T2 (container) · T3 (transaction)
> 2. **Relationships** — Q1 owns · Q2 occupies · Q3 settles · Q4 composes
> 3. **Cardinality** — flag *active vs historical* (1-1 active / 1-N historical)
> 4. **Identity** — assigned (A) vs derived (D)

---

### 🅛 Library

**Step 1 — 3-Tier Sweep**
- T1: `User`, `Book`
- T2: `Copy` (1 user at a time)
- T3: `Loan`, `Reservation`, `Fine`

**Step 2 — Relationships**
- Q1 owns: `User → Loan`
- Q2 occupies: `Loan → Copy`
- Q3 settles: `Loan → Fine` (if late)
- Q4 composes: `Book → Copy`

**Step 3 — Cardinality**
- `User ↔ Loan` = 1-N historical
- `Copy ↔ Loan` = 1-1 *active*, 1-N historical
- `Book ↔ Copy` = 1-N

**Step 4 — Identity**
- `User → userId (A)`, `Book → isbn (D)`, `Copy → bookId+copyNumber (D)`, `Loan → loanId (A)`

---

### 🅟 Parking Lot

**Step 1 — 3-Tier Sweep**
- T1: `Vehicle`, `ParkingLot`
- T2: `Slot` (on `Floor`)
- T3: `Ticket`, `Payment`

**Step 2 — Relationships**
- Q1 owns: `Vehicle → Ticket`
- Q2 occupies: `Ticket → Slot`
- Q3 settles: `Ticket → Payment`
- Q4 composes: `ParkingLot → Floor → Slot`

**Step 3 — Cardinality**
- `Vehicle ↔ Ticket` = 1-1 active, 1-N historical
- `Slot ↔ Vehicle` = 1-1 active, 1-N historical
- `Floor ↔ Slot` = 1-N; `ParkingLot ↔ Floor` = 1-N
- `Ticket ↔ Payment` = 1-1

**Step 4 — Identity**
- `ParkingLot → buildingName/lotId (A)`, `Floor → floorNo (D)`, `Slot → floorId+slotNumber (D)`, `Vehicle → licensePlate (D)`, `Ticket → ticketId (A)`, `Payment → paymentId/txnId (A)`

---

### 🅗 Hotel

**Step 1 — 3-Tier Sweep**
- T1: `Guest`, `Hotel`
- T2: `Room` (of `RoomType`)
- T3: `Reservation`, `Invoice`, `Payment`

**Step 2 — Relationships**
- Q1 owns: `Guest → Reservation`
- Q2 occupies: `Reservation → Room` (per date range)
- Q3 settles: `Reservation → Invoice → Payment`
- Q4 composes: `Hotel → Floor → Room`; `RoomType → Room` (1-N)

**Step 3 — Cardinality**
- `Guest ↔ Reservation` = 1-N
- `Room ↔ Reservation` = 1-N over time, **1-1 per overlapping date range**
- `RoomType ↔ Room` = 1-N
- `Reservation ↔ Invoice` = 1-1

**Step 4 — Identity**
- `Guest → guestId (A)`, `Room → hotelId+roomNumber (D)`, `Reservation → reservationId (A)`, `Invoice → invoiceId (A)`

---

### 🅡 Ride-Sharing

**Step 1 — 3-Tier Sweep**
- T1: `Rider`, `Driver`
- T2: `Vehicle` (1 driver active)
- T3: `RideRequest → Trip`, `Payment`, `Rating`

**Step 2 — Relationships**
- Q1 owns: `Rider → RideRequest → Trip` (state evolution)
- Q2 occupies: `Trip → Vehicle`
- Q3 settles: `Trip → Payment + Rating`
- Q4 composes: `Driver → Vehicle(s)`

**Step 3 — Cardinality**
- `Rider ↔ Trip` = 1-N historical, 1-1 active
- `Driver ↔ Vehicle` = 1-1 active (or 1-N if owns many — clarify)
- `Trip ↔ Rider` = 1-1 (1-N for carpool — clarify)
- `Trip ↔ Payment` = 1-1, `Trip ↔ Rating` = 1-2 (rider+driver)

**Step 4 — Identity**
- `Rider → riderId (A)`, `Driver → driverId (A)`, `Vehicle → vehicleId (A) or licensePlate (D)`, `Trip → tripId (A)`

---

### 🅜 Movie Booking

**Step 1 — 3-Tier Sweep**
- T1: `Movie`, `Theatre`, `User`
- T2: `Seat` (in `Screen`, per `Show`)
- T3: `Hold`, `Booking`, `Payment`

**Step 2 — Relationships**
- Q1 owns: `User → Booking`
- Q2 occupies: `Booking → Seat(s)` (per Show, via Hold)
- Q3 settles: `Hold → Booking → Payment`
- Q4 composes: `Theatre → Screen → Seat`; `Show = Movie + Screen + Time`

**Step 3 — Cardinality**
- `Show ↔ Booking` = 1-N
- `Booking ↔ Seat` = 1-N (group)
- `Seat ↔ Show` = 1-1 booking per show
- `Booking ↔ Payment` = 1-1

**Step 4 — Identity**
- `Movie → movieId (A)`, `Show → movieId+screenId+startTime (D)`, `Seat → showId+row+col (D)`, `Booking → bookingId (A)`

---

### 🅐 ATM

**Step 1 — 3-Tier Sweep**
- T1: `Customer`, `ATM`
- T2: `Account`, `Card`, `CashInventory`
- T3: `Session`, `Transaction`

**Step 2 — Relationships**
- Q1 owns: `Customer → Session → Transaction`
- Q2 occupies: `Transaction → Account` (debit/credit)
- Q3 settles: `Transaction → Receipt` (audit)
- Q4 composes: `Customer → Account(s)`; `Account → Card(s)`; `ATM → CashInventory`

**Step 3 — Cardinality**
- `Customer ↔ Account` = 1-N
- `Account ↔ Card` = 1-N
- `Session ↔ Transaction` = 1-N
- `Account ↔ Transaction` = 1-N (ledger)

**Step 4 — Identity**
- `Customer → customerId (A)`, `Account → accountNumber (A)`, `Card → cardNumber (A)`, `Session → sessionId (A)`, `Transaction → txnId (A)`

---

### 🅚 KV Store

**Step 1 — 3-Tier Sweep**
- T1: `Key`, `Value`
- T2: `Entry` (in `Cache`)
- T3: `ExpiryEvent`, `EvictionEvent`

**Step 2 — Relationships**
- Q1 owns: `Client → put/get` (no actor record)
- Q2 occupies: `Entry → Key` slot in Cache
- Q3 settles: `Entry → ExpiryEvent / EvictionEvent`
- Q4 composes: `Cache → Entry(s)`; `Cache → EvictionPolicy`

**Step 3 — Cardinality**
- `Cache ↔ Entry` = 1-N (`Map<K,V>`)
- `Entry ↔ ExpiryPolicy` = N-1
- `Cache ↔ EvictionPolicy` = 1-1

**Step 4 — Identity**
- `Entry → key (D, user-supplied)`; `Cache` = singleton

---

### 🅤 URL Shortener

**Step 1 — 3-Tier Sweep**
- T1: `LongURL`, `ShortURL`, `Owner`
- T2: `ShortLink` (1 alias slot per code)
- T3: `ClickEvent`, `ExpiryPolicy`

**Step 2 — Relationships**
- Q1 owns: `Owner → ShortLink`
- Q2 occupies: `ShortLink → shortCode` slot (unique)
- Q3 settles: `ShortLink → ClickEvent(s)`
- Q4 composes: governed by `ExpiryPolicy`

**Step 3 — Cardinality**
- `Owner ↔ ShortLink` = 1-N
- `LongURL ↔ ShortLink` = 1-1 OR 1-N (custom aliases — clarify)
- `ShortLink ↔ ClickEvent` = 1-N

**Step 4 — Identity**
- `ShortLink → shortCode (A or D for custom alias)`, `Owner → userId (A)`, `ClickEvent → eventId (A)`

---

### 🅣 Tic-Tac-Toe

**Step 1 — 3-Tier Sweep**
- T1: `Player`
- T2: `Cell` (3×3 on `Board`)
- T3: `Move`, `GameResult`

**Step 2 — Relationships**
- Q1 owns: `Player → Move`
- Q2 occupies: `Move → Cell` (1 symbol)
- Q3 settles: `Game → GameResult`
- Q4 composes: `Game → Board → Cell`

**Step 3 — Cardinality**
- `Game ↔ Player` = 1-2
- `Game ↔ Move` = 1-N ordered
- `Cell ↔ Symbol` = 1-1 per game

**Step 4 — Identity**
- `Game → gameId (A)`, `Player → playerId (A)`, `Move → gameId+sequenceNumber (D)`, `Cell → row+col (D)`

---

### 🅞 Online Learning

**Step 1 — 3-Tier Sweep**
- T1: `Student`, `Instructor`, `Course`
- T2: `Lesson` (slot in course)
- T3: `Enrollment`, `Progress`, `Quiz`, `Certificate`, `Payment`

**Step 2 — Relationships**
- Q1 owns: `Student → Enrollment`
- Q2 occupies: `Enrollment → Course/Lesson`
- Q3 settles: `Enrollment → Certificate + Payment`
- Q4 composes: `Course → Lesson(s)`

**Step 3 — Cardinality**
- `Student ↔ Course` = N-N (via `Enrollment`)
- `Course ↔ Lesson` = 1-N ordered
- `Enrollment ↔ Progress` = 1-1 per lesson

**Step 4 — Identity**
- `Student → studentId (A)`, `Course → courseId (A)`, `Lesson → courseId+lessonNumber (D)`, `Enrollment → studentId+courseId (D) or enrollmentId (A)`

---

### 🅧 Twitter/X

**Step 1 — 3-Tier Sweep**
- T1: `User`, `Tweet`
- T2: `Feed` / `Timeline` (per user)
- T3: `Follow`, `Like`, `Retweet`, `Reply`

**Step 2 — Relationships**
- Q1 owns: `User → Tweet`; `User → Follow`
- Q2 occupies: `Tweet → Feed` (of followers)
- Q3 settles: `Tweet → Like + Reply + Retweet`
- Q4 composes: `User → Feed` (1-1)

**Step 3 — Cardinality**
- `User ↔ Follow ↔ User` = N-N self-referential
- `User ↔ Tweet` = 1-N
- `Tweet ↔ Like` = 1-N
- `User ↔ Feed` = 1-1

**Step 4 — Identity**
- `User → userId (A) or handle (D)`, `Tweet → tweetId (A)`, `Follow → followerId+followeeId (D)`, `Like → userId+tweetId (D)`

---

### 🅕 Food Delivery

**Step 1 — 3-Tier Sweep**
- T1: `Customer`, `Restaurant`, `MenuItem`
- T2: `Cart` (1 active per customer)
- T3: `Order`, `OrderStatusEvent`, `Payment`

**Step 2 — Relationships**
- Q1 owns: `Customer → Cart → Order` (state evolution)
- Q2 occupies: `Order → Restaurant + DeliveryAgent`
- Q3 settles: `Order → Payment + OrderStatusEvent(s)`
- Q4 composes: `Restaurant → MenuItem(s)`

**Step 3 — Cardinality**
- `Customer ↔ Order` = 1-N
- `Order ↔ MenuItem` = N-N (via `LineItem`)
- `Order ↔ DeliveryAgent` = N-1 active

**Step 4 — Identity**
- `Customer → customerId (A)`, `Restaurant → restaurantId (A)`, `Order → orderId (A)`, `MenuItem → restaurantId+itemId (D)`

---

### 🅔 E-commerce / Cart

**Step 1 — 3-Tier Sweep**
- T1: `User`, `Product`
- T2: `Cart`, `Inventory` (per warehouse)
- T3: `Order`, `Payment`, `Shipment`, `Discount`

**Step 2 — Relationships**
- Q1 owns: `User → Cart → Order`
- Q2 occupies: `Order → Inventory` (decrements)
- Q3 settles: `Order → Payment + Shipment`
- Q4 composes: `Warehouse → Inventory → Product`

**Step 3 — Cardinality**
- `User ↔ Cart` = 1-1 active
- `Cart ↔ Product` = N-N (via `CartItem`)
- `Order ↔ Shipment` = 1-N (split shipments)

**Step 4 — Identity**
- `User → userId (A)`, `Product → productId (A) or SKU (D)`, `Cart → userId (D, 1-1)`, `Order → orderId (A)`

---

### 🅒 Chess

**Step 1 — 3-Tier Sweep**
- T1: `Player`, `Piece`
- T2: `Cell` (8×8); `Piece` position
- T3: `Move`, `GameResult`, `Clock` tick

**Step 2 — Relationships**
- Q1 owns: `Player → Move`
- Q2 occupies: `Move → Cell` (from→to)
- Q3 settles: `Game → GameResult`
- Q4 composes: `Board → Cell` (8×8); `Game → Clock`

**Step 3 — Cardinality**
- `Game ↔ Player` = 1-2
- `Game ↔ Move` = 1-N ordered ledger
- `Board ↔ Piece` = 1-N (max 32)

**Step 4 — Identity**
- `Game → gameId (A)`, `Player → playerId (A)`, `Move → gameId+moveNumber (D)`, `Piece → gameId+pieceId (D)`

---

### 🅢 Snake & Ladder

**Step 1 — 3-Tier Sweep**
- T1: `Player`, `Dice`
- T2: `Cell` (1-100 on `Board`)
- T3: `MoveResult`, `GameResult`

**Step 2 — Relationships**
- Q1 owns: `Player → MoveResult` (via Dice)
- Q2 occupies: `MoveResult → Cell`
- Q3 settles: `Game → GameResult`
- Q4 composes: `Board → Cell + Snake(s) + Ladder(s)`

**Step 3 — Cardinality**
- `Game ↔ Player` = 1-N (typically 2-4)
- `Board ↔ Snake` = 1-N; `Board ↔ Ladder` = 1-N
- `Game ↔ MoveResult` = 1-N history

**Step 4 — Identity**
- `Game → gameId (A)`, `Player → playerId (A)`, `Cell → cellNumber (D, 1-100)`

---

### 🅔 Elevator

**Step 1 — 3-Tier Sweep**
- T1: `Building`, `Person`
- T2: `Elevator` (cabin) at `Floor`
- T3: `Request` (queued button press)

**Step 2 — Relationships**
- Q1 owns: `Person → Request`
- Q2 occupies: `Request → Elevator → Floor`
- Q3 settles: `Request → completed event`
- Q4 composes: `Building → Elevator(s) + Floor(s)`; `Building → DispatchPolicy`

**Step 3 — Cardinality**
- `Building ↔ Elevator` = 1-N
- `Elevator ↔ Request` = 1-N pending queue
- `Elevator ↔ Floor` = 1-1 current position

**Step 4 — Identity**
- `Building → buildingId (A)`, `Elevator → elevatorId (A)`, `Floor → floorNumber (D)`, `Request → requestId (A)`

---

### 🅥 Vending Machine

**Step 1 — 3-Tier Sweep**
- T1: `Item`, `Machine`, `User`
- T2: `Slot` (A1, B2 — holds N units)
- T3: `Transaction`, `Refund`

**Step 2 — Relationships**
- Q1 owns: `User → Transaction`
- Q2 occupies: `Transaction → Slot` (decrements count)
- Q3 settles: `Transaction → Refund?` (optional)
- Q4 composes: `Machine → Slot(s)`

**Step 3 — Cardinality**
- `Machine ↔ Slot` = 1-N (`Map<SlotId, Slot>`)
- `Slot ↔ Item` = 1-1 + count
- `User ↔ Transaction` = 1-N

**Step 4 — Identity**
- `Machine → machineId (A)`, `Slot → slotCode (D, e.g., A1)`, `Item → itemId (A) or barcode (D)`, `Transaction → txnId (A)`

---

### 🅢 Splitwise

**Step 1 — 3-Tier Sweep**
- T1: `User`, `Group`
- T2: `Group` (logical container)
- T3: `Expense`, `Split`, `Settlement`, `Balance`

**Step 2 — Relationships**
- Q1 owns: `User → Expense` (in Group)
- Q2 occupies: `Expense → Split(s)` (per user)
- Q3 settles: `Expense → Settlement` (clears Balance)
- Q4 composes: `Group → User(s)`; `Group → Expense(s)`

**Step 3 — Cardinality**
- `Group ↔ User` = N-N
- `Group ↔ Expense` = 1-N
- `Expense ↔ Split` = 1-N (per participant)
- `User ↔ User Balance` = N-N pair-wise

**Step 4 — Identity**
- `User → userId (A)`, `Group → groupId (A)`, `Expense → expenseId (A)`, `Split → expenseId+userId (D)`, `Settlement → settlementId (A)`

---

### 🅝 Notification System

**Step 1 — 3-Tier Sweep**
- T1: `User`
- T2: `Channel` (email/SMS/push pipe)
- T3: `Notification`, `DeliveryAttempt`

**Step 2 — Relationships**
- Q1 owns: `System → Notification`
- Q2 occupies: `Notification → Channel` (via `Subscription`)
- Q3 settles: `Notification → DeliveryAttempt(s)`
- Q4 composes: `User → Subscription(s) → Channel`; `Notification → Template`

**Step 3 — Cardinality**
- `User ↔ Subscription` = N-N
- `Notification ↔ DeliveryAttempt` = 1-N (retries)
- `Channel ↔ Template` = 1-N

**Step 4 — Identity**
- `User → userId (A)`, `Notification → notificationId (A)`, `Subscription → userId+topic+channel (D)`, `Template → templateId (A)`

---

### 🅒 Chat / Messaging

**Step 1 — 3-Tier Sweep**
- T1: `User`, `Message`
- T2: `Conversation` (holds participants + messages)
- T3: `ReadReceipt`, `Attachment` upload

**Step 2 — Relationships**
- Q1 owns: `User → Message`
- Q2 occupies: `Message → Conversation`
- Q3 settles: `Message → ReadReceipt(s)`
- Q4 composes: `Conversation → Participant(s)` (N-N via join)

**Step 3 — Cardinality**
- `Conversation ↔ User` = N-N via `Participant`
- `Conversation ↔ Message` = 1-N ordered
- `Message ↔ ReadReceipt` = 1-N (per recipient)

**Step 4 — Identity**
- `User → userId (A)`, `Conversation → conversationId (A)`, `Message → messageId (A)`, `ReadReceipt → messageId+userId (D)`

---

### 🅕 File Storage (Dropbox)

**Step 1 — 3-Tier Sweep**
- T1: `User`, `File`
- T2: `Folder` (tree); `Quota` (per user)
- T3: `Version`, `ShareLink`, `SyncEvent`

**Step 2 — Relationships**
- Q1 owns: `User → File/Version`
- Q2 occupies: `File → Folder` (path)
- Q3 settles: `File → ShareLink` (with `Permission`)
- Q4 composes: `Folder → File(s)` (tree)

**Step 3 — Cardinality**
- `User ↔ File` = 1-N owned, N-N shared
- `File ↔ Version` = 1-N
- `Folder ↔ File` = 1-N

**Step 4 — Identity**
- `User → userId (A)`, `File → fileId (A)`, `Version → fileId+versionNumber (D)`, `ShareLink → token (A, opaque)`

---

### 🅢 Stock Exchange

**Step 1 — 3-Tier Sweep**
- T1: `User`, `Stock`
- T2: `OrderBook` (buy queue + sell queue per Stock)
- T3: `Order`, `Trade`, `Position` snapshot

**Step 2 — Relationships**
- Q1 owns: `User → Order`
- Q2 occupies: `Order → OrderBook` (per Stock)
- Q3 settles: `Order ⊕ Order → Trade`
- Q4 composes: `Stock → OrderBook` (1-1)

**Step 3 — Cardinality**
- `Stock ↔ OrderBook` = 1-1
- `User ↔ Order` = 1-N
- `Order ↔ Trade` = 1-N (partial fills)
- `User ↔ Position` = 1-N (one per stock)

**Step 4 — Identity**
- `User → userId (A)`, `Stock → ticker (D, e.g., AAPL)`, `Order → orderId (A)`, `Trade → tradeId (A)`, `Position → userId+ticker (D)`

---

### 🅛 Logger

**Step 1 — 3-Tier Sweep**
- T1: `Message` (system source)
- T2: `Appender` / `Sink` (output pipe)
- T3: `LogEvent`

**Step 2 — Relationships**
- Q1 owns: `Code → LogEvent`
- Q2 occupies: `LogEvent → Appender` (via `Filter`)
- Q3 settles: `Appender → Formatted output`
- Q4 composes: `Logger → Appender(s)` (hierarchical)

**Step 3 — Cardinality**
- `Logger ↔ Appender` = 1-N
- `Appender ↔ Filter` = 1-N
- `Appender ↔ Formatter` = 1-1

**Step 4 — Identity**
- `LogEvent → eventId (A) or implicit`, `Appender → appenderName (D)`, `Logger → loggerName (D, hierarchical)`

---

### 🅡 Rate Limiter

**Step 1 — 3-Tier Sweep**
- T1: `Client`, `Request`
- T2: `Bucket` (per client, N tokens)
- T3: `AllowDecision` (with timestamp)

**Step 2 — Relationships**
- Q1 owns: `Client → Request`
- Q2 occupies: `Request → Bucket` (per Client+Rule)
- Q3 settles: `Request → AllowDecision`
- Q4 composes: `Rule → Bucket(s)`; `Rule → Window + Quota`

**Step 3 — Cardinality**
- `Client ↔ Bucket` = 1-1 active
- `API ↔ Rule` = N-1
- `Rule ↔ Bucket` = 1-N

**Step 4 — Identity**
- `Bucket → clientId+ruleId (D)`, `Rule → ruleId (A)`, `Client → apiKey or IP (D)`

---

### 🅒 Calendar

**Step 1 — 3-Tier Sweep**
- T1: `User`, `Event`
- T2: `Calendar` (date×time grid)
- T3: `Invitee`+`RSVP`, `Reminder` fire

**Step 2 — Relationships**
- Q1 owns: `User → Event`
- Q2 occupies: `Event → Calendar slot` (date+time)
- Q3 settles: `Event → Reminder(s) + RSVP(s)`
- Q4 composes: `User → Calendar`; `Event → Recurrence`

**Step 3 — Cardinality**
- `User ↔ Event` = 1-N organized, N-N attended (via `Invitee`)
- `Event ↔ Recurrence` = 1-1
- `Event ↔ Reminder` = 1-N per user

**Step 4 — Identity**
- `User → userId (A)`, `Event → eventId (A)`, `Invitee → eventId+userId (D)`, `Recurrence → eventId (D, 1-1)`

---

### 🅜 Music Streaming

**Step 1 — 3-Tier Sweep**
- T1: `User`, `Song`, `Artist`
- T2: `Playlist` (ordered slot list)
- T3: `PlayEvent`, `Subscription`

**Step 2 — Relationships**
- Q1 owns: `User → PlayEvent`; `User → Playlist`
- Q2 occupies: `PlayEvent → Song` (in Playlist)
- Q3 settles: `PlayEvent → contributes to Recommendation`
- Q4 composes: `User → Playlist → Song(s)` (N-N ordered)

**Step 3 — Cardinality**
- `User ↔ Playlist` = 1-N
- `Playlist ↔ Song` = N-N ordered
- `User ↔ Subscription` = 1-1 active

**Step 4 — Identity**
- `User → userId (A)`, `Song → songId (A)`, `Playlist → playlistId (A)`, `Artist → artistId (A)`, `PlayEvent → eventId (A)`

---

### 🅥 Video Streaming (Netflix)

**Step 1 — 3-Tier Sweep**
- T1: `User`, `Video`
- T2: `Episode` slot in `Season` / `Show`
- T3: `WatchHistory`, `Subscription`

**Step 2 — Relationships**
- Q1 owns: `User → WatchHistory entry`
- Q2 occupies: `WatchHistory → Episode` (in Season/Show)
- Q3 settles: `WatchHistory → resume position`
- Q4 composes: `Show → Season → Episode`

**Step 3 — Cardinality**
- `Show ↔ Season ↔ Episode` = 1-N → 1-N
- `User ↔ WatchHistory` = 1-N (one per video)
- `User ↔ Profile` = 1-N (multi-profile)

**Step 4 — Identity**
- `User → userId (A)`, `Video → videoId (A)`, `Episode → showId+seasonNum+episodeNum (D)`, `WatchHistory → userId+videoId (D)`

---

### 🅐 Airbnb

**Step 1 — 3-Tier Sweep**
- T1: `Host`, `Guest`, `Listing`
- T2: `Listing` per date (availability calendar)
- T3: `Booking`, `Review`, `Payment`

**Step 2 — Relationships**
- Q1 owns: `Guest → Booking`
- Q2 occupies: `Booking → Listing` (per date range)
- Q3 settles: `Booking → Payment + Review(s)`
- Q4 composes: `Host → Listing(s)`

**Step 3 — Cardinality**
- `Host ↔ Listing` = 1-N
- `Guest ↔ Booking` = 1-N
- `Listing ↔ Booking` = 1-N over time, non-overlapping per date
- `Booking ↔ Review` = 1-2 (guest + host)

**Step 4 — Identity**
- `Host → hostId (A)`, `Listing → listingId (A)`, `Booking → bookingId (A)`, `Review → reviewId (A)`

---

### 🅑 Banking

**Step 1 — 3-Tier Sweep**
- T1: `Customer`
- T2: `Account`, `Card`
- T3: `Transaction` (debit/credit), `Statement`

**Step 2 — Relationships**
- Q1 owns: `Customer → Transaction`
- Q2 occupies: `Transaction → Account` (debit/credit)
- Q3 settles: `Transaction → Statement entry`
- Q4 composes: `Customer → Account → Card`; `Account → Beneficiary(s)`

**Step 3 — Cardinality**
- `Customer ↔ Account` = 1-N or N-N (joint)
- `Account ↔ Transaction` = 1-N (ledger)
- `Account ↔ Card` = 1-N

**Step 4 — Identity**
- `Customer → customerId (A)`, `Account → accountNumber (A)`, `Card → cardNumber (A)`, `Transaction → txnId (A)`, `Beneficiary → customerId+accountNumber (D)`

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

### Example Reference Answer (Hotel Reservation)

> Use this as a *style template*. Your answer should look this shape — short, gate-tagged, and end with a clarifying question.

**Gate 1 [NOUNS]**
- Visible: `Hotel`, `Room`, `Guest`
- Hidden I'd probe: `Reservation`, `RoomType`, `Invoice`, `Payment`, `Stay`
- *Question to interviewer:* *"I see Hotel, Room, and Guest — should I also model `Reservation` as a separate entity holding check-in/check-out + status, and is `RoomType` (Deluxe/Suite) a separate entity for pricing/availability?"*

**Gate 2 [RELATIONSHIPS]**
- `Guest → Reservation`: ownership, transactional (guest creates many reservations over time)
- `Reservation → Room`: assignment, temporary (reservation occupies one room for a date range)
- *Question to interviewer:* *"Is a reservation always tied to one specific room at booking time, or do we assign room only at check-in?"*

**Gate 3 [CARDINALITY]**
- `Guest ↔ Reservation` = 1-to-many (`List<Reservation>` per guest)
- `Room ↔ Reservation` = 1-to-many over time, but **1-to-1 per overlapping date range** (no double-booking)
- *Question to interviewer:* *"For overlap detection, should I treat `Room ↔ Reservation` as 1-1 active per date — meaning I need a date-range index on rooms?"*

**Gate 4 [IDENTITY]**
- `Guest → guestId` (assigned, system-generated)
- `Room → roomNumber` (derived, natural key within hotel)
- `Reservation → reservationId` (assigned, system-generated)
- `Hotel → hotelId` (assigned)
- *Question to interviewer:* *"Is `roomNumber` unique within a hotel, or globally? If multi-hotel, the key becomes composite `hotelId+roomNumber`."*

**Why this scores 4/4:**
1. ✅ Listed visible + at least 2 hidden entities → Gate 1
2. ✅ Picked 2 relationships and labeled ownership + temporality → Gate 2
3. ✅ Stated cardinality + flagged the **temporal nuance** (1-1 active vs 1-N historical) → Gate 3
4. ✅ Distinguished assigned vs derived IDs and surfaced the composite-key edge case → Gate 4
5. ✅ Each gate ended with a *clarifying question* — that's how interviewers know you're collaborating, not assuming.

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
