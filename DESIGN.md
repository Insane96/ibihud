# Item & Block Info Hud — Design

Standalone mod (`itemblockinfohud`) extracted from the `HudInfos` feature of Insane's Survival Overhaul (ISO).

Shows infos on the top left of the screen (cardinal direction, altitude, biome, time/day) when the player has certain
items in the inventory (also inside searchable containers), is looking at an item frame holding one of them, or is
looking at certain blocks.

## Decisions

- **Regular mod, required on both sides.** Item/block tags are synced from the server and replace the client ones,
  so a client-only mod can't rely on tags.
- **No server config / server override.** The toggles are player preferences (`CLIENT` config); the server only
  decides *which* items/blocks enable the infos, through tags.
- **No Serene Seasons integration.**
- **ISO and this mod are independent**: ISO declares no dependency on this mod and integrates only through tags
  (datagen with `addOptional`).

## To do

### API

Internally, infos become a list of registered entries instead of hardcoded `tryRenderX` methods; the built-in infos
are simply the first ones registered. The public API then just exposes that internal mechanism.

- **HUD info registration**: another mod adds a line to the HUD.
  - Data: id (`ResourceLocation`), item tag, block tag, enabled condition (e.g. a config option), and a renderer
    `(Player) -> Component` (nullable, or a list for multi-line infos).
  - Registration order = render order.
- **Content providers**: for containers with non-vanilla storage (custom backpacks, etc.).
  - E.g. `registerContentsProvider(Predicate<ItemStack>, Function<ItemStack, Iterable<ItemStack>>)`.
  - Vanilla components (`BUNDLE_CONTENTS`, `CONTAINER`) are handled by default.
- Registration happens client-side (e.g. in `FMLClientSetupEvent`); to decide whether to expose it through a
  custom mod bus event or thread-safe static methods.

## Ideas

- Curios slots.
- More infos (e.g. coordinates, moon phase, weather).
