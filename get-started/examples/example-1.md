---
description: Hologram that contains multiple functions together
---

# Example #1

{% code title="hologram_example1.yml" %}
```yaml
location: world:0.500:100.000:0.500
enabled: true
display-range: 64
update-range: 64
update-interval: 20
facing: 0.0
down-origin: true
pages:
- lines:
  - content: <#B22AFE>&l&nDECENT HOLOGRAMS</#ff8157>
    height: 0.3
  - content: Very powerful hologram plugin and API
    height: 0.6
  - content: '&a✔ <#b9ff28>RGB Support'
    height: 0.3
    offsetX: 1.03
  - content: '&a✔ <RAINBOW1>Rainbow Text</RAINBOW>'
    height: 0.3
    offsetX: 1.01
  - content: '&a✔ <ANIM:wave:&b,&f>Custom Animations</ANIM>'
    height: 0.3
    offsetX: 0.71
  - content: '&a✔ &bMany Useful Commands'
    height: 0.3
    offsetX: 0.42
  - content: '&a✔ &bPlaceholderAPI Support'
    height: 0.3
    offsetX: 0.33
  - content: '&a✔ &bCustomizable height for each line'
    height: 0.3
    offsetX: -0.26
  - content: '&a✔ &bCustomizable Offsets for each line'
    height: 0.3
    offsetX: -0.36
  - content: '&a✔ &bPermissions for Holograms & Lines'
    height: 0.3
    offsetX: -0.33
  - content: '&a✔ &bDisplay & Update distance'
    height: 0.3
    offsetX: 0.23
  - content: '&a✔ &bDamage Display Feature'
    height: 0.3
    offsetX: 0.36
  - content: '&a✔ &bHeal Display Feature'
    height: 0.6
    offsetX: 0.57
  - content: <#B22AFE><- Left Click | Right Click -></#ff8157>
    height: 0.3
  actions:
    RIGHT:
    - NEXT_PAGE
- lines:
  - content: <#B22AFE>&l&nDECENT HOLOGRAMS</#ff8157>
    height: 0.3
  - content: '&fMultiple different line types'
    height: 0.4
  - content: '#ENTITY: GUARDIAN'
    height: 1.5
  - content: '#HEAD: PLAYER_HEAD (d0by)'
    height: 0.75
  - content: '#SMALLHEAD: PLAYER_HEAD (Venty_)'
    height: 0.6
  - content: '#ICON: GRASS_BLOCK'
    height: 0.6
  - content: <#B22AFE><- Left Click | Right Click -></#ff8157>
    height: 0.3
  actions:
    LEFT:
    - PREV_PAGE
    RIGHT:
    - NEXT_PAGE
- lines:
  - content: <#B22AFE>&l&nDECENT HOLOGRAMS</#ff8157>
    height: 0.3
  - content: '&fAnimations? I guess..'
    height: 0.6
  - content: '<#ANIM:wave:&f,&b&n>Wave Animation</#ANIM>'
    height: 0.3
  - content: '<#ANIM:burn:&f,&b&n>Burn Animation</#ANIM>'
    height: 0.3
  - content: '<#ANIM:typewriter>Typewriter Animation</#ANIM>'
    height: 0.3
  - content: '<#ANIM:scroll>Scroll Animation</#ANIM>'
    height: 0.3
  - content: '&uColors Animation'
    height: 0.3
  - content: '<#ANIM:example>Custom Animation</#ANIM>'
    height: 0.6
  - content: <#B22AFE><- Left Click | Right Click -></#ff8157>
    height: 0.3
  actions:
    LEFT:
    - PREV_PAGE
```
{% endcode %}
