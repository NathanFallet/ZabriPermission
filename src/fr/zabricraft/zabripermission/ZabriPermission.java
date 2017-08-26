/*
 *  Copyright (C) 2017 FALLET Nathan
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 */

package fr.zabricraft.zabripermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.zabricraft.zabripermission.commands.Cmd;
import fr.zabricraft.zabripermission.events.PlayerJoin;
import fr.zabricraft.zabripermission.events.PlayerQuit;
import fr.zabricraft.zabripermission.utils.Metrics;
import fr.zabricraft.zabripermission.utils.Metrics.Graph;
import fr.zabricraft.zabripermission.utils.Metrics.Plotter;
import fr.zabricraft.zabripermission.utils.Metrics2;
import fr.zabricraft.zabripermission.utils.Updater;
import fr.zabricraft.zabripermission.utils.ZabriGroup;
import fr.zabricraft.zabripermission.utils.ZabriPlayer;

public class ZabriPermission extends JavaPlugin {
	
	private static ZabriPermission instance;
	
	public static ZabriPermission getInstance(){
		return instance;
	}
	
	private ArrayList<ZabriPlayer> players = new ArrayList<ZabriPlayer>();
	private ArrayList<ZabriGroup> groups = new ArrayList<ZabriGroup>();
	
	public ZabriPlayer getPlayer(UUID uuid){
		for(ZabriPlayer current : players){
			if(current.getUuid().equals(uuid)){
				return current;
			}
		}
		return null;
	}
	
	public ArrayList<String> getGroups(){
		ArrayList<String> result = new ArrayList<String>();
		for(ZabriGroup g : groups){
			result.add(g.getName());
		}
		return result;
	}
	
	public ZabriGroup getGroup(String name){
		for(ZabriGroup current : groups){
			if(current.getName().equalsIgnoreCase(name)){
				return current;
			}
		}
		return null;
	}
	
	public ZabriGroup getDefaultGroup(){
		for(ZabriGroup current : groups){
			if(current.isDefault()){
				return current;
			}
		}
		return null;
	}
	
	public void addGroup(String name){
		groups.add(new ZabriGroup(name));
	}
	
	public void deleteGroup(ZabriGroup g){
		if(g != null){
			g.getFile().delete();
			groups.remove(g);
		}
	}
	
	public void reloadPerms(){
		for(Player p : Bukkit.getOnlinePlayers()){
			ZabriPlayer zp = getPlayer(p.getUniqueId());
			if(zp != null){
				zp.save();
				p.removeAttachment(zp.getAttachment());
				players.remove(zp);
			}
			initPlayer(p);
		}
	}
	
	public void initPlayer(Player p){
		players.add(new ZabriPlayer(p));
	}
	
	public void uninitPlayer(ZabriPlayer p){
		if(players.contains(p)){
			p.save();
			players.remove(p);
		}
	}
	
	public void onEnable(){
		instance = this;
		
		saveDefaultConfig();
		
		File dir = new File("plugins/ZabriPermission/groups/");
		if(dir != null && dir.exists()){
			for(File f : dir.listFiles()){
				if(f.getName().endsWith(".yml")){
					addGroup(f.getName().substring(0, f.getName().length()-4));
				}
			}
		}
		if(getDefaultGroup() == null){
			ZabriGroup zg = getGroup("Default");
			if(zg == null){
				zg = new ZabriGroup("Default");
				zg.setPrefix("&e");
				groups.add(zg);
			}
			zg.setDefault(true);
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			initPlayer(p);
		}
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new PlayerQuit(), this);
		
		getCommand("zabripermission").setExecutor(new Cmd());
		getCommand("zabripermission").setAliases(Arrays.asList("zp"));
		getCommand("zabripermission").setPermissionMessage("§cVous devez avoir la permission §4zabripermission.use §cpour utiliser cette commande.");
		
		if(getConfig().getBoolean("change-tablist-name")){
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
				@Override
				public void run() {
					for(Player p : Bukkit.getOnlinePlayers()){
						p.setPlayerListName(p.getDisplayName());
					}
				}
			}, 0, 20);
		}
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				Updater.checkForUpdate(getInstance());
			}
		}, 0, 18000);
		try {
	        Metrics metrics = new Metrics(this);
	        
	        Graph graph1 = metrics.createGraph("Nombre de groupes");
	        
	        graph1.addPlotter(new Plotter(groups.size()+" groupe"+(groups.size() > 1 ? "s" : "")){
				@Override
				public int getValue() {
					return 1;
				}
	        });
	        
	        metrics.start();
	        
	        Metrics2 metrics2 = new Metrics2(this);
	        metrics2.addCustomChart(new Metrics2.SimplePie("groups_count") {
				@Override
				public String getValue() {
					return (groups.size()+" groupe"+(groups.size() > 1 ? "s" : ""));
				}
			});
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void onDisable(){
		for(ZabriPlayer p : players){
			p.save();
		}
		players.clear();
		for(ZabriGroup g : groups){
			g.save();
		}
		groups.clear();
	}

}
