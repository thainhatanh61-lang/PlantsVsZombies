# Plants vs. Zombies Fan Game — Full Implementation Specification
> **For AI Agent Use:** This document is a complete, ordered implementation guide. Follow each section top-to-bottom. Each section lists the exact asset paths, behaviors, and logic required. Do not skip sections; later sections depend on earlier ones.

---

## 0. Asset Inventory Reference

Before coding anything, map these asset paths. All paths are relative to `resources/graphics/`.

```
Plants/
  Sunflower/          ← idle frames
  Peashooter/         ← idle + shoot frames
  Wallnut/            ← idle + damaged frames (cracked1, cracked2)
  PotatoMine/         ← unarmed/ + armed/ subfolders (or create if missing)
  Jalapeno/           ← idle + activated frames
  Chomper/            ← idle/ + attack/ + digest/ subfolders
  CherryBomb/         ← idle + explode frames
  Spikeweed/          ← idle frames
  Sunshroom/          ← idle frames (NO sleep folder needed)
  Puffshroom/         ← idle frames + mushroom_bullet/ frames
  Scaredyshroom/      ← idle + scared frames
  Iceshroom/          ← idle + activated frames + IceshroomTrap/ subfolder
  Hyposhroom/         ← idle frames
  mushroom_bullet/    ← projectile frames for Puffshroom

UI/
  SeedChooser/
    Cards/            ← <PlantName>_card.png  AND  <PlantName>_card_move.png (if exists)
  shovel.jpg          ← NEW shovel image (replaces old shovel sprite)

Backgrounds/
  background_0.jpg    ← Day mode background
  background_1.jpg    ← Night mode background

Menu/
  Mainmenu.jpg
  adventure_0.png
  adventure_1.png

Zombies/
  <ZombieType>/       ← standard zombie sprite sheets

Effects/
  lawnmowneractivated.gif
  burntZombie.gif
  jalapenoFire.gif
```

---

## 1. Main Menu

**File to modify:** `scenes/MainMenuScene.py` (or equivalent menu scene)

### Layout Instructions
1. Render `resources/graphics/Menu/Mainmenu.jpg` as the full background.
2. Locate the **grave/tombstone** element on the right side of the image (it is a visual landmark in the JPG).
3. Place `adventure_0.png` anchored to the **top of the grave** — centered horizontally on the grave's x-position.
4. Place `adventure_1.png` directly **below** `adventure_0.png`, no gap, same horizontal anchor.
5. Both images are buttons. Clicking either starts Adventure Mode.
6. Leave all other grave decorations and bottom-of-screen elements unchanged.

---

## 2. Day / Night Mode System

### 2.1 Mode Detection
- **Day Mode** → load `background_0.jpg`, use Day Plant roster.
- **Night Mode** → load `background_1.jpg`, use Night Plant roster.
- Mode is determined by the level definition (e.g., `level.mode = "day" | "night"`).

### 2.2 Grid
- Grid dimensions and cell size must be **identical** in both modes.
- In Night Mode, apply a darkening overlay (RGBA `0,0,0,80`) over each grid cell so plants are visible against the dark background.
- Grid lines: same color and opacity as Day Mode.

---

## 3. Plant Roster & Stats

### 3.1 Day Mode Plants

| Plant | Sun Cost | HP | Recharge | Special |
|-------|----------|----|----------|---------|
| Sunflower | 50 | 300 | 7s | Produces sun every 24s |
| Peashooter | 100 | 300 | 7s | Fires pea every 1.5s, 20 dmg/pea |
| Wall-nut | 50 | 4000 | 30s | No attack |
| Potato Mine | 25 | 1 | 30s | See §6 for full logic |
| Jalapeño | 125 | 300 | 50s | See §7 for full logic |
| Chomper | 150 | 300 | 30s | See §3.3 |
| Cherry Bomb | 150 | 300 | 50s | 1800 dmg AoE 3×3, 3s fuse |
| Spikeweed | 100 | 300 | 7s | 20 dmg/hit, damages wheels/cones |

