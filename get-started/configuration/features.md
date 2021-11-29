---
description: Default features.
---

# Features

## Commands

You can simply view all commands for features using the following command.

> /dh features help

## Damage Display

When damage is dealt, display a temporary holographic text showing the amount of damage.

### Configuration

This feature can be configured in the main 'config.yml' file.

```yaml
damage-display:
  # Do you want this feature enabled? [true/false]
  enabled: false
  # How long will the hologram stay in ticks
  duration: 40
  # Damage placeholder: {damage}
  # Animations and Placeholders DO work here
  appearance: '&c{damage}'
```

## Heal Display

When entity heals, display a temporary holographic text showing the amount of health recovered.

### Configuration

This feature can be configured in the main 'config.yml' file.

```yaml
healing-display:
  # Do you want this feature enabled? [true/false]
  enabled: false
  # How long will the hologram stay in ticks
  duration: 40
  # Heal placeholder: {heal}
  # Animations and Placeholders DO work here
  appearance: '&a+ {heal}'
```
