package im.cave.ms.connection.packet.opcode;

import im.cave.ms.tools.Util;

public enum SendOpcode {
    LOGIN_STATUS(0x00),
    WORLD_INFORMATION(0x01),
    //LATEST_CONNECTED_WORLD
    //RECOMMENDED_WORLD_MESSAGE
    REQUEST_SHARE_INFORMATION(0x04),
    SELECT_WORLD_BUTTON(0x05),//CHECK_USER_LIMIT_RESULT
    SELECT_WORLD_RESULT(0x06),
    SELECT_CHARACTER_RESULT(0x07),
    ACCOUNT_INFO_RESULT(0x08),
    //CREATE_MAPLE_ACCOUNT_RESULT
    CHECK_DUPLICATED_ID_RESULT(0x0A),
    CREATE_NEW_CHARACTER_RESULT(0x0B),
    //DELETE_CHARACTER_RESULT
    RESERVED_DELETE_CHARACTER_RESULT(0x0D),
    RESERVED_DELETE_CHARACTER_CANCEL_RESULT(0x0E),
    //SET_CHARACTER_ID
    CHANGE_CHANNEL(0x10),
    PING(0x11),
    C_PING(0x12),
    UNK13(0x13),
    AUTHEN_CODE_CHANGED(0x14),
    AUTHEN_MESSAGE(0x15),
    UNK16(0x16),
    SECURITY_PACKET(0x17),
    PRIVATE_SERVER_PACKET(0x18), //18 00 01 00
    //
    //
    //
    CLIENT_START(0x2A),//2c-2
    INIT_OPCODE_ENCRYPTION(0x2B), //177 2d - 2
    //
    //
    //
    //
    //
    SERVER_STATE_RESULT(0x32),
    SERVER_KEY_VALUE(0x33),
    REQUIRE_SET_ACC_GENDER(0x3E), //177
    SET_ACC_GENDER_RESULT(0x3F), //177
    CHAR_SLOTS_EXPAND_RESULT(0x4B),
    OPEN_CREATE_CHAR_LAYOUT_RESULT(0x4F), //56 - 7
    SERVER_LIST_BG(0x56), // 5D-7
    UNK5F(0x5F),
    UNK60(0x60),
    UNK61(0x61),
    UNK62(0x62),
    UNK63(0x63),
    UNK64(0x64),
    UNK65(0x65),
    UNK66(0x66),
    RELOAD_BACK(0x67),
    INVENTORY_OPERATION(0x68),  //6F - 7
    INVENTORY_GROW(0x69),//
    STAT_CHANGED(0x6A),//
    GIVE_BUFF(0x6B),//
    REMOVE_BUFF(0x6C),//
    FORCED_STAT_SET(0x6D),
    FORCED_STAT_RESET(0x6E),
    UNK6F(0x6F),
    CHANGE_SKILL_RECORD_RESULT(0x70),
    CHANGE_STEAL_MEMORY_RESULT(0x71),
    USER_DAMAGE_ON_FALLING_CHECK(0x72),
    PERSONAL_SHOP_BUY_CHECK(0x73),
    MOB_DROP_MESO_PICKUP(0x74),
    BREAK_TIME_FIELD_ENTER(0x75),
    RUNE_ACT_SUCCESS(0x76),
    RESULT_STEAL_SKILL_LIST(0x77),
    SKILL_USE_RESULT(0x78),
    EXCL_REQUEST(0x79),
    FAME_RESPONSE(0x7A), //
    UNK7B(0x7B),
    MESSAGE(0x7C),
    MAPLE_NOTES(0x7D),
    MAP_TRANSFER_RESULT(0x7E),
    ANTI_MACRO_RESULT(0x7F),
    UNK80(0x80),
    UNK81(0x81),
    UNK82(0x82),
    UNK83(0x83),
    UNK84(0x84),
    CLAIM_RESULT(0x85),
    SET_CLAIM_SVR_AVAILABLE_TIME(0x86),
    CLAIM_SVR_STATUS_CHANGED(0x88),
    UNK89(0x89),
    STAR_PLANET_USER_COUNT(0x8A),
    SET_TAMING_MOB_INFO(0x8B),
    QUEST_CLEAR(0x8C),
    ENTRUSTED_SHOP_CHECK_RESULT(0x8D),
    SKILL_LEARN_ITEM_RESULT(0x8E),
    SKILL_RESET_ITEM_RESULT(0x8F),
    ABILITY_RESET_ITEM_RESULT(0x90),
    EXP_CONSUME_ITEM_RESULT(0x91),
    EXP_ITEM_GET_RESULT(0x92),
    CHAR_SLOT_INC_RESULT(0x93),
    GATHER_ITEM_RESULT(0x94), //-7
    SORT_ITEM_RESULT(0x95),//
    UNK96(0x96),//
    CHAR_INFO(0x97), //9F- 8
    PARTY_RESULT(0x98),//A0-8
    PARTY_MEMBER_CANDIDATE_RESULT(0x99),//a1-8
    URUS_PARTY_MEMBER_CANDIDATE_RESULT(0x9A),
    PARTY_CANDIDATE_RESULT(0x9B),
    URUS_PARTY_RESULT(0x9C),
    INTRUSION_FRIEND_CANDIDATE_RESULT(0x9D),
    INTRUSION_LOBBY_CANDIDATE_RESULT(0x9E),
    EXPEDITION_RESULT(0x9F),
    STAR_FRIEND_RESULT(0xA0),
    LOAD_ACCOUNT_ID_OF_CHARACTER_FRIEND_RESULT(0xA1),
    FRIEND_RESULT(0xA2),
    GUILD_REQUEST(0xA3),
    GUILD_RESULT(0xA4),
    ALLIANCE_RESULT(0xA5),
    TOWN_PORTAL(0xA6),
    BROADCAST_MSG(0xA7),
    INCUBATOR_RESULT(0xA8),
    INCUBATOR_HOT_ITEM_RESULT(0xA9),
    SHOP_SCANNER_RESULT(0xAA),
    SHOP_LINK_RESULT(0xAB),
    MARRIAGE_REQUEST(0xAC),
    MARRIAGE_RESULT(0xAD),
    ACHIEVEMENT(0xAE),//成就系统
    CASH_PET_FOOD_RESULT(0xAF),
    CASH_PET_PICK_UP_ON_OFF_RESULT(0xB0),
    CASH_PET_SKILL_SETTING_RESULT(0xB1),//宠物自动喂养
    CASH_LOOK_CHANGE_RESULT(0xB2),
    CASH_PET_DYEING_RESULT(0xB3),
    SET_WEEK_EVENT_MESSAGE(0xB4),
    SET_POTION_DISCOUNT_RATE(0xB5),
    BRIDLE_MOB_CATCH_FAIL(0xB6),
    IMITATED_NPC_RESULT(0xB7),
    IMITATED_NPC_DATA(0xB8),
    LIMITED_NPC_DISABLE_INFO(0xB9), //hide?
    MONSTER_BOOK_SET_CARD(0xBA),
    MONSTER_BOOK_SET_COVER(0xBB),
    HOUR_CHANGE(0xBC),
    MINIMAP_ON_OFF(0xBD), // 01
    CONSULT_AUTH_KEY_UPDATE(0xBE),
    CLASS_COMPETITION_AUTH_KEY_UPDATE(0xBF),
    WEB_BOARD_AUTH_KEY_UPDATE(0xC0),
    SESSION_VALUE(0xC1),
    PARTY_VALUE(0xC2),
    FIELD_SET_VARIABLE(0xC3),
    FIELD_VALUE(0xC4),
    BONUS_EXP_RATE_CHANGED(0xC5),
    NOTIFY_LEVEL_UP(0xC6),
    NOTIFY_WEDDING(0xC7),
    NOTIFY_JOB_CHANGE(0xC8),
    SET_BUY_EQUIP_EXT(0xC9),
    SET_PASSENSER_REQUEST(0xCA),
    SCRIPT_PROGRESS_MESSAGE_BY_SOUL(0xCB),
    SCRIPT_PROGRESS_MESSAGE(0xCD),//
    SCRIPT_PROGRESS_ITEM_MESSAGE(0xCE),
    STATIC_SCREEN_MESSAGE(0xCF),
    OFF_STATIC_SCREEN_MESSAGE(0xD0),
    WEATHER_EFFECT_NOTICE(0xD1),
    WEATHER_EFFECT_NOTICE_Y(0xD2),
    D3(0xD3),
    PROGRESS_MESSAGE_FONT(0xD4),//
    DATA_CRC_CHECK_FAILED(0xD5),
    SHOW_SLOT_MESSAGE(0xD6),
    WILD_HUNTER_INFO(0xD7),
    ZERO_INFO(0xD8),
    ZERO_WP(0xD9),
    ZERO_INFO_SUB_HP(0xDA),
    UI_OPEN(0xDB),
    CLEAR_ANNOUNCED_QUEST(0xDC),
    RESULT_INSTANCE_TABLE(0xDE),//e7-9
    COOL_TIME_SET(0xDF),
    ITEM_POT_CHANGE(0xE0),
    SET_ITEM_COOL_TIME(0xE1),
    SET_AD_DISPLAY_INFO(0xE2),
    SET_AD_DISPLAY_STATUS(0xE3),
    //
    LINKED_MESSAGE(0xE5),
    REMOVE_SON_OF_LINKED_SKILL_RESULT(0xE6),
    SET_SON_OF_LINKED_SKILL_RESULT(0xE7),
    SON_OF_LINKED_SKILL_MESSAGE(0xE8),
    E9(0xE9),
    EA(0xEA),
    SET_MAPLE_STYLE_INFO(0xEB),
    EC(0xEC),
    ED(0xED),
    DOJO_RANK_RESULT(0xEE),//F7-9
    //EF
    //F0
    //F1
    //F2
    //F3
    //F4
    //F5
    //F6
    START_NAVIGATION(0xF7),
    FUNC_KEY_SET_BY_SCRIPT(0xF8),
    CHARACTER_POTENTIAL_SET(0xF9), //102-9
    CHARACTER_POTENTIAL_RESET(0xFA),//
    CHARACTER_HONOR_POINT(0xFB),//
    READY_FOR_RESPAWN(0xFC),
    READY_FOR_RESPAWN_BY_POINT(0xFD),
    OPEN_READY_FOR_RESPAWN_UI(0xFE),
    CHARACTER_HONOR_GIFT(0xFF),
    CROSS_HUNTER_COMPLETE_RESULT(0x100),
    CROSS_HUNTER_SHOP_RESULT(0x101),
    UNK_102(0x102),
    SET_CASH_ITEM_NOTICE(0x103),
    SET_SPECIAL_CASH_ITEM(0x104),
    SHOW_EVENT_NOTICE(0x105),
    BOARD_GAME_RESULT(0x106),
    YUT_GAME_RESULT(0x107),
    VALUE_PACK_RESULT(0x108),
    NAVI_FLYING_RESULT(0x109),
    SET_EXCL_REQUEST_SENT(0x10A),
    CHECK_WEDDING_EX_RESULT(0x10B),
    BINGO_RESULT(0x10C),
    BINGO_CASSANDRA_RESULT(0x10D),
    UPDATE_VIP_GRADE(0x10E),
    MESO_RANGER_RESULT(0x10F),
    SET_MAPLE_POINT(0x110),
    SET_MIRACLE_TIME_INFO(0x111),
    UNK_112(0x112),
    HYPER_SKILL_RESET_RESULT(0x113),
    GET_SERVER_TIME(0x114),
    GET_CHARACTER_POSITION(0x115),
    UNK_116(0x116),
    UNK_117(0x117),
    SET_FIX_DAMAGE(0x118),
    RETURN_EFFECT_CONFIRM(0x119),
    RETURN_EFFECT_MODIFIED(0x11A),
    MEMORIAL_CUBE_RESULT(0x11B),
    BLACK_CUBE_RESULT(0x11C),
    //11D
    MEMORIAL_CUBE_MODIFIED(0x11E),
    //11F
    //120
    DRESS_UP_INFO_MODIFIED(0x121),
    //122
    RESET_STATE_FOR_OFF_SKILL(0x123),
    SET_OFF_STATE_FOR_OFF_SKILL(0x124),
    RELOGIN_AUTH_KEY(0x125),// - 9
    AVATAR_PACK_TEST(0x126),
    EVOLVING_RESULT(0x127),
    ACTION_BAR_RESULT(0x128),
    GUILD_CONTENT_RESULT(0x129),
    GUILD_SEARCH_RESULT(0x12A),
    //
    //
    //
    //
    //
    CHECK_PROCESS_RESULT(0x130),  //  01 bool
    //
    UPDATE_QUEST_EX(0x132),// - 9
    SET_ACCOUNT_INFO(0x133), //13C - 9
    //
    //
    SET_AVATAR_MEGAPHONE(0x136), //-9
    //
    //
    //
    //
    //
    CANCEL_TITLE_EFFECT(0x13C), //-9
    //
    //
    //
    //
    //
    //
    //
    //
    //
    EQUIPMENT_ENCHANT(0x161), //16A-9
    CASH_SHOP_EVENT_INFO(0x170),//179-9
    LEGIONS(0x1AD),
    CHAR_AVATAR_CHANGE_EYES_COLOR(0x1D6),
    CHAR_AVATAR_CHANGE_RESULT(0x1D7), //38
    CHAR_AVATAR_CHANGE_SELECT(0x1DC),//0x1E9-d
    POTION_POT_MESSAGE(0x1E6),//
    //
    POTION_POT_UPDATE(0x1E8),//
    CHARACTER_MODIFIED(0x1F0), //1fb-b
    UPDATE_MAPLE_POINT(0x1F6),//201-b