### 3.2 Night Mode Plants

| Plant | Sun Cost | HP | Recharge | Special |
|-------|----------|----|----------|---------|
| Sunshroom | 25 | 300 | 7s | Produces sun: small(15) early, normal(25) mid, large(50) late |
| Puff-shroom | 0 | 200 | 7s | Fires mushroom spore every 1.5s, 20 dmg; disappears after 120s |
| Scaredy-shroom | 25 | 300 | 7s | Fires 20 dmg/shot; cowers (no attack) when zombie is in same row within 3 cells |
| Wall-nut | 50 | 4000 | 30s | (same as Day) |
| Cherry Bomb | 150 | 300 | 50s | (same as Day) |
| Ice-shroom | 75 | 300 | 50s | See §8 for full logic |
| Potato Mine | 25 | 1 | 30s | (same as Day, §6) |
| Hypno-shroom | 75 | 300 | 30s | Zombie that eats it turns friendly, walks left, attacks other zombies |

> **Night Mode Rule:** No plant in night mode uses a `sleep/` subfolder. Mushrooms are awake-only.

### 3.3 Chomper Animation States
Chomper has three animation folders: `idle/`, `attack/`, `digest/`.
- **idle**: default loop.
- **attack**: triggered when zombie enters chomper's cell — plays once, consumes zombie instantly (1-hit kill on normal zombies; cannot consume armored/large zombies in one bite).
- **digest**: plays in a loop for **42 seconds** after eating. Chomper cannot attack while digesting. After 42s, returns to idle.

---

## 4. Seed Chooser (Plant Selection UI)

### 4.1 Layout
```
[ Plant Card 1 ] [ Plant Card 2 ] ... [ Plant Card N ]  |  [ SHOVEL ]
```
- Plant cards are displayed in a horizontal row on the **left/center** of the seed bar.
- The **Shovel** is a separate button placed to the **right** of all plant cards, visually separated by a divider line or a small gap.
- Shovel image: `resources/graphics/UI/shovel.jpg` (this replaces any previously used shovel sprite).

### 4.2 Plant Card Display
- Each card displays `<PlantName>_card.png` from `resources/graphics/UI/SeedChooser/Cards/`.
- If a plant's sun cost is not met, render the card with 50% opacity and a grey tint. It must not be selectable.
- Show a cooldown bar (depleting from top to bottom) over the card while recharging.

### 4.3 Cursor Attachment on Click ("Move Card")
When a player clicks a valid plant card:
1. Check for `<PlantName>_card_move.png` in the Cards folder.
   - If it **exists** → use `_move.png` as the cursor-attached image.
   - If it **does not exist** → use the standard `_card.png` as the cursor-attached image.
2. Attach that image to the mouse cursor (centered or slightly offset for visibility).
3. The image follows the cursor until:
   - The player **clicks a valid grid cell** → plant is placed, image detached.
   - The player **right-clicks** or **clicks outside the grid** → action cancelled, image detached.
4. While attached, highlight hovered grid cells with a green overlay (valid) or red overlay (occupied/invalid).

---

## 5. Shovel Behavior

- Clicking the Shovel button activates shovel mode (cursor changes to shovel icon using `shovel.jpg`).
- Clicking a planted cell removes the plant and refunds **0 sun** (standard PVZ behavior).
- Clicking an empty cell or right-clicking deactivates shovel mode.
- Shovel mode is **mutually exclusive** with plant-placement mode.

---

## 6. Potato Mine — Special Logic

Potato Mine has two states: **Unarmed** and **Armed**.

### State: Unarmed (just planted)
- Uses sprites from `Plants/PotatoMine/unarmed/` (create this subfolder and sprites if missing).
- Duration: **15 seconds** after planting.
- During Unarmed state: zombies **do not eat** the mine. They **walk over it** as if it were not there.
- HP: effectively infinite / invulnerable while unarmed.

