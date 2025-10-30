# 🌍 BetterWarpSystem

A lightweight and configurable **Bukkit/Spigot/Paper** plugin for managing simple player warps — with support for customizable messages, sounds, colors, and runtime configuration reloads.

---

## ✨ Features

- 🧭 **Player warps** — set, teleport, list, and delete named warps  
- 💾 **Persistent storage** — saves warps as YAML files on disk  
- 🧩 **Fully configurable** — customize messages, colors, and sounds via `config.yml`  
- 🔁 **Live reloading** — apply configuration changes at runtime  
- ⚙️ **Optional confirmations** — prevent accidental warp deletion  
- 💬 **Flexible notifications** — choose between chat messages, actionbar alerts, or both  

---

## 🧱 Requirements

- **Java:** 21  
- **Server:** Spigot / Paper (built against Spigot API **1.21.x**)  
- **Build tool:** Maven (for compiling from source)

---

## 🚀 Installation

1. Place the generated **JAR** file in your server’s `plugins/` folder.  
2. Start the server — the plugin will auto-create its data folder and default `config.yml`.  
3. Edit `plugins/WarpSysPlugin/config.yml` to adjust settings.  
4. Use the reload command in-game to apply changes instantly.

---

## ⚙️ Configuration

All user-facing text, sounds, and colors are defined in `config.yml`.

### 🔑 Key Settings (default values)

| Setting | Default | Description |
|----------|----------|-------------|
| `actionbar` | `true` | Show actionbar notifications |
| `message` | `true` | Send chat messages |
| `sound` | `true` | Play sounds on actions |
| `color.primary` | `&7` | Primary text color |
| `color.secondary` | `&a` | Highlight color |
| `warps.folder` | `warplocations` | Folder where warps are stored |

### 🔊 Sound Settings

Uses Bukkit’s [`Sound`](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html) enum names:

```yaml
sounds:
  success: 'ENTITY_VILLAGER_TRADE'
  fail: 'ENTITY_VILLAGER_NO'
  delete_confirm: 'BLOCK_ANVIL_PLACE'
```

### 🧨 Deletion Confirmation

```yaml
confirmation:
  delete:
    enabled: true
    ttl_seconds: 20
```

### 💬 Example Messages

```yaml
messages:
  reloaded: '&aConfiguration reloaded.'
  warp-set: '&aSuccessfully set warp "{warp}".'
  warp-deleted: '&aWarp "{warp}" deleted successfully.'
  delete-confirm: '&ePlease confirm deletion: &c/delwarp confirm {warp} &ewithin {seconds}s.'
```

> 💡 Use `&` color codes — they’re automatically translated at runtime.

---

## 🕹️ Commands

| Command | Description | Permission |
|----------|--------------|------------|
| `/setwarp <name>` | Set a warp at your current location | `warpsystem.setwarp` |
| `/warp <name>` | Teleport to a saved warp | *(everyone)* |
| `/warps` | List all available warps | *(everyone)* |
| `/delwarp <name>` | Start deletion flow | `warpsystem.delete` |
| `/delwarp confirm <name>` | Confirm deletion within TTL | `warpsystem.delete` |
| `/warpsysreloadconfig` | Reload the configuration | `warpsystem.*` |

---

## 🔐 Permissions

| Permission | Description | Default |
|-------------|--------------|----------|
| `warpsystem.setwarp` | Allows creating warps | OP |
| `warpsystem.delete` | Allows deleting warps | OP |
| `warpsystem.*` | Grants all permissions | OP |

---

## 📁 Data Storage

- Each warp is saved as an individual YAML file in the configured folder (default: `plugins/WarpSysPlugin/warplocations/`).  
- Configuration values are re-read at runtime when you run `/warpsysreloadconfig`.  
- Deletion confirmations are stored per-player and expire after the configured TTL.

---

## 🧩 Troubleshooting

- If warps aren’t being created or listed, make sure the plugin can write to its `plugins/` subfolder.  
- If sounds fail, double-check the Bukkit `Sound` enum names in your config. Invalid names fall back to defaults.

---

## 🤝 Contributing

Contributions are welcome!  
Please open pull requests or issues for bugs, improvements, or new features. Keep changes small, focused, and tested.

---

## 📜 License

```
MIT License

Copyright (c) 2025 Larrox

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
