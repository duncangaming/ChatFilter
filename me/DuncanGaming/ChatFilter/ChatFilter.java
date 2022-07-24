/*    */ package me.DuncanGaming.ChatFilter;
/*    */ 
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.plugin.PluginManager;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ 
/*    */ public class ChatFilter
/*    */   extends JavaPlugin implements Listener {
/*    */   public void onEnable() {
/* 11 */     PluginManager pm = getServer().getPluginManager();
/* 12 */     pm.registerEvents(new ChatEvent(this), (Plugin)this);
/* 13 */     getServer().getPluginManager().registerEvents(this, (Plugin)this);
/* 14 */     getCommand("chatfilter").setExecutor(new Reload(this));
/* 15 */     loadConfig();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {}
/*    */ 
/*    */   
/*    */   public void loadConfig() {
/* 23 */     getConfig().options().copyDefaults(true);
/* 24 */     saveConfig();
/*    */   }
/*    */ }


/* Location:              C:\Users\Duncan Wijnberg\Downloads\ChatFilter.jar!\me\DuncanGaming\ChatFilter\ChatFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */