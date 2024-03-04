package eu.decentsoftware.holograms.api.listeners;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import eu.decentsoftware.holograms.event.DecentHologramsRegisterEvent;
import eu.decentsoftware.holograms.event.HologramClickEvent;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class PlayerListener implements Listener {

    private final DecentHolograms decentHolograms;

    public PlayerListener(DecentHolograms decentHolograms) {
        this.decentHolograms = decentHolograms;
        this.ref();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        S.async(() -> decentHolograms.getHologramManager().updateVisibility(player));
        decentHolograms.getPacketListener().hook(player);
        if (decentHolograms.isUpdateAvailable() && player.hasPermission("dh.admin")) {
            Lang.sendUpdateMessage(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        S.async(() -> decentHolograms.getHologramManager().onQuit(player));
        decentHolograms.getPacketListener().unhook(player);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        // TODO: All holograms (and entities) get hidden on the client, when the client
        //  teleports or respawns. This only seems to be happening on some client versions
        //  so we need to find which versions are affected and only re-show the holograms
        //  to those clients (or on those server versions).
        //  -
        //  For now, this only causes visual glitches where even if a player gets teleported
        //  by a fraction of a block, the holograms still disappear and reappear for them.
        //  -
        //  tl:dr Figure out which versions need this.
        S.async(() -> decentHolograms.getHologramManager().hideAll(player));
    }


    // 视角对着的全息
    private Map<Player,String> viewHolograms = new ConcurrentHashMap<>();


    private Map<Player,Long> tickLog = new ConcurrentHashMap<>();

    public Map<Chunk,List<Hologram>> chunkListMap = new ConcurrentHashMap<>();

    private void ref(){
        chunkListMap.clear();
        decentHolograms.getHologramManager().getHolograms().stream().forEach(hologram -> {
            Chunk chunk = hologram.getLocation().getChunk();
            chunkListMap.putIfAbsent(chunk,new ArrayList<>());
            chunkListMap.get(chunk).add(hologram);
        });
    }


    @EventHandler
    public void onRegisterHd(DecentHologramsRegisterEvent registerEvent){
        this.ref();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent){
        Player player = playerMoveEvent.getPlayer();
        Long orDefault = tickLog.getOrDefault(player, 0L);

        if(chunkListMap.isEmpty()) ref();

        if((System.currentTimeMillis()-orDefault) >= 200L){
            Optional.ofNullable(chunkListMap.get(player.getLocation().getChunk())).ifPresent(list->{
                List<Hologram> holograms = list.stream().collect(Collectors.toList());
                Vector playerDirection = player.getEyeLocation().getDirection();
                double threshold = 0.2; // 0.2弧度

                tickLog.put(player,System.currentTimeMillis());

                holograms.removeIf(hologram -> {
                    Location location = hologram.getLocation();
                    Vector toHologram = location.toVector().subtract(player.getEyeLocation().toVector());
                    double angle = playerDirection.angle(toHologram);
                    return angle>=threshold;
                });

                String currentViewHd = viewHolograms.get(player);


                for (Hologram hologram : holograms) {
                    if(currentViewHd==null) viewHolograms.put(player,hologram.getName());

                    currentViewHd = viewHolograms.get(player);

                    // 如果从一个全息移动到了另一个全息上
                    if(!currentViewHd.equalsIgnoreCase(hologram.getName())){
                        Hologram fromHologram = decentHolograms.getHologramManager().getHologram(currentViewHd);
                        List<Integer> clickableEntityIds = fromHologram.getPage(player).getClickableEntityIds();
                        if(clickableEntityIds.size()>0){
                            fromHologram.onClick(player,clickableEntityIds.get(0),ClickType.HOVER_LEAVE);
                            viewHolograms.put(player,hologram.getName());
                        }
                    }

                    List<Integer> clickableEntityIds = hologram.getPage(player).getClickableEntityIds();
                    if(!clickableEntityIds.isEmpty()){
                        hologram.onClick(player,clickableEntityIds.get(0), ClickType.HOVER);
                    }
                    return;
                }

                if(currentViewHd!=null){
                    Hologram fromHologram = decentHolograms.getHologramManager().getHologram(currentViewHd);
                    List<Integer> clickableEntityIds = fromHologram.getPage(player).getClickableEntityIds();
                    if(clickableEntityIds.size()>0){
                        fromHologram.onClick(player,clickableEntityIds.get(0),ClickType.HOVER_LEAVE);
                        viewHolograms.remove(player);
                    }
                }
            });
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        S.async(() -> decentHolograms.getHologramManager().hideAll(player));
    }

}
