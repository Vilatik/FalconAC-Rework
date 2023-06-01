package me.liuli.falcon.manager;


import cn.nukkit.utils.Config;

public enum CheckType {
    KILLAURA(CheckCategory.COMBAT),
    KA_BOT(CheckCategory.COMBAT),
    AIMBOT(CheckCategory.COMBAT),
    CRITICALS(CheckCategory.COMBAT),
    VELOCITY(CheckCategory.COMBAT),
    SPEED(CheckCategory.MOVEMENT),
    FLIGHT(CheckCategory.MOVEMENT),
    STRAFE(CheckCategory.MOVEMENT),
    WATER_WALK(CheckCategory.MOVEMENT),
    NOCLIP(CheckCategory.MOVEMENT),
    ILLEGAL_INTERACT(CheckCategory.WORLD),
    FAST_PLACE(CheckCategory.WORLD),
    TIMER(CheckCategory.WORLD),
    NOSWING(CheckCategory.MISC),
    BADPACKETS(CheckCategory.MISC);

    public boolean enable = false;
    public boolean canSmartFlag = false;
    public float addVl = 1;
    public CheckCategory category;
    public Config otherData = new Config();

    private CheckType(CheckCategory category) {
        this.category = category;
    }
}