package GameObject;

import java.io.Serializable;

public class Monsters implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 747669969230458693L;
	private String monsterName;
	private String monsterId;
	private String monsterDesc;
	private int monsterMaxHealth;
	private int monsterCurrentHealth;
	private int monsterDamage;
	private double monsterHitPercentage;

	//Create Default Object
	public Monsters() {
		this.monsterId = "E00";
		this.monsterName = "Invalid Monster Name";
		this.monsterDesc = "Invalid Monster Description";
		this.monsterMaxHealth = 00;
		this.monsterCurrentHealth = monsterMaxHealth;
		this.monsterDamage = 00;
		this.monsterHitPercentage = 0;
	}

	//Create Object with set parameter
	public Monsters(String monsterID, String monsterName, String monsterDesc, int monsterMaxHealth, int monsterDamage,
			double monsterHitPercentage) {
		this.monsterId = monsterID;
		this.monsterName = monsterName;
		this.monsterDesc = monsterDesc;
		this.monsterMaxHealth = monsterMaxHealth;
		this.monsterCurrentHealth = monsterMaxHealth;
		this.monsterDamage = monsterDamage;
		this.monsterHitPercentage = monsterHitPercentage;
	}


	//Set and get monster id
	public void setMonsterId(String monsterID) {
		this.monsterId = monsterID;
	}
	public String getMonsterId() {
		return monsterId;
	}


	//Set and get monster name
	public void setMonsterName(String monsterName) {
		this.monsterName = monsterName;
	}
	public String getMonsterName() {
		return monsterName;
	}


	//Set and get monster description
	public void setMonsterDesc(String monsterDesc) {
		/*If monster description is more then 50 character, then
		  it'll format the description with a space break; */

		if(monsterDesc.length() > 50) {
			int totalCharacterLength = 0;
			int descriptionLength = monsterDesc.length();
			String outputString ="";

			for (String word : monsterDesc.split(" ")) {
				totalCharacterLength += word.length();
				descriptionLength -= word.length()+1;
				outputString += word + " ";

				if(totalCharacterLength > 40 && descriptionLength > 0) {
					totalCharacterLength = 0;
					outputString += "\n";
				}
			}
			monsterDesc = outputString;
		}

		this.monsterDesc = monsterDesc;
	}
	public String getMonsterDesc() {
		return monsterDesc;
	}

	
	
	/*Use for player attack method
	 *Parameter: Action value of weapon type item
	 *Function: Gets the damage and subtract it from the monster health
	 */
	public void setMonsterMaxHealth(int monsterMaxHealth) {
		this.monsterMaxHealth = monsterMaxHealth;
	}
	public int getMonsterMaxHealth() {
		return monsterMaxHealth;
	}
	public void setMonsterCurrentHealth(int monsterCurrentHealth) {
		this.monsterCurrentHealth = monsterCurrentHealth;
	}
	public int getMonsterCurrentHealth() {
		return monsterCurrentHealth;
	}
	public void takeDmg(int dmg) {
		if((monsterCurrentHealth - dmg) <= 0) {
			this.monsterCurrentHealth = 0;
		}else {
			this.monsterCurrentHealth -= dmg;
		}
	}

	


	//Set and get monster damage
	public void setMonsterDamage(int monsterDamage) {
		this.monsterDamage = monsterDamage;
	}
	public int getMonsterDamage() {
		return monsterDamage;
	}


	//Set and get monster hit percentage
	public void setMonsterHitPercentage(double monsterHitPercentage) {
		this.monsterHitPercentage = monsterHitPercentage;
	}
	public double getMonsterHitPercentage() {
		return monsterHitPercentage;
	}


	/*Make the monster attack the player, use in 
	 *conjection with player method takeDmg 
	 *
	 *Uses the method "attackHit"
	 *
	 *Function: If hit returns true, then the monster hit you
	 *else it return 0 damage
	 */
	public int attackPlayer() {
		if(attackHit() == true) {
			return monsterDamage;
		}
		else{
			return 0;
		}
	}


	/*Sub Method of "attackPlayer" 
	 * 
	 *YOU DO NOT CALL THIS METHOD ANYWHERE ELSE
	 */
	public boolean attackHit() {
		//Creates a random value between 1 and 100
		double chance = Math.random() * 100;
		
		//copy of monster hit percentage and multiply by a hundred to get a non decimal
		double monsterHit = monsterHitPercentage*100;
		
		/*Minus mosnterHit value with chance
		 * and if it's greater then or equal to zero
		 * then the hit percentage falls within the bounds
		 * and return true, else it return false 
		 */
		if ((monsterHit - chance) >= 0) 
			return true;
		else 
			return false;	
	}


	
	public String toString() {
		String returnString = "";

		//Add Monster Name
		returnString += getMonsterName() + " - ";

		//Add Monster Health
		returnString += "Health: " + getMonsterCurrentHealth() + "/" + getMonsterMaxHealth();
		
		//Add Monster Combat Stats
		returnString += "\nAttack: " + getMonsterDamage() + " - Accuracy: " + (getMonsterHitPercentage()*100) + "%";
		
		//Add Monster Description
		returnString += "\n" + getMonsterDesc() + "\n";
		
		return returnString;
	}

}
