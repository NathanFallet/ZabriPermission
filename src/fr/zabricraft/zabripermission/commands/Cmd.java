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

package fr.zabricraft.zabripermission.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.zabricraft.zabripermission.ZabriPermission;
import fr.zabricraft.zabripermission.utils.ZabriGroup;
import fr.zabricraft.zabripermission.utils.ZabriPlayer;

public class Cmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 0){
			if(args[0].equalsIgnoreCase("list")){
				String list = "";
				for(String g : ZabriPermission.getInstance().getGroups()){
					if(!list.isEmpty()){
						list += ", ";
					}
					list += g;
				}
				sender.sendMessage("§eGroupes : "+list);
			}else if(args[0].equalsIgnoreCase("add")){
				if(args.length == 2){
					if(ZabriPermission.getInstance().getGroup(args[1]) == null){
						ZabriPermission.getInstance().addGroup(args[1]);
						sender.sendMessage("§aLe groupe §e"+args[1]+" §aa bien été ajouté !");
					}else{
						sender.sendMessage("§cCe groupe existe déjà !");
					}
				}else{
					sender.sendMessage("§c/"+label+" add <nom du groupe>");
				}
			}else if(args[0].equalsIgnoreCase("del")){
				if(args.length == 2){
					ZabriGroup g = ZabriPermission.getInstance().getGroup(args[1]);
					if(g != null){
						if(!g.isDefault()){
							ZabriPermission.getInstance().deleteGroup(g);
							sender.sendMessage("§aLe groupe §e"+g.getName()+" §aa bien été supprimé !");
						}else{
							sender.sendMessage("§cVous ne pouvez pas supprimer le groupe par défaut !");
						}
					}else{
						sender.sendMessage("§cCe groupe n'existe pas !");
					}
				}else{
					sender.sendMessage("§c/"+label+" del <groupe>");
				}
			}else if(args[0].equalsIgnoreCase("info")){
				if(args.length == 2){
					ZabriGroup g = ZabriPermission.getInstance().getGroup(args[1]);
					if(g != null){
						sender.sendMessage("§6-- Groupe : "+g.getName()+" --\n"
								+ "§ePrefixe : §r"+g.getPrefix()+"§r\n"
								+ "§eSuffixe : §r"+g.getSuffix()+"§r\n"
								+ "§ePermissions : "+g.getPermissions().toString()+"\n"
								+ "§eHéritances : "+g.getInherits().toString());
					}else{
						sender.sendMessage("§cCe groupe n'existe pas !");
					}
				}else{
					sender.sendMessage("§c/"+label+" info <groupe>");
				}
			}else if(args[0].equalsIgnoreCase("prefix")){
				if(args.length >= 3){
					ZabriGroup g = ZabriPermission.getInstance().getGroup(args[1]);
					if(g != null){
						String prefix = "";
						for(int i = 2; i < args.length; i++){
							prefix += " "+args[i];
						}
						prefix = prefix.substring(1);
						if(prefix.startsWith("'") && prefix.endsWith("'")){
							prefix = prefix.substring(1, prefix.length()-1);
						}
						g.setPrefix(prefix);
						ZabriPermission.getInstance().reloadPerms();
						sender.sendMessage("§aLe prefixe a bien été changé !");
					}else{
						sender.sendMessage("§cCe groupe n'existe pas !");
					}
				}else{
					sender.sendMessage("§c/"+label+" prefix <groupe> <prefixe>");
				}
			}else if(args[0].equalsIgnoreCase("suffix")){
				if(args.length >= 3){
					ZabriGroup g = ZabriPermission.getInstance().getGroup(args[1]);
					if(g != null){
						String suffix = "";
						for(int i = 2; i < args.length; i++){
							suffix += " "+args[i];
						}
						suffix = suffix.substring(1);
						if(suffix.startsWith("'") && suffix.endsWith("'")){
							suffix = suffix.substring(1, suffix.length()-1);
						}
						g.setSuffix(suffix);
						ZabriPermission.getInstance().reloadPerms();
						sender.sendMessage("§aLe suffixe a bien été changé !");
					}else{
						sender.sendMessage("§cCe groupe n'existe pas !");
					}
				}else{
					sender.sendMessage("§c/"+label+" suffix <groupe> <suffixe>");
				}
			}else if(args[0].equalsIgnoreCase("addp")){
				if(args.length == 3){
					ZabriGroup g = ZabriPermission.getInstance().getGroup(args[1]);
					if(g != null){
						if(!g.getPermissions().contains(args[2])){
							g.addPermission(args[2]);
							ZabriPermission.getInstance().reloadPerms();
							sender.sendMessage("§aLa permission a bien été ajouté !");
						}else{
							sender.sendMessage("§cCe groupe possède déjà cette permission !");
						}
					}else{
						sender.sendMessage("§cCe groupe n'existe pas !");
					}
				}else{
					sender.sendMessage("§c/"+label+" addp <groupe> <permission>");
				}
			}else if(args[0].equalsIgnoreCase("delp")){
				if(args.length == 3){
					ZabriGroup g = ZabriPermission.getInstance().getGroup(args[1]);
					if(g != null){
						if(g.getPermissions().contains(args[2])){
							g.removePermission(args[2]);
							ZabriPermission.getInstance().reloadPerms();
							sender.sendMessage("§aLa permission a bien été supprimé !");
						}else{
							sender.sendMessage("§cCe groupe ne possède pas cette permission !");
						}
					}else{
						sender.sendMessage("§cCe groupe n'existe pas !");
					}
				}else{
					sender.sendMessage("§c/"+label+" delp <groupe> <permission>");
				}
			}else if(args[0].equalsIgnoreCase("addi")){
				if(args.length == 3){
					ZabriGroup g = ZabriPermission.getInstance().getGroup(args[1]);
					if(g != null){
						if(!g.getInherits().contains(args[2])){
							g.getInherits().add(args[2]);
							ZabriPermission.getInstance().reloadPerms();
							sender.sendMessage("§aL'héritance a bien été ajouté !");
						}else{
							sender.sendMessage("§cCe groupe possède déjà cette héritance !");
						}
					}else{
						sender.sendMessage("§cCe groupe n'existe pas !");
					}
				}else{
					sender.sendMessage("§c/"+label+" addi <groupe> <héritance>");
				}
			}else if(args[0].equalsIgnoreCase("deli")){
				if(args.length == 3){
					ZabriGroup g = ZabriPermission.getInstance().getGroup(args[1]);
					if(g != null){
						if(g.getInherits().contains(args[2])){
							g.getInherits().remove(args[2]);
							ZabriPermission.getInstance().reloadPerms();
							sender.sendMessage("§aL'héritance a bien été supprimé !");
						}else{
							sender.sendMessage("§cCe groupe ne possède pas cette héritance !");
						}
					}else{
						sender.sendMessage("§cCe groupe n'existe pas !");
					}
				}else{
					sender.sendMessage("§c/"+label+" deli <groupe> <héritance>");
				}
			}else if(args[0].equalsIgnoreCase("player")){
				if(args.length == 3){
					Player p = Bukkit.getPlayer(args[1]);
					if(p != null && p.isOnline()){
						ZabriPlayer zp = ZabriPermission.getInstance().getPlayer(p.getUniqueId());
						if(zp != null){
							ZabriGroup g = ZabriPermission.getInstance().getGroup(args[2]);
							if(g != null){
								zp.setGroup(g.getName());
								ZabriPermission.getInstance().reloadPerms();
								sender.sendMessage("§e"+p.getName()+" §aest maintenant §e"+g.getName()+" §a!");
							}else{
								sender.sendMessage("§cCe groupe n'existe pas !");
							}
						}else{
							sender.sendMessage("§cJoueur introuvable !");
						}
					}else{
						sender.sendMessage("§cJoueur introuvable !");
					}
				}else{
					sender.sendMessage("§c/"+label+" player <pseudo> <groupe>");
				}
			}else if(args[0].equalsIgnoreCase("playeraddp")){
				if(args.length == 3){
					Player p = Bukkit.getPlayer(args[1]);
					if(p != null && p.isOnline()){
						ZabriPlayer zp = ZabriPermission.getInstance().getPlayer(p.getUniqueId());
						if(zp != null){
							if(!zp.getPermissions().contains(args[2])){
								zp.getPermissions().add(args[2]);
								ZabriPermission.getInstance().reloadPerms();
								sender.sendMessage("§aLa permission a bien été ajouté !");
							}else{
								sender.sendMessage("§cCe joueur possède déjà cette permission !");
							}
						}else{
							sender.sendMessage("§cJoueur introuvable !");
						}
					}else{
						sender.sendMessage("§cJoueur introuvable !");
					}
				}else{
					sender.sendMessage("§c/"+label+" playeraddp <pseudo> <permission>");
				}
			}else if(args[0].equalsIgnoreCase("playerdelp")){
				if(args.length == 3){
					Player p = Bukkit.getPlayer(args[1]);
					if(p != null && p.isOnline()){
						ZabriPlayer zp = ZabriPermission.getInstance().getPlayer(p.getUniqueId());
						if(zp != null){
							if(zp.getPermissions().contains(args[2])){
								zp.getPermissions().remove(args[2]);
								ZabriPermission.getInstance().reloadPerms();
								sender.sendMessage("§aLa permission a bien été supprimé !");
							}else{
								sender.sendMessage("§cCe joueur ne possède pas cette permission !");
							}
						}else{
							sender.sendMessage("§cJoueur introuvable !");
						}
					}else{
						sender.sendMessage("§cJoueur introuvable !");
					}
				}else{
					sender.sendMessage("§c/"+label+" playerdelp <pseudo> <permission>");
				}
			}else{
				sendHelp(sender, label);
			}
		}else{
			sendHelp(sender, label);
		}
		return true;
	}
	
	public void sendHelp(CommandSender sender, String label){
		sender.sendMessage("§e/"+label+" list : Lister les groupes\n"
				+ "§e/"+label+" add <nom du groupe> : Ajouter un groupe\n"
				+ "§e/"+label+" del <groupe> : Supprimer un groupe\n"
				+ "§e/"+label+" info <groupe> : Afficher les informations d'un groupe\n"
				+ "§e/"+label+" prefix <groupe> <prefixe> : Changer le prefix d'un groupe\n"
				+ "§e/"+label+" suffix <groupe> <suffixe> : Changer le suffixe d'un groupe\n"
				+ "§e/"+label+" addp <groupe> <permission> : Ajouter une permission à un groupe\n"
				+ "§e/"+label+" delp <groupe> <permission> : Supprimer une permission à un groupe\n"
				+ "§e/"+label+" addi <groupe> <héritance> : Ajouter une héritance à un groupe\n"
				+ "§e/"+label+" deli <groupe> <héritance> : Supprimer une héritance à un groupe\n"
				+ "§e/"+label+" player <pseudo> <groupe> : Changer le groupe d'un joueur\n"
				+ "§e/"+label+" playeraddp <pseudo> <permission> : Ajouter une permission à un joueur\n"
				+ "§e/"+label+" playerdelp <pseudo> <permission> : Supprimer une permission à un joueur\n");
	}

}
