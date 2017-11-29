package Controller;

import java.util.Scanner;

import Model.GameModel;
import View.GameView;

public class GameController {

	GameModel model = new GameModel();
	GameView view = new GameView();
	private Scanner input = new Scanner(System.in);
	boolean winCondition = false;

	/*Initialize the Game first with the
	 *game state "Main Menu"
	 */
	public void initializeGame() {
		model.setState("Main Menu");
	};

	/*Prints out the choice that the user can
	 *take to the view class depending on the 
	 *state of the game
	 */
	public void printChoice() {
		String output = "";

		switch(model.getState()) {
		case "New Game":
			output += "Choose the game that you want to play.\n";
			model.setGameFolderList();
			for(String gameFolder : model.getGameFolderList()) {
				output += " - " +gameFolder + "\n";
			}
			break;
		case "Main Menu":
			output += "==================================================\n";
			output += "|| || ||       EMPYREAN - Main Menu       || || ||\n";
			output += "==================================================\n";
			output += "                  1. New Game\n";
			output += "                  2. Load Game\n";
			output += "                  3. Exit Game\n";
			break;
		case "Player Creation":
			output += "Please enter your name:\n";
			break;
		case "Action Menu":
			output += ">>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<\n";
			output += ">>>>>       Action - What will you do?       <<<<<\n";
			output += ">>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<\n";
			output += "                1. Move\n";
			output += "                2. Examine Room\n";
			output += "                3. Check Inventory\n";
			output += "                4. Save Game\n";
			output += "                5. Quit Game\n";
			break;
		case "Move Player":
			output += "Which direction do you want to move? Type \"Exit\" to cancel\n";
			output += "Exits: " + model.getPlayer().getCurrentRoom().getRoomConnection()+"\n";
			break;
		case "Save Menu":
			output += "Save Data:\n";

			for(int x = 1; x <= 10;x++) {
				if(model.getSaveList().get(x) != null) {
					output += (x + ". Name: " + model.getSaveList().get(x).getPlayerData().getPlayerName()
							+ "; Time: " + model.getSaveList().get(x).getPlayerData().getGameTime()
							+ "; Room: " + model.getSaveList().get(x).getPlayerData().getCurrentRoom().getRoomId()+"\n");
				}
				else {
					output +=(x + ". Empty" +"\n");
				}
			}
			output += "Choose a save slot. Type \"Exit\" to cancel\n";
			break;
		case "Save Conflict":
			output += "A save file in slot "+ model.getSaveData().getSaveId()+ " already exists. Overwrite save data?\n";
			output += "1. Yes\n";
			output += "2. No\n";
			break;
		case "Inventory Menu":
			output += "- = PLAYER: " + model.getPlayer().getPlayerName() + " = -\n";
			output += "Health: " + model.getPlayer().getPlayerCurrentHealth() + "/" + model.getPlayer().getPlayerMaxHealth() + "\n";
			output += "Weapon: "+ model.getPlayer().getWeapon().getItemName() + "\n";
			output += "\n- = Inventory = -\n";
			output += model.getPlayer().getInventoryListString();
			output +=  "\nWhat will you do?\n";
			output +=  "1. Examine Item\n";
			output +=  "2. Drop Item\n";
			output +=  "3. Use Item\n";
			output +=  "4. Equip Item\n";
			output +=  "5. Exit\n";
			break;
		case "Select Item":
			output += ". . : : Inventory : : . .\n";
			output += model.getPlayer().getInventoryListString();
			output += "\nWhich item do you want to ";
			switch(model.getStoredState()) {
			case"Examine Item":
				output += "examine";
				break;
			case"Drop Item":
				output += "drop";
				break;
			case"Use Item":
				output += "use";
				break;
			case"Equip Item":
				output += "equip";
				break;
			}

			output += "? Type \"Exit\" to cancel.\n";
			break;
		case "Puzzle Menu":
			output += "- = PUZZLE = -\n";
			output += "The door is locked!\n";
			output += "\nWhat will you do?\n";
			output += "1. Input Number\n";
			output += "2. Use Item\n";
			output += "3. Hint\n";
			output += "4. Exit\n";
			break;
		case "Input Number":
			output += "Type in a number. Type \"Exit\" to cancel.\n";
			break;	
		case "Use Item Puzzle":
			output += "Inventory:\n";
			output += model.getPlayer().getInventoryListString();
			output += "\nWhich item do you want to use? Type \"Exit\" to cancel.\n";
			break;	
		case "Combat Menu":
			output += "- - - - -           ENCOUNTER!           - - - - -\n";
			output += "--------------------------------------------------\n";
			for(int x = 0; x < model.getPlayer().getCurrentRoom().getRoomMonster().size(); x++) {
				output += model.getPlayer().getCurrentRoom().getRoomMonster().get(x).toString() + "\n";
			}
			output += "--------------------------------------------------\n";
			output += "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n";
			output += "\\\\\\\\\\       COMBAT - What will you do?       /////\n";
			output += "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n";
			output += "                1. Attack\n";
			output += "                2. Defend\n";
			output += "                3. Examine Enemy\n";
			output += "                4. View Inventory\n";
			output += "                5. Run Away\n";
			break;
		case "Multiple Monster":
			output +="Which enemy do you want to attack? Type \"Exit\" to cancel\n";
			break;
		case "Loot Item":
			output += "You found a " + model.getLoot().getItemName() + ".\n";
			output += "Do you want to pick it up?\n\n";
			output += "1. Yes\n";
			output += "2. No\n";
			break;
		default:
			view.println("Error: Not Valid State (Method printChoice())");
			view.println(model.getState());
			System.exit(0);
			break;
		}

		view.println(output);
		output = "";
	}

	/*After the choice for the player is print out
	 *depending on the player's input, it calls the method
	 *that corresponding to that command, otherwise
	 *nothing happen and the game state reminds, which
	 *will prompt the user choice again and input
	 */
	public void readUserInput() {
		printChoice();

		view.print("Input:\n> ");
		String userInput = input.nextLine().trim();
		view.println("--------------------------------------------------");

		switch(model.getState()) {
		case "Main Menu":
			mainMenu(userInput);
			break;
		case "Action Menu":
			actionMenu(userInput);
			break;
		case "Save Menu":
			if(model.getStoredState().equalsIgnoreCase("Load Game")) {
				loadGame(userInput);
			}else {
				saveGame(userInput);
			}
			break;
		case "New Game":
			selectGameFolder(userInput);
			break;
		case "Player Creation":
			playerCreation(userInput);
			break;
		case "Move Player":
			movePlayer(userInput);
			break;
		case "Save Conflict":
			saveConflict(userInput);
			break;
		case "Inventory Menu":
			inventoryMenu(userInput);
			break;
		case "Select Item":
			checkUserItem(userInput);
			break;
		case "Puzzle Menu":
			puzzleMenu(userInput);
			break;
		case "Input Number":
			inputNumber(userInput);
			break;
		case "Use Item Puzzle":
			checkUserItem(userInput);
			break;
		case "Combat Menu":
			combatMenu(userInput);
			break;
		case "Loot Item":
			lootItem(userInput);
			break;
		case "Multiple Monster":
			attackMultipleMonster(userInput);
			break;
		default:
			view.println("Error: Not Valid State (Method readuserInput())");
			view.println(model.getState());
			System.exit(0);
		}
	}


	//-------------------------------------------------------------------------------------------------	
	/*Main Menu that set the state depending on the
	 *player input
	 */
	public void mainMenu(String userInput) {
		switch(userInput.toLowerCase()) {
		case "new game" : case "1":
			model.setState("New Game");
			break;
		case "load game" : case "2":
			model.setStoredState("Load Game");
			model.setState("Save Menu");
			model.setSaveList();
			break;
		case "exit game" : case "3":
			view.println("Exiting Game");
			System.exit(0);
			break;
		default:
			view.println("Invalid Input");
			view.println("--------------------------------------------------");
			break;
		}
	}


	/*Check the userinput, and load the gamefolder and all
	 *asset in the game folder to the game
	 *Need Input: Game Folder
	 *Related State Affliction: New Game(Originated from), PlayerCreation
	 */
	public void selectGameFolder(String userInput) {
		if(model.checkValidGameFolder(userInput)) {
			model.setGameFolder(userInput);
			model.loadGameFolder("Item");
			model.loadGameFolder("Monster");
			model.loadGameFolder("Puzzle");
			model.loadGameFolder("Room");
			model.setState("Player Creation");
		}else {
			view.println("Invalid Input");
			view.println("--------------------------------------------------");
		}
	}


	/*Tell the model to create a player class with the player name
	 *then set the state to action menu to start the game
	 *Need Input: Player Name
	 *Related State Affliction: New Game(Method: selectGameFolder), ActionMenu
	 */
	public void playerCreation(String userInput) {
		model.makeNewPlayer(userInput);
		view.println(model.getPlayer().getCurrentRoom().toString());
		model.setState("Action Menu");
	}

	//-------------------------------------------------------------------------------------------------	
	/*Action Menu that set the state depending on the
	 *player input
	 */
	public void actionMenu(String userInput) {
		switch(userInput.toLowerCase()) {
		case "move" : case "1":
			model.setState("Move Player");
			break;
		case "examine room" : case "2":
			view.println(model.getPlayer().getCurrentRoom().toString());
			if(model.getPlayer().getCurrentRoom().getRoomItem().isEmpty()) {
				view.println("You didn't find anything of interest.");
				view.println("--------------------------------------------------");
				model.setState("Action Menu");
				model.updateRoom(model.getPlayer().getCurrentRoom());
			}else {
				model.setLootList(model.getPlayer().getCurrentRoom().getRoomItem());
				model.setStoredState("Room");
				model.setState("Loot Item");
				model.updateRoom(model.getPlayer().getCurrentRoom());
			}
			break;
		case "check inventory" : case "3":
			model.setStoredState("Action");
			model.setState("Inventory Menu");
			model.updateRoom(model.getPlayer().getCurrentRoom());
			break;
		case "save game" : case "4":
			model.setState("Save Menu");
			model.setSaveList();
			break;
		case "quit game" : case "5":
			model.setState("Main Menu");
			break;
		default:
			view.println("Invalid Input");
			view.println("--------------------------------------------------");
			break;
		}
	}

	/*Move the player from one room to another, depending
	 *on the next room, it checks wether it has a monster
	 *or a puzzle. Then set the state accordingly
	 *Need Input: Direction
	 *Related State Affliction: Action Menu(Originated from), Combat Menu, Puzzle Menu
	 */
	public void movePlayer(String userInput) {
		if(userInput.equalsIgnoreCase("exit")) {
			model.setState("Action Menu");
		}else if(model.checkValidDirection(userInput)) {
			model.setNextRoom(userInput);
			if(model.getNextRoomPuzzle() != null) {
				model.setState("Puzzle Menu");
			}
			else if(model.getNextRoom().getRoomMonster().isEmpty()) {
				model.getPlayer().setCurrentRoom(model.getNextRoom());
				view.println(model.getPlayer().getCurrentRoom().toString());	
				model.setState("Action Menu");		
			}
			else{
				model.getPlayer().setCurrentRoom(model.getNextRoom());
				model.setState("Combat Menu");
			}
		}
		else {
			view.println("You can't go that way!\n");
		}
	}

	private void lootItem(String userInput) {
		switch(userInput.toLowerCase()) {
		case "yes": case "y": case "1":
			if(model.getStoredState().equalsIgnoreCase("Room")) {
				for(int x = 0; x < model.getPlayer().getCurrentRoom().getRoomItem().size(); x++) {
					if(model.getLoot().equals(model.getPlayer().getCurrentRoom().getRoomItem().get(x))) {
						model.getPlayer().getCurrentRoom().removeRoomItemId(model.getLoot());
					}
				}
			}

			model.getPlayer().addItemToInventory(model.getLoot());
			view.println("You picked up the " + model.getLoot().getItemName() + ".");
			view.println("--------------------------------------------------");
			model.removeLoot();


			if(model.getLootList().isEmpty()) {
				model.setState("Action Menu");
			}
			break;
		case "no": case "n": case "2":
			view.println("You decided to leave the " + model.getLoot().getItemName() + ".");
			view.println("--------------------------------------------------");
			model.removeLoot();
			if(model.getLootList().isEmpty()) {
				model.setState("Action Menu");
			}
			break;
		default:
			view.println("Invalid Input");
			view.println("--------------------------------------------------");
			break;
		}

	}

	/*Save the game of the player
	 *Need Input: Exit or Number of the Save Slot
	 *Related State Affliction: Action Menu(Originated from), Save Conflict
	 */
	private void saveGame(String userInput) {
		try {
			if(userInput.equalsIgnoreCase("Exit")) {
				model.setState("Action Menu");
			}
			else if(Integer.parseInt(userInput) >= 1 && Integer.parseInt(userInput) <= 10) {
				int userInputInt = Integer.parseInt(userInput);
				model.setSaveData(userInputInt);
				if(model.getSaveList().get(userInputInt) != null) {
					model.setState("Save Conflict");
				}
				else {
					view.println("Save Successful");
					view.println("--------------------------------------------------");
					model.saveGameData(model.getSaveData());
					model.setState("Action Menu");
				}
			}else {
				view.println("Invalid Input");
				view.println("--------------------------------------------------");
			}
		}catch(Exception e) {
			view.println("Invalid Input");
			view.println("--------------------------------------------------");
		}

	}

	private void loadGame(String userInput) {
		try {
			if(userInput.equalsIgnoreCase("Exit")) {
				model.setState("Main Menu");
			}
			else if(Integer.parseInt(userInput) >= 1 && Integer.parseInt(userInput) <= 10) {
				int userInputInt = Integer.parseInt(userInput);

				if(model.getSaveList().get(userInputInt) != null) {
					model.loadGameSaveData(model.getSaveList().get(userInputInt));
					view.println("Successfully loaded Slot " + userInputInt);
					view.println("--------------------------------------------------");
					model.setState("Action Menu");
				}
				else {
					view.println("No save data exists in Slot " + userInputInt);
					view.println("--------------------------------------------------");
				}
			}else {
				view.println("Invalid Input");
				view.println("--------------------------------------------------");
			}
		}catch(Exception e) {
			view.println("Invalid Input");
			view.println("--------------------------------------------------");
		}

	}

	/*If there is an already existing save file in the save slot
	 *this method is called asking for the user if they want to override
	 *Need Input: Yes or No
	 *Related State Affliction: Action Menu(Method: saveGame), Action Menu
	 */
	private void saveConflict(String userInput) {
		switch(userInput.toLowerCase()) {
		case "yes" : case "1":
			view.println("Save Successful");
			view.println("--------------------------------------------------");
			model.saveGameData(model.getSaveData());
			model.setState("Action Menu");
			break;
		case "no" : case "2":
			view.println("Overwrite cancelled");
			view.println("--------------------------------------------------");
			model.setState("Action Menu");
			break;
		default:
			view.println("Invalid Input");
			view.println("--------------------------------------------------");
			break;
		}
	}

	//-------------------------------------------------------------------------------------------------	
	/*Combat Menu, gets the user input and depending on
	 *what the user input depends on what happen
	 */
	private void combatMenu(String userInput) {
		switch(userInput.toLowerCase()) {
		case "attack" : case "1":
			attackMonster();
			model.updateRoom(model.getPlayer().getCurrentRoom());
			break;
		case "defend" : case "2":
			view.println("You block all attacks, and take no damage!");
			view.println("--------------------------------------------------");
			break;
		case "examine enemy" : case "3":
			for(int x = 0; x < model.getPlayer().getCurrentRoom().getRoomMonster().size(); x++) {
				view.println(model.getPlayer().getCurrentRoom().getRoomMonster().get(x).toString()+ "\n");
			}
			view.println("--------------------------------------------------");
			break;
		case "check Inventory": case "check": case "inventory": case "4":
			model.setState("Inventory Menu");
			model.setStoredState("Combat");
			model.updateRoom(model.getPlayer().getCurrentRoom());
			break;
		case "run away": case "run": case "5":
			runAway();
			model.updateRoom(model.getPlayer().getCurrentRoom());
			break;
		default:
			view.println("Invalid Input");
			view.println("--------------------------------------------------");
		}

	}

	/*Check the amount of monster in the room and if it's more then
	 *one then it set the state to multiple monster other wise you
	 *attack the one monster in the room. It set the current monster
	 *then check if you have ammo or not. If you don't have ammo
	 *then you the monster attacks, otherwise you attack the monster
	 *and use up your ammo, then it check if the monster is dead
	 *if there is still a monster in the room after you attack
	 *then it called a method to have the mosnter attack you
	 *Parent Method: combatMenu()
	 *Child Method: attackPlayer()
	 *Related State Affliction: Combat Menu, Action Menu, Multiple Monster, Combat Menu
	 */
	private void attackMonster() {
		if(model.getPlayer().getCurrentRoom().getRoomMonster().size() == 1) {
			model.setCurrentMonster(model.getPlayer().getCurrentRoom().getRoomMonster().get(0));

			if(checkWeaponAmmo()) {
				view.println("You don't have any ammo for your weapon!\n");
			}else {

				view.println("You fire your weapon at the " + model.getCurrentMonster().getMonsterName() + "!");
				model.getCurrentMonster().takeDmg(model.getPlayer().getWeapon().getItemActionValue());
				model.getPlayer().useWeaponAmmo(model.getPlayer().getWeapon());
				view.println("The " + model.getCurrentMonster().getMonsterName() + " took " + 
						model.getPlayer().getWeapon().getItemActionValue() + " damage!\n");

				if(checkMonsterDeath()) {
					view.println("The " + model.getCurrentMonster().getMonsterName() + " slumps over, defeated.");
					view.println("--------------------------------------------------");
					model.getPlayer().getCurrentRoom().removeRoomMonster(model.getCurrentMonster());
					model.setLootList(model.getMonsterLootList());
					if(!model.getLootList().isEmpty()) {
						for(int x = 0; x < model.getLootList().size();x++) {
							view.println("The enemy drops " + model.getLoot().getItemName() +" on the floor of the room");
							model.getPlayer().getCurrentRoom().addRoomItem(model.getLoot());
							model.removeLoot();
						}
						view.println("--------------------------------------------------");
					}
					model.decreaseMonsterAlive();

				}
			}
			if(!model.getPlayer().getCurrentRoom().getRoomMonster().isEmpty()) {
				attackPlayer();
			}else {
				model.setState("Action Menu");
				checkWinCondition();
			}
		}else {
			model.setState("Multiple Monster");
		}
	}

	/*If there is more then one monster in a room, then it gets the 
	 *user input and check if what the user entered was valid then
	 *precede to attack like normal. All monster will attack you
	 *if there is multiple monster in a room
	 *Parent Method: combatMenu()
	 *Child Method: attackPlayer()
	 *Related State Affliction: Combat Menu, Action Menu, Multiple Monster, Combat Menu
	 */
	private void attackMultipleMonster(String userInput) {
		if(userInput.equalsIgnoreCase("Exit")) {
			model.setState("Action Menu");
		}
		else if(model.checkValidMonster(userInput) && !model.getStoredState().equalsIgnoreCase("Throw")) {
			if(checkWeaponAmmo()) {
				view.println("You don't have any ammo for your weapon!\n");
			}else {
				for(int x = 0; x < model.getPlayer().getCurrentRoom().getRoomMonster().size(); x++) {
					if(model.getPlayer().getCurrentRoom().getRoomMonster().get(x).getMonsterName().equalsIgnoreCase(userInput)) {
						model.setCurrentMonster(model.getPlayer().getCurrentRoom().getRoomMonster().get(x));
					}
				}
				
				view.println("You attack the " + model.getCurrentMonster().getMonsterName() + "!");
				model.getCurrentMonster().takeDmg(model.getPlayer().getWeapon().getItemActionValue());
				model.getPlayer().useWeaponAmmo(model.getPlayer().getWeapon());
				view.println("The " + model.getCurrentMonster().getMonsterName() + " took " + 
						model.getPlayer().getWeapon().getItemActionValue() + " damage!\n");

				if(checkMonsterDeath()) {
					view.println("The " + model.getCurrentMonster().getMonsterName() + " slumps over, defeated.");
					view.println("--------------------------------------------------");
					model.getPlayer().getCurrentRoom().removeRoomMonster(model.getCurrentMonster());
					model.setLootList(model.getMonsterLootList());
					if(!model.getLootList().isEmpty()) {
						for(int x = 0; x < model.getLootList().size();x++) {
							view.println("The enemy drops " + model.getLoot().getItemName() +" on the floor of the room");
							model.getPlayer().getCurrentRoom().addRoomItem(model.getLoot());
							model.removeLoot();
						}
						view.println("--------------------------------------------------");
					}
					model.decreaseMonsterAlive();

				}
			}
			if(!model.getPlayer().getCurrentRoom().getRoomMonster().isEmpty()) {
				for(int x = 0; x < model.getPlayer().getCurrentRoom().getRoomMonster().size(); x++) {
					model.setCurrentMonster(model.getPlayer().getCurrentRoom().getRoomMonster().get(x));
					attackPlayer();
				}
				model.setState("Combat Menu");
			}else {
				model.setState("Action Menu");
				checkWinCondition();
			}
		}else if(model.checkValidMonster(userInput) && model.getStoredState().equalsIgnoreCase("Throw")) {
			for(int x = 0; x < model.getPlayer().getCurrentRoom().getRoomMonster().size(); x++) {
				if(model.getPlayer().getCurrentRoom().getRoomMonster().get(x).getMonsterName().equalsIgnoreCase(userInput)) {
					model.setCurrentMonster(model.getPlayer().getCurrentRoom().getRoomMonster().get(x));
				}
			}
			if(model.getPlayer().getSelectedItem().getItemAmount() == 0)
			{
				model.setStoredState("Combat");
				attackMultipleMonster(userInput);
			}
			else
			{
				view.println("You used the " + model.getPlayer().getSelectedItem().getItemName() + ".");
				model.getCurrentMonster().takeDmg(model.getPlayer().getSelectedItem().getItemActionValue());
				model.getPlayer().removeItemFromInventory(model.getPlayer().getSelectedItem());
				view.println("The " + model.getCurrentMonster().getMonsterName() + " took " + 
						model.getPlayer().getSelectedItem().getItemActionValue() + " damage!\n");
	
				if(checkMonsterDeath()) {
					view.println("The " + model.getCurrentMonster().getMonsterName() + " slumps over, defeated.");
					view.println("--------------------------------------------------");
						if(model.getCurrentMonster().getMonsterType().equalsIgnoreCase("Final Boss")) {
							checkWinCondition();
						}
					model.getPlayer().getCurrentRoom().removeRoomMonster(model.getCurrentMonster());
					model.setLootList(model.getMonsterLootList());
					if(!model.getLootList().isEmpty()) {
						for(int x = 0; x < model.getLootList().size();x++) {
							view.println("The enemy drops " + model.getLoot().getItemName() +" onto the floor of the room.");
							model.getPlayer().getCurrentRoom().addRoomItem(model.getLoot());
							model.removeLoot();
						}
						view.println("--------------------------------------------------");
					}
					setWinCondition();
					model.decreaseMonsterAlive();
	
				}
				if(!model.getPlayer().getCurrentRoom().getRoomMonster().isEmpty()) {
					for(int x = 0; x < model.getPlayer().getCurrentRoom().getRoomMonster().size(); x++) {
						model.setCurrentMonster(model.getPlayer().getCurrentRoom().getRoomMonster().get(x));
						attackPlayer();
					}
					model.setState("Combat Menu");
				}else {
					model.setState("Action Menu");
					if(checkWinCondition()) {
						model.setState("Main Menu");
					}
					setWinCondition();
				}
			}
		}
		else{
			view.println("Invalid Input");
			view.println("--------------------------------------------------");

		}
	}

	/*Run away from the monster, and restore the monster health
	 *then set the player back to the previous room that they
	 *were in before
	 *Parent Method; Action Menu
	 *Related State Affliction: Combat Menu, Action Menu
	 */
	private void runAway() {
		view.println("You ran away from the encounter.");
		view.println("--------------------------------------------------");
		for(int x = 0; x < model.getPlayer().getCurrentRoom().getRoomMonster().size(); x++) {
			model.setCurrentMonster(model.getPlayer().getCurrentRoom().getRoomMonster().get(x));
			model.getCurrentMonster().setMonsterCurrentHealth(model.getCurrentMonster().getMonsterMaxHealth());
		}
		model.getPlayer().setCurrentRoom(model.getPlayer().getPreviousRoom());
		model.setState("Action Menu");
	}

	/*Return if you have ammo or not
	 *Parent Method: attackMonster()
	 */
	public boolean checkWeaponAmmo() {
		if(model.getPlayer().getWeaponAmmo(model.getPlayer().getWeapon()) == 0) {
			return true;
		}else {
			return false;
		}
	}

	/*Return if you have ammo or not
	 *Parent Method: attackMonster()
	 */
	public boolean checkMonsterDeath() {
		if(model.getCurrentMonster().getMonsterCurrentHealth() <= 0) {
			return true;
		}else {
			return false;
		}
	}

	/*The monster caculate on whether it can hit the player
	 *then deals damage and then display the health and check
	 *if you die from the mosnter attack
	 *Child Method: showPlayerHealth(), checkPlayerDeath()
	 *Related State Affliction: Combat Menu, Use Item Puzzle
	 */
	public void attackPlayer() {
		view.println("The " + model.getCurrentMonster().getMonsterName() + " attacks!");
		int mosnterDamage = model.getCurrentMonster().attackPlayer();
		if(mosnterDamage == 0) {
			view.println("The attack missed!");
		}else {
			view.print("You took " + mosnterDamage + " damage!");
			model.getPlayer().takeDmg(mosnterDamage);
			showPlayerHealth();
			checkPlayerDeath();
		}
		view.println("--------------------------------------------------");
	}

	/*Show the player health to the view
	 *Parent Method: attackPlayer(), usePlayerItem
	 *Related State Affliction: Combat Menu, Use Item Puzzle
	 */
	private void showPlayerHealth() {
		view.println(" (" + model.getPlayer().getPlayerCurrentHealth() + "/" + model.getPlayer().getPlayerMaxHealth() + ")");
	}

	/*Check to see if the player is dead, and if he
	 *is then it set the state to the main menu
	 *Parent Method: attackPlayer(), inputNumber()
	 *Related State Affliction: Combat Menu, Use Item Puzzle, Main Menu
	 */
	private void checkPlayerDeath() {
		if(model.getPlayer().getPlayerCurrentHealth() <= 0) {
			view.println("\n\n--------------------------------------------------");
			view.println("||||||||||||||||||||||||||||||||||||||||||||||||||");
			view.println("=====        YOU DIED   -   GAME OVER        =====");
			view.println("||||||||||||||||||||||||||||||||||||||||||||||||||");
			view.println("--------------------------------------------------\n\n\n");
			model.setState("Main Menu");
		}
	}
	
	/*Check to see if the win condition is met and
	 *if there is zero enemy alive then the you win
	 *and sent to back to the main menu
	 *Parent Method: attackMonster(), attackMultipleMonster()
	 *Related State Affliction: Main Menu, Combat Menu, Multiple Monster
	 */
	private void setWinCondition() {	
			if(model.getCurrentMonster().getMonsterType().equalsIgnoreCase("Final Boss")) {
				winCondition = true;
			}else if(model.getMonsterAlive() == 0) {
				winCondition = true;
			}
	}

	/*Check to see if the win condition is met and
	 *if there is zero enemy alive then the you win
	 *and sent to back to the main menu
	 *Parent Method: attackMonster(), attackMultipleMonster()
	 *Related State Affliction: Main Menu, Combat Menu, Multiple Monster
	 */
	private boolean checkWinCondition() {	
		if(winCondition == true) {
			view.println("\n\n--------------------------------------------------");
			view.println("||||||||||||||||||||||||||||||||||||||||||||||||||");
			view.println("=====        YOU WIN!    -    VICTORY        =====");
			view.println("||||||||||||||||||||||||||||||||||||||||||||||||||");
			view.println("--------------------------------------------------\n\n\n");
			return true;
		}
		return false;
	}


	//-------------------------------------------------------------------------------------------------	

	/*Action Menu that set the state or print out information
	 *depending on the player input 
	 */
	private void puzzleMenu(String userInput) {
		switch(userInput.toLowerCase()) {
		case "input number" : case "1":
			if(model.getNextRoomPuzzle().getPuzzleType().equalsIgnoreCase("input"))
				model.setState("Input Number");
			else if (model.getNextRoomPuzzle().getPuzzleType().equalsIgnoreCase("item")) {
				view.println("There's nothing to input.");
				view.println("--------------------------------------------------");
			}
			else {
				view.println("Error with Puzzle Type " + model.getNextRoomPuzzle().getPuzzleType());
				System.exit(0);
			}
			break;
		case "inventory" : case "2":
			model.setStoredState("Puzzle");
			model.setState("Use Item Puzzle");
			break;
		case "hint" : case "3":
			view.println(model.getNextRoomPuzzle().getPuzzleDesc() + "\n");
			view.println("HINT:\n" + model.getNextRoomPuzzle().getPuzzleHint() + "\n");
			break;
		case "exit" : case "4":
			model.setState("Action Menu");
			break;
		default:
			view.println("Invalid Input");
			view.println("--------------------------------------------------");
		}

	}

	/*Check if the item that is currently selected has the
	 *item Id need to solve the puzzle, if it does then it
	 *check the next room and see if there is a monster or not
	 *then depending on that, it set the state of the menu
	 *otherwise you go back to the puzzle menu
	 *Need Input: None
	 *Related State Affliction: Use Item Puzzle(Method: puzzleMenu()), Action Menu, Combat Menu
	 */
	private void usePuzzleItem() {
		if(model.getNextRoomPuzzle().getPuzzleSolution().equalsIgnoreCase(model.getPlayer().getSelectedItem().getItemId())) {
			view.println("You used the " + model.getPlayer().getSelectedItem().getItemName() + ", and the door opens!");
			view.println("--------------------------------------------------");
			checkMonsterRoom();
		}else {
			view.println("Nothing interesting happens.");
			view.println("--------------------------------------------------");
			model.setState("Puzzle Menu");
		}

	}

	/*Check what the userinput is the solution to the puzzle
	 *if it is then it check the next room to see if there is a monster
	 *Need Input: Number from the user to solve puzzle
	 *Child Method: checkMonsterRoom()
	 *Related State Affliction: Use Item Puzzle(Method: puzzleMenu())
	 */
	private void inputNumber(String userInput) {
		if(userInput.equalsIgnoreCase("exit")) {
			model.setState("Puzzle Menu");
		}
		else if(model.getNextRoomPuzzle().getPuzzleSolution().equalsIgnoreCase(userInput)) {
			view.println("You enter the number, and the door opens!");
			view.println("--------------------------------------------------");
			checkMonsterRoom();

		}else {
			model.getPlayer().takeDmg(model.getNextRoomPuzzle().getPuzzleDamage());
			view.print("You input the wrong number and get shocked for " + model.getNextRoomPuzzle().getPuzzleDamage() + " damage!");
			showPlayerHealth();
			view.println("--------------------------------------------------");
			checkPlayerDeath();
		}

	}

	/*Check if the next room has a monster or not, if
	 *it does then it switch to combat menu, other wise
	 *it print out the room information and set the state
	 *to Action Menu
	 *Need Input: None
	 *Parent Method: usePuzzleItem()
	 *Related State Affliction: Action Menu, Combat Menu
	 */
	public void checkMonsterRoom() {
		if(model.getNextRoom().getRoomMonster().isEmpty()) {
			model.getPlayer().setCurrentRoom(model.getNextRoom());
			view.println(model.getPlayer().getCurrentRoom().toString());
			model.setState("Action Menu");
		}
		else {
			model.getPlayer().setCurrentRoom(model.getNextRoom());
			model.setState("Combat Menu");
		}
	}




	//-------------------------------------------------------------------------------------------------	
	/*Inventory Menu that set the state depending on the
	 *player input
	 */
	public void inventoryMenu(String userInput) {
		if(model.getStoredState().equalsIgnoreCase("Combat")) {
			switch(userInput.toLowerCase()) {
			case "examine item": case "1":
				model.setState("Select Item");
				model.setStoredState("Examine Item");
				break;
			case "use item": case "3":
				model.setState("Select Item");
				model.setStoredState("Use Item");
				break;
			case "drop item": case "2":
				model.setState("Select Item");
				model.setStoredState("Drop Item");
				break;
			case "equip item": case "4":
				model.setState("Select Item");
				model.setStoredState("Equip Item");
				break;
			case "exit": case "5":
				model.setState("Combat Menu");
				break;
			default:
				view.println("Invalid Input");
				view.println("--------------------------------------------------");
				break;
			}
		}else if(model.getStoredState().equalsIgnoreCase("Action")){
			switch(userInput.toLowerCase()) {
			case "examine item": case "1":
				model.setState("Select Item");
				model.setStoredState("Examine Item");
				break;
			case "drop item": case "2":
				model.setState("Select Item");
				model.setStoredState("Drop Item");
				break;
			case "use item": case "3":
				model.setState("Select Item");
				model.setStoredState("Use Item");
				break;
			case "equip item": case "4":
				model.setState("Select Item");
				model.setStoredState("Equip Item");
				break;
			case "exit": case "5":
				model.setState("Action Menu");
				break;
			default:
				view.println("Invalid Input");
				view.println("--------------------------------------------------");
				break;
			}
		}
	}

	/*Check if the item that the user chose is valid
	 *and if it is, it get the store state and depending
	 *on what it is, it execute that method. If the player
	 *is exiting, depending on the stored state, the player
	 *can either exit through the puzzle or action menu
	 *Child Method: examineItem(), dropItem(),useItem(),equipItem(),usePuzzleItem()
	 *Related State Affliction: Examine Item, Drop Item, Use Item, Equip Item, 
	 *Puzzle, Puzzle Menu, Action Menu
	 */
	private void checkUserItem(String userInput) {
		if(userInput.equalsIgnoreCase("exit")) {
			if(model.getStoredState().equalsIgnoreCase("Puzzle")) {
				model.setState("Puzzle Menu");
			}else{
				model.setState("Action Menu");
			}
		}
		else if(model.checkValidItem(userInput) || (model.getStoredState().equalsIgnoreCase("Examine Item") && 
				model.getPlayer().getWeapon().getItemName().equalsIgnoreCase(userInput))) {
			if((model.getStoredState().equalsIgnoreCase("Examine Item") && 
					model.getPlayer().getWeapon().getItemName().equalsIgnoreCase(userInput))) {
				model.getPlayer().setSelectedItem(model.getPlayer().getWeapon());
			}
			for(int x = 0; x < model.getPlayer().getInventoryList().size(); x++) {
				if(model.getPlayer().getInventoryList().get(x).getItemName().equalsIgnoreCase(userInput)) {
					model.getPlayer().setSelectedItem(model.getPlayer().getInventoryList().get(x));
				}
			}
			switch(model.getStoredState()) {
			case"Examine Item":
				examineItem();
				break;
			case"Drop Item":
				dropItem();
				break;
			case"Use Item":
				useItem();
				break;
			case"Equip Item":
				equipItem();
				break;	
			case"Puzzle":
				usePuzzleItem();
				break;
			default:
				System.out.println("Error, Invalid StoreState(Method: checkUserItem)");
				System.out.println(model.getStoredState());
				System.exit(0);
			}
		}else {
			view.println("Invalid Input");
			view.println("--------------------------------------------------");
		}		
	}

	/*Print out the item description and if it's a weapon
	 *it also print out the amount of ammo that gun has
	 *Parent Method: checkUserItem();
	 *Child Method: switchState();
	 */
	private void examineItem() {
		view.println(model.getPlayer().getSelectedItem().toString());
		if(model.getPlayer().getSelectedItem().getItemType().equalsIgnoreCase("weapon")) {
			view.println("\nAmmo x " + model.getPlayer().getWeaponAmmo(model.getPlayer().getSelectedItem()));
		}

		view.println("--------------------------------------------------");
		switchState();
	}

	/*Drop the item that was selected to the ground
	 *Parent Method: checkUserItem();
	 *Child Method: switchState();
	 */
	private void dropItem() {
		model.getPlayer().getCurrentRoom().addRoomItem(model.getPlayer().getSelectedItem());
		model.getPlayer().removeItemFromInventory(model.getPlayer().getSelectedItem());
		view.println("You drop the " + model.getPlayer().getSelectedItem().getItemName()+ ".");
		view.println("--------------------------------------------------");
		switchState();

	}

	/*Use healing item if you are not at full health,
	 *if item is throwable then monster take damage,
	 *anything else then nothing happen
	 *Parent Method: checkUserItem();
	 *Child Method: switchState();
	 */
	private void useItem() {
		if(model.getPlayer().getSelectedItem().getItemType().equalsIgnoreCase("healing")) {
			if(!(model.getPlayer().getPlayerCurrentHealth() == model.getPlayer().getPlayerMaxHealth())) {
				model.getPlayer().healHealth(model.getPlayer().getSelectedItem().getItemActionValue());
				view.println("You recover " + model.getPlayer().getSelectedItem().getItemActionValue() + " health.");
				model.getPlayer().removeItemFromInventory(model.getPlayer().getSelectedItem());
				view.println("--------------------------------------------------");
				switchState();
			}else {
				view.println("You are already at full health.");
				view.println("--------------------------------------------------");
				switchState();
			}
		}
		else if(model.getPlayer().getSelectedItem().getItemType().equalsIgnoreCase("throwable")) {
			if(!model.getPlayer().getCurrentRoom().getRoomMonster().isEmpty()) {
				if(model.getPlayer().getCurrentRoom().getRoomMonster().size() == 1) {
					model.setCurrentMonster(model.getPlayer().getCurrentRoom().getRoomMonster().get(0));
					view.println("You threw the " + model.getPlayer().getSelectedItem().getItemName());
					model.getCurrentMonster().takeDmg(model.getPlayer().getSelectedItem().getItemActionValue());
					model.getPlayer().removeItemFromInventory(model.getPlayer().getSelectedItem());
					view.println("The " + model.getCurrentMonster().getMonsterName() + " took " + 
							model.getPlayer().getSelectedItem().getItemActionValue() + " damage!\n");

					if(checkMonsterDeath()) {
						view.println("The " + model.getCurrentMonster().getMonsterName() + " slumps over, defeated.");
						view.println("--------------------------------------------------");
						model.getPlayer().getCurrentRoom().removeRoomMonster(model.getCurrentMonster());
						model.setLootList(model.getMonsterLootList());
						if(!model.getLootList().isEmpty()) {
							for(int x = 0; x < model.getLootList().size();x++) {
								view.println("The enemy drops " + model.getLoot().getItemName() +" on the floor of the room");
								model.getPlayer().getCurrentRoom().addRoomItem(model.getLoot());
								model.removeLoot();
							}
							view.println("--------------------------------------------------");
						}
						model.decreaseMonsterAlive();

					}
					if(!model.getPlayer().getCurrentRoom().getRoomMonster().isEmpty()) {
						attackPlayer();
						model.setState("Combat Menu");
					}else {
						model.setState("Action Menu");
						checkWinCondition();
					}
				}else{
					model.setStoredState("Throw");
					model.setState("Multiple Monster");
				}
			}else {
				view.println("You can't use that right now");
				view.println("--------------------------------------------------");
				switchState();
			}
		}else {
			view.println("Nothing interesting happens.");
			view.println("--------------------------------------------------");
			switchState();
		}	
	}

	/*If the user have the same weapon that they want to
	 *equip then nothing happen. If the selected item is
	 *not a weapon then it does nothing other wise it 
	 *equip the weapon to the user.
	 *Parent Method: checkUserItem();
	 *Child Method: switchState();
	 */
	private void equipItem() {
		if(model.getPlayer().getSelectedItem().getItemName().equals(model.getPlayer().getWeapon().getItemName())) {
			view.println("You're already holding a weapon like that!");
			view.println("--------------------------------------------------");
			switchState();
		}else {
			if(model.getPlayer().getSelectedItem().getItemType().equalsIgnoreCase("Weapon")) {
				model.getPlayer().equipWeapon(model.getPlayer().getSelectedItem());
				view.println("You equipped the " + model.getPlayer().getWeapon().getItemName());
				view.println("--------------------------------------------------");
				switchState();
			}else {
				view.println("That isn't a weapon!");
				view.println("--------------------------------------------------");
				switchState();
			}

		}

	}

	/*After finishing an action with an item command, then it check the
	 *if there is a monster currently in the room to see if combat is
	 *going and if it is then it set state to combat menu, otherwise you
	 *return to the action menu
	 *Parent Method: equipItem(),useItem(), dropItem(),examineItem();
	 *Child Method: switchState();
	 */
	public void switchState() {
		if(!model.getPlayer().getCurrentRoom().getRoomMonster().isEmpty()) {
			model.setState("Combat Menu");
		}else {
			model.setState("Action Menu");
		}
	}









}
