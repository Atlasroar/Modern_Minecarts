# ModernMinecarts

## Description

ModernMinecarts is all about rekindling your love for Minecarts.

It makes them a viable method of transport for players and items again, while keeping a vanilla style.

### Copper Rails

These rails are twice as fast as powered rails.

However, oxidation causes them to become slower over time, down to half the speed of powered rails.

They are crafted like regular powered rails, using copper instead of gold.
Wax them with honeycomb to keep them from oxidizing.

---

### Chaining Minecarts

Link minecarts together using chains to transport multiple entities or large quantities of items simultaneously.

Connected hopper minecarts and chest minecarts join their inventories, allowing loading and unloading from a single hopper minecart.

---

### Furnace Minecarts

Furnace Minecarts have gotten an upgrade, now travelling twice as fast.
They have a new UI screen with a speedometer and a fuel slot that accepts all fuel types.

They also refuel themselves from attached chest and hopper minecarts,
but slow down under heavy load.
Chain multiple Furnace Minecarts together when pulling large trains to avoid this.

---

### New Rail Types

Directed Powered Rail: A redstone-powered rail that launches stationary minecarts and accelerates all carts in one direction. Place it while facing along the track to set its direction, or right-click it with an empty hand to reverse it.

Rail Jump: This ramp lets minecarts leap over gaps, even when not using experimental features.
It is crafted with a rail and a stick, or by using a stick on an already placed rail.

Rail Crossing: A simple rail intersection that acts like a straight rail from all directions.

Powered Detector Rail: It merges the functionalities of powered and detector rails. 
Stop empty carts or trains until fully loaded, or invert it to unload full minecarts and send them off once empty.
Shift right-click to swap the direction it faces.

---

### Customizability

The Mod now features a config file that allows you to customize the mod to your liking.

The following features can be disabled:
- Furnace Minecart Chunkloading
- Minecart Chaining
- Copper Rails
- Rail Crossing
- Powered Detector Rail
- Rail Jump

The following values can be tweaked:

- Copper Rail Speeds for each stage of oxidation

### Datapack Recipes

Rail recipes use vanilla crafting recipe formats and can be replaced by datapacks or other mods by overriding their recipe IDs:

- `modernminecarts:copper_rail`
- `modernminecarts:directed_powered_rail_recipe`
- `modernminecarts:powered_detector_rail`
- `modernminecarts:rail_crossing_recipe`
- `modernminecarts:rail_jump`
- `modernminecarts:waxed_copper_rail`
- `modernminecarts:waxed_exposed_copper_rail`
- `modernminecarts:waxed_weathered_copper_rail`
- `modernminecarts:waxed_oxidized_copper_rail`
- `minecraft:powered_rail`

Place a replacement JSON file at `data/<namespace>/recipes/<recipe_id>.json` in a datapack, using `minecraft:crafting_shaped` or `minecraft:crafting_shapeless` as appropriate. The vanilla powered rail recipe is included to retain the mod's 12-rail yield; overriding `minecraft:powered_rail` restores full datapack control over that recipe too.


---

### Other Changes

- Non-powered rails other than crossings can reach the speed of normal copper rails, with a configurable lower cap when ascending.
- Doubled the powered rail crafting result from 6 to 12 rails.
- Reduced minecart air drag to allow for further jumps.

The copper-rail mechanics are adapted from [Copper Rails](https://github.com/FXCourel/Copper-Rails/tree/1.20.1), released under CC0-1.0.

---

## v1.2.0 Changelog

### Feature Changes

- Removed Copper smithing templates
- Added Copper Rail crafting recipe (Powered Rail recipe using copper)
- All modded crafting recipes now appear in the recipe book.
- Shift-right-clicking in the air with a chain now removes its linking nbt-data.
- Added Fabric and Neoforge as supported ModLaunchers.
- Added configs for custom rail speeds, disabling specific features and changing crafting recipe yields.

### Bug Fixes

- Furnace minecarts now work independently from chained minecarts again
- Furnace minecarts no longer consume fuel on powered rails.
- Removed warnings and debug statements to prevent log spam.
- Fixed bug that prevented villagers from becoming toolsmiths
- Fixed issues with wax on and wax off achievements
- Improved architecture on Fabric and Neoforge, which should lead to more mod compatibility on these versions.
- Readded regular rail to creative menu on 1.19.2

## v1.2.1 Changelog

- Added config option for vanilla powered rail speed
- Fixed Server side issues on Forge versions
- Removed more debug statements

## Curretly supported versions

#### Forge

- 1.19.2
- 1.19.3
- 1.19.4
- 1.20.1
- 1.20.2

#### NeoForge

- 1.20.1
- 1.21.1
- 1.21.3-1.21.4
- 1.21.11
- 26.1
- 26.2

#### Fabric

- 1.20.1
- 1.21.1
- 1.21.11
- 26.1
- 26.2
