package com.Cayviel.HardCoreWorlds;

import net.minecraft.server.Packet103SetSlot;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.WorldCreator;

public class playerL extends PlayerListener {
	
	Plugin hcw;
	playerL(HardCoreWorlds HCW){
		hcw = HCW;
	}
	
	public void onPlayerLogin (PlayerLoginEvent hi){
		Player player = hi.getPlayer();
		World inWorld = player.getWorld();

		VirtualHunger.delay(80, player);
		
		MiscFunctions.WorldListUpdate(inWorld);
		if(! BanManager.EnterWorldRequest(player,inWorld)){
			safetyWcheck();
			player.teleport(BanManager.Ereturnworld.getSpawnLocation());
			delayInvUpdate(player);
			return;
		}
	}
	
	public void onPlayerRespawn (PlayerRespawnEvent respawn){
		Player player = respawn.getPlayer();			//entity is a player
		World world = player.getWorld();
		if (! Config.getHc(world,player)) return;		//if world is not hardcore, and player isnt hardcore in this world, return
		if (world.getName() == BanManager.BannedList.getString("Unbannable World")) return; // if this is the Unbannable world, let them pass
		if (BanManager.isBanned(player, world)){
			safetyWcheck(); //ensure Unbannable World exists
			Location spawn = BanManager.Ereturnworld.getSpawnLocation();
			respawn.setRespawnLocation(spawn); //define the spawn location to be the world the player is now in
			delaySpawn(player, spawn);
		}
	}
	
	public void delaySpawn(final Player player, final Location spawn){
		hcw.getServer().getScheduler().scheduleSyncDelayedTask(hcw, new Runnable(){		//Schedule a delayed task with the above time
			public void run() {												
				player.teleport(spawn);
				delayInvUpdate(player);
			}
		},1); //delay for 1 ticks
	}
	
	public void delayInvUpdate(final Player player){
		hcw.getServer().getScheduler().scheduleSyncDelayedTask(hcw, new Runnable(){		//Schedule a delayed task with the above time
			public void run() {		
				updateInventory(player);
		}
		},1); //delay for 1 ticks	
	}
	public void worldEnterResponse(Player player, World from, World to){
		if (! BanManager.EnterWorldRequest(player, to)){//if player is not granted permission to enter world,  
			BanManager.banMessage(player,to);
			SafetyCheck(player, to, from); //Return previous world, or to safety world in case of weird scenario
		}
	}
	
	public void onPlayerPreLogin (PlayerPreLoginEvent hi){
		String playerN = hi.getName();
		if (! BanManager.isServerBanned(playerN)) return;
		BanManager.updateServerBan(playerN);
		if (BanManager.isServerBanned(playerN)){
			double[] times = BanManager.getServerBanTimes(playerN);
			if(times[1]>times[0]){
				hi.disallow(Result.KICK_BANNED, "Sorry. You are banned from this server.");
			}
			hi.disallow(Result.KICK_BANNED, "Sorry. You are banned for about "+(times[1]-BanManager.getHour())+" more hours");
		}
	}

	public void onPlayerChangedWorld(PlayerChangedWorldEvent pMo){

		World fromW, toW;
		Player player = pMo.getPlayer();
		
		toW = player.getWorld();
		fromW = pMo.getFrom();
		MiscFunctions.WorldListUpdate(toW); //update the world list to include the entered world, if new
		//if (! Config.getHc(toW.getName())) return; //if not hardcore, return
		worldEnterResponse(player,fromW,toW);
		//else just continue on with the world change as normal
	}
	
	public void SafetyCheck(Player player, World toW, World fromW){
		if (BanManager.isBanned(player, toW)){
			if (BanManager.isBanned(player, fromW)){
				safetyWcheck();
				/*delaySpawn(player, BanManager.Ereturnworld.getSpawnLocation());*/
				player.teleport(BanManager.Ereturnworld.getSpawnLocation());
				delayInvUpdate(player);
				return;
			}else{
				/*delaySpawn(player, fromW.getSpawnLocation());*/
				player.teleport(fromW.getSpawnLocation());
				delayInvUpdate(player);
			return;
			}
		}
	}
	
	public static void safetyWcheck(){
		if (BanManager.Ereturnworld == null){
			WorldCreator wc = new WorldCreator(BanManager.BannedList.getString("Unbannable World"));
			wc.environment(Environment.NORMAL);
			BanManager.Ereturnworld = wc.createWorld();
		}
	}
	
    public static void updateInventory(Player p) {
        CraftPlayer c = (CraftPlayer) p;
        for (int i = 0;i < 36;i++) {
            int nativeindex = i;
            if (i < 9) nativeindex = i + 36;
            ItemStack olditem =  c.getInventory().getItem(i);
            net.minecraft.server.ItemStack item = null;
            if (olditem != null && olditem.getType() != Material.AIR) {
                item = new net.minecraft.server.ItemStack(0, 0, 0);
                item.id = olditem.getTypeId();
                item.count = olditem.getAmount();
                item.setData(olditem.getData().getData());
            }
            Packet103SetSlot pack = new Packet103SetSlot(0, nativeindex, item);
            c.getHandle().netServerHandler.sendPacket(pack);
        }
    }
	
}