package com.Cayviel.HardCoreWorlds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public class FileSetup {
	
	public static void initialize(File f, FileConfiguration fc, Defaultable dI){
		boolean firstrun = false;
		if(!f.exists()){
			try{firstrun = true;
				f.createNewFile();
			} catch(IOException ioe){
				ioe.printStackTrace();
				}
			}
		initfiles(f);
		load(fc, f);
		if (firstrun){
			dI.setDefs(f);
		}
	}
	
	public static void initialize(File f, FileConfiguration fc){
		if(!f.exists()){
			try{f.createNewFile();
			} catch(IOException ioe){
				ioe.printStackTrace();
				}
			}
		initfiles(f);
		load(fc, f);
	}
	
    public static void initfiles(File file) {
    	try {firstRun(file);
    	}catch (Exception e){
    		e.printStackTrace();
    		}
    	}  
    
	private static void firstRun(File file) throws Exception {
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	public static void load(FileConfiguration fc, File f){
		  try {fc.load(f);
		    } catch (FileNotFoundException e1) {e1.printStackTrace();
		    } catch (IOException e1) {e1.printStackTrace();
		    } catch (InvalidConfigurationException e1) {e1.printStackTrace();
			}
	}
	
	public static void saveconfig(FileConfiguration fc, File f){
		try {fc.save(f);
		} catch (IOException e) {e.printStackTrace();
		}
	}
	
	
}
