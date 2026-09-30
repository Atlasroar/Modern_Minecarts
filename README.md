# ModernMinecarts

> **AI assistance disclosure:** AI assistance was used to update this README and to help develop portions of this mod.
>
> **Original author:** ModernMinecarts was originally created by [Kipama](https://github.com/Kipama/Modern_Minecarts). This project builds on Kipama's work; original authorship is credited here.

The Minecart Update you've been waiting for.

ModernMinecarts is all about rekindling your love for Minecarts.

It makes them a viable method of transport for players and items again, while keeping a vanilla style.

## Features

Short Feature List:

- **Copper Rails:** Fast rails that slow down as they oxidize; vanilla Powered Rails are fastest by default.
- **Chains:** Link minecarts by shift-right-clicking a minecart while holding a chain.
- **Furnace Minecarts:** Fuel-powered engines with an in-game fuel and speedometer UI; accept furnace fuels.
- **Shared Inventories:** Chained chest and hopper minecarts share storage access. Furnace minecarts can draw fuel from attached chest and hopper minecarts.
- **Rail Jump:** A ramp made from a regular rail and a stick, or by using a stick on an existing rail.
- **Rail Crossing:** An intersection that supports travel from all directions.
- **Directed Powered Rail:** Accelerates minecarts in one direction, including stationary carts.
- **Powered Detector Rail:** Powers based on cart occupancy or inventory fullness; invert it to change the trigger.
- **Shared Rail Power:** Vanilla, copper, waxed copper, and Directed Powered Rails propagate redstone power across rail types.
- **Configuration:** Adjust rail speeds and feature toggles in Mod Menu or the config file; replace rail recipes with datapacks.

## Copper Rails

Copper Rails are the second-fastest rails by default. As they oxidize, their speed decreases through four stages. Waxed versions retain the speed of their current oxidation stage. They use copper in the powered-rail crafting pattern; apply honeycomb to prevent further oxidation.

| Rail | Default speed |
| --- | ---: |
| Vanilla Powered Rail | 1.0 |
| Copper / Waxed Copper Rail | 0.8 |
| Exposed Copper / Waxed Exposed Copper Rail | 0.6 |
| Weathered Copper / Waxed Weathered Copper Rail | 0.3 |
| Oxidized Copper / Waxed Oxidized Copper Rail | 0.2 |

## Chaining Minecarts

Hold a chain and shift-right-click minecarts to link them into a train. Chained chest and hopper minecart storage can be accessed together, allowing loading and unloading through a single hopper. Furnace minecarts can draw fuel from attached chest and hopper minecarts.

## Furnace Minecarts

Furnace Minecarts are upgraded fuel-powered engines. Interact with one to open its fuel interface and speedometer. They accept furnace fuels, can draw fuel from attached chest or hopper minecarts, and can keep chunks loaded while burning fuel if chunkloading is enabled. Trains slow under heavy load; chaining multiple furnace minecarts can help haul heavier trains. Furnace minecarts do not burn fuel while riding an actively powered rail and stop on unpowered powered rails.

## New Rail Types

### Rail Jump

This ramp launches minecarts over gaps without requiring experimental features. Craft it with a rail and a stick, or use a stick on an already placed rail.

### Rail Crossing

A simple intersection that supports straight travel from all directions.

### Directed Powered Rail

Always accelerates minecarts in the same direction, including stationary carts. Use it to build one-way tracks and redstone-controlled train stations. When placed, its track and travel direction align with the direction you face; right-click it with an empty hand to reverse direction.

### Powered Detector Rail

Combines powered-rail and detector-rail behavior. It can hold an empty cart or train until it is occupied or loaded, or be inverted to trigger when an inventory is empty. Shift-right-click to change the direction it faces; right-click normally to invert its fullness behavior.

## Customizability

With [Mod Menu](https://github.com/TerraformersMC/ModMenu) installed, open the configuration screen from the Modern Minecarts entry in the Mods menu. Mod Menu is optional and is not bundled with the mod.

The configuration screen and `config/modernminecarts.properties` provide settings for:

- Copper, exposed, weathered, and oxidized Copper Rail speeds
- Vanilla Powered Rail speed
- Maximum speed on ascending rails
- Furnace Minecart chunkloading
- Minecart chaining
- Copper Rails, Rail Crossings, Powered Detector Rail, and Rail Jump feature toggles

Speed settings saved in Mod Menu apply immediately. Feature toggles take full effect after restarting the game. Manual edits to the properties file are read when the game starts. Speed values are limited to `0.01`–`1.6`. Directed Powered Rails are always registered and do not have a feature toggle.

Powered Rails default to `1.0`, faster than every Copper Rail oxidation stage. Existing configuration files that still contain the old default of `0.4` are upgraded automatically; other saved speed values are retained.

### Datapack Recipes

Rail recipe result counts and ingredients are defined by vanilla-format recipe JSON and can be changed by datapacks or other mods; they are not configuration-file settings. Replace the recipe by providing a recipe JSON with the same ID:

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

Place the override at `data/<namespace>/recipes/<recipe_id>.json` in your datapack. The default powered-rail recipe yields 12 rails.

## Other Changes

- Non-powered rails, except Rail Crossings, can reach the default Copper Rail speed; ascending rails use the configured speed cap.
- Reduced air drag on minecarts to allow for further jumps.

## Credits

- **Original mod author:** [Kipama — ModernMinecarts](https://github.com/Kipama/Modern_Minecarts)
- Copper Rail mechanics adapted from [FXCourel — Copper-Rails](https://github.com/FXCourel/Copper-Rails/tree/1.20.1), released under CC0-1.0.
- Mod Menu integration uses the optional API from [TerraformersMC — ModMenu](https://github.com/TerraformersMC/ModMenu).

## Supported Version

- Minecraft 1.20.1
- Fabric
