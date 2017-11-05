package Model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class GameManager{
	private Scanner input = new Scanner(System.in);
	private String gameFolder = "";
	private String[] itemCode = {"Item Name:","Item ID:","Item Description:",
			"Item Type:","Item Action Value:","Item Usuage Times:"};
	private String[] monsterCode = {"Monster Name:","Monster ID:","Monster Description:",
			"Monster Health:","Monster Damage:","Monster Hit Percentage:"};
	private String[] roomCode = {"Room Floor:","Room ID:","Room Description:",
			"Room Connection:","Room Access:","Room Item:","Room Monster:"};
	private HashMap<String, Items> itemList = new HashMap<String, Items>();
	private HashMap<String, Monsters> monsterList = new HashMap<String, Monsters>();
	private HashMap<String, Rooms> roomList = new HashMap<String, Rooms>();
	private Players player;

	//Get User Input
	public String getUserInput() {
		System.out.print("\nInput:\n> ");
		return input.nextLine();
	}

	//Get an ArrayList of all the game in the game folder
	public ArrayList<String> getGameFolderList(){
		try {
			ArrayList<String> gameList = new ArrayList<String>();
			File folderPath = new File("./res/Game Folder/");
			File[] files = folderPath.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					gameList.add(file.getName());
				}
			}
			return gameList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//Set the game folder to point where to get all the asset from
	public void setGameFolder() {
		ArrayList<String> gameList = getGameFolderList();
		System.out.println("Choose the game folder that you want to use:");	

		for(String gamefolder : gameList) {
			System.out.println(gamefolder);
		}

		String userFolderChoice = getUserInput();
		boolean vaildChoice = false;

		for(String folder : gameList) {
			search:
				if(userFolderChoice.equalsIgnoreCase(folder)) {
					gameFolder = "./res/Game Folder/" + folder + "/";
					vaildChoice = true;
					break search;
				}
		}

		if(vaildChoice == false) {
			System.out.println("Invalid Game Folder");
			setGameFolder();
		}
	}

	//Make object list of all subfolder
	public void makeListObject(String gameFolder, String gameSubFolder) {
		String folderPath = gameFolder + gameSubFolder;
		try {	
			File folder = new File(folderPath);
			File[] listOfFiles = folder.listFiles();
			String fileName = null;
			for (File file : listOfFiles) {
				if (file.isFile()) {
					fileName = file.getName();
					folderPath = gameFolder + gameSubFolder + fileName;
					makeGameObject(folderPath, gameSubFolder);
				}
			}
		}
		catch(Exception e) {
			System.out.println("Error in "+ gameSubFolder + ":/n" + e.toString());
		}	
	}

	//Make Object
	public void makeGameObject(String filePath, String object) {
		try {
			//File Reader
			BufferedReader bufferedReader = 
					new BufferedReader(new FileReader(filePath));

			String fileLine = null;
			String setCode = null;

			Items itemObject = new Items();
			Monsters monsterObject = new Monsters();
			Rooms roomObject = new Rooms();

			while((fileLine = bufferedReader.readLine()) != null) {
				search:
					for(String itemSetCode: itemCode) {
						if(itemSetCode.equalsIgnoreCase(fileLine)) {
							setCode = itemSetCode;
							fileLine = bufferedReader.readLine();
							break search;
						}
					}
			search2:
				for(String monsterSetCode: monsterCode) {
					if(monsterSetCode.equalsIgnoreCase(fileLine)) {
						setCode = monsterSetCode;
						fileLine = bufferedReader.readLine();
						break search2;
					}
				}
					search3:
						for(String roomSetCode: roomCode) {
							if(roomSetCode.equalsIgnoreCase(fileLine)) {
								setCode = roomSetCode;
								fileLine = bufferedReader.readLine();
								break search3;
							}
						}

				if(setCode != null) {
					//Depending on the set code, it'll set the information it got from flieLine
					switch(setCode) {
					case "Item Name:":
						itemObject.setItemName(fileLine);
						break;
					case "Item ID:":
						itemObject.setItemId(fileLine);
						break;
					case "Item Description:":
						itemObject.setItemDesc(fileLine);
						break;
					case "Item Type:":
						itemObject.setItemType(fileLine);
						break;	
					case "Item Action Value:":
						itemObject.setItemActionValue(fileLine);
						break;	
					case "Item Usage Times:":
						itemObject.setItemUsageTime(Integer.parseInt(fileLine));
						break;	
					case "Monster Name:":
						monsterObject.setMonsterName(fileLine);
						break;
					case "Monster ID:":
						monsterObject.setMonsterId(fileLine);
						break;
					case "Monster Description:":
						monsterObject.setMonsterDesc(fileLine);
						break;
					case "Monster Health:":
						monsterObject.setMonsterHealth(Integer.parseInt(fileLine));
						break;	
					case "Monster Damage:":
						monsterObject.setMonsterDamage(Integer.parseInt(fileLine));
						break;	
					case "Monster Hit Percentage:":
						monsterObject.setMonsterHitPercentage(Double.parseDouble(fileLine));
						break;	
					case "Room Floor:":
						roomObject.setRoomFloor(fileLine);
						break;
					case "Room ID:":
						roomObject.setRoomId(fileLine);
						break;
					case "Room Description:":
						roomObject.setRoomDescription(fileLine);
						break;
					case "Room Connection:":
						String[] RoomConnection = fileLine.split(":");
						String Direction = RoomConnection[1];
						String roomID = RoomConnection[0];	
						roomObject.setRoomConnection(Direction, roomID);
						break;
					case "Room Access:":
						boolean roomAccess = false;
						if(fileLine.equalsIgnoreCase("True"))
							roomAccess = true;
						else if(fileLine.equalsIgnoreCase("False"))
							roomAccess = false;
						else 
							System.out.println("Error setting room access at " + filePath);

						roomObject.setRoomAccess(roomAccess);
						break;
					case "Room Item:":
						if(!fileLine.equals("null")) {
							roomObject.addRoomItemId(fileLine);
							break;
						}
					case "Room Monster:":
						if(!fileLine.equals("null")) {
							roomObject.addRoomMonsterId(fileLine);
							break;
						}
					}	
				}
			}

			if(object.equals("Item/"))
				itemList.put(itemObject.getItemId(), itemObject);
			else if(object.equals("Monster/"))
				monsterList.put(monsterObject.getMonsterId(), monsterObject);
			else if(object.equals("Room/"))
				roomList.put(roomObject.getRoomId(), roomObject);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void newGame() {
		setGameFolder();
		makeListObject(gameFolder,"Item/");
		makeListObject(gameFolder,"Monster/");
		makeListObject(gameFolder,"Room/");

		System.out.println("Please choose your name:");
		String playerName = getUserInput();
		player = new Players(playerName,100,itemList.get("I01"),0,roomList.get("R01"));
		//Debug Save later
		//saveGame("S1",player);

		System.out.println("New Game Created");
		System.out.println("\nPrivate your mission is to investigate this building and clear out all enemy");
		System.out.println("You jumped out of the plane and landed on the roof");
		System.out.println(player.getCurrentRoom().toString());
		action();
	}

	public void action() {
		if(player.getCurrentRoom().getRoomMonsterId().isEmpty()) {
			System.out.println("\nWhat will you do?");
			System.out.println("1. Move");
			System.out.println("2. Search Room");
			System.out.println("3. Check Inventory");
			System.out.println("4. Get Room Description");

			String userInput = getUserInput();

			switch(userInput) {
			case "Move":  case  "1":
				System.out.print("\nWhat direction you wanna move?\n");
				System.out.println(player.getCurrentRoom().getRoomConnection());
				String roomDirection = getUserInput();
				if(player.getCurrentRoom().getRoomNavigationList().get(roomDirection) == null) {
					System.out.println("\nInvalid Input");
					action();
					break;
				}
				else {
					player.setCurrentRoom(player.getCurrentRoom().getRoomNavigationList().get(roomDirection),roomList);
					if(player.getCurrentRoom().getRoomMonsterId().isEmpty())
						System.out.println(player.getCurrentRoom().toString());
					action();
				}
				break;
			case "Search Room":  case  "2":
				if(!player.getCurrentRoom().getRoomItemId().isEmpty()) {

					for(Iterator<String> iterator = player.getCurrentRoom().getRoomItemId().iterator(); iterator.hasNext();) {
						String itemId = iterator.next();
						Items itemObject = itemList.get(itemId);
						itemObject = itemList.get(itemId);
						System.out.println("\nYou found a " + itemObject.getItemName());
						System.out.println("Do you want to pick it up?");
						System.out.println("1.Yes");
						System.out.println("2.No");
						String choice = getUserInput();
						switch(choice) {
						case "Yes": case "1":
							iterator.remove();
							System.out.println("\nYou added it to your inventory");
							player.addItemToInventory(itemObject);
							break;
						case "No": case "2":
							System.out.println("\nYou decided not to pick it up");
							break;
						}

					}
				}
				else if(player.getCurrentRoom().getRoomItemId().isEmpty()){
					System.out.println("\nYou search, but you found nothing in this room");
				}
				else {
					System.out.println("\nError Searching Room");
				}
				action();
				break;
			case "Check Inventory" :  case  "3":
				if(!player.getInventoryList().isEmpty()) {
					inventoryMenu();
				}
				else if(player.getInventoryList().isEmpty()){
					System.out.println("\nYou have nothing in your inventory");
				}
				else {
					System.out.println("\nError Checking Item");
				}
				action();
				break;
			case "Get Room Description" :  case  "4":
				System.out.println(player.getCurrentRoom().toString());
				action();
				break;
			default:
				System.out.println("\nWrong Command inputed");
				action();
				break;
			}


		}
		else if(!player.getCurrentRoom().getRoomMonsterId().isEmpty()){
			System.out.println("\nYou encounter a monster");
			System.out.println("You enter combat with the monster");
			System.out.println("You enter combat with the monster");
			combatMenu();
		}
		else {
			System.out.println("Error with action");
		}
	}

	public void inventoryMenu() {
		System.out.println("\nInventory:");

		for(Items item: player.getInventoryList()) {
			System.out.println(item.getItemName());			
		}

		System.out.println("\nWhat will you do?");
		System.out.println("1. Examine Item");
		System.out.println("2. Drop Item");
		System.out.println("3. Use Item");
		System.out.println("4. Equip Item");
		System.out.println("5. Quit");

		String userInput = getUserInput();
		String itemName = "";
		boolean foundItem = false;

		switch(userInput) {
		case "Examine Item" : case "1":
			System.out.println("\nWhat item do you want to examine?");
			itemName = getUserInput();
			foundItem = false;
			search:
				for(Items item:player.getInventoryList()) {
					if(itemName.equalsIgnoreCase(item.getItemName())) {
						foundItem = true;
						System.out.println(item.toString());
						inventoryMenu();
						break search;
					}
				}
			if(foundItem == false) {
				System.out.println("\nInvalid Input");
				inventoryMenu();
			}
			break;
		case "Drop Item" : case "2":
			System.out.println("\nWhat item do you want to drop?");
			itemName = getUserInput();
			foundItem = false;
			search:
				for(Iterator<Items> iterator = player.getInventoryList().iterator(); iterator.hasNext();) {
					Items itemIterator = iterator.next();
					if(itemName.equalsIgnoreCase(itemIterator.getItemName())) {
						foundItem = true;
						iterator.remove();
						player.getCurrentRoom().addRoomItemId(itemIterator.getItemId());
						System.out.println("\nYou drop your " + itemIterator.getItemName());
						if(player.getInventoryList().isEmpty()) {
							System.out.println("You have nothing left in your inventory");
							action();
						}
						else {
							inventoryMenu();
						}
						break search;
					}
				}
			if(foundItem == false) {
				System.out.println("\nInvalid Input");
				inventoryMenu();
			}
			break;
		case "Use Item" : case "3":
			break;
		case "Equip Item" : case "4":
			break;
		case "Quit" : case "5":
			break;
		default:
			System.out.println("\nWrong Command inputed");
			inventoryMenu();
			break;
		}

	}

	public void combatMenu() {
		System.out.println("\nWhat will you do?");
		System.out.println("1. Attack");
		System.out.println("2. Defend");
		System.out.println("3. Examine Monster");
		System.out.println("4. Run Away");
		
		String userInput = getUserInput();
		
		switch(userInput) {
		case "Attack" : case "1":
			break;
		case "Defend" : case "2":
			break;
		case "Run Away" : case "3":
			break;
		}
		
	}

}