import java.util.*;

public class AdventureGame {
    static String currentRoom = "Entrance";
    static List<String> inventory = new ArrayList<>();
    static int playerHealth = 100;
    static boolean treasureCollected = false;

    static Map<String, Map<String, String>> navigation = new HashMap<>() {{
        put("Entrance", Map.of("north", "Forest", "east", "Dungeon"));
        put("Forest", Map.of("south", "Entrance"));
        put("Dungeon", Map.of("west", "Entrance", "east", "Treasure Room"));
        put("Treasure Room", Map.of("west", "Dungeon"));
    }};
    static Map<String, String> roomDescriptions = new HashMap<>() {{
        put("Entrance", "You are at the entrance of a mysterious adventure. Paths lead to the Forest and Dungeon.");
        put("Forest", "A tranquil forest. A friendly NPC is here to help.");
        put("Dungeon", "A dark and eerie dungeon. Beware of enemies!");
        put("Treasure Room", "The final room! The treasure is here.");
    }};
    static Map<String, String> roomItems = new HashMap<>() {{
        put("Forest", "Potion");
        put("Treasure Room", "Treasure");
    }};

    static void showRoomDescription() {
        System.out.println("\n========================");
        System.out.println(roomDescriptions.get(currentRoom));
        System.out.println("Available directions: " + navigation.get(currentRoom).keySet());
        System.out.println("========================");
        displayMap();
    }

    static void displayMap() {
        System.out.println("\nMap:");
        System.out.println("                [Forest]");
        System.out.println("                   |");
        System.out.println("[Entrance] --- [Dungeon] --- [Treasure Room]");
        System.out.println("\nYou are currently at: [" + currentRoom + "]");
    }

    static void move(String direction) {
        direction = direction.trim().toLowerCase();
        if (navigation.get(currentRoom).containsKey(direction)) {
            currentRoom = navigation.get(currentRoom).get(direction);
            System.out.println("\nYou moved to: " + currentRoom);
            showRoomDescription();
        } else {
            System.out.println("You can't go that way!");
        }
    }

    static void checkInventory() {
        System.out.println("Inventory: " + (inventory.isEmpty() ? "Empty" : inventory));
    }

    static void interact() {
        if (currentRoom.equals("Forest")) {
            System.out.println("You meet a friendly NPC. They give you a hint: 'Beware of the Dungeon!'");
            if (!inventory.contains("Potion")) {
                System.out.println("The NPC gives you a Potion.");
                inventory.add("Potion");
            }
        } else {
            System.out.println("There's no one to talk to here.");
        }
    }

    static void combat() {
        System.out.println("An enemy attacks you!");
        int enemyHealth = 50;
        Scanner scanner = new Scanner(System.in);

        while (playerHealth > 0 && enemyHealth > 0) {
            System.out.println("Your Health: " + playerHealth + ", Enemy Health: " + enemyHealth);
            System.out.println("Do you want to 'attack' or 'run'?");
            String action = scanner.nextLine().toLowerCase().trim();

            if (action.equals("attack")) {
                int damage = (int) (Math.random() * 20) + 10;
                System.out.println("You hit the enemy for " + damage + " damage!");
                enemyHealth -= damage;
                if (enemyHealth <= 0) {
                    System.out.println("You defeated the enemy!");
                    return;
                }
                damage = (int) (Math.random() * 15) + 5;
                System.out.println("The enemy hits you for " + damage + " damage!");
                playerHealth -= damage;
            } else if (action.equals("run")) {
                System.out.println("You ran back to the Entrance!");
                currentRoom = "Entrance";
                showRoomDescription();
                return;
            } else {
                System.out.println("Invalid action. Type 'attack' or 'run'.");
            }
        }

        if (playerHealth <= 0) {
            System.out.println("You were defeated. Game Over!");
            System.exit(0);
        }
    }

    static void collectItem() {
        if (roomItems.containsKey(currentRoom)) {
            String item = roomItems.get(currentRoom);
            if (!inventory.contains(item)) {
                System.out.println("You found a " + item + "!");
                inventory.add(item);
                if (item.equals("Treasure")) {
                    treasureCollected = true;
                    System.out.println("You collected the treasure! You Win!");
                    System.exit(0);
                }
            } else {
                System.out.println("You've already collected the item here.");
            }
        } else {
            System.out.println("There's nothing to collect here.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Adventure Game!");
        showRoomDescription();

        while (true) {
            System.out.println("\nWhat would you like to do? (go [direction], check inventory, talk, attack, collect)");
            String input = scanner.nextLine().toLowerCase().trim();
            String[] command = input.split(" ", 2);

            switch (command[0]) {
                case "go":
                    if (command.length > 1) move(command[1]);
                    else System.out.println("Specify a direction to go to.");
                    break;
                case "check":
                    if (input.equals("check inventory")) checkInventory();
                    else System.out.println("Invalid command.");
                    break;
                case "talk":
                    interact();
                    break;
                case "attack":
                    if (currentRoom.equals("Dungeon")) combat();
                    else System.out.println("There's nothing to attack here.");
                    break;
                case "collect":
                    collectItem();
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }
}
