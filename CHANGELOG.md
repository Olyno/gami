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
 * ``GameTimerMessage``: A ``GameMessage`` showed when a game starts.
   * Get  a ``Integer``, ``GameMessageTarget`` and a ``String`` in the constructor.
 * ``GameManager``, ``Game`` and ``Team``: Messages format changed. Please have a look in ``GameMessage`` model.
   * Added a ``getMessages(GameMessageTarget)`` method: Returns a filtered list of message by ``GameMessageTarget``.
   * Added a ``getMessages()`` method: Returns the list of message of the ``Game`` or the ``Team``.
 * ``Point``: Renamed ``target`` to author. This is not a target who scores a point, but an author, so a player.
 * ``GameMessageTarget``: Take a ``Predicate`` object in second parameter. It's used to make a filter system for messages.

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