package im.cave.ms.network.packet.opcode;

import im.cave.ms.tools.Util;

public enum SendOpcode {
    //Login Server
    LOGIN_STATUS(0x00),
    SERVERLIST(0x01),
    SERVERSTATUS(0x05),//CHECK_USER_LIMIT_RESULT
    CHARLIST(0x06),
    SERVER_IP(0x07),
    AUTH_SUCCESS(0x08),
    PING(0x11),
    CHAR_NAME_RESPONSE(0x0A),
    ADD_NEW_CHAR_ENTRY(0x0B),
    DELETE_CHAR_TIME(0x0D),
    CANCEL_DELETE_CHAR(0x0E),
    OPEN_CREATE_CHAR(0x56),
    SERVER_LIST_BG(0x5D),
    DELETE_CHAR(0x84),

    //Channel Server
    CHANGE_CHANNEL(0x10),
    CPING(0x12),
    OPCODE_TABLE(0x2D),
    CHAR_SLOTS_EXPAND_RESULT(0x4B),
    INVENTORY_OPERATION(0x6F),
    UPDATE_STATS(0x71),
    GIVE_BUFF(0x72),
    REMOVE_BUFF(0x73),
    CHANGE_SKILL_RESULT(0x77),
    FAME_RESPONSE(0x81),
    SHOW_STATUS_INFO(0x83),
    GATHER_ITEM_RESULT(0x9C),
    SORT_ITEM_RESULT(0x9D),
    CHAR_INFO(0x9F),
    PARTY_RESULT(0xA0),
    SERVER_MSG(0xB0),
    PET_AUTO_EAT_MSG(0xBA),
    EVENT_MESSAGE(0xDA),
    SERVER_NOTICE(0xD6),
    DEBUG_MSG(0xDD),
    RESULT_INSTANCE_TABLE(0xe7),
    RANK(0xF7),
    CHARACTER_POTENTIAL_SET(0x102),
    CHARACTER_POTENTIAL_RESET(0x103),
    CHARACTER_HONOR_POINT(0x104),
    CHANGE_CHAR_KEY(0x12E),
    ACCOUNT(0x13C),
    UPDATE_QUEST_EX(0x13B),
    CANCEL_TITLE_EFFECT(0x145),
    EQUIP_ENCHANT(0x16A),
    CASH_SHOP_EVENT_INFO(0x179),
    UPDATE_VOUCHER(0x201),
    CASH_POINT_RESULT(0x219),
    SLOT_EXPAND_RESULT(0x21A),
    INIT_SKILL_MACRO(0x235),
    SET_MAP(0x236),
    SET_CASH_SHOP(0x239),
    SET_CASH_SHOP_INFO(0x23A),
    FIELD_EFFECT(0x244),
    FIELD_MESSAGE(0x245),
    CLOCK(0x24A),
    QUICKSLOT_INIT(0x258),
    QUICK_MOVE(0x26B),
    USER_SIT_REMOTE(0x273),
    SET_SOUL_EFFECT(0x29A),
    SIT_RESULT(0x29B), //sit
    USER_ENTER_FIELD(0x2B5),
    USER_LEAVE_FIELD(0x2B6),
    CHATTEXT(0x2B7),
    BLACK_BOARD(0x2B9),
    SHOW_ITEM_UPGRADE_EFFECT(0x2BC),
    SHOW_ITEM_RELEASE_EFFECT(0x2C0),
    SET_DAMAGE_SKIN(0x2DC),
    CANCEL_CHAIR(0x2E1),
    FAMILIAR_OPERATION(0x2E2),
    HIDDEN_EFFECT_EQUIP(0x316),
    PET_ACTIVATED(0x318),
    ANDROID_CREATED(0x327),
    ANDROID_MOVE(0x328),
    ANDROID_ACTION_SET(0x329),
    ANDROID_MODIFIED(0x32A),
    ANDROID_REMOVED(0x32B),
    REMOTE_MOVE(0x345),
    REMOTE_CLOSE_RANGE_ATTACK(0x346),
    REMOTE_RANGED_ATTACK(0x347),
    REMOTE_MAGIC_ATTACK(348),
    REMOTE_HIT(0x34D),
    REMOTE_EMOTION(0x34E),
    REMOTE_SET_ACTIVE_PORTABLE_CHAIR(0x357),
    REMOTE_AVATAR_MODIFIED(0x358),
    REMOTE_EFFECT(0x359),
    EFFECT(0x383),
    QUEST_RESULT(0x388),
    DISABLE_UI(0x394),
    LOCK_UI(0x395),
    NOTICE_MSG(0x3A2),
    CHAT_MSG(0x3A3),
    IN_GAME_DIRECTION_EVENT(0x3B2),
    SET_DEAD(0x3CB),
    OPEN_DEAD_UI(0x3CC),
    FINAL_ATTACK(0x3D5),
    ELF_TIP(0x405),
    SKILL_COOLTIME_SET(0x40F),
    DAMAGE_SKIN_SAVE_RESULT(0x42D),
    OPEN_WORLDMAP(0x48F),
    SPAWN_SUMMON(0x4B7),
    SUMMON_MOVE(0x4B9),
    SPAWN_MOB(0x4D5),
    REMOVE_MOB(0x4D6),
    MOB_CHANGE_CONTROLLER(0x4D7),
    MOB_MOVE(0x4DE),
    MOB_CONTROL_ACK(0x4DF),
    HP_INDICATOR(0x4EC),
    SPAWN_NPC(0x542),
    REMOVE_NPC(0x543),
    SPAWN_NPC_REQUEST_CONTROLLER(0x545),
    NPC_ANIMATION(0x549),
    DROP_ENTER_FIELD(0x569),
    PICK_UP_DROP(0x56B),
    NPC_TALK(0x741),
    NPC_SHOP_OPEN(0x742),
    NPC_SHOP_RESULT(0x743),
    TRUNK_OPERATION(0x75A),
    MINI_ROOM(0x761),
    CASH_SHOP_QUERY_CASH_RESULT(0X7BE),
    CASH_SHOP_CASH_ITEM_RESULT(0x7BF),
    SIGNIN_REWARDS(0x7EF),
    OPEN_UNITY_PORTAL(0x7F3),
    KEYMAP_INIT(0x84A),
    BATTLE_ANALYSIS(0x879),
    ;
    private final int code;

    SendOpcode(int code) {
        this.code = code;
    }

    public short getValue() {
        return (short) code;
    }

    public static Object getByValue(short op) {
        return Util.findWithPred(values(), sendOpcode -> sendOpcode.code == op);
    }

}
