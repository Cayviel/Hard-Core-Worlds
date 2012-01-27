package com.Cayviel.HardCoreWorlds;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class BanManager{
	HardCoreWorlds hcw;

	BanManager(HardCoreWorlds HCW){
		hcw = HCW;
	}
	
	static World Ereturnworld;
	static File BannedListFile;
    static FileConfiguration BannedList = new YamlConfiguration();	
    
	public static void init() {
		File BLF = new File(HardCoreWorlds.datafolder, "Ban.yml");
	    FileSetup.initialize(BLF,BannedList);
	    BannedListFile = BLF;
	    checkUnbannable();
		BannedList.addDefault("Unbannable World", "world");
		Ereturnworld = HardCoreWorlds.server.getWorld(BannedList.getString("Unbannable World"));
		//this is null if the "Unbannable World" doesn't exist on the server
	}
	
	private static void setubW(FileConfiguration BannedList, File BLF){
		BannedList.set("Unbannable World","world");
		try { BannedList.save(BLF);} catch (IOException e) {e.printStackTrace();}
	}
	
	public static String getubWN(){
		playerL.safetyWcheck();
		return Ereturnworld.getName();
	}
	
	public static boolean isBanned(Player player, World world){
		updateBan(player, world);
		boolean banned = BannedList.getBoolean("Player."+player.getName()+".World."+world.getName()+".Banned",false);
		return banned;
	}
	
	public static boolean isBanned(String playerN, String worldN){
		updateBan(playerN, worldN);
		boolean banned = BannedList.getBoolean("Player."+playerN+".World."+worldN+".Banned",false);
		return banned;
	}

	public static boolean isServerBanned(String playerN){
		FileSetup.load(BannedList, BannedListFile);
		boolean banned = BannedList.getBoolean("Player."+playerN+".Server.Ban",false);
		return banned;
	}
	
	public static boolean isBanPerm(Player player, World world){
		FileSetup.load(BannedList, BannedListFile);
		if (getBanBegin(player, world) > getBanEnds(player, world) ) return true;
		return false;
	}
	
	public static boolean isBanPerm(String player, String world){
		FileSetup.load(BannedList, BannedListFile);
		if (getBanBegin(player, world) > getBanEnds(player, world) ) return true;
		return false;
	}
	
	public static int getTimeLeft(Player player, World world){
		FileSetup.load(BannedList, BannedListFile);
		return getBanEnds(player, world)-getHour();
	}
	public static void checkUnbannable(){ 
	    String ubW = BannedList.getString("Unbannable World");
		if (ubW == null){setubW(BannedList,BannedListFile);}else{ 
			if (ubW.trim().isEmpty()){
				setubW(BannedList,BannedListFile);
    		}
		}
	}
	
	private static int getBanBegin(Player player, World world){
		Integer ii = BannedList.getInt("Player."+ player.getName()+".World."+world.getName()+".Ban Began", getHour());
		int i;
		if (ii != null){i = ii;}else{i=0;}
		return i;
	}

	private static int getBanBegin(String player, String world){
		Integer ii = BannedList.getInt("Player."+ player+".World."+world+".Ban Began", getHour());
		int i;
		if (ii != null){i = ii;}else{i=0;}
		return i;
	}
	
	private static int getBanEnds(Player player, World world){
		Integer ii = BannedList.getInt("Player."+ player.getName()+".World."+world.getName()+".Ban Ends", getHour());
		int i;
		if (ii != null){i = ii;}else{i=0;}
		return i;
	}

	private static int getBanEnds(String playerN, String worldN){
		Integer ii = BannedList.getInt("Player."+ playerN+".World."+worldN+".Ban Ends", getHour());
		int i;
		if (ii != null){i = ii;}else{i=0;}
		return i;
	}
	
	public static void ban(OfflinePlayer player, World world, HardCoreWorlds hcw){
	
		if (!player.isOnline()){
			if (hasServerBanI(player)){
				serverBan(player,hcw);
				return;
			}
		}else{
			Player playeron = Bukkit.getPlayer(player.getName());
			if (hasServerBanI(playeron)){
				serverBan(playeron,hcw);
				return;
			}
		}
		playerL.safetyWcheck();
		if (Ereturnworld == world){return;}
		BannedList.set("Player."+ player.getName()+".World."+world.getName()+".Banned", true);
		BannedList.set("Player."+ player.getName()+".World."+world.getName()+".Ban Began", getHour());
		BannedList.set("Player."+ player.getName()+".World."+world.getName()+".Ban Ends", getHour() + Config.getBanL(world.getName()));
		try {BannedList.save(BannedListFile);} catch (IOException e) {e.printStackTrace();}
	}
	
	public static boolean ban(String player, String world){
		playerL.safetyWcheck();
		if (Ereturnworld.getName() == world){return false;}
		BannedList.set("Player."+ player+".World."+world+".Banned", true);
		BannedList.set("Player."+ player+".World."+world+".Ban Began", getHour());
		BannedList.set("Player."+ player+".World."+world+".Ban Ends", getHour() + Config.getBanL(world));
		
		try {BannedList.save(BannedListFile);} catch (IOException e) {e.printStackTrace();}
		return true;
	}
	
	public static void serverBan(OfflinePlayer player, HardCoreWorlds hcw){

		if (player instanceof Player){
		banKickIn5(((Player)player),"You have been banned from this server!",hcw);
		((Player)player).sendMessage(ChatColor.RED + "You have been Banned from this server!  You have 5 seconds... Goodbye!");
		}
		BannedList.set("Player."+player.getName()+".Server.Ban", true);
		BannedList.set("Player."+player.getName()+".Server.Ban Began", getHour());
		BannedList.set("Player."+player.getName()+".Server.Ban Ends",  getHour()+Config.getServerBanDuration());
		
		try {BannedList.save(BannedListFile);} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void serverBan(OfflinePlayer player, HardCoreWorlds hcw, int duration){
		if (player instanceof Player){
		banKickIn5(((Player)player),"You have been banned from this server!",hcw);
		((Player)player).sendMessage(ChatColor.RED + "You have been Banned from this server!  You have 5 seconds... Goodbye!");
		}
		BannedList.set("Player."+player.getName()+".Server.Ban", true);
		BannedList.set("Player."+player.getName()+".Server.Ban Began", getHour());
		BannedList.set("Player."+player.getName()+".Server.Ban Ends", getHour() + duration);
		
		try {BannedList.save(BannedListFile);} catch (IOException e) {e.printStackTrace();}
	}

	public static void unServerBan(String playerN){
		BannedList.set("Player."+playerN+".Server", null);
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerN);
		player.setBanned(false);
		try {BannedList.save(BannedListFile);} catch (IOException e) {e.printStackTrace();}
	}

	public static int[] getServerBanTimes(Player player){
		int[] times = {0,0};
		times[0]=BannedList.getInt("Player."+player.getName()+".Server.Ban Began");
		times[1]=BannedList.getInt("Player."+player.getName()+".Server.Ban Ends");
		return times;
	}

	public static int[] getServerBanTimes(String playerN){
		int[] times = {0,0};
		times[0]=BannedList.getInt("Player."+playerN+".Server.Ban Began");
		times[1]=BannedList.getInt("Player."+playerN+".Server.Ban Ends");
		return times;
	}

	public static boolean EnterWorldRequest(Player playerE, World worldE){
		if (hasIgnoreBan(playerE,worldE)) return true; // if the player has ignore ban for this specific world, immediately grant access
		
		if (! Config.getHc(worldE,playerE)) return true;	//if the world is not hardcore for the player, grant access
			
		if (hasIgnoreBan(playerE)&&(! hasWorldBan(playerE, worldE))) return true; //if the player has a general ignore, and is not specifically banned in this world, grant access

		if (!isBanned(playerE, worldE)) return true; //if the player is not banned in the world, grant access

		//The player doesn't ignore bans, is hardcore in this world, and is banned in this world.
		return false;
	}

	static int getHour(){
		Calendar cal = Calendar.getInstance();
		return (cal.get(Calendar.YEAR)-2012)*8760+(cal.get(Calendar.DAY_OF_YEAR)-1)*24+cal.get(Calendar.HOUR_OF_DAY);
	}

	public static void updateBan(Player player, World world){
		FileSetup.load(BannedList, BannedListFile);
		if ( getHour() >=  getBanEnds(player, world) && (! isBanPerm(player, world)) ) {
			unBan(player.getName(),world.getName());
		}
		try {BannedList.save(BannedListFile);} catch (IOException e) {e.printStackTrace();}
	}

	public static void updateBan(String playerN, String worldN){
		FileSetup.load(BannedList, BannedListFile);
		if ( getHour() >=  getBanEnds(playerN, worldN) && (! isBanPerm(playerN, worldN)) ) {
			unBan(playerN,worldN);
		}
	}

	public static void updateServerBan(String playerN){
		int begins = getServerBanTimes(playerN)[0];
		int ends = getServerBanTimes(playerN)[1];
		FileSetup.load(BannedList, BannedListFile);
		boolean banned = BannedList.getBoolean("Player."+playerN+".Server.Ban",false);
		if ((getHour() >= ends && begins <= ends)|| !banned) {
			unServerBan(playerN);
		}
	}

	public static void unBan(String playerN, String worldN){
		if (playerInList(playerN)){	
			BannedList.set("Player."+ playerN +".World."+worldN, null);
			BannedList.set("Player."+ playerN +".Server", null);
			try {BannedList.save(BannedListFile);} catch (IOException e) {e.printStackTrace();}
		}
		unServerBan(playerN);
	}

	public static boolean hasIgnoreBan(Player player){
		String subPerm1 = "ban.ignore";
		String subPerm2 = "ban.rmvforignore";
		return hasWRperm(player,subPerm1,subPerm2, false);
	}
	
	public static boolean hasIgnoreBan(Player player, World world){
		String subPerm1 = "ban.ignore."+world.getName();
		String subPerm2 = "ban.rmvforignore."+world.getName();
		return hasWRperm(player,subPerm1,subPerm2, false);
	}
	
	public static boolean hasWorldBan(Player player, World world){
		String subPerm1 = "ban.world."+world.getName();
		String subPerm2 = "ban.rmvforworld."+world.getName();
		return hasWRperm(player,subPerm1,subPerm2, false);
	}
	
		
	public static boolean hasServerBanI(OfflinePlayer player){
		String subPerm1 = "ban.world.*";
		String subPerm2 = "ban.rmvforserver";
		if (player instanceof Player){
			return hasWRperm((Player)player,subPerm1,subPerm2, false);
		}else{
			return hasWRperm(player,subPerm1,subPerm2, false);
			}
	}

	public static boolean hasWRperm(Player player,String subPerm1, String subPerm2, boolean def){
		if (HardCoreWorlds.getPerm(subPerm1, player, def)){
			return(!HardCoreWorlds.getPerm(subPerm2, player, def));
		}
		return false;
	}
	
	public static boolean hasWRperm(OfflinePlayer player,String subPerm1, String subPerm2, boolean def){
		if (HardCoreWorlds.getPerm(subPerm1, player, def)){
			return(!HardCoreWorlds.getPerm(subPerm2, player, def));
		}
		return false;
	}
	
	public static boolean playerInList(String playerN){
		return BannedList.contains("Player."+playerN);
	}

	public static void setBanDuration(String playerN, String worldN, int hours){
			BannedList.set("Player."+playerN+".World."+worldN+".Ban Ends", getHour()+hours);
			try {BannedList.save(BannedListFile);} catch (IOException e) {e.printStackTrace();}
	}

	public static void banMessage(Player player, World world){
		if (BanManager.isBanPerm(player, world)){
			player.sendMessage(ChatColor.LIGHT_PURPLE + "You are expelled from world '" + world.getName() + "' forever!");
		}else{
			player.sendMessage(ChatColor.LIGHT_PURPLE + "You are expelled from world '" + world.getName() + "' for about " + BanManager.getTimeLeft(player, world)+ " more hours!");	
		}
	}
	
	public static void banKickIn5(final Player player, final String kickmessage, HardCoreWorlds hcw){
		
		Runnable runit = new Runnable() {
		    public void run() {
		    		player.kickPlayer(kickmessage);
		    		player.setBanned(true);
		    	}
		};
		player.getServer().getScheduler().scheduleSyncDelayedTask(hcw, runit, 100);
	}
}