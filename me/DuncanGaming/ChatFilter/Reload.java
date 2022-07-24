/*    */ package me.DuncanGaming.ChatFilter;
/*    */ 
/*    */ import net.md_5.bungee.api.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Reload
/*    */   implements CommandExecutor
/*    */ {
/*    */   ChatFilter plugin;
/*    */   
/*    */   public Reload(ChatFilter main) {
/* 16 */     this.plugin = main;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
/* 24 */     if (label.equalsIgnoreCase("chatfilter")) {
/* 25 */       if (args.length != 0) {
/*    */         
/* 27 */         if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("chatfilter.admin"))
/*    */         {
/* 29 */           this.plugin.reloadConfig();
/*    */         }
/*    */       }
/*    */       else {
/*    */         
/* 34 */         sender.sendMessage(ChatColor.RED + "Please specify what command you want to run");
/*    */       } 
/*    */     }
/*    */     
/* 38 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Duncan Wijnberg\Downloads\ChatFilter.jar!\me\DuncanGaming\ChatFilter\Reload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */