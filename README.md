# BetterWarpSystem

A small and configurable Bukkit/Spigot plugin to manage simple player warps (set, warp, list, delete) with runtime configuration and optional actionbar/messages/sounds.

---

## Features

- Set and save named warps to disk
- Teleport to saved warps
- List available warps
- Delete warps with optional confirmation
- Fully configurable messages, colors and sounds via `config.yml`
- Config reloadable at runtime (`plugin.reloadConfig()` / provided reload command)

---

## Requirements

- Java 21
- Spigot / PaperMC compatible server (built against Spigot API 1.21.x)
- Maven (to build from source)

---

## Building

Build the plugin JAR using Maven:

```bash
mvn -f D:\IdeaProjects\BetterWarpSystem\pom.xml clean package
```

The shaded JAR will appear in `target/`.

---

## Installation

1. Copy the generated JAR into your server `plugins/` folder.
2. Start the server. On first run the plugin will create its data folder and a default `config.yml`.
3. Edit `plugins/WarpSysPlugin/config.yml` to tweak settings if needed and run the reload command in-game to apply changes.

---

## Configuration

All user-facing texts, colors, sounds and folder names are configurable in `config.yml`.

Important keys (defaults shown):

- `actionbar`: true — enable/disable actionbar notifications
- `message`: true — enable/disable normal chat messages
- `sound`: true — enable/disable sounds on actions
- `color.primary`: `&7` — primary color prefix
- `color.secondary`: `&a` — secondary color for highlights
- `warps.folder`: `warplocations` — folder inside plugin data folder where warps are saved

Sound keys (use Bukkit `Sound` enum names):

- `sounds.success`: `ENTITY_VILLAGER_TRADE`
- `sounds.fail`: `ENTITY_VILLAGER_NO`
- `sounds.delete_confirm`: `BLOCK_ANVIL_PLACE`

Confirmation settings (for deletion):

- `confirmation.delete.enabled`: true
- `confirmation.delete.ttl_seconds`: 20

Messages are under `messages.*` and include placeholders such as `{warp}` and `{list}`.

Example snippet (from the packaged defaults):

```yaml
actionbar: true
sound: true
message: true
color:
  primary: '&7'
  secondary: '&a'

warps:
  folder: 'warplocations'

sounds:
  success: 'ENTITY_VILLAGER_TRADE'
  fail: 'ENTITY_VILLAGER_NO'
  delete_confirm: 'BLOCK_ANVIL_PLACE'

confirmation:
  delete:
    enabled: true
    ttl_seconds: 20

messages:
  reloaded: '&aConfiguration reloaded.'
  warp-set: '&aSuccessfully set warp "{warp}".'
  warp-deleted: '&aWarp "{warp}" deleted successfully.'
  delete-confirm: '&ePlease confirm deletion by running: &c/delwarp confirm {warp} &ewithin {seconds}s.'
```

Notes:
- Colors in messages use `&` color codes and are translated at runtime.
- If you change `config.yml` while the server is running, use the reload command to apply changes.

---

## Commands

- `/setwarp <name>` — Set a warp at your current location (permission: `warpsystem.setwarp`)
- `/warp <name>` — Teleport to a saved warp
- `/warps` — List available warps
- `/delwarp <name>` — Begin delete flow (if confirmation enabled this creates a pending confirmation)
- `/delwarp confirm <name>` — Confirm deletion for previously pending warp (within TTL)
- `/warpsysreloadconfig` — Reload plugin configuration from disk

---

## Permissions

- `warpsystem.setwarp` — set warps (default: op)
- `warpsystem.delete` — delete warps (default: op)
- `warpsystem.*` — all warp permissions

---

## Behavior notes

- The plugin stores warp data as individual YAML files under the configured warps folder inside the plugin data folder (e.g. `plugins/WarpSysPlugin/warplocations/`).
- Commands read configuration at runtime via `plugin.getConfig()`, so after running `plugin.reloadConfig()` or using the provided reload command, new config values are used immediately.
- Deletion confirmation state is kept in memory per-player and expires after `confirmation.delete.ttl_seconds`.

---

## Troubleshooting

- If warps are not being listed or created, ensure the plugin has permission to create directories under the server's `plugins/` directory and that `warps.folder` points to the intended relative folder.
- If sounds don't play, verify you used valid Bukkit `Sound` enum names in `config.yml`. Invalid names fall back to safe defaults.

---

## Contributing

Contributions are welcome. Please open pull requests or issues on the repository. Keep changes small and focused. Add tests or clear manual validation instructions for behavioral changes.

---

## License

Add your license here (MIT, Apache-2.0, etc.).

