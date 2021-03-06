package com.Cayviel.HardCoreWorlds;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class VirtualHunger implements Listener {
	private static HardCoreWorlds hcw;
	VirtualHunger(HardCoreWorlds p){
		hcw = p;
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent hungerchange){
		if (!(hungerchange.getEntity() instanceof  Player)){return;}
		Player player = (Player)hungerchange.getEntity();
		if(hungerchange.getFoodLevel()==0 && player.getHealth() <= 10 ){
			delay(80, player);
		}
	}
	
	@EventHandler
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
		String worldN = player.getWorld().getName();
		int health = player.getHealth();
		int foodlevel = player.getFoodLevel();
		if (Config.getWorldMinHP(worldN) < health && health<=10 && foodlevel == 0  && Config.getHc(player.getWorld(), player)){
			player.damage(1);
			delay(80, player);
		}
	}
	
}
