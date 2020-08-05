# Gami changelog

## 0.1.1

**\/!\\ Breaking changes \/!\\**

 * Messages api has changed completely. Please have a look in the new one.
 * Interfaces are now called "enum", that makes more sense.
 * Games and Teams accept custom types using generics

### Enums

 * ``GameMessageType``: What a message is for.
 * ``GameMessageAs``: How should the message be displayed.
 * ``FileFormat``: List of formats to save a game and load it later

### Models

 * ``Gami``:
   * Added a ``loadGame(Path)`` method: Load a game from a file.
   * Added a ``loadGames(List<Path>)`` method: Load multiple games from multiple files.
 * ``GameMessage``: A message showed to a game or a team.
   * Get a ``GameMessageTarget`` and a ``String`` in the constructor.
 * ``GameTimerMessage``: A ``GameMessage`` showed when a game starts.
   * Get  a ``Integer``, ``GameMessageTarget`` and a ``String`` in the constructor.
 * ``GameManager``, ``Game`` and ``Team``: Messages format changed. Please have a look in ``GameMessage`` model.
   * Added a ``getMessages(GameMessageTarget)`` method: Returns a filtered list of ``GameMessage`` by ``GameMessageTarget``.
   * Added a ``getMessages()`` method: Returns the list of ``GameMessage`` of the ``Game`` or the ``Team``.
 * ``Game``:
   * Added a ``removeTeam(Team)`` method: Remove a team from a game.
   * Added a ``setTimer(Integer)`` method: Define duration of the timer, before the game starts.
   * Added a ``save(Path, FileFormat)`` method: Save a game in a file.
 * ``Team``:
   * Added a ``getGame()`` method: Returns the ``Game`` of the ``Team``. 
   * Added a ``getTotalPoints()`` method: Returns the amount of points as ``Integer``.
 * ``Point``: Renamed ``target`` to author. This is not a target who scores a point, but an author, so a player.
 * ``GameMessageTarget``: Take a ``Predicate`` object in second parameter. It's used to make a filter system for messages.

### Listeners

 * ``GameListener``:
   * Added ``onGameSaved`` event
   * Added ``onGameLoaded`` event
   * Added ``onTeamAdded`` event
   * Added ``OnTeamRemoved`` event

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