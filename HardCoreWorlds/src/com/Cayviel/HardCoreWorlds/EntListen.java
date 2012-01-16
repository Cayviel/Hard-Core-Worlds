package com.Cayviel.HardCoreWorlds;

import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class EntListen extends EntityListener{
	private static HardCoreWorlds hcw;
	EntListen(HardCoreWorlds HCW){
		hcw= HCW;
	}
	
	public void onEntityDamage(EntityDamageEvent damageE){
		if(!(damageE instanceof EntityDamageByEntityEvent)) return;	//continue only if entity damages entity
		if (!(damageE.getEntity() instanceof Player)) return;		//continue only if player is hurt
		Player player = (Player)damageE.getEntity();
		if (! Config.getHc(player.getWorld(), player)) return; //continue only if hardcore is true
		EntityDamageByEntityEvent damage =  (EntityDamageByEntityEvent) damageE;
		if(!(damage.getDamager() instanceof Creature)) return; //continue only if a creature is doing the attacking
		int sd = MobDifficulties.getDamage(damage.getDamager(), damage.getEntity().getWorld().getName()); //get Modified damage
		if (sd != -1) damage.setDamage(sd);	//if defaulted, us standard default values for damage
		
	}
	
	public void onEntityDeath(EntityDeathEvent dead){

		if (!(dead.getEntity() instanceof Player)){return;} //if not instance of player return
		Player player = (Player)dead.getEntity();			//entity is a player
		World world = player.getWorld();					
		if (! Config.getHc(world,player)) return;		//if world is not hardcore, and player isnt hardcore in this world, return
		if (world.getName() == BanManager.BannedList.getString("Unbannable World")) return; // if this is the Unbannable world, return
		
		BanManager.ban(player, world, hcw); //Ban the player
		
		playerL.safetyWcheck(); //ensure Unbannable World exists
		player.teleport(BanManager.Ereturnworld.getSpawnLocation()); //teleport player there.
	}
	
}