    CASH_POINT_RESULT(0x20E), //219-b
    SLOT_EXPAND_RESULT(0x20F),//
    HOTTIME_REWARD_RESULT(0x212),
    MACRO_SYS_DATA_INIT(0x22A),//235-b
    SET_MAP(0x22B), //236 - B
    SET_AUCTION(0x22C),
    SET_CASH_SHOP(0x22E),//-b
    SET_CASH_SHOP_INFO(0x22F),//
    WHISPER(0x237),//
    FIELD_EFFECT(0x239),//
    FIELD_MESSAGE(0x23A), //
    CLOCK(0x23F),//
    UNK243(0x243),
    QUICKSLOT_INIT(0x24D),  //258 -B
    UNK252(0x252),
    CREATE_FORCE_ATOM(0x25B),
    PROGRESS(0x25F),
    QUICK_MOVE(0x260),//26b - b
    CREATE_OBSTACLE(0x261),
    UNK262(0x262),
    CLEAR_OBSTACLE(0x263),
    UNK264(0x264),
    B2_FOOT_HOLD_CREATE(0x265),
    DEBUFF_OBJ_ON(0X266),
    CREATE_FALLING_CATCHER(0X267),
    CHASE_EFFECT_SET(0X268),
    MESO_EXCHANGE_RESULT(0X269),
    SET_MIRROR_DUNGEINFO(0X26A),
    SET_INTRUSION(0x26B),
    CANNOT_DROP(0x26C),
    FOOT_HOLD_OFF(0x26D),
    LADDER_ROPE_OFF(0x26E),
    MOMENT_AREA_OFF(0x26F),
    MOMENT_AREA_OFF_ALL(0x270),
    CHAT_LET_CLIENT_CONNECT(0x271),
    CHAT_INDUCE_CLIENT_CONNECT(0x272),
    PACKET(0x273),
    ELITE_STATE(0x274),
    PLAY_SOUND(0x275),
    STACK_EVENT_GAUGE(0x276),
    SET_UNIFIELD(0x277),
    STAR_PLANET_BURNING_TIME_INFO(0x278),
    //28e user_stand_up
    UNK27E(0x27E),
    UNK28E(0x28E),
    USER_SIT(0x28F), //29B-C
    USER_ENTER_FIELD(0x2AB),
    USER_LEAVE_FIELD(0x2AC),
    CHAT(0x2AD),
    UNK2AE(0x2AE),
    BLACK_BOARD(0x2AF),//2b9 - a //小黑板
    SHOW_ITEM_UPGRADE_EFFECT(0x2B2),//
    SHOW_ITEM_SKILL_SOCKET_UPGRADE_EFFECT(0x2B3),
    SHOW_ITEM_SKILL_OPTION_UPGRADE_EFFECT(0x2B4),
    SHOW_ITEM_RELEASE_EFFECT(0x2B6),//
    SHOW_CUBE_EFFECT(0x2B9), //唯一魔方 8F 1D 00 00  01  8A 3D 4D 00 00 00 00 00 00 00 00 00
    SHOW_ITEM_ADDITIONAL_RELEASE_EFFECT(0x2BA), //8F 1D 00 00 char Id| 01 | 64 3F 4D 00 itemId|00 00 00 00 00 00 00 00
    ADDITIONAL_CUBE_RESULT(0x2BF),
    SET_DAMAGE_SKIN(0x2D2),//
    SET_SOUL_EFFECT(0x2D6),//
    SIT_RESULT(0x2D7), //2e1-A
    FAMILIAR(0x2D8),//
    LEGACY_CUBE_RESULT(0x2E2),
    //2e6
    PET_TRAINING_EFFECT(0x306),
    HIDDEN_EFFECT_EQUIP(0x30F), // -7
    LIMIT_BREAK_UI(0x310),//-7
    PET_ACTIVATED(0x311),//-7
    PET_MOVE(0x312),//-7
    PET_ACTION_SPEAK(0x313),//-7
    PET_NAME_CHANGED(0x315),//-7
    PET_LOAD_EXCEPTION_LIST(0x316),//-7
    //
    //
    //
    //
    PET_ACTION(0x31A),
    PET_ACTION_COMMAND(0x31B),//
    ANDROID_CREATED(0x320),//
    ANDROID_MOVE(0x321),//
    ANDROID_ACTION_SET(0x322),//
    ANDROID_MODIFIED(0x323),//
    ANDROID_REMOVED(0x324), //
    SKILL_PET_MOVE(0x32C),

    REMOTE_MOVE(0x33E), //345-7
    REMOTE_CLOSE_RANGE_ATTACK(0x33F),//
    REMOTE_RANGED_ATTACK(0x340),//
    REMOTE_MAGIC_ATTACK(341),//
    //
    //
    //
    REMOTE_SKILL_CANCEL(0x345), //charId + skillId
    REMOTE_HIT(0x346),//
    REMOTE_EMOTION(0x347),//
    //
    //
    //
    //
    //
    //
    //
    //
    REMOTE_SET_ACTIVE_PORTABLE_CHAIR(0x350),//
    REMOTE_AVATAR_MODIFIED(0x351), //358-7
    REMOTE_EFFECT(0x352),//359-7
    REMOTE_SET_TEMPORARY_STAT(0x353),
    REMOTE_RESET_TEMPORARY_STAT(0x354),
    REMOTE_RECEIVE_HP(0x355),//
    REMOTE_GUILD_NAME_CHANGED(0x357),
    REMOTE_GUILD_MARK_CHANGED(0x358),
    EFFECT(0x37C), //0x383 - 7
    TELEPORT(0x37D),
    QUEST_RESULT(0x381),//
    PET_SKILL_CHANGED(0x383),//38A-7
    OPEN_UI(0x389),//-7 1121 服装回收
    CLOSE_UI(0x38A),//
    OPEN_UI_WITH_OPTION(0x38B),//  00 00 00 00 06 00 00 00 00 00 00 00 背包:特殊栏
    SET_DIRECTION_MODE(0x38C),//393-7
    SET_IN_GAME_DIRECTION_MODE(0x38D),//
    SET_STAND_ALONE_MODE(0x38E),//395-7
    NOTICE_MSG(0x39B),//3a2-7
    CHAT_MSG(0x39C),//3a3-7
    BAG_ITEM_USE_RESULT(0x3A9),// //BAG_ITEM_USE_RESULT
    DODGE_SKILL_READY(0x3AE),//闪避技能准备
    IN_GAME_DIRECTION_EVENT(0x3B2),
    LIFE_COUNT(0x3BA),
    CHECK_TRICK_OR_TREAT_RESULT(0x3C1),
    USER_B2_BODY(0x3C4),
    SET_DEAD(0x3C5),//
    OPEN_DEAD_UI(0x3C6),//
    //
    //
    //
    //
    //
    //
    //
    //
    FINAL_ATTACK(0x3CF), //3d5 - 6
    ILLUSTRATION_MSG(0x3FF), //405-6
    LEOPARD_SKILL_USE(0x401),//
    SKILL_COOLTIME_SET(0x409), //40F-6
    ADDITIONAL_ATTACK(0x424),//
    DAMAGE_SKIN_SAVE_RESULT(0x427),//42D-6
    OPEN_WORLDMAP(0x48F), //没变
    MULTI_PERSON_CHAIR_AREA(0x498),
    SPAWN_SUMMON(0x4BA),//+3
    REMOVE_SUMMON(0x4BB), //03 00 00 00 01 00 00 00 01
    SUMMON_MOVE(0x4BC),//+3
    SUMMONED_ATTACK(0x4BD),
    SUMMONED_ATTACK_PVP(0x4BE),
    SUMMONED_SET_REFERENCE(0x4BF),
    SUMMON_SKILL(0x4C0),
    SPAWN_MOB(0x4D8), //4d5+3
    REMOVE_MOB(0x4D9),//4d6+3
    MOB_CHANGE_CONTROLLER(0x4DA),//4d7+3
    MOB_MOVE(0x4E1), //4de+3
    MOB_CONTROL_ACK(0x4E2), //4DF+3
    //MOB_CONTROL_HINT
    MOB_STAT_SET(0x4E4),//
    MOB_STAT_RESET(0x4E5),//
    MOB_SUSPEND_RESET(0x4E6),//
    MOB_AFFECTED(0x4E7),//
    MOB_DAMAGED(0x4E8),//
    HP_INDICATOR(0x4EF), //4ec+3
    MOB_SKILL_DELAY(0x4F5),
    ESCORT_FULL_PATH(0x4F6),
    ESCORT_STOP_END_PERMISSION(0x4F7),
    ESCORT_STOP_BY_SCRIPT(0x4F8),
    ESCORT_STOP_SAY(0x4F9),
    SPAWN_NPC(0x54B), //542 + 9
    REMOVE_NPC(0x54C),//543+9
    //54D    544+9
    SPAWN_NPC_REQUEST_CONTROLLER(0x54E), //545+9
    //
    //
    //
    NPC_ANIMATION(0x552), //549 + 9
    DROP_ENTER_FIELD(0x571), //569+8
    DROP_LEAVE_FIELD(0x573), //56b+8
    REACTOR_CHANGE_STATE(0x582),
    REACTOR_ENTER_FIELD(0x584),//
    REACTOR_REMOVE(0x586),//
    REACTOR_LEAVE_FIELD(0x58C),
    BINGO(0x6A5),
    BINGO_NUMBER(0x6A6),
    //6A7
    //6AC Bingo
    NPC_TALK(0x729), //741-18
    NPC_SHOP_OPEN(0x72A),//
    NPC_SHOP_RESULT(0x72B),//

