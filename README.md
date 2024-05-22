# Decent Holograms
[![SpigotMC Downloads](https://img.shields.io/spiget/downloads/96927?label=Downloads) 
![SpigotMC Version](https://img.shields.io/spiget/version/96927?label=Release) 
![Tested Versions](https://img.shields.io/spiget/tested-versions/96927?label=Supports)](https://www.spigotmc.org/resources/96927/) 
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)
>A lightweight yet very powerful hologram plugin with many features and configuration options.

**Links:**
- [Modrinth (Download)](https://modrinth.com/plugin/decentholograms)
- [SpigotMC (Download)](https://www.spigotmc.org/resources/96927/)
- [Discord (Support)](https://discord.decentsoftware.eu/)
- [Wiki (Documentation)](https://wiki.decentholograms.eu/)

## Support
We are mostly active on Discord so the best way to get support is joining our [Discord Server](https://discord.decentsoftware.eu). Also, it is okay to report bugs here on GitHub or in the 'Discussion' page on the [Spigot Page](https://decentholograms.eu) of Decent Holograms.

## Minecraft Limitations
- Text is always facing the player.
- Text size or font cannot be changed.
- Some entities make sounds. It only applies to a few entities like the Warden which makes this heartbeat sound.
- Icons (#ICON:) are always going to rotate and bob up and down.

## Contributing [![PR's Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat)](http://makeapullrequest.com)
Pull requests are welcome. But for major changes, please create an issue to discuss the changes first.

## Building
Building DecentHolograms is very simple. All you need is JDK 8+, Maven, Git and an IDE or Command Line.

1. Clone the project to your machine using Git.
2. Open the project using your IDE or open a command line at the projects' location.
3. Run `mvn clean package` and DecentHolograms will build.
4. You can find the jar at `./decent-holograms-plugin/target/DecentHolograms-VERSION.jar`

## API [![](https://jitpack.io/v/decentsoftware-eu/decentholograms.svg)](https://jitpack.io/#decentsoftware-eu/decentholograms)
How to get DecentHolograms API into your project:

> Replace `VERSION` with the current version of DecentHolograms. (Latest release)

<details>
<summary>Maven</summary>

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependencies>
    <dependency>
        <groupId>com.github.decentsoftware-eu</groupId>
        <artifactId>decentholograms</artifactId>
        <version>VERSION</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```
</details>

<details>
<summary>Gradle</summary>

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.decentsoftware-eu:decentholograms:VERSION'
}
```
</details>

## bStats
[![](https://bstats.org/signatures/bukkit/DecentHolograms.svg)](https://bstats.org/plugin/bukkit/DecentHolograms)
