---
description: Main configuration file
---

# Config

## Default config.yml

```yaml
defaults:
  # Default line
  text: Blank Line
  # Default Hologram display range in blocks.
  display-range: 64
  # Default Hologram update range in blocks.
  update-range: 64
  # Default Hologram update interval in ticks.
  update-interval: 20
  # Default heigths of different hologram line types.
  heigth:
    text: 0.3
    icon: 0.6
    head: 0.75
    smallhead: 0.6
  # Default value of Origin
  origin: UP

# Check for updates on plugin startup? [true/false]
update-checker: true
```

Main 'config.yml' file also contains configuration for [Features](features.md).
