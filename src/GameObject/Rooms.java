package GameObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Rooms implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3774932443351106622L;
	private String roomId;
	private String roomFloor;
	private String roomDescription;
	private String roomAccess;
	private ArrayList<Items> roomItem;
	private ArrayList<Monsters> roomMonster;
	private HashMap<String, String> roomNavigationList = new HashMap<String, String>();
	private HashMap<String, String> roomAccessList = new HashMap<String, String>();

	//Create Default Object
	public Rooms(){
		this.roomId = "R00";
		this.roomFloor = "Invalid Floor";
		this.roomDescription = "Invalid Description";
		this.roomAccess = "null";
		this.roomItem = new ArrayList<Items>();
		this.roomMonster = new ArrayList<Monsters>();
	}

	//Create Object with set parameter
	public Rooms(String roomId, String roomFloor, String roomDescription, String roomAccess,
			ArrayList<Items> roomItem, ArrayList<Monsters> roomMonster, HashMap<String, String> roomNavigationList,
			HashMap<String, String> roomAccessList) {
		super();
		this.roomId = roomId;
		this.roomFloor = roomFloor;
		this.roomDescription = roomDescription;
		this.roomAccess = roomAccess;
		this.roomItem = roomItem;
		this.roomMonster = roomMonster;
		this.roomNavigationList = roomNavigationList;
		this.roomAccessList = roomAccessList;
	}

	//Set and get room id
	public void setRoomId(String roomId){
		this.roomId = roomId;
	}
	public String getRoomId(){
		return roomId;
	}
	
	//Set and get the room floor
	public void setRoomFloor(String roomFloor){
		this.roomFloor = roomFloor;
	}
	public String getRoomFloor(){
		return roomFloor;
	}
	
	
	//Set and get the room description
	public void setRoomDescription(String roomDescription){
		/*If there are more then 50 character in a line, 
		it'll make a new line*/
		if(roomDescription.length() > 50) {
			int totalCharacterLength = 0;
			int descriptionLength = roomDescription.length();
			String outputString ="";

			for (String word : roomDescription.split(" ")) {
				totalCharacterLength += word.length();
				descriptionLength -= word.length()+1;
				outputString += word + " ";

				if(totalCharacterLength > 40 && descriptionLength > 0) {
					totalCharacterLength = 0;
					outputString += "\n";
				}
			}
			roomDescription = outputString;
		}
		this.roomDescription = roomDescription;
	}
	public String getRoomDescription(){
		return roomDescription;
	}
	
	
	/*Set and get the room access
	 *Check to see the current room the user is in, then check to
	 *see if there is a puzzle id corresponding to it, if there is
	 *a match then the user will be prompted with the puzzle
	 */
	public void setRoomAccess(String currentRoomId, String puzzleID) {
		this.roomAccessList.put(currentRoomId, puzzleID);	
	}
	public String getRoomAccess() {
		String RoomAccess = "";
		Set set = roomAccessList.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()) { 
			Map.Entry mEntry = (Map.Entry)iterator.next();
			String coordinate =   (String) mEntry.getValue();
			RoomAccess += mEntry.getKey();
			if(iterator.hasNext()) {
				RoomAccess += ", ";
			}
		}
		return RoomAccess;
	}
	public HashMap<String, String> getRoomAccessList() {
		return roomAccessList;
	}
	
	
	/*Add or remove an item in the room by needing an input
	 *of the item id that you want in the room
	 *
	 *Get an arraylist of all the item id in the room
	 */
	public void addRoomItem(Items item) {
		if(item.getItemType().equalsIgnoreCase("Ammo")) {
			boolean itemFound= false;
			String itemName = "";
			for(int x = 0; x < roomItem.size(); x++) {
				itemName = roomItem.get(x).getItemName();
				if(roomItem.get(x).getItemName().equalsIgnoreCase(item.getItemName())) {
					itemFound = true;
					for(int y = 0; y < item.getItemAmount();y++) {
						roomItem.get(x).increaseItemAmount();
					}
				}
			}

			if(!itemFound) {
				int itemAmount = item.getItemAmount()-1;
				Items newItem = item;
				newItem.setItemAmount(1);
				this.roomItem.add(newItem);
				int roomItemNum= 0;
				for(int x = 0; x < roomItem.size(); x++) {
					if(roomItem.get(x).getItemName().equalsIgnoreCase(item.getItemName())) {
						roomItemNum = x;
						break;
					}
				}
				for(int y = 0; y < itemAmount;y++) {
					roomItem.get(roomItemNum).increaseItemAmount();
				}
			}


		}else {
			this.roomItem.add(item);
		}
	}
	public void removeRoomItemId(Items item) {
		boolean remove = false;
		
		search:
		for(int x = 0; x < this.roomItem.size(); x++) {
			if(this.roomItem.get(x).equals(item)) {
				this.roomItem.remove(x);
				remove = true;
				break search;
			}
		}
		
		if(!remove)
		System.out.println("Error: Item " + item.getItemName() + "/" + 
				item.getItemId() + "  does not exist in this room");
	}
	public void setRoomItemId(ArrayList<Items> roomItem) {
		this.roomItem = roomItem;
	}
	public ArrayList<Items> getRoomItem() {
		return roomItem;
	}
	
	
	/*Add or remove an mosnter in the room by needing an input
	 *of the mosnter id that you want in the room
	 *
	 *Get an arraylist of all the mosnter id in the room
	 */
	public void addRoomMonster(Monsters monster) {
		this.roomMonster.add(monster);
	}
	public void removeRoomMonster(Monsters monster) {
		boolean remove = false;
		
		search:
		for(int x = 0; x < this.roomMonster.size(); x++) {
			if(this.roomMonster.get(x).equals(monster)) {
				this.roomMonster.remove(x);
				remove = true;
				break search;
			}
		}
		
		if(!remove)
		System.out.println("Error: Monster " + monster.getMonsterName() + "/" +
				monster.getMonsterId() + " does not exist in this room");
	}
	public void setRoomMonsterId(ArrayList<Monsters> roomMonster) {
		this.roomMonster = roomMonster;
	}
	public ArrayList<Monsters> getRoomMonster() {
		return roomMonster;
	}
	
	
	/*Set the room connection to other room by inputing the direction
	 *then the room id of where that direction will take you and put
	 *both of it into a hashmap
	 *
	 *Return room connection with only the direction seperated by comma
	 */
	public void setRoomConnection(String direction, String roomId) {
		this.roomNavigationList.put(direction, roomId);	
	}
	
	public String getRoomConnection() {
		String RoomConnection = "";
		Set set = roomNavigationList.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()) { 
			Map.Entry mEntry = (Map.Entry)iterator.next();
			String coordinate =   (String) mEntry.getValue();
			RoomConnection += mEntry.getKey();
			if(iterator.hasNext()) {
				RoomConnection += ", ";
			}
		}
		return RoomConnection;
	}
	public HashMap<String, String> getRoomNavigationList() {
		return roomNavigationList;
	}
		
	
	public String toString() {
		String returnString = "";

		//Add Room Name
		returnString += "Floor: " + getRoomFloor() + "\n\n";

		//Add Room Description
		returnString += getRoomDescription();
		
		//Add Guide Line
		returnString += "\n--------------------------------------------------";
		
		return returnString;
	}
}