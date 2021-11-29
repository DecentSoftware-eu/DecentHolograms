---
description: A hologram that contains actions
---

# Example #5

{% code title="hologram_example5.yml" %}
```yaml
location: world:0.500:100.000:0.500
enabled: true
display-range: 64
update-range: 64
update-interval: 20
facing: 0.0
down-origin: false
pages:
- lines:
  - content: '&f&lServer Name'
    height: 0.3
  - content: '&fOur server is the best in the world'
    height: 0.3
  - content: '&f(Right or left mouse click)'
    height: 0.3
  actions: 
    LEFT:
    - MESSAGE:&fSome text that you just see in minecraft chat when you left click 
    - CONSOLE:give {player} dirt 5
    - TELEPORT:world:10:100:10
    RIGHT:
    - MESSAGE:&fSome text that you just see in minecraft chat when you right click
    - COMMAND:plugins
    - SOUND:BLOCK_GLASS_BREAK
    SHIFT_LEFT:
    - PERMISSION:some.permission.use
    - MESSAGE:&fCongratulations, you have enough permissions after left click with shift.
```
{% endcode %}
