package library.project.controller;

import library.project.dto.*;
import library.project.model.*;
import library.project.repository.GameRepository;
import library.project.repository.PlatformRepository;
import library.project.repository.RoleRepository;
import library.project.service.GameService;
import library.project.service.EntryService;
import library.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("project/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;
    private final EntryService entryService;
    private final UserService userService;
    private final GameRepository gameRepository;
    private final RoleRepository roleRepository;
    private final PlatformRepository platformRepository;

    @GetMapping
    public ResponseEntity<String> info(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok("""
                    As user you can:
                        > /all              (GET)
                        > /myGameList       (GET)
                        > /addGameEntry     (POST + GameEntryRequest without Id in body)
                        > /removeGameEntry  (POST + Only Id from GameEntryRequest in body)
                        > /newGameRequest   (POST + NewGameRequest without Id in body)
                    As admin you can:
                        > /admin/all                    (GET)
                        > /admin/allEntries             (GET)
                        > /admin/addNewGame             (POST + NewGameRequest without Id in body)
                        > /admin/requests               (GET)
                        > /admin/approveRequest/{id}    (POST)
                        > /admin/editGame               (POST + NewGameRequest)
                        > /admin/deleteGame             (DELETE + Only Id from NewGameRequest in body)
                        > /admin/platform               (POST + PlatformRequest in body)
                        > /admin/platform               (DELETE + PlatformRequest in body)
                    """);
    }

    @GetMapping("/all")
    public List<Game> getAllVisibleGames(@AuthenticationPrincipal UserDetails userDetails) {
        return gameService.findAllVisible();
    }

    @GetMapping("/myGameList")
    public List<GameEntry> getAllGameEntriesForUser(@AuthenticationPrincipal UserDetails userDetails) {
        return entryService.findAllGameEntryByUserId(userService.findByLogin(userDetails.getUsername()).get().getId());
    }

    @PostMapping("/addGameEntry")
    public ResponseEntity<GameEntry> addNewGameEntry(
            @RequestBody GameEntryRequest gameEntryRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            GameEntry gameEntry = GameEntry.builder()
                    .id(UUID.randomUUID().toString())
                    .game(gameService.findById(gameEntryRequest.getGameId()).orElseThrow())
                    .user(userService.findByLogin(userDetails.getUsername()).orElseThrow())
                    .hours(gameEntryRequest.getHours())
                    .rating(gameEntryRequest.getRating())
                    .review(gameEntryRequest.getReview())
                    .visible(true)
                    .build();
            entryService.saveGE(gameEntry);
            return ResponseEntity.status(HttpStatus.CREATED).body(gameEntry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/removeGameEntry")
    public ResponseEntity<GameEntry> removeGameEntry(
            @RequestBody RemoveEntryRequest removeEntryRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<GameEntry> gameEntry = entryService.findByGameEntryId(removeEntryRequest.getEntryId());
        // check if the entry exists
        if (gameEntry.isEmpty()) return ResponseEntity.notFound().build();
        // check if the entry is his
        if (gameEntry.get().getUser().getLogin().equals(userDetails.getUsername())) {
            gameEntry.get().setVisible(false);
            entryService.saveGE(gameEntry.get());
            return ResponseEntity.ok(gameEntry.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/newGameRequest")
    public ResponseEntity<Game> newGameRequest(
            @RequestBody NewGameRequest newGameRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            Game game = Game.builder()
                    .id(UUID.randomUUID().toString())
                    .title(newGameRequest.getTitle())
                    .developer(newGameRequest.getDeveloper())
                    .publisher(newGameRequest.getPublisher())
                    .visible(false)
                    .build();
            gameService.save(game);
            return ResponseEntity.status(HttpStatus.CREATED).body(game);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //////////////// Admin actions /////////////////

    @GetMapping("/admin/all")
    public List<Game> getAllGames(@AuthenticationPrincipal UserDetails userDetails) {
        return gameService.findAll();
    }

    @GetMapping("/admin/allEntries")
    public List<GameEntry> getAllGameEntries(@AuthenticationPrincipal UserDetails userDetails) {
        return entryService.findAllGE();
    }

    // add a new game
    @PostMapping("/admin/addNewGame")
    public ResponseEntity<Game> addNewGame(
            @RequestBody NewGameRequest newGameRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            Game game = Game.builder()
                    .id(UUID.randomUUID().toString())
                    .title(newGameRequest.getTitle())
                    .developer(newGameRequest.getDeveloper())
                    .publisher(newGameRequest.getPublisher())
                    .visible(true)
                    .build();
            gameService.save(game);
            return ResponseEntity.status(HttpStatus.CREATED).body(game);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // see requests for games / removed games
    @GetMapping("/admin/requests")
    public List<Game> getAllGameRequests(@AuthenticationPrincipal UserDetails userDetails) {
        return gameService.findAllInvisible();
    }

    @PostMapping("/admin/approveRequest/{id}")
    public ResponseEntity<Game> getAllGamesWithNewOne(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<Game> game = gameService.findById(id);
        if (game.isPresent()) {
            game.get().setVisible(true);
            gameService.save(game.get());
            return ResponseEntity.ok(game.orElseThrow());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // edit already existing games
    @PostMapping("/admin/editGame")
    public ResponseEntity<Game> editGame(
            @RequestBody NewGameRequest newGameRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            Optional<Game> game =  gameService.findById(newGameRequest.getId());
            if (game.isPresent()) {
                game.get().setTitle(newGameRequest.getTitle());
                game.get().setDeveloper(newGameRequest.getDeveloper());
                game.get().setPublisher(newGameRequest.getPublisher());
                gameService.save(game.orElseThrow());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(game.orElseThrow());
            }
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/admin/deleteGame")
    public ResponseEntity<Game> deleteGame(
            @RequestBody NewGameRequest newGameRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            Optional<Game> removedGame = gameService.deleteById(newGameRequest.getId());
            return removedGame.map(game -> ResponseEntity.status(HttpStatus.ACCEPTED).body(game)).orElseGet(() -> ResponseEntity.badRequest().build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/admin/platform")
    public ResponseEntity<Set<Platform>> addPlatform(
            @RequestBody PlatformRequest req,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<Game> game = gameService.findById(req.getGameId());
        if (game.isEmpty()) {
            System.out.println("Game with that id does not exist");
            return ResponseEntity.notFound().build();
        }

        Optional<Platform> platformToAdd = platformRepository.findByName(req.getPlatformName());
        if (platformToAdd.isEmpty()){
            System.out.println("Specified platform does not exist");
            return ResponseEntity.notFound().build();
        }

        if (game.get().getPlatforms().contains(platformToAdd.get())) {
            System.out.println("User already has specified platform");
            return ResponseEntity.accepted().build();
        }

        Set<Platform> platforms = game.get().getPlatforms();
        platforms.add(platformToAdd.get());
        game.get().setPlatforms(platforms);
        return ResponseEntity.ok(gameService.save(game.get()).getPlatforms());
    }

    @DeleteMapping("/admin/platform")
    public ResponseEntity<Set<Platform>> removePlatform(
            @RequestBody PlatformRequest req,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<Game> game = gameService.findById(req.getGameId());
        if (game.isEmpty()) {
            System.out.println("Game with that id does not exist");
            return ResponseEntity.notFound().build();
        }

        Optional<Platform> platformToRemove = platformRepository.findByName(req.getPlatformName());
        if (platformToRemove.isEmpty()){
            System.out.println("Specified platform does not exist");
            return ResponseEntity.notFound().build();
        }

        if (!game.get().getPlatforms().contains(platformToRemove.get())) {
            System.out.println("Game does not have specified platform");
            return ResponseEntity.notFound().build();
        }

        Set<Platform> newGamePlatforms = new HashSet<>(Collections.emptySet());
        for (Platform platform : game.get().getPlatforms()) {
            if (!platform.equals(platformToRemove.get())) newGamePlatforms.add(platform);
        }
        game.get().setPlatforms(newGamePlatforms);
        return ResponseEntity.ok(gameService.save(game.get()).getPlatforms());
    }
}
