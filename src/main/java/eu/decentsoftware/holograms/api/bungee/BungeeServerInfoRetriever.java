/*
 *  Copyright (c) 2023, jones (https://jonesdev.xyz) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.decentsoftware.holograms.api.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.BungeeUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeServerInfoRetriever implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        try {
            final ByteArrayDataInput in = ByteStreams.newDataInput(message);

            final String sub = in.readUTF();

            if (sub.equals("PlayerCount")) {
                final String server = in.readUTF();
                final int playerCount = in.readInt();

                if (BungeeUtils.getServerOnlineCache().containsKey(server)) {
                    BungeeUtils.getServerOnlineCache().replace(server, playerCount);
                    return;
                }

                BungeeUtils.getServerOnlineCache().put(server, playerCount);
            }
        } catch (Exception exception) {
            DecentHologramsAPI.get().getPlugin().getLogger().severe("Could not retrieve player count. (Invalid server?)");
        }
    }
}
