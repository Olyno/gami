# Gami changelog

## 0.1.1

**\/!\\ Breaking changes \/!\\**

 * Messages api has changed completely. Please have a look in the new one.
 * Interfaces are now called "enum", that makes more sense.

### Enums

 * ``GameMessageType``: What a message is for.
 * ``GameMessageAs``: How should the message be displayed.

### Models

 * ``GameMessage``: A message showed to a game or a team.
   * Get a ``GameMessageTarget`` and a ``String`` in the constructor.
 * ``GameTimerMessage``: A message showed when a game starts.
 * ``GameManager``, ``Game`` and ``Team``: Messages format changed. Please have a look in ``GameMessage`` model.
 * ``Point``: Renamed ``target`` to author. This is not a target who scores a point, but an author, so a player.

## 0.1.0

### Interfaces

 * ``GameMessageTarget``: Who is supposed to get the message in game
   * ``GameMessageTarget.GLOBAL``: All players in the game or team of player
   * ``GameMessageTarget.PLAYER``: Only the player
 * ``GameState``: State of the game
   * ``GameState.WAITING``: Waiting players, can't start the game
   * ``GameState.START``: Game starting
   * ``GameState.STARTED``: Game started, players are in game
   * ``GameState.ENDED``: Game stopped or finished

### Models

 * ``Game``: Game stuff
   * See documentation
 * ``Team``: Team stuff
   * See documentation
 * ``GameManager``: Including all game and team common stuff
   * See documentation
 * ``Point``: A point object containing a lot of informations as author of the point.
   * See documentation

### Listeners
 * ``GameListener``: All game related events like when a player join game.
 * ``TeamListener``: All team related events like when a player join team.