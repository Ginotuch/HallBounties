# HallBounties
This plugin has been written specifically for the Hall of Heads in the [Gabbage](https://gabbage.net) minecraft server.

## Usage
* `/bounty list` will list active bounties.
* `/bounty add <bounty_name> <price> [quantity]` adds a new bounty. `[quantity]` is optional. Example: `/bounty add SheepHead 500 4`
* `/bounty pay <bounty_name> <player_name> [quantity]` pays a player for the bounty, and the specified amount (will multiply price). `[quantity]` is optional.
* `/bounty remove <bounty_name>` will remove a bounty.

## Permissions
* `hallbounties.use` lets you use `/bounty list`
* `hallbounties.add` lets you add bounties `/bounty add`
* `hallbounties.pay` lets you pay someone for a bounty `/bounty pay`
* `hallbounties.remove` lets you remove a bounty from the list `/bounty remove`

## Todo
* Add logging
* Better message system
* API call instead of console command(?)

#### Requirements
* [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/) (Last tested with EssentialsX 2.17.2 but as long as `economy pay` is a command it should be fine)