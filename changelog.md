# 1.1.1.1
* Fixed items inside containers using the `minecraft:container` component (e.g. Shulker Boxes) in the `itemblockinfohud:searchable_containers` tag not being searched

# 1.1.1.0
* Fixed time minutes only going from 00 to 49
* Day now starts from 1 instead of 0
* Searchable containers in the offhand are now searched too
* Lightning rod no longer enables the weather info

# 1.1.0.0-beta
* Added position info (`itemblockinfohud:position`), enabled by the compass
* Added moon phase info (`itemblockinfohud:moon_phase`), enabled by the clock and Supplementaries' clock block
* Added weather info (`itemblockinfohud:weather`), enabled by looking at a lightning rod or Supplementaries' wind vane. Takes into account the biome (cloudy instead of rain in dry biomes, snow in cold biomes) and is hidden in dimensions without a sky (e.g. Nether and End)
* Day has been split to own info (`itemblockinfohud:day`)
* Removed `hud/` from tags (e.g. `itemblockinfohud:hud/time` has become `itemblockinfohud:time`)
* Mod now shows a warning if not present server-side and allows connecting to servers that have it if not present client-side
* Instead of searching for items in hardcoded containers (bundles and ISO's Pouches), items are searched in any item in the `itemblockinfohud:searchable_containers` that uses the `minecraft:container` or `minecraft:bundle_contents` data component
* Changed translation keys to use the mod's namespace
* Added italian translation

# 1.0.0.0-alpha
* Initial release, split from Insane's Survival Overhaul