### State: Armed (after 15 seconds)
- Switches to sprites from `Plants/PotatoMine/armed/` (create if missing).
- During Armed state: if any zombie **steps on the mine's cell**, trigger explosion:
  - Play a pop-up/explosion animation (use existing Potato Mine frames or create).
  - Deal **1800 damage** to all zombies in a 1-cell radius (the mine's cell + adjacent cells).
  - Remove the mine after explosion.
- Zombies still **cannot eat** the mine in either state.

> **Asset creation note (if subfolders are missing):** Create `unarmed/` with a single sprite showing a small sprout. Create `armed/` with a sprite showing the mine fully emerged. At minimum 2–4 frames each for idle animation.

---

## 7. Jalapeño — Special Logic

### Activation
- When planted, Jalapeño starts a **3-second fuse** (idle animation plays).
- After 3 seconds, it activates.

### Fire Row Effect
1. Spawn `jalapenoFire.gif` starting at the Jalapeño's cell and propagate **left and right** across the **entire row** (all 9 columns).
2. The fire GIF instances should tile or chain across the row so the whole row appears to be on fire simultaneously.
3. Each fire instance persists for **5 seconds** then fades out.
4. **Damage:** Any zombie in the row takes **1800 damage per second** for the duration (effectively instant kill on all normal/cone/bucket zombies).

### Zombie Death by Jalapeño
- If a zombie's HP reaches 0 while the jalapeño fire is active in its row, play `burntZombie.gif` at the zombie's position instead of the normal death animation.
- `burntZombie.gif` plays once (no loop), then the zombie is removed.

---

## 8. Ice-shroom — Special Logic

### Activation
- 3-second fuse after planting, then activates.
- Play Ice-shroom activated animation.

### Freeze Effect
1. Freeze **all zombies on screen** for **10 seconds**.
2. While frozen:
   - Zombies stop moving and stop attacking.
   - Apply a **blue tint overlay** on each zombie sprite (RGBA `0, 100, 255, 100` blended over the zombie).
3. Use `Plants/Iceshroom/IceshroomTrap/` sprites as a ground-level ice trap visual that appears **under zombie feet** during the freeze duration.
   - Place one IceshroomTrap sprite centered on each frozen zombie's base position.
4. After 10 seconds, unfreeze zombies (remove blue tint, remove trap sprites, zombies resume movement).
5. Chilled zombies (post-unfreeze) move at 50% speed for another 5 seconds.

---

## 9. Mushroom Projectiles (Puffshroom & Scaredyshroom)

- **Puffshroom** fires spores using sprites from `resources/graphics/Plants/mushroom_bullet/`.
- **Scaredyshroom** also fires using `mushroom_bullet/` sprites (same projectile).
- Projectile behavior: same as Peashooter pea (travels right, 20 dmg on hit, removed on impact or at row end).
- Scaredyshroom fires normally unless a zombie is within **3 cells** in the same row → enter cower state (use scared sprite from `Scaredyshroom/scared/`), no firing until zombie moves beyond 3-cell range.

---

## 10. Zombie Stats (PVZ-Accurate HP)

| Zombie Type | HP | Notes |
|-------------|-----|-------|
| Normal Zombie | 200 | No armor |
| Conehead Zombie | 560 | Cone adds 360 HP on top of 200 |
| Buckethead Zombie | 1300 | Bucket adds 1100 HP on top of 200 |
| Flag Zombie | 200 | Same as Normal, signals wave |
| Pole Vaulting Zombie | 500 | Vaults over first plant |
| Newspaper Zombie | 200+100 | Paper absorbs 100; enrages (2× speed) when paper destroyed |

Adjust zombie HP constants in `data/zombie_stats.py` (or equivalent config file) to match the table above.

---

## 11. Lawnmower Activation

**File:** wherever lawnmower collision is handled.

### Trigger
- When a zombie reaches column 0 (the leftmost column, the mower's row), the lawnmower activates.

### Animation
1. At the moment of contact, play `resources/graphics/Effects/lawnmowneractivated.gif` at the lawnmower's position.
2. The mower travels right at high speed across the **entire row**.
3. Any zombie it contacts takes **instant kill damage** (999999 dmg) and plays their normal death animation.
4. The GIF plays for its full duration as the mower crosses the row.
5. After crossing, the mower is removed (not replaced in this session).

---

## 12. Sun Economy Adjustments (Balancing)

**Current problem:** Sun spawns too frequently in too large amounts.

### Changes
| Parameter | Old Value | New Value |
|-----------|-----------|-----------|
| Sky sun spawn interval | ~7 seconds | **15 seconds** |
| Sky sun amount per drop | 25 | **25** (unchanged) |
| Sky sun max on screen at once | unlimited | **3** |
| Sunflower produce interval | ~15s | **24 seconds** |
| Sunflower sun amount | 25 | **25** (unchanged) |
| Starting sun | 150 | **100** |

Apply these in `data/game_config.py` or the constants file where sun timers are defined.

---

## 13. Missing Asset Creation Guide

If any of the following assets do not exist, create them as described:

### PotatoMine subfolders
- `Plants/PotatoMine/unarmed/` — 4-frame sprout animation, ~32×32px, green sprout emerging from soil.
- `Plants/PotatoMine/armed/` — 4-frame armed mine animation, ~48×48px, round mine fully above ground.

### Any missing `_card.png`
- Create a placeholder card (80×50px) with the plant's name and a simple colored background matching plant color identity.

### Any missing `_card_move.png`
- Per §4.3: fall back to the standard card. No need to create synthetic _move files.

---

## 14. Implementation Order for Agent

Execute in this exact order to avoid dependency issues:

```
1. Asset audit     → verify all paths in §0 exist; flag missing ones
2. Missing assets  → create per §13
3. Stats config    → update zombie HP (§10) and sun economy (§12)
4. Main Menu       → implement per §1
5. Day/Night modes → background + grid system (§2)
6. Plant roster    → register all plants with stats (§3)
7. Seed chooser UI → layout + cursor attachment (§4)
8. Shovel          → separate button + behavior (§5)
9. Potato Mine     → two-state unarmed/armed logic (§6)
10. Jalapeño       → fire row + burntZombie death (§7)
11. Ice-shroom     → freeze + blue tint + trap sprites (§8)
12. Mushroom shots → Puffshroom + Scaredyshroom (§9)
13. Lawnmower      → activation GIF + mow logic (§11)
14. Chomper        → three-state animation (§3.3)
15. Integration QA → full playthrough of Day level then Night level
```

---

## 15. QA Checklist

After implementation, verify each item:

- [ ] Main menu shows adventure_0 and adventure_1 on the grave correctly
- [ ] Day background loads with correct plant roster (8 plants)
- [ ] Night background loads with correct plant roster (8 plants), no sleep folders triggered
- [ ] Grid is identical size in both modes
- [ ] Clicking a plant card attaches the _move card (or fallback card) to the cursor
- [ ] Cursor card disappears on valid plant placement and on cancel
- [ ] Shovel is right-of-plants, uses shovel.jpg, removes plants correctly
- [ ] Potato Mine: zombies walk over it when unarmed; explodes when armed and stepped on
- [ ] Potato Mine: cannot be eaten in either state
- [ ] Jalapeño: fire spans full row, plays jalapenoFire.gif, kills all row zombies
- [ ] Jalapeño kill → burntZombie.gif plays instead of normal death
- [ ] Ice-shroom: all zombies freeze, turn blue, IceshroomTrap appears under feet
- [ ] Chomper: eats zombie in one bite, digests 42s, cannot attack while digesting
- [ ] Puffshroom and Scaredyshroom fire mushroom_bullet sprites
- [ ] Scaredyshroom cowers when zombie is within 3 cells
- [ ] Lawnmower: lawnmowneractivated.gif plays on zombie contact, mows full row
- [ ] Sun spawns at 15s intervals, max 3 on screen, game starts with 100 sun
- [ ] Zombie HP matches table in §10
- [ ] Night mode zombies frozen by Ice-shroom have visible blue tint
```