    TRUNK_OPERATION(0x742),//75a-18
    CHAT_ROOM(0x747), //-19
    TRADE_ROOM(0x748), //761-19
    EXPRESS(0x7AA),
    CASH_SHOP_QUERY_CASH_RESULT(0X7AC), //0x7be-12
    CASH_SHOP_CASH_ITEM_RESULT(0x7AD),//
    CASH_SHOP_SAVE_COLLOCATION_RESULT(0x7B3), //
    CASH_SHOP_UPDATE_STATS(0x7C0),
    POTION_POT_CREATE(0x7C8), //
    AUCTION(0x7CB),
    DAILY_BONUS_INIT(0x7DB), //
    OPEN_UNITY_PORTAL(0x7DF), //7f3-14
    LEGION(0x7E1),
    CMS_LIMIT(0x807),
    KEYMAP_INIT(0x831), //84A - 19
    UNK832(0x832), //85 84 1E 00
    UNK833(0x833), //00 00 00 00
    UNK834(0x834), //00 00 00 00
    FIELD_ATTACK_CREATE(0x850),
    FIELD_ATTACK_REMOVE_BY_KEY(0x851),
    FIELD_ATTACK_REMOVE_LIST(0x852),
    FIELD_ATTACK_REMOVE_ALL(0x853),
    //
    //
    FIELD_ATTACK_SET_ATTACK(0x856),
    FIELD_ATTACK_RESULT_BOARD(0x857),
    FIELD_ATTACK_RESULT_GET_OFF(0x858),
    FIELD_ATTACK_PUSH_ACT(0x859),
    //
    BATTLE_ANALYSIS(0x863),//879-16
    REMAINING_MAP_TRANSFER_COUPON(0x882),//897-15
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
