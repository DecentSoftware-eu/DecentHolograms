
# Decent Holograms
A lightweight yet very powerful hologram plugin with many features and configuration options.

- **SpigotMC:** https://www.spigotmc.org/resources/96927/
- **Discord:** https://discord.decentsoftware.eu/
- **Wiki:** https://wiki.decentholograms.eu/

## Support
We are mostly active on Discord so the best way to get support is joining our [Discord Server](https://discord.decentsoftware.eu). Also, it is okay to report bugs here on GitHub or in the 'Discussion' page on the [Spigot Page](https://decentholograms.eu) of Decent Holograms.

## Minecraft Limitations

- Text is always facing the player.
- Text size or font cannot be changed.
- Some entities make sounds. It only applies to a few entities like the Warden which makes this heartbeat sound.
- Icons (#ICON:) are always going to rotate and bob up and down.

## Known Bugs

- Visbility is automatically updated for all players making it impossible to hide holograms for certain players. Only way to avoid this currently is by configuring a permission to see the hologram (More on wiki). This is going to be improved in the version 3.0.0.

## Contributing

Pull requests are welcome. But for major changes, please create an issue to discuss the changes first.

## API [![](https://jitpack.io/v/decentsoftware-eu/decentholograms.svg)](https://jitpack.io/#decentsoftware-eu/decentholograms)
How to get DecentHolograms API into your project:

Replace TAG with the current version of DecentHolograms. (Latest release)

### Gradle:
```java
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.decentsoftware-eu:decentholograms:TAG'
}
```

### Maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.decentsoftware-eu</groupId>
        <artifactId>decentholograms</artifactId>
        <version>TAG</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

## bStats
[![](https://bstats.org/signatures/bukkit/DecentHolograms.svg)](https://bstats.org/plugin/bukkit/DecentHolograms)
