# Item & Block Info Hud — Design

Mod standalone (`itemblockinfohud`) estratta dalla feature `HudInfos` di Insane's Survival Overhaul
(`ISO/src/main/java/insane96mcp/insanesurvivaloverhaul/module/client/hudinfos/HudInfos.java`).

Mostra info in alto a sinistra (direzione cardinale, altitudine, bioma, ora/giorno) quando il giocatore ha
certi item nell'inventario (anche dentro contenitori cercabili), guarda un item frame che li contiene,
o guarda certi blocchi.

## Stato attuale

Porting diretto della feature, senza refactor:
- Tag ancora con prefisso `hud/` (`itemblockinfohud:hud/depth`, ecc.), niente `searchable_containers`.
- Il Pouch di ISO è riconosciuto temporaneamente per id (`insanesurvivaloverhaul:pouch`) leggendo
  `DataComponents.CONTAINER`, nessuna dipendenza da ISO.
- Chiavi lang ancora `hud_info.*` come in ISO.
- Config `season` rimosso (era già inutilizzato).
- ISO: `HudInfos`, tag `hud/*` e chiavi lang già rimossi, dipendenza `optional` aggiunta.
  Resta da fare solo l'aggiunta del Pouch a `searchable_containers` quando esisterà il tag.

Il resto di questo documento descrive il design di arrivo.

## Decisioni

- **Mod normale, richiesta su entrambi i lati.** I tag item/block sono sincronizzati dal server e sostituiscono
  quelli del client, quindi una mod solo client non può affidarsi ai tag.
- **Nessun server config / override dal server.** Il comportamento resta quello attuale: i toggle sono preferenze
  del giocatore (config `CLIENT`), il server decide solo *quali* item/blocchi attivano le info tramite i tag.
- **Serene Seasons rimossa**, nessuna integrazione (via anche il tag `season`).
- **ISO dipende dalla mod in modo `optional`**: l'obiettivo della separazione è poterla usare senza ISO e
  viceversa.
- **ISO non ha dipendenze in compilazione** dalla mod: si integra solo via tag (datagen con `addOptional`).

## Struttura (modello: MobsPropertiesRandomness)

- Classe `@Mod` principale: `ILModConfig` single-module, `ModConfig.Type.CLIENT`
  (`new ILModConfig(id("main"), "Single Module", ModConfig.Type.CLIENT, modEventBus, classLoader)`).
- Classe `@Mod(dist = Dist.CLIENT)`: registra il layer HUD (`RegisterGuiLayersEvent`) e la config screen.
- Feature `HudInfos` (InsaneLib `Feature`), stesso config attuale meno `season`:
  `cardinalDirection`, `depth`, `time`, `biome`.
- Dipendenza da InsaneLib in `build.gradle` / `neoforge.mods.toml` come in MPR.
- Da ripulire dal template: blocco/item/creative tab di esempio, `Config.java` di esempio,
  `itemblockinfohud.mixins.json` se non servono mixin, `README.md`, `TEMPLATE_LICENSE.txt`.

## Tag (senza prefisso `hud/`)

Item tag:

| Tag | Default |
|---|---|
| `itemblockinfohud:cardinal_direction` | `minecraft:compass` |
| `itemblockinfohud:depth` | optional `supplementaries:altimeter`, optional `caverns_and_chasms:depth_gauge` |
| `itemblockinfohud:time` | `minecraft:clock` |
| `itemblockinfohud:biome` | vuoto |
| `itemblockinfohud:searchable_containers` | `minecraft:bundle` |

Block tag (vuoti di default): `cardinal_direction`, `depth`, `time`, `biome`.

### `searchable_containers`

Item il cui contenuto viene ispezionato per cercare gli item delle info. Il contenuto viene letto in modo generico
dai component vanilla `DataComponents.BUNDLE_CONTENTS` e `DataComponents.CONTAINER`, più eventuali content provider
registrati via API.

- Le shulker box **non** sono nel tag di default (come il comportamento attuale), ma un pack può aggiungerle.
- ISO aggiunge il Pouch (`insanesurvivaloverhaul:pouch`, usa `DataComponents.CONTAINER`) con `addOptional`.

## API

L'implementazione interna è strutturata come una lista di info registrate (non metodi `tryRenderX` hardcoded):
le info built-in sono semplicemente le prime registrate. Così l'API pubblica è solo un'esposizione del meccanismo
interno.

- **Registrazione info HUD**: un'altra mod aggiunge una riga all'HUD.
  - Dati: id (`ResourceLocation`), tag item, tag block, condizione di abilitazione (es. config), e un renderer
    `(Player) -> Component` (o nullable/lista per info multi-riga).
  - Ordine di registrazione = ordine di render.
- **Content provider**: per contenitori con storage non vanilla (zaini custom, ecc.).
  - Es. `registerContentsProvider(Predicate<ItemStack>, Function<ItemStack, Iterable<ItemStack>>)`.
  - I component vanilla (`BUNDLE_CONTENTS`, `CONTAINER`) sono gestiti di default.
- Le registrazioni avvengono lato client (es. in `FMLClientSetupEvent`); da valutare se esporle tramite evento
  custom sul mod bus o metodi statici thread-safe.

Possibili espansioni future: slot Curios, info aggiuntive (es. coordinate, fase lunare, meteo).

## Lingua

Chiavi spostate nel namespace della mod, es.:

- `itemblockinfohud.cardinal_direction.north` … `south_east`
- `itemblockinfohud.depth` → `"Altitude: %d"`
- `itemblockinfohud.time` → `"%s (Day: %d)"`

Il bioma usa le chiavi vanilla `biome.<namespace>.<path>`.

## Migrazione in ISO

- Rimuovere `module/client/hudinfos/HudInfos.java` e la registrazione in `InsaneSO.java`
  (`eventBus.addListener(HudInfos::registerGuiLayers)`).
- Rimuovere i tag `HUD_*` da `ISOItemTagsProvider` / `ISOBlockTagsProvider` e i JSON generati in
  `src/generated/resources/data/insanesurvivaloverhaul/tags/item/hud/`.
- Rimuovere le chiavi lang `hud_info.cardinal_direction.*`, `hud_info.depth`, `hud_info.time`
  (**tenere** `hud_info.glow_block_*`, usate da `GlowBlockClient`).
- Aggiungere al datagen il Pouch in `itemblockinfohud:searchable_containers` (`addOptional`).
- Aggiungere `itemblockinfohud` come dipendenza `optional` in `neoforge.mods.toml`.
- Aggiornare `PORTING.md` / changelog.
