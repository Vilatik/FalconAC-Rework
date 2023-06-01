package me.liuli.falcon.cache;

import me.liuli.falcon.FalconAC;
import me.liuli.falcon.manager.CheckCategory;
import me.liuli.falcon.manager.CheckType;
import me.liuli.falcon.manager.PunishResult;
import me.liuli.falcon.utils.OtherUtil;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;

import java.io.File;
import java.util.Map;

public class Configuration {
    public static boolean checkOp, consoleDebug, playerDebug, flag, punishBoardcast;
    public static int ban;
    private static Config langJSON, configJSON;

    //global values
    public static int accountForTeleports;
    public static boolean smartFlag;

    public static void loadConfig() {
        if (!new File(FalconAC.plugin.getDataFolder().getPath() + "/lang.yml").exists()) {
            OtherUtil.writeFile(FalconAC.plugin.getDataFolder().getPath() + "/lang.yml", OtherUtil.getTextFromResource("lang.yml"));
        }
        langJSON =  new Config(FalconAC.plugin.getDataFolder().getPath() + "/lang.yml", Config.YAML);
        LANG.ALERT_PREFIX.setStr(langJSON.getString("ALERT_PREFIX"));
        LANG.WARNING.setStr(langJSON.getString("WARNING"));
        LANG.DEBUG.setStr(langJSON.getString("DEBUG"));
        LANG.KICK.setStr(langJSON.getString("KICK"));
        LANG.BAN.setStr(langJSON.getString("BAN"));
        LANG.KICK_REASON.setStr(langJSON.getString("KICK_REASON"));
        LANG.BAN_REASON.setStr(langJSON.getString("BAN_REASON"));

        if (!new File(FalconAC.plugin.getDataFolder().getPath() + "/config.yml").exists()) {
            OtherUtil.writeFile(FalconAC.plugin.getDataFolder().getPath() + "/config.yml", OtherUtil.getTextFromResource("config.yml"));
        }
        configJSON = new Config(FalconAC.plugin.getDataFolder().getPath() + "/config.yml", Config.YAML);
        if (configJSON.getInt("config-version") != FalconAC.CONFIG_VERSION) {
            throw new IllegalArgumentException("WRONG CONFIG VERSION!PLEASE DELETE/UPDATE YOUR CONFIG!");
        }
        // JSONObject checksJSON = configJSON.get("checks"), moduleJSON = configJSON.getJSONObject("modules");

        //load common configs
        checkOp = configJSON.getBoolean("checkOp");
        consoleDebug = configJSON.getSection("debug").getBoolean("console");
        playerDebug = configJSON.getSection("debug").getBoolean("player");
        flag = configJSON.getBoolean("flag");
        ban = configJSON.getInt("ban");
        punishBoardcast = configJSON.getBoolean("punish-boardcast");

        //load checks
        loadCategory(CheckCategory.COMBAT, configJSON.getSection("checks").getSection("combat"));
        loadCategory(CheckCategory.MOVEMENT, configJSON.getSection("checks").getSection("movement"));
        loadCategory(CheckCategory.WORLD, configJSON.getSection("checks").getSection("world"));
        loadCategory(CheckCategory.MISC, configJSON.getSection("checks").getSection("misc"));

        //load modules
        //combat
        loadType(CheckType.KILLAURA, configJSON.getSection("modules").getSection("killaura"));
        loadType(CheckType.KA_BOT, configJSON.getSection("modules").getSection("killaura_bot"));
        loadType(CheckType.AIMBOT, configJSON.getSection("modules").getSection("aimbot"));
        loadType(CheckType.CRITICALS, configJSON.getSection("modules").getSection("critical"));
        loadType(CheckType.VELOCITY, configJSON.getSection("modules").getSection("velocity"));
        //movement
        loadType(CheckType.SPEED, configJSON.getSection("modules").getSection("speed"));
        loadType(CheckType.FLIGHT, configJSON.getSection("modules").getSection("flight"));
        loadType(CheckType.STRAFE, configJSON.getSection("modules").getSection("strafe"));
        loadType(CheckType.WATER_WALK, configJSON.getSection("modules").getSection("waterwalk"));
        loadType(CheckType.NOCLIP, configJSON.getSection("modules").getSection("noclip"));
        //world
        loadType(CheckType.ILLEGAL_INTERACT, configJSON.getSection("modules").getSection("illegalinteract"));
        loadType(CheckType.FAST_PLACE, configJSON.getSection("modules").getSection("fastplace"));
        loadType(CheckType.TIMER, configJSON.getSection("modules").getSection("timer"));
        //misc
        loadType(CheckType.NOSWING, configJSON.getSection("modules").getSection("noswing"));
        loadType(CheckType.BADPACKETS, configJSON.getSection("modules").getSection("badpackets"));

        ConfigSection globalValues=configJSON.getSection("global");
        accountForTeleports=globalValues.getInt("accountForTeleports");
        smartFlag=globalValues.getBoolean("smartFlag");

        //register smartFlag
        CheckType.VELOCITY.canSmartFlag=true;
        CheckType.NOCLIP.canSmartFlag=true;
    }

    private static void loadCategory(CheckCategory category, ConfigSection configSection) {
        category.vl = configSection.getLong("vl");
        category.minusVl = configSection.getLong("vl-minus");
        category.passMinus = configSection.getLong("pass-minus");
        category.flagVl = configSection.getLong("flag");
        category.warnVl = configSection.getLong("warn");
        category.result = PunishResult.valueOf(configSection.getString("result"));
    }

    private static void loadType(CheckType checkType, ConfigSection configSection) {
        checkType.enable = configSection.getBoolean("enable");
        configSection.remove("enable");
        checkType.addVl = configSection.getLong("vl");
        configSection.remove("vl");
        for (Map.Entry<String, Object> entry : configSection.entrySet()) {
            checkType.otherData.set(entry.getKey(), entry.getValue());
        }
    }

    public enum LANG {
        ALERT_PREFIX, WARNING, DEBUG, KICK, BAN, KICK_REASON, BAN_REASON;
        private String str = "";

        public String proc(String[] args) {
            String s = new String(this.str);
            int count = 1;
            for (String arg : args) {
                s = s.replaceAll("%" + count, arg);
                count++;
            }
            return s;
        }

        public void setStr(String str) {
            this.str = str.replaceAll("&", "§");
        }

        public String proc() {
            return this.str;
        }
    }
}
