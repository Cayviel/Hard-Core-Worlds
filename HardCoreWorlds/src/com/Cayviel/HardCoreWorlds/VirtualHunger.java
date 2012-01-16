package com.Cayviel.HardCoreWorlds;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class VirtualHunger extends EntityListener {
	private static HardCoreWorlds hcw;
	VirtualHunger(HardCoreWorlds p){
		hcw = p;
	}
	
	public void onFoodLevelChange(FoodLevelChangeEvent hungerchange){
		if (!(hungerchange.getEntity() instanceof  Player)){return;}
		Player player = (Player)hungerchange.getEntity();
		if(hungerchange.getFoodLevel()==0 && player.getHealth() <= 10){
			delay(80, player);
		}
	}
	
	public void onEntityDamage(EntityDamageEvent eDamage){
		if (!(eDamage.getEntity() instanceof  Player)){return;}
		Player player = (Player)eDamage.getEntity();
		if (player.getFoodLevel() == 0 && (player.getHealth() <= 10 + eDamage.getDamage())){
			delay(80, player);
		}
	}

	public static void delay(long time, final Player player){
		
		Runnable runit = new Runnable() {
		    public void run() {
		    		vhunger(player);
		    	}
		};
		player.getServer().getScheduler().scheduleSyncDelayedTask(hcw, runit,time);
	}
	
	public static void vhunger(Player player){
		int health = player.getHealth();
		int foodlevel = player.getFoodLevel();
		if (health<=10 && foodlevel == 0  && Config.getHc(player.getWorld(), player)){
			player.damage(1);
			delay(80, player);
		}
	}
	
}
