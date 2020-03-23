# Gami

Gami is a java librairy to create games easily.

 * ⚡ Fast: Gami try to offer the best experience with the best speed available
 * 💪 Flexbile: Works with all stuff you use, as simple java game or Bukkit/spigot plugin
 * 🧠 Human logic: All syntaxes are easy to understand and to use

## Usage

```java
public class YourProject {

    public void main(String[] args) {
        Game myFirstGame = new Game("myFirstGame");
        Team redTeam = new Team("red", myFirstGame);
        Team blueTeam = new Team("blue", myFirstGame);
        myFirstGame.start();
    }

}
```

## Getting started

First, you need to add the JitPack repository at the END of all your repositories. Gami is not available in Maven Central.

```groovy
repositories {
    jcenter()
    ...
    maven {
        url 'https://jitpack.io'
    }
}
```

or if you use Maven

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then you will also need to add Gami as a dependency.

```groovy
dependencies {
    implementation 'com.github.Olyno:Gami:[versionTag]'
}
```

or if you use Maven

```
<dependency>
    <groupId>com.github.Olyno</groupId>
    <artifactId>Gami</artifactId>
    <version>[versionTag]</version>
</dependency>
```

## Documentation

All documentation is available [here](https://olyno.github.io/gami).

## Contributing

There are many ways to contribute to Gami, and many of them do not involve writing any code. Here's a few ideas to get started:

 * Simply start using Gami. Go through the [Getting Started](https://github.com/Olyno/gami/blob/master/README.md#getting-started). Does everything work as expected? If not, we're always looking for improvements. Let us know by [opening an issue](https://github.com/Olyno/gami/issues).
 * If you find an issue you would like to fix, [open a pull request](https://github.com/Olyno/gami/pulls).

*Contributing text based on [Svelte's contribution guideline](https://github.com/sveltejs/svelte/blob/master/CONTRIBUTING.md)*

## License

Code released under GNU GPLv3 license.

Copyright ©, [Olyno](https://github.com/Olyno).