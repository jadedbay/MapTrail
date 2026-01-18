# MapTrail

A plugin for **Hytale**, adds a trail of markers to the map showing your recent path through the world.

*CurseForge*: https://www.curseforge.com/hytale/mods/maptrail

*Github*: https://github.com/jadedbay/MapTrail

## Installation
1. Download `MapTrail-x.x.x.jar` from [CurseForge](https://www.curseforge.com/hytale/mods/maptrail) or [Releases](https://github.com/jadedbay/MapTrail/releases)
2. Place in your `Mods/` folder
3. Enable it in your world settings

## Features
- Adds markers to the map showing your recent path
- Each player can only see their own trail
- Use commands to adjust how the trail looks
- Removes player's markers from memory when they disconnect

## Commands

`/maptrail markers <value>` - Set the maximum amount of markers saved and displayed per player (default: 100)

`/maptrail distance <value>` - Set the distance between each marker (default: 8.0)

`/maptrail size <small|medium> <value>` - Controls when markers change size (default: small = 0.15, medium = 0.45)

`/maptrail config` - View the current config values
