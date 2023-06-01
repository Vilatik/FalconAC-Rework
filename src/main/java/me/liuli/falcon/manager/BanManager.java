package me.liuli.falcon.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;


import me.liuli.falcon.FalconAC;
import me.liuli.falcon.cache.Configuration;
import me.liuli.falcon.utils.OtherUtil;

import java.io.File;
import java.util.UUID;

public class BanManager {
    private static Config banJSON;
    private static String dataPath;

    public static void loadBanData() {
        dataPath = FalconAC.plugin.getDataFolder().getPath() + "/data/ban.json";
        if (!new File(dataPath).exists()) {
            OtherUtil.writeFile(dataPath, "{}");
        }
        banJSON = new Config(FalconAC.plugin.getDataFolder().getPath() + "/data/ban.json", Config.JSON);
    }

    public static void addBan(Player player, long expire) {
        String banid = banIdGen(player.getUniqueId());
        banJSON.set(banid, expire);
        saveBanData();
        checkBan(player);
    }

    public static void removeBan(String banid) {
        String upperId=banid.toUpperCase();
        if(banJSON.exists(upperId)) {
            banJSON.remove(upperId);
            FalconAC.plugin.getLogger().warning("UNBAN BANID:"+upperId+" FROM BANID.");
        }else{
            UUID offlineUUID=Server.getInstance().getOfflinePlayer(banid).getUniqueId();
            if(offlineUUID!=null){
                String offlineBanId=banIdGen(offlineUUID);
                banJSON.remove(offlineBanId);
                FalconAC.plugin.getLogger().warning("UNBAN BANID:"+offlineBanId+" FROM PLAYERNAME("+banid+").");
            }else {
                FalconAC.plugin.getLogger().warning("CANNOT FIND PLAYER WITH NAME "+banid+".");
            }
        }
        saveBanData();
    }

    public static void checkBan(Player player) {
        String banid = banIdGen(player.getUniqueId());
        Long expire = banJSON.getLong(banid);
        if (expire != null) {
            if (OtherUtil.getTime() > expire && expire != -1) {
                banJSON.remove(banid);
                saveBanData();
            } else {
                String time;
                if (expire == -1) {
                    time = "FOREVER";
                } else {
                    time = OtherUtil.t2s(expire - OtherUtil.getTime());
                }
                AnticheatManager.kick(player,Configuration.LANG.BAN_REASON.proc(new String[]{time, banid}));
            }
        }
    }

    public static void saveBanData() {
        banJSON.save();
    }

    private static String banIdGen(UUID uuid) {
        return uuid.toString().replaceAll("-", "").toUpperCase();
    }
}
