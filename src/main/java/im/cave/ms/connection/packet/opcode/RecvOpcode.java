package im.cave.ms.connection.packet.opcode;

public enum RecvOpcode {
    CUSTOM_LOGIN(0x7777),

    UNK1(0x66),
    BEFORE_LOGIN(0x67),
    SDO_LOGIN_REQUEST(0x69),
    SERVERSTATUS_REQUEST(0x6A),
    CHARLIST_REQUEST(0x6B),
    CLIENT_LOAD_DONE(0x6E),
    USER_ENTER_SERVER(0x71),
    CHAR_SELECTED(0x72), //没变
    SERVERLIST_REQUEST2(0x74), // 75 - 1
    CHECK_CHAR_NAME_REQUEST(0x75), //77 -2
    RETURN_SELECT_WORLD_LAYOUT(0x76),
    CREATE_CHAR_REQUEST(0x7E), //80 - 2
    DELETE_CHAR(0x82), // 84 - 2
    DELETE_CHAR_CONFIRM(0x83), //
    CANCEL_DELETE_CHAR(0x84), //86 - 2
    PONG(0x99), //9C - 3
    CPONG(0x9A), //9D-3
    ERROR_PACKET(0x9C),//
    SHARE_INFORMATION_RESULT(0x9E), // 00 or 01 bool
    CLIENT_START(0x9F),//
    CLIENT_ERROR(0xA2),
    SET_BURNING_CHAR(0XA7),//
    AFTER_CREATE_CHAR(0xA8),//AB-3
    SET_TOS(0xB9),//
    SET_ACCOUNT_GENDER(0xBA),//
    USER_CASH_POINT_REQUEST(0xC2),//c5-3
    USER_SLOT_EXPAND_REQUEST(0xC3),
    USER_TRANSFER_FIELD_REQUEST(0xCB), //c8+3
    USER_TRANSFER_CHANNEL_REQUEST(0xCC), //cc-3
    OPEN_CREATE_CHAR_LAYOUT(0XCC),
    MIGRATE_TO_CASH_SHOP_REQUEST(0xD0), //CD+3
    PLAYER_MOVE(0XD6),
    USER_SIT_REQUEST(0xD7),
    USER_PORTABLE_CHAIR_SIT_REQUEST(0xD8),
    CLOSE_RANGE_ATTACK(0xDB),
    RANGED_ATTACK(0xDC),
    MAGIC_ATTACK(0xDD),
    PLAYER_BODY_ATTACK(0xDE),
    USER_AREA_DOT_ATTACK(0xDF),
    USER_MOVING_SHOOT_ATTACK_PREPARE(0xE0),
    USER_ATTACK_USER(0xE1),
    CHAR_HIT(0xE2),
    GENERAL_CHAT(0xE4),
    CHAR_EMOTION(0xE7),
    USER_ACTIVATE_EFFECT_ITEM(0xE9),
    USER_ACTIVATE_NICK_ITEM(0xEB),
    USER_ACTIVATE_DAMAGE_SKIN(0xEC),
    USER_ACTIVATE_DAMAGE_SKIN__PREMIUM(0xED),
    USER_ACTIVATE_DAMAGE_SKIN_PREMIUM(0xEE),
    USER_DAMAGE_SKIN_SAVE_REQUEST(0xF0),
    USER_SELECT_NPC(0xF7),
    USER_SCRIPT_MESSAGE_ANSWER(0xF8),
    USER_SHOP_REQUEST(0xF9),
    TRUNK_OPERATION(0xFA),
    EXPRESS_REQUEST(0xFC),
    USER_GATHER_ITEM_REQUEST(0x107),
    USER_SORT_ITEM_REQUEST(0x108),
    USER_CHANGE_SLOT_POSITION_REQUEST(0x109),
    USER_CHANGE_BAG_SLOT_POSITION_REQUEST(0x10C), //todo
    USER_STAT_CHANGE_ITEM_USE_REQUEST(0x10E),
    USER_STAT_CHANGE_ITEM_CANCEL_REQUEST(0x10F),
    USER_PET_FOOD_ITEM_USE_REQUEST(0x112),
    USER_SCRIPT_ITEM_USE_REQUEST(0x114),
    USER_RECIPE_OPEN_ITEM_USE_REQUEST(0x115),
    USER_CONSUME_CASH_ITEM_USE_REQUEST(0x116),
    USER_CASH_PET_PICK_UP_ON_OFF_REQUEST(0x118),
    USER_CASH_PET_SKILL_SETTING_REQUEST(0x119),
    USER_PORTAL_SCROLL_USE_REQUEST(0x127),
    USER_FIELD_TRANSFER_REQUEST(0x128),
    USER_UPGRADE_ITEM_USE_REQUEST(0x129),
    USER_UPGRADE_ASSIST_ITEM_USE_REQUEST(0x12A),
    USER_HYPER_UPGRADE_ITEM_USE_REQUEST(0x12B),
    USER_FLAME_ITEM_USE_REQUEST(0x12C),
    USER_ITEM_OPTION_UPGRADE_ITEM_USE_REQUEST(0x131),
    USER_ADDITIONAL_OPT_UPGRADE_ITEM_USE_REQUEST(0x132),
    EQUIP_ENCHANT_REQUEST(0x139),
    //13E ITEM_BAG
    USER_ITEM_RELEASE_REQUEST(0x13F),
    USER_ABILITY_UP_REQUEST(0x148),
    USER_ABILITY_MASS_UP_REQUEST(0x149),
    CHANGE_STAT_REQUEST(0x14B),
    USER_SKILL_UP_REQUEST(0x150),
    USER_SKILL_USE_REQUEST(0x151),
    USER_SKILL_CANCEL_REQUEST(0x153),
    USER_ADD_FAME_REQUEST(0x159),
    CHAR_INFO_REQUEST(0x15C),
    USER_ACTIVATE_PET_REQUEST(0x15D),//
    USER_REGISTER_PET_AUTO_BUFF_REQUEST(0x15E),
    PORTAL_SPECIAL(0x160),
    USER_QUEST_REQUEST(0x169),
    USER_MACRO_SYS_DATA_MODIFIED(0x171),
    USER_LOTTERY_ITEM_USE_REQUEST(0x173),
    USER_REQUEST_INSTANCE_TABLE(0x180),
    USER_REQUEST_CHARACTER_POTENTIAL_SKILL_RAND_SET_UI(0X193),
    GROUP_MESSAGE(0x1AD),
    WHISPER(0x1B0),//
    MESSENGER(0x1B1),
    CHAT_ROOM(0x1B2),//
    TRADE_ROOM(0x1B3),
    PARTY_REQUEST(0x1B5),
    PARTY_INVITE_RESPONSE(0x1B6),
    GUILD_REQUEST(0x1BB),
    FRIEND_REQUEST(0x1C9),//
    MAPLE_NOTES_REQUEST(0x1CB),
    CHANGE_KEYMAP(0x1D2), //1d4 -2
    DODGE(0x1EE),// skillId  + tick 闪避
    USER_HYPER_SKILL_UP_REQUEST(0x206),//209-3
    USER_HYPER_SKILL_RESET_REQUEST(0x207),//20A-3
    USER_HYPER_STAT_UP_REQUEST(0x208), //20B-3
    USER_HYPER_STAT_RESET_REQUEST(0x209), //20C-3
    USER_REQUEST_CHANGE_MOB_ZONE_STATE(0x210),//
    CHANGE_CHAR_REQUEST(0x218),//-3
    CHECK_TRICK_OR_TREAT_REQUEST(0x21A),
    CHAR_NAME(0x21B), //charId + charName
    ANDROID_SHOP_REQUEST(0x22D),//-3
    COMBO_KILL_CHECK(0x230), //
    FAMILIAR(0x248),
    USER_SOUL_EFFECT_REQUEST(0x24A),
    LIE_DETECTOR_TEST(0x273),
    USER_AVATAR_MODIFY_COUPON_USE_REQUEST(0x27A), //-4
    PET_MOVE(0x27C), //280-4
    PET_ACTION_SPEAK(0x27D),//
    PET_INTERACTION_REQUEST(0x27E),//
    PET_DROP_PICK_UP_REQUEST(0x27F),//
    PET_SET_EXCEPTION_LIST(0x285),
    PET_FOOD_ITEM_USE_REQUEST(0x286),
    SUMMON_MOVE(0x28D),//291-4
    ANDROID_MOVE(0x2A3),//-4
    ANDROID_ACTION_SET(0x2A4),//
    CHANGE_QUICKSLOT(0x2AA), // 2AE-4
    AFTER_INV_OP(0x2AB), //C9 07 00 00 | tick : C4 2D 68 01 | 00 00 15 00 00 00
    RUNNING_PROCESS(0x2B2), //SCAN_RUNNING_PROCESS
    SEND_MAPLE_NOTES(0x2B7),
    UPDATE_TICK(0x330), //330 - 10
    SIGN_IN(0x340), // ?
    SKILL_COMMAND_LOCK(0x33B), //34d-12
    UNITY_PORTAL_REQUEST(0x343),//355-12
    ACHIEVEMENT(0x361),
    SKILL_COOL_IN_TEN_SECOND(0x38F),//
    POTION_POT_USE_REQUEST(0x3AE), //3b6-8
    POTION_POT_ADD_REQUEST(0x3AF),//
    POTION_POT_OPTION_SET_REQUEST(0x3B0),//
    POTION_POT_INC_REQUEST(0x3B1),//
    USER_OPEN_MYSTERY_EGG(0x3DF),//
    WORLD_MAP_TRANSFER(0x3E8),//3f1-9
    OPEN_WORLD_MAP(0x3F0),//3f9 - 9
    MOB_MOVE(0x41B), //424-9
    MOB_APPLY_CTRL(0x41C),//
    NPC_ANIMATION(0x443), //44A - 7
    PICK_UP_ITEM(0x449), //450 - 7
    SET_REACTOR_STATUS(0x44C),//
    REQUEST_RECOMMEND_PLAYERS(0x47A),//481-7
    REQUEST_RECOMMEND_PARTIES(0x47C), //483-7
    QUICK_MOVE_SELECT(0x487), //48d - 6
    CASH_SHOP_POINT_REQUEST(0x5AE), //5b7-9
    CASH_SHOP_CASH_ITEM_REQUEST(0x5AF),//
    CASH_SHOP_SAVE_COLLOCATION(0x5B4),//
    BATTLE_ANALYSIS(0x5D7), //-B
    UNK5E0(0x5E0), //01 00 00 00 00 00 00 00 01
    EQUIP_EFFECT_OPT(0x600), //60A-A

    // OPCODE ENCRYPT BEGIN
    BEGIN(0xCA),
    //OPCODE ENCRYPT END
    END(0xA7F);

    private final int code;

    RecvOpcode(int code) {
        this.code = code;
    }

    public int getValue() {
        return code;
    }

    public static RecvOpcode getOpcode(int op) {
        for (RecvOpcode inHeader : RecvOpcode.values()) {
            if (inHeader.getValue() == op) {
                return inHeader;
            }
        }
        return null;
    }

}
