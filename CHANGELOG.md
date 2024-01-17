# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to [Forge Recommended Versioning](https://mcforge.readthedocs.io/en/latest/conventions/versioning/).

## [1.20.4-2.1.0.0] - 2024-01-17
### Changed
- Update to Minecraft 1.20.4 (Forge 49.0.19, Neoforge 20.4.109-beta)

## [1.20.2-2.0.0.0] - 2024-01-17
### Changed
- Move to Multiloader mod template to support Forge and Neoforge
- Update to Minecraft 1.20.2 (Forge 48.1.0, Neoforge 20.2.86)

## [1.20.2-1.8.0.0] - 2023-10-17
### Changed
- update and move back to Forge 1.20.2-48.0.23 (from NeoForge) until it is stable

## [1.20.1-1.7.0.1] - 2023-08-14
### Fixed
- Incompatibility with Tubes Reloaded (and maybe other mods) (thanks to Dam-Buty for the report)

## [1.20.1-1.7.0.0] - 2023-08-09
### Changed
- Changed Forge to NeoForge 1.20.1-47.1.54 (compatible with Forge 47.1.0)

## [1.20.1-1.6.1.0] - 2023-06-27
### Changed
- Update to Forge 1.20.1-47.0.34 to add compatibility with Chiseled Bookshelf

## [1.20-1.6.0.0] - 2023-06-08
### Changed
- Update to Forge 1.20-46.0.1 #14

## [1.19.4-1.5.0.0] - 2023-03-18
### Changed
- Update to Forge 1.19.4-45.0.9

## [1.19.3-1.4.3.1] - 2023-02-05
### Changed
- Translation fixes for pt_br and addition of translation for pt_pt #12 (thanks to sanduicheirainox)

## [1.19.3-1.4.3.0] - 2022-12-30
### Changed
- Update to Forge 1.19.3-44.0.41

### Fixed
- The brick hopper did not fill up with the item it contained even though there was space.

## [1.19-1.4.2.1] - 2022-12-28
### Fixed
- incompatibility with comparators when Canary is installed

## [1.19-1.4.2.0] - 2022-07-14
### Changed
- Update mod to Forge 1.19-41.0.96 to fix startup crashes since Forge 1.19-41.0.94

## [1.19-1.4.1.0] - 2022-06-30
### Changed
- Update mod to Forge 1.19-41.0.56 to support interaction with chest boats

## [1.19-1.4.0.0] - 2022-06-23
### Added
- Support filling and emptying for entities with item handlers (Chest Minecart ...)

### Changed
- Update mod to Forge 1.19-41.0.45

## [1.18.1-1.3.1.1] - 2022-05-31
### Added
- Chinese (zh_cn) language support #7 (thanks to rentommy)

## [1.18.1-1.3.1.0] - 2022-01-16
### Changed
- Update mod to Forge 1.18.1-39.0.0 (fix Log4J security issue)

### Fixed
- Items disappeared from hoppers inventory when closing the world https://github.com/cech12/WoodenHopper/issues/7 (thanks to JoeThePanda for the report)

## [1.18-1.3.0.1] - 2021-12-04
### Changed
- Update mod to Forge 1.18-38.0.4

## [1.17.1-1.3.0.1] - 2021-08-13
### Fixed
- Add version check for startup issues

## [1.17.1-1.3.0.0] - 2021-08-13
### Changed
- Update mod to Forge 1.17.1-37.0.32

## [1.16.5-1.3.0.0] - 2021-08-13
### Added
- Russian and Ukrainian translation #5 (thanks to vstannumdum aka DMHYT)

### Changed
- Update mod to Forge 1.16.5-36.1.0
- changed versioning to fit [Forge Recommended Versioning](https://mcforge.readthedocs.io/en/latest/conventions/versioning/)

## [1.2.0_1.16] - 2021-04-04
### Added
- Russian language support added #3 (thanks to sashafiesta)
- two new config options added #4 (thanks to martiesim for the idea)
- brickHopperPullItemsFromWorldEnabled - enable/disable pulling item entities from world (default enabled)
- brickHopperPullItemsFromInventoriesEnabled - enable/disable pulling items from inventories (default enabled)

## [1.1.1_1.16] - 2021-03-17
### Added
- pt_br language support added (thanks to Mikeliro)

## [1.1.0_1.16] - 2021-03-17
### Fixed
- Bugfix: Brick Hopper did not interact with the Composter (thanks to Jomeaga for the bug report)

## [1.0.0_1.16] - 2021-02-02
### Added
- Adds a Brick Hopper to the game.
- It is a bit slower than a vanilla hopper. (12 ticks instead of 8 - configurable)
- It has only three slots.
