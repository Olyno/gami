# Gami changelog

## 0.2.0

**\/!\\ Breaking changes \/!\\**

 * Messages api has changed completely. Please have a look in the new one.
 * Interfaces are now called "enum", that makes more sense.
 * Games and Teams methods accept custom types using generics
 * Adding a player to a game or a team will automatically place him as spectator if the game or the team is full.
 * Changed wildcard to ``Object`` type for ``spectators`` and ``players`` properties.
 * Replaced all ``LinkedList`` to ``ArrayList`` to improve global speed.
 * The ``games`` ``HashMap`` is replaced with a simple ``ArrayList`` due to memory performances and useless ``Hashmap`` use.
 * The ``getTeam(String)`` method has been replaced with ``getTeamByName(String)``.

### Enums

 * ``GameMessageType``: What a message is for.
 * ``GameMessageAs``: How should the message be displayed.
 * ``FileFormat``: List of formats to save a game and load it later

### Models

 * ``Gami``:
   * Added a ``loadGame(Path)`` method: Load a game from a file.
   * Added a ``loadGames(List<Path>)`` method: Load multiple games from multiple files.
   * Added a ``getGamesByFilter(Predicate<? super Game>)`` method: Returns a list of games dependening of a filter.
   * Added a ``getGameByFilter(Predicate<? super Game>)`` method: Returns the first game which match with the filter.
   * Added a ``getGameByName(String)`` method: Returns the first game found with the target name.
 * ``GameMessage``: A message showed to a game or a team.
   * Get a ``GameMessageTarget`` and a ``String`` in the constructor.
 * ``GameTimerMessage``: A ``GameMessage`` showed when a game starts.
   * Get  a ``Integer``, ``GameMessageTarget`` and a ``String`` in the constructor.
 * ``GameManager``, ``Game`` and ``Team``: Messages format changed. Please have a look in ``GameMessage`` model.
   * Added a ``getMessages(GameMessageTarget)`` method: Returns a filtered list of ``GameMessage`` by ``GameMessageTarget``.
   * Added a ``getMessages()`` method: Returns the list of ``GameMessage`` of the ``Game`` or the ``Team``.
   * Added a ``getMaxSpectator()`` method: Returns the maximum of spectator in the ``Game`` or the ``Team``.
   * Added a ``setMaxSpectator(Integer)`` method: Set the maximum of spectator in the ``Game`` or the ``Team``.
 * ``Game``:
   * Added a ``addSpectator(Player)`` method: Add a spectator to the game.
   * Added a ``removeSpectator(Player)`` method: Remove a spectator from the game.
   * Added a ``removeTeam(Team)`` method: Remove a team from a game.
   * Added a ``setTimer(Integer)`` method: Define duration of the timer, before the game starts.
   * Added a ``save(Path, FileFormat)`` and ``save(String, FileFormat)`` methods: Save a game in a file.
   * Added a ``createSession()`` method: Create a copy of this game.
   * Added a ``deleteSession(String)`` method: Delete a game session from its id.
   * Added a ``getSession(String)`` method: Get a game session from its id.
   * Added a ``getSessions()`` method: Get all game sessions.
   * Added a ``getTeamsByFilter(Predicate<? super Team> filter)`` method: Returns a list of teams dependening of a filter.
   * Added a ``getTeamByFilter(Predicate<? super Team> filter)`` method: Returns the first team which match with the filter.
   * Added a ``isSession()`` method: Returns a boolean depending if the game is a session or not.
   * Added a ``id`` field: Private field which is the position of the ``Game`` inside the ``games`` list.
 * ``Team``:
   * Added a ``getGame()`` method: Returns the ``Game`` of the ``Team``. 
   * Added a ``getTotalPoints()`` method: Returns the amount of points as ``Integer``.
 * ``Point``: Renamed ``target`` to author. This is not a target who scores a point, but an author, so a player.
 * ``GameMessageTarget``: Take a ``Predicate`` object in second parameter. It's used to make a filter system for messages.
 * ``Point``, ``Team`` and ``Game``:
   * Added ``equals`` and ``hashCode`` methods to be able to compare objects.

### Listeners

 * ``GameListener``:
   * Added ``onSessionCreated`` event
   * Added ``onSessionDeleted`` event
   * Added ``onGameSaved`` event
   * Added ``onGameLoaded`` event
   * Added ``onTeamAdded`` event
   * Added ``OnTeamRemoved`` event

### Fix

  * ``GameManager``:
    * Fix ``getMessages`` methods: Returns an ``ArrayList`` by default.
    * Fix ``name`` value: Replace all spaces with underscores.
    * Fix ``spectators`` methods: Used ``players`` property instead of ``spectators``.
    * Fix array list comparaison by overriding the ``equals`` method.

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