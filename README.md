# TimedRewards

A Minecraft plugin that automatically gives configured rewards (items and commands) to all online players at specified intervals with PlaceholderAPI support for scoreboard integration.

## Features

- **Global Timer System** - All online players receive rewards when the timer reaches 0, regardless of individual online time
- **Flexible Rewards** - Configure both items and commands (perfect for crate keys, economy, etc.)
- **PlaceholderAPI Integration** - Display reward timers in scoreboards and other plugins
- **Fully Customizable** - All messages, prefixes, sounds, and rewards configurable via YAML
- **Timer Management** - Pause, resume, and monitor all reward timers
- **Multiple Rewards** - Run unlimited simultaneous timed rewards with different intervals

## Requirements

- Minecraft Server 1.20.4+ (Paper/Spigot)
- Java 17+
- PlaceholderAPI (for placeholder support)

## Installation

1. Download the latest TimedRewards.jar
2. Place it in your server's `plugins` folder
3. (Optional) Install PlaceholderAPI for placeholder support
4. Restart your server
5. Configure rewards in `plugins/TimedRewards/items.yml`

## Configuration

### items.yml

Configure your timed rewards with items and/or commands:

```yaml
rewards:
  hourly-diamonds:
    enabled: true
    display-name: "&b&lHourly Diamonds"
    interval: 60  # Minutes
    items:
      - material: DIAMOND
        amount: 5
        name: "&b&lHourly Diamond"
        lore:
          - "&7Received from hourly rewards!"
    commands: []

  crate-keys:
    enabled: true
    display-name: "&e&lCrate Key Reward"
    interval: 30  # 30 minutes
    items: []
    commands:
      - "crate givekey %player% vote 1"
      - "eco give %player% 1000"

  mixed-reward:
    enabled: true
    display-name: "&d&lMixed Reward"
    interval: 120  # 2 hours
    items:
      - material: EMERALD
        amount: 10
    commands:
      - "lp user %player% permission set special.perk true"
```

### config.yml

General plugin settings:

```yaml
# Enable/disable the entire plugin
enabled: true

# Broadcast message settings
broadcast:
  enabled: true
  title:
    enabled: true
    fade-in: 10
    stay: 70
    fade-out: 20
  sound:
    enabled: true
    type: "ENTITY_PLAYER_LEVELUP"
    volume: 1.0
    pitch: 1.0
```

### messages.yml

Customize all plugin messages and the prefix.

## Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/timedrewards reload` | `timedrewards.admin` | Reload all configuration files |
| `/timedrewards list` | `timedrewards.admin` | View all rewards and their timers |
| `/timedrewards pause` | `timedrewards.admin` | Pause all reward timers |
| `/timedrewards resume` | `timedrewards.admin` | Resume all reward timers |

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `timedrewards.admin` | Access to all admin commands | op |

## Placeholders

Requires PlaceholderAPI to be installed.

| Placeholder | Description | Example Output |
|-------------|-------------|----------------|
| `%timedrewards_<reward-id>_time%` | Formatted time remaining | `1h 23m 45s` |
| `%timedrewards_<reward-id>_seconds%` | Total seconds remaining | `5025` |
| `%timedrewards_<reward-id>_minutes%` | Total minutes remaining | `83` |
| `%timedrewards_<reward-id>_name%` | Display name of reward | `Hourly Diamonds` |

### Example Scoreboard Usage

```yaml
# Using in scoreboards (e.g., with plugins like FeatherBoard)
lines:
  - "&b&lNext Reward:"
  - "&f%timedrewards_hourly-diamonds_time%"
  - ""
  - "&e&lCrate Keys:"
  - "&f%timedrewards_crate-keys_time%"
```

## How It Works

1. **Global Timers** - Each reward in `items.yml` has its own independent timer
2. **Countdown** - Timers count down every second
3. **Reward Distribution** - When a timer reaches 0, ALL online players receive the reward
4. **Auto Reset** - Timer automatically resets to the configured interval and starts again
5. **Commands Execute** - Commands run as console with `%player%` replaced with each player's name

## Example Use Cases

- **Hourly vote keys** - Reward active players with crate keys every hour
- **Daily login rewards** - Give special items to players online at specific times
- **Active player bonuses** - Reward players who are online when timer expires
- **Economy boosts** - Automatically give money to online players
- **Permission rewards** - Grant temporary permissions at intervals

## Support

For issues, suggestions, or questions, please contact me on Discord: Dutchwilco

## Author
**Dutchwilco**
All rights reserved.
