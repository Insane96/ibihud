Don't write code unless explicitly asked to.  
Your job is to analyze user edits to check for errors or possible bugs.
Ignore unused imports.

## Project

**Item & Block Info Hud** (`itemblockinfohud`, Modrinth slug `ibihud`): Minecraft mod extracted from the `HudInfos`
feature of Insane's Survival Overhaul (ISO). Shows infos on the top left of the screen (cardinal direction, altitude,
biome, time/day) when the player has certain items in the inventory (also inside searchable containers), is looking
at an item frame holding one of them, or is looking at certain blocks.

Design decisions, to do and ideas are in `DESIGN.md`; user-facing changes go in `changelog.md` under `# Upcoming`.

## Stack

- Minecraft 1.21.1, NeoForge (ModDevGradle), Java 21, Parchment mappings. Versions in `gradle.properties`.
- Depends on **InsaneLib** (from Modrinth maven): features are classes extending `Feature` annotated with
  `@LoadFeature`, config fields are `public static` fields annotated with `@Config`, loaded through `ILModConfig`.
- `src/main/templates/META-INF/neoforge.mods.toml` is expanded with properties from `gradle.properties` by the
  `generateModMetadata` task.

## Structure (`src/main/java/insane96mcp/itemblockinfohud/`)

- `ItemBlockInfoHud` — common entrypoint: registers the `CLIENT` config, datagen, network payload.
- `ItemBlockInfoHudClient` — client entrypoint (`dist = Dist.CLIENT`): config screen, GUI layer, warning in chat on
  login if the server doesn't have the mod.
- `feature/HudInfos` — the whole HUD: config toggles, GUI layer, the `tryRenderX`/`renderX` methods and the checks
  (inventory, searchable containers via `CONTAINER`/`BUNDLE_CONTENTS` components, item frame, looked-at block).
- `data/generator/IBIH{Item,Block}TagsProvider` — tag keys (`itemblockinfohud:cardinal_direction`, `depth`, `time`,
  `biome`, plus item tag `searchable_containers`) and their default contents. Optional entries for other mods use
  `addOptional`.
- `network/ServerPresencePayload` — never sent; registered as `optional()` only so the client can detect whether the
  server has the mod (`connection.hasChannel`).

## Key constraints

- The mod is needed server-side to work: tags are synced from the server and replace the client ones.
  `displayTest="IGNORE_ALL_VERSION"` lets clients join servers without it (a chat warning is shown instead), and the
  optional payload lets clients without it join servers that have it.
- HUD toggles are client config only; the server decides which items/blocks enable the infos, only through tags.
- Client-only code (`Minecraft`, GUI events) must not be reachable from the common entrypoint.
- Lang keys use the mod namespace (`itemblockinfohud.*`, built with `ItemBlockInfoHud.lang(path)`); biome names use
  vanilla `biome.<namespace>.<path>` keys.

## Commands

- `./gradlew build` — build the jar.
- `./gradlew runClient` / `runServer` — run the game.
- `./gradlew runData` — regenerate `src/generated/resources` (tags). Re-run after changing the tags providers and
  commit the generated files.
- `publish.sh` — publishes to maven local and triggers the GitHub `publish.yaml` workflow on the current branch.
