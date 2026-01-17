# MapTrail

A plugin for Hytale, adds a trail of markers to the map showing your recent path through the world.

*Github*: https://github.com/jadedbay/MapTrail

## Installation
1. Download `MapTrail-x.x.x.jar` from [Releases](https://github.com/jadedbay/MapTrail/releases)
2. Place in your server's `mods/` folder
3. Restart the server

## Features
- <strong>Trail Markers</strong> - Adds markers to the map showing your recent path
- <strong>Individual Trail</strong> - Each player can only see their own trail
- <strong>Customizable</strong> - Use commands to adjust how the trail looks
- <strong>Optimization</strong> - Removes player's markers from memory when they disconnect

## Commands

`/maptrail markers <value>` - Sets the maximum amount of markers saved and displayed per player (default: 100)

`/maptrail distance <value>` - Sets the distance between each marker (default: 8.0)
