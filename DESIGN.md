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

Internally, infos are already a registry of `HUDInfo` entries (id, renderer `(Player) -> Component`, config toggle,
tag consumers for datagen); the built-in infos are the first ones registered, registration order = render order, and
the id is also the name of both the item and the block tag that enable the info. The public API just has to expose
that mechanism.

- **HUD info registration**: another mod adds a line to the HUD.
  - The registry is currently frozen when the config spec is built (in the mod constructor), before other mods can
    register: either move the spec building later, give external infos their own config, or collect registrations
    before the spec is built.
  - Renderer still to extend: nullable (info not shown) or a list for multi-line infos.
- **Content providers**: for containers with non-vanilla storage (custom backpacks, etc.).
  - E.g. `registerContentsProvider(Predicate<ItemStack>, Function<ItemStack, Iterable<ItemStack>>)`.
  - Vanilla components (`BUNDLE_CONTENTS`, `CONTAINER`) are handled by default, for items in the
    `searchable_containers` tag.
- Registration happens client-side (e.g. in `FMLClientSetupEvent`); to decide whether to expose it through a
  custom mod bus event or thread-safe static methods.

## Ideas

- Curios slots.
- More infos (e.g. coordinates, moon phase, weather).
