/*     */ package me.DuncanGaming.ChatFilter;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.UUID;
/*     */ import net.md_5.bungee.api.ChatColor;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.player.AsyncPlayerChatEvent;
/*     */ import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
/*     */ import org.bukkit.event.player.PlayerCommandPreprocessEvent;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerMoveEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChatEvent
/*     */   implements Listener
/*     */ {
/*  23 */   private Plugin plugin = (Plugin)ChatFilter.getPlugin(ChatFilter.class);
/*  24 */   public HashMap<Player, Boolean> hasMoved = new HashMap<>();
/*  25 */   public HashMap<UUID, String> lastChat = new HashMap<>();
/*  26 */   public HashMap<UUID, String> lastCmd = new HashMap<>();
/*  27 */   public HashMap<UUID, Long> msgCooldown = new HashMap<>();
/*  28 */   public int msgcooldowntime = this.plugin.getConfig().getInt("delay");
/*  29 */   public HashMap<UUID, Long> relogCooldown = new HashMap<>();
/*  30 */   public int relogcooldowntime = this.plugin.getConfig().getInt("Rejoin-time");
/*     */   
/*     */   public ChatEvent(ChatFilter chatFilter) {}
/*     */   
/*     */   @EventHandler
/*     */   public void onChat(AsyncPlayerChatEvent e) {
/*     */     byte b;
/*     */     int i;
/*     */     String[] arrayOfString;
/*  39 */     for (i = (arrayOfString = e.getMessage().split(" ")).length, b = 0; b < i; ) { String s = arrayOfString[b];
/*     */       
/*  41 */       for (int j = 0; j < this.plugin.getConfig().getStringList("Filtered_Words").size(); j++) {
/*     */         
/*  43 */         if (s.contains(this.plugin.getConfig().getStringList("Filtered_Words").get(j)) && !e.getPlayer().hasPermission("chatfilter.admin") && !e.getPlayer().isOp()) {
/*  44 */           String str = this.plugin.getConfig().getStringList("Filtered_Words").get(j);
/*  45 */           e.setMessage(e.getMessage().replace(str, String.join("", Collections.nCopies(str.length(), "*"))));
/*     */         } 
/*     */       } 
/*     */       
/*     */       b++; }
/*     */ 
/*     */     
/*  52 */     if (!this.hasMoved.containsKey(e.getPlayer())) {
/*  53 */       e.setCancelled(true);
/*  54 */       e.getPlayer().sendMessage(ChatColor.RED + "You must move before chatting");
/*     */       return;
/*     */     } 
/*  57 */     if (!((Boolean)this.hasMoved.get(e.getPlayer())).booleanValue() && !e.getPlayer().hasPermission("chatfilter.admin")) {
/*  58 */       e.setCancelled(true);
/*  59 */       e.getPlayer().sendMessage(ChatColor.RED + "You must move before chatting");
/*     */     } 
/*     */     
/*  62 */     if (this.msgCooldown.containsKey(e.getPlayer().getUniqueId()) && !e.getPlayer().hasPermission("chatfilter.admin")) {
/*  63 */       long secondsleft = ((Long)this.msgCooldown.get(e.getPlayer().getUniqueId())).longValue() / 1000L + this.msgcooldowntime - System.currentTimeMillis() / 1000L;
/*  64 */       if (secondsleft > 0L) {
/*  65 */         e.setCancelled(true);
/*  66 */         e.getPlayer().sendMessage(ChatColor.RED + "You must wait " + secondsleft + " seconds before chatting");
/*     */       } 
/*     */     } else {
/*     */       
/*  70 */       this.msgCooldown.put(e.getPlayer().getUniqueId(), Long.valueOf(System.currentTimeMillis()));
/*     */     } 
/*     */     
/*  73 */     if (!this.lastChat.containsKey(e.getPlayer().getUniqueId())) {
/*  74 */       this.lastChat.put(e.getPlayer().getUniqueId(), e.getMessage());
/*     */       return;
/*     */     } 
/*  77 */     if (((String)this.lastChat.get(e.getPlayer().getUniqueId())).equals(e.getMessage()) && this.plugin.getConfig().getBoolean("Block-Dupe-Messages") && !e.getPlayer().hasPermission("chatfilter.admin")) {
/*  78 */       e.setCancelled(true);
/*  79 */       e.getPlayer().sendMessage(ChatColor.RED + "You can't chat duplicate messages");
/*     */     } 
/*  81 */     this.lastChat.put(e.getPlayer().getUniqueId(), e.getMessage());
/*  82 */     if (this.plugin.getConfig().getInt("delay") != 0) e.getPlayer().hasPermission("chatfilter.admin");
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void sendCommand(PlayerCommandPreprocessEvent e) {
/*  92 */     if (!this.lastCmd.containsKey(e.getPlayer().getUniqueId())) {
/*  93 */       this.lastCmd.put(e.getPlayer().getUniqueId(), e.getMessage());
/*     */       return;
/*     */     } 
/*  96 */     if (((String)this.lastCmd.get(e.getPlayer().getUniqueId())).equals(e.getMessage()) && this.plugin.getConfig().getBoolean("Block-Dupe-Commands") && !e.getPlayer().hasPermission("chatfilter.admin")) {
/*  97 */       e.setCancelled(true);
/*  98 */       e.getPlayer().sendMessage(ChatColor.RED + "You can't send duplicate commands");
/*     */     } 
/* 100 */     this.lastCmd.put(e.getPlayer().getUniqueId(), e.getMessage());
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onJoin(PlayerJoinEvent e) {
/* 106 */     UUID u = e.getPlayer().getUniqueId();
/* 107 */     if (this.plugin.getConfig().getBoolean("Block-Until-Moved")) {
/* 108 */       this.hasMoved.put(e.getPlayer(), Boolean.valueOf(false));
/* 109 */     } else if (!this.plugin.getConfig().getBoolean("Block-Until-Moved")) {
/* 110 */       this.hasMoved.put(e.getPlayer(), Boolean.valueOf(true));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onLogin(AsyncPlayerPreLoginEvent e) {
/* 117 */     UUID u = e.getUniqueId();
/* 118 */     long secondsleft = ((Long)this.relogCooldown.get(e.getUniqueId())).longValue() / 1000L + this.relogcooldowntime - System.currentTimeMillis() / 1000L;
/* 119 */     if (secondsleft > 0L) {
/* 120 */       e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
/* 121 */       e.setKickMessage("You must wait " + secondsleft + " seconds before rejoining");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onLeave(PlayerQuitEvent e) {
/* 128 */     Player u = e.getPlayer();
/* 129 */     if (this.plugin.getConfig().getBoolean("Block-Until-Moved")) {
/* 130 */       this.hasMoved.put(u, Boolean.valueOf(false));
/* 131 */     } else if (!this.plugin.getConfig().getBoolean("Block-Until-Moved")) {
/* 132 */       this.hasMoved.put(u, Boolean.valueOf(true));
/*     */     } 
/* 134 */     if (this.plugin.getConfig().getInt("Rejoin-time") != 0 && !e.getPlayer().hasPermission("chatfilter.admin"))
/*     */     {
/* 136 */       this.relogCooldown.put(e.getPlayer().getUniqueId(), Long.valueOf(System.currentTimeMillis()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onMove(PlayerMoveEvent e) {
/* 146 */     UUID u = e.getPlayer().getUniqueId();
/* 147 */     if (!this.hasMoved.containsKey(e.getPlayer()) && this.plugin.getConfig().getBoolean("Block-Until-Moved")) {
/* 148 */       this.hasMoved.put(e.getPlayer(), Boolean.valueOf(true));
/*     */     }
/* 150 */     if (this.plugin.getConfig().getBoolean("Block-Until-Moved") && !((Boolean)this.hasMoved.get(e.getPlayer())).booleanValue())
/* 151 */       this.hasMoved.put(e.getPlayer(), Boolean.valueOf(true)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Duncan Wijnberg\Downloads\ChatFilter.jar!\me\DuncanGaming\ChatFilter\ChatEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */