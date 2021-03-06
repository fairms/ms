package im.cave.ms.connection.server.channel.handler;

import im.cave.ms.client.Account;
import im.cave.ms.client.MapleClient;
import im.cave.ms.client.character.*;
import im.cave.ms.client.character.items.*;
import im.cave.ms.client.character.job.MapleJob;
import im.cave.ms.client.character.potential.CharacterPotential;
import im.cave.ms.client.character.potential.CharacterPotentialMan;
import im.cave.ms.client.character.skill.*;
import im.cave.ms.client.character.temp.TemporaryStatManager;
import im.cave.ms.client.field.Effect;
import im.cave.ms.client.field.MapleMap;
import im.cave.ms.client.field.Portal;
import im.cave.ms.client.field.movement.MovementInfo;
import im.cave.ms.client.field.obj.Android;
import im.cave.ms.client.field.obj.Drop;
import im.cave.ms.client.field.obj.MapleMapObj;
import im.cave.ms.client.field.obj.Reactor;
import im.cave.ms.client.field.obj.mob.Mob;
import im.cave.ms.client.multiplayer.party.PartyMember;
import im.cave.ms.client.storage.Locker;
import im.cave.ms.connection.netty.InPacket;
import im.cave.ms.connection.packet.*;
import im.cave.ms.connection.packet.opcode.RecvOpcode;
import im.cave.ms.connection.packet.result.FameResult;
import im.cave.ms.connection.server.Server;
import im.cave.ms.connection.server.cashshop.CashShopServer;
import im.cave.ms.constants.*;
import im.cave.ms.enums.*;
import im.cave.ms.provider.data.ItemData;
import im.cave.ms.provider.data.SkillData;
import im.cave.ms.provider.data.VCoreData;
import im.cave.ms.provider.info.AndroidInfo;
import im.cave.ms.provider.info.CashItemInfo;
import im.cave.ms.provider.info.SkillInfo;
import im.cave.ms.provider.info.VCore;
import im.cave.ms.tools.DateUtil;
import im.cave.ms.tools.Pair;
import im.cave.ms.tools.Position;
import im.cave.ms.tools.Rect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static im.cave.ms.client.character.temp.CharacterTemporaryStat.KeyDownMoving;
import static im.cave.ms.connection.packet.opcode.RecvOpcode.*;
import static im.cave.ms.constants.GameConstants.QUICKSLOT_SIZE;
import static im.cave.ms.constants.QuestConstants.*;
import static im.cave.ms.constants.ServerConstants.ONE_DAY_TIMES;

/**
 * @author fair
 * @version V1.0
 * @Package im.cave.ms.net.handler.channelId
 * @date 12/1 14:59
 */
public class UserHandler {

    private static final Logger log = LoggerFactory.getLogger(UserHandler.class);

    public static void handleHit(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        HitInfo hitInfo = new HitInfo();
        in.skip(8);
        player.setTick(in.readInt());
        in.readByte(); //ff
        in.readShort(); //00 00
        int damage = in.readInt();
        hitInfo.hpDamage = damage;
        if (JobConstants.isGmJob(player.getJob())) {
            return;
        }
        in.skip(2);
        if (in.available() >= 13) {
            hitInfo.mobID = in.readInt();
            hitInfo.templateID = in.readInt();
            in.skip(4);   //objId
            if (in.available() >= 1) {
                hitInfo.specialEffectSkill = in.readByte();
            }
        }
        HashMap<Stat, Long> stats = new HashMap<>();
        int curHp = (int) player.getStat(Stat.HP);
        int newHp = curHp - damage;
        if (newHp <= 0) {
            newHp = 0;
            c.announce(UserPacket.sendRebirthConfirm(true, false,
                    false, false
                    , false, 0, 0));
        }
        player.setStat(Stat.HP, newHp);
        stats.put(Stat.HP, (long) newHp);
        c.announce(UserPacket.statChanged(stats, player));
        if (player.getParty() != null) {
            player.updatePartyHpBar();
        }
        player.getMap().broadcastMessage(player, UserRemote.hit(player, hitInfo), false);

    }

    /*
        USER_SHOOT_ATTACK @
        USER_MELEE_ATTACK @

     */
    public static void handleAttack(InPacket in, MapleClient c, RecvOpcode opcode) {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        AttackInfo attackInfo = new AttackInfo();
        attackInfo.attackHeader = opcode;
        if (opcode == USER_SHOOT_ATTACK) {
            attackInfo.boxAttack = in.readByte() != 0;
        }
        attackInfo.fieldKey = in.readByte(); //map key
        byte mask = in.readByte();
        attackInfo.hits = (byte) (mask & 0xF);
        attackInfo.mobCount = (mask >>> 4) & 0xF;
        in.readInt(); //00 00 00 00
        attackInfo.skillId = in.readInt();
        attackInfo.skillLevel = in.readInt();
        in.readByte();
        in.readLong(); // CRC USER_MELEE_ATTACK = 0

        /*
            0A A1 36 22
            00
            01 00
            00 00 00 00
            00 00 00 00
            00 00 00 00
         */
        in.skip(18);
        if (attackInfo.attackHeader != USER_MAGIC_ATTACK) {
            in.readByte();//unk 00
        }

        Position position = in.readPositionInt(); //mob pos

        in.readLong(); // 00 00 00 00
        in.readInt(); //  00 00 00 00
        in.readInt(); // CRC
        if (attackInfo.skillId == player.getPrepareSkill().getLeft()) {
            in.readInt();//如果是按压技能的话
        }
        in.skip(3); // 00 00 00
        if (attackInfo.attackHeader == USER_SHOOT_ATTACK) {
            in.readInt();
            in.readByte();
        }
        attackInfo.attackAction = in.readByte();
        attackInfo.direction = in.readByte(); // left:0x80 right:0x00
        attackInfo.requestTime = in.readInt();
        attackInfo.attackActionType = in.readByte(); // 武器类型
        attackInfo.attackSpeed = in.readByte();
        player.setTick(in.readInt());
        in.readInt(); //00 00 00 00
        if (attackInfo.attackHeader == USER_MELEE_ATTACK) {
            in.readInt(); //00 00 00 00
        }
        if (attackInfo.attackHeader == USER_SHOOT_ATTACK) {
            in.readInt(); // 00
            in.readShort(); // 00
            in.readByte(); // 1E
            attackInfo.rect = in.readShortRect();
        }
        for (int i = 0; i < attackInfo.mobCount; i++) {
            MobAttackInfo mobAttackInfo = new MobAttackInfo();
            mobAttackInfo.objectId = in.readInt();
            mobAttackInfo.hitAction = in.readByte();
            in.readShort(); //00 00
            mobAttackInfo.left = in.readByte();
            in.readByte(); //03
            mobAttackInfo.templateID = in.readInt();
            mobAttackInfo.calcDamageStatIndex = in.readByte();
            mobAttackInfo.hitX = in.readShort(); //MOB pos
            mobAttackInfo.hitY = in.readShort();
            in.readShort(); //x
            in.readShort(); //y
            if (attackInfo.attackHeader == USER_MAGIC_ATTACK) {
                mobAttackInfo.hpPerc = in.readByte();
                short unk = in.readShort(); //unk
            } else {
                short interval = in.readShort();
                byte unk2 = in.readByte(); //1 正常 2 趴着
            }
            in.readLong(); // 00
            mobAttackInfo.damages = new long[attackInfo.hits];
            for (byte j = 0; j < attackInfo.hits; j++) {
                mobAttackInfo.damages[j] = in.readLong();
            }
            in.readInt(); // 00 00 00 00
            in.readInt(); // crc
            mobAttackInfo.type = in.readByte();
            if (mobAttackInfo.type == 1) {
                mobAttackInfo.currentAnimationName = in.readMapleAsciiString();
                in.readMapleAsciiString();
                mobAttackInfo.animationDeltaL = in.readInt();
                mobAttackInfo.hitPartRunTimesSize = in.readInt();
                mobAttackInfo.hitPartRunTimes = new String[mobAttackInfo.hitPartRunTimesSize];
                for (int j = 0; j < mobAttackInfo.hitPartRunTimesSize; j++) {
                    mobAttackInfo.hitPartRunTimes[j] = in.readMapleAsciiString();
                }
            } else if (mobAttackInfo.type == 2) {
                player.dropMessage("mobAttackInfo.type == 2 !!!");
            }
            in.skip(18); //unk pos
            attackInfo.mobAttackInfo.add(mobAttackInfo);
        }
        Position pos = in.readPosition();
        player.getJobHandler().handleAttack(c, attackInfo);
        handleAttack(c, attackInfo);
    }

    public static void handleAttack(MapleClient c, AttackInfo attackInfo) {
        int killedCount = 0;
        int lastKilledMob = 0;
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        int skillId = attackInfo.skillId;
        if (!player.applyMpCon(skillId, attackInfo.skillLevel)) {
            return;
        }
        if (attackInfo.attackHeader != null) {
            switch (attackInfo.attackHeader) {
                case SUMMON_ATTACK:
                    player.getMap().broadcastMessage(player, SummonPacket.summonAttack(player.getId(), attackInfo, false), false);
                    break;
//                case FAMILIAR_ATTACK:
//                    chr.getField().broadcastPacket(CFamiliar.familiarAttack(chr.getId(), attackInfo), chr);
//                    break;
                default:
                    player.getMap().broadcastMessage(player, UserRemote.attack(player, attackInfo), false);
            }
        }
        for (MobAttackInfo mobAttackInfo : attackInfo.mobAttackInfo) {
            MapleMap map = player.getMap();
            Mob mob = (Mob) map.getObj(mobAttackInfo.objectId);
            if (mob == null) {
                player.dropMessage("unhandled mob is null");
            } else if (mob.getHp() > 0) {
                long totalDamage = Arrays.stream(mobAttackInfo.damages).sum();
                mob.damage(player, totalDamage);
                if (mob.getHp() <= 0) {
                    killedCount++;
                    lastKilledMob = mob.getObjectId();
                    if (player.getLevel() - mob.getForcedMobStat().getLevel() <= 15) {
                        player.addDailyMobKillCount();
                    }
                }
                //todo handle reflect
            }

        }
        if (killedCount > 0) {
            if (System.currentTimeMillis() - player.getLastKill() < 10000) {
                player.comboKill(lastKilledMob);
            } else {
                player.setCombo(0);
            }
            player.setLastKill(System.currentTimeMillis());
        }
        if (killedCount >= 3) {
            //todo
            player.announce(UserPacket.stylishKillMessage(1000, killedCount));
            player.addExp(1000, null);
        }
    }

    public static void handlePlayerMove(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        in.skip(1);    //unknown
        in.skip(4);    //map relate
        in.skip(4);    //tick
        in.skip(1);    //unknown
        MovementInfo movementInfo = new MovementInfo(in);
        movementInfo.applyTo(player);
        player.getMap().sendMapObjectPackets(player);
        player.getMap().broadcastMessage(player, UserPacket.move(player, movementInfo), false);
    }

    //打开角色的信息面板
    public static void handleCharInfoReq(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        player.setTick(in.readInt());
        int charId = in.readInt();
        MapleCharacter chr = player.getMap().getCharById(charId);
        if (chr == null) {
            c.announce(MessagePacket.broadcastMsg("角色不存在", BroadcastMsgType.ALERT));
            return;
        }
        c.announce(UserRemote.charInfo(chr));
    }

    // cancel portableChair / sit townChair
    public static void handleUserSitRequest(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        short fieldSeatId = in.readShort();
        player.setChairId(fieldSeatId);
        c.announce(UserPacket.sitResult(player.getId(), fieldSeatId));
        player.getMap().broadcastMessage(player, UserRemote.remoteSetActivePortableChair(player.getId(), new PortableChair()), false);
    }

    public static void handleUserPortableChairSitRequest(InPacket in, MapleClient c) {
        MapleCharacter chr = c.getPlayer();
        int mapId = in.readInt();
        //todo check map limit
        int chairId = in.readInt();
        int emotion = in.readByte();
        in.readInt();
        Position position = in.readPositionInt();
        String msg = in.readMapleAsciiString();
        in.readInt();
        int unk1 = in.readInt();
        short unk2 = in.readShort();
        in.skip(3);
        int unk3 = in.readInt();
        byte unk4 = in.readByte();
        c.announce(UserPacket.enableActions());
        c.announce(UserPacket.userSit());
        PortableChair portableChair = new PortableChair();
        chr.getMap().broadcastMessage(chr, UserRemote.remoteSetActivePortableChair(chr.getId(), portableChair), false);
    }

    /*
        技能开始
     */
    public static void handleSkillUp(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        player.setTick(in.readInt());
        int skillId = in.readInt();
        int level = in.readInt();
        if (level < 1) {
            c.close();
            return;
        }
        Skill skill = SkillData.getSkill(skillId);
        if (skill == null) {
            return;
        }
        Skill curSkill = player.getSkill(skill.getSkillId());
        byte jobLevel = (byte) JobConstants.getJobLevel((short) skill.getRootId());
        if (JobConstants.isZero((short) skill.getRootId())) {
            jobLevel = JobConstants.getJobLevelByZeroSkillID(skillId);
        }
        Map<Stat, Long> stats;
        int rootId = skill.getRootId();
        if ((!JobConstants.isBeginnerJob((short) rootId) && !SkillConstants.isMatching(rootId, player.getJob())) || SkillConstants.isSkillFromItem(skillId)) {
            log.error(String.format("Character %d tried adding an invalid skill (job %d, skill id %d)",
                    player.getId(), skillId, rootId));
            return;
        }
        if (JobConstants.isBeginnerJob((short) rootId)) {
            stats = new HashMap<>();
            int spentSp = player.getSkills().stream()
                    .filter(s -> JobConstants.isBeginnerJob((short) s.getRootId()))
                    .mapToInt(Skill::getCurrentLevel).sum();
            int totalSp;
            if (JobConstants.isResistance((short) skill.getRootId())) {
                totalSp = Math.min(player.getLevel(), GameConstants.RESISTANCE_SP_MAX_LV) - 1; // sp gained from 2~10
            } else {
                totalSp = Math.min(player.getLevel(), GameConstants.BEGINNER_SP_MAX_LV) - 1; // sp gained from 2~7
            }
            if (totalSp - spentSp >= level) {
                int curLevel = curSkill == null ? 0 : curSkill.getCurrentLevel();
                int max = curSkill == null ? skill.getMaxLevel() : curSkill.getMaxLevel();
                int newLevel = Math.min(curLevel + level, max);
                skill.setCurrentLevel(newLevel);
            }
        } else if (JobConstants.isExtendSpJob(player.getJob())) {
            List<Integer> remainingSp = player.getRemainingSp();
            Integer sp = remainingSp.get(jobLevel - 1);
            if (sp >= level) {
                int curLevel = curSkill == null ? 0 : curSkill.getCurrentLevel();
                int max = curSkill == null ? skill.getMaxLevel() : curSkill.getMaxLevel();
                int newLevel = Math.min(curLevel + level, max);
                skill.setCurrentLevel(newLevel);
                player.addSp(-level, jobLevel);
                stats = new HashMap<>();
                stats.put(Stat.AVAILABLESP, (long) 1);
            } else {
                log.error(String.format("Character %d tried adding a skill without having the required amount of sp" +
                                " (required %d, has %d)",
                        player.getId(), level, sp));
                return;
            }
        } else {
            Integer currentSp = player.getRemainingSp().get(0);
            if (currentSp >= level) {
                int curLevel = curSkill == null ? 0 : curSkill.getCurrentLevel();
                int max = curSkill == null ? skill.getMaxLevel() : curSkill.getMaxLevel();
                int newLevel = Math.min(curLevel + level, max);
                skill.setCurrentLevel(newLevel);
                player.addSp(-level, 1);
                stats = new HashMap<>();
                stats.put(Stat.AVAILABLESP, (long) 1);
            } else {
                log.error(String.format("Character %d tried adding a skill without having the required amount of sp" +
                                " (required %d, has %d)",
                        player.getId(), currentSp, level));
                return;
            }
        }

        c.announce(UserPacket.statChanged(stats, player));
        player.addSkill(skill);
        c.announce(UserPacket.changeSkillRecordResult(skill));

    }

    public static void handleUseSkill(InPacket in, MapleClient c) throws Exception {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        in.readInt(); //crc
        int skillId = in.readInt();
        if (SkillConstants.isZeroSkill(skillId)) {
            in.readByte();
        }
        int skillLevel = in.readInt();
        if (player.applyMpCon(skillId, skillLevel) && !player.isSkillInCd(skillId)) {
            player.getMap().broadcastMessage(UserRemote.effect(player.getId(), Effect.skillUse(skillId, (byte) skillLevel, 0)));
            SkillInfo skillInfo = SkillData.getSkillInfo(skillId);
            MapleJob sourceJobHandler = player.getJobHandler();
            if (sourceJobHandler.isBuff(skillId) && skillInfo.isMassSpell()) {
                Rect rect = skillInfo.getFirstRect();
                if (rect != null) {
                    Rect rectAround = player.getRectAround(rect);
                    for (PartyMember pm : player.getParty().getOnlineMembers()) {
                        if (pm.getChr() != null
                                && pm.getMapId() == player.getMapId()
                                && rectAround.hasPositionInside(pm.getChr().getPosition())) {
                            MapleCharacter ptChr = pm.getChr();
                            Effect effect = Effect.skillAffected(skillId, skillLevel, 0);
                            if (ptChr != player) { // Caster shouldn't get the Affected Skill Effect
                                ptChr.getMap().broadcastMessage(ptChr,
                                        UserRemote.effect(ptChr.getId(), effect)
                                        , false);
                                ptChr.announce(UserPacket.effect(effect));
                            }
                            sourceJobHandler.handleSkill(pm.getChr().getClient(), skillId, skillLevel, in);
                        }
                    }
                }
                sourceJobHandler.handleSkill(c, skillId, skillLevel, in);
            } else {
                sourceJobHandler.handleSkill(c, skillId, skillLevel, in);
            }
        }
    }

    //取消技能
    public static void handleUserSkillCancel(InPacket in, MapleClient c) {
        int skillId = in.readInt();
        MapleCharacter player = c.getPlayer();
        Pair<Integer, Integer> prepareSkill = player.getPrepareSkill();
        if (prepareSkill != null && skillId == prepareSkill.getLeft()) {
            player.setSkillCooltime(skillId, prepareSkill.getRight());
            return;
        }
        in.readByte();
        TemporaryStatManager tsm = player.getTemporaryStatManager();
        tsm.removeStatsBySkill(skillId);
    }

    /*
        技能结束
     */

    //拾取
    public static void handlePickUp(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        byte mapKey = in.readByte();
        player.setTick(in.readInt());
        Position position = in.readPosition();
        int dropId = in.readInt();
        MapleMap map = player.getMap();
        MapleMapObj obj = map.getObj(dropId);
        if (obj instanceof Drop) {
            Drop drop = (Drop) obj;
            player.addDrop(drop);
            map.removeDrop(dropId, DropLeaveType.CharPickup1, player.getId(), false);
        }
    }

    public static void handleEquipEffectOpt(int pos, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        Inventory inventory = player.getEquippedInventory();
        Equip equip = (Equip) inventory.getItem((short) pos);
        if (equip == null) {
            c.announce(UserPacket.enableActions());
            return;
        }
        equip.setShowEffect(!equip.isShowEffect());
        player.getMap().broadcastMessage(UserRemote.hiddenEffectEquips(player));
    }

    //自动回复
    public static void handleChangeStatRequest(InPacket in, MapleClient c) {

        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        player.setTick(in.readInt());
        long mask = in.readLong();
        List<Stat> stats = Stat.getStatsByMask(mask);
        HashMap<Stat, Long> updatedStats = new HashMap<>();
        for (Stat stat : stats) {
            updatedStats.put(stat, (long) in.readShort());
        }
        if (updatedStats.containsKey(Stat.HP)) {
            player.heal(Math.toIntExact(updatedStats.get(Stat.HP)));
        }
        if (updatedStats.containsKey(Stat.MP)) {
            player.healMP(Math.toIntExact(updatedStats.get(Stat.MP)));
        }
    }

    public static void handleChangeQuickSlot(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        ArrayList<Integer> aKeys = new ArrayList<>();
        if (in.available() == QUICKSLOT_SIZE * 4) {
            for (int i = 0; i < QUICKSLOT_SIZE; i++) {
                aKeys.add(in.readInt());
            }
        }
        player.setQuickSlots(aKeys);
    }

    public static void handleChangeKeyMap(InPacket in, MapleClient c) {
        in.skip(4);
        int size = in.readInt();
        MapleKeyMap keyMap = c.getPlayer().getKeyMap();
        if (keyMap == null) {
            keyMap = new MapleKeyMap(false);
        }
        for (int i = 0; i < size; i++) {
            int key = in.readInt();
            byte type = in.readByte();
            int action = in.readInt();
            keyMap.putKeyBinding(key, type, action);
        }
        c.getPlayer().setKeyMap(keyMap);
    }

    public static void handleAPUpdateRequest(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null || player.getRemainingAp() <= 0) {
            return;
        }
        player.setTick(in.readInt());
        short stat = in.readShort();
        Stat charStat = Stat.getByValue(stat);
        if (charStat == null) {
            return;
        }
        int amount = 1;
        if (charStat == Stat.MAXMP || charStat == Stat.MAXHP) {
            amount = 20;
        }
        player.addStat(charStat, amount);
        player.addStat(Stat.AVAILABLEAP, (short) -1);
        Map<Stat, Long> stats = new HashMap<>();
        stats.put(charStat, player.getStat(charStat));
        stats.put(Stat.AVAILABLEAP, player.getStat(Stat.AVAILABLEAP));
        c.announce(UserPacket.statChanged(stats, true, player));
    }

    public static void handleAPMassUpdateRequest(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null || player.getRemainingAp() <= 0) {
            return;
        }
        player.setTick(in.readInt());
        int type = in.readInt();
        int amount;
        Stat charStat = null;
        if (type == 1) {
            charStat = Stat.getByValue(in.readLong());
        } else if (type == 2) {
            in.readInt();
            in.readInt();
            in.readInt();
            charStat = Stat.getByValue(in.readLong());
        }
        if (charStat == null) {
            return;
        }
        amount = in.readInt();
        int addStat = amount;
        if (player.getRemainingAp() < amount) {
            return;
        }
        if (charStat == Stat.MAXMP || charStat == Stat.MAXHP) {
            addStat *= 20;
        }
        player.addStat(charStat, addStat);
        player.addStat(Stat.AVAILABLEAP, -amount);
        Map<Stat, Long> stats = new HashMap<>();
        stats.put(charStat, player.getStat(charStat));
        stats.put(Stat.AVAILABLEAP, player.getStat(Stat.AVAILABLEAP));
        c.announce(UserPacket.statChanged(stats, true, player));
    }

    //内在能力重置
    public static void handleUserRequestCharacterPotentialSkillRandSetUI(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        int cost = GameConstants.CHAR_POT_RESET_COST;
        int rate = in.readInt(); //锁定的等级
        boolean locked = rate > 0;
        Set<Byte> lockedLines = null;
        if (locked) {
            lockedLines = new HashSet<>();
            cost += GameConstants.getCharPotGradeLockCost(rate);
            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                lockedLines.add((byte) in.readInt());
                if (lockedLines.size() == 0) {
                    cost += GameConstants.CHAR_POT_LOCK_1_COST;
                } else {
                    cost += GameConstants.CHAR_POT_LOCK_2_COST;
                }
            }
        }
        if (cost > player.getHonerPoint()) {
            player.chatMessage("You do not have enough honor exp for that action.");
            return;
        }
        player.addHonerPoint(-cost);

        CharacterPotentialMan cpm = player.getPotentialMan();
        Set<CharacterPotential> potentials = cpm.randomizer(lockedLines, CharPotGrade.Rare.ordinal());
        int i = 0;
        for (CharacterPotential potential : potentials) {
            ++i;
            cpm.addPotential(potential, i == potentials.size());
        }
        c.announce(UserPacket.noticeMsg("内在能力重新设置成功。"));
    }

    public static void handleUserDamageSkinSaveRequest(InPacket in, MapleClient c) {
        boolean delete = in.readByte() != 0; //type
        MapleCharacter player = c.getPlayer();
        DamageSkinSaveData damageSkin = player.getDamageSkin();
        DamageSkinType error = null;
        if (player.getDamageSkins().size() >= GameConstants.DAMAGE_SKIN_MAX_SIZE) {
            error = DamageSkinType.DamageSkinSave_Fail_SlotCount;
        }
        if (error != null) {
            player.announce(UserPacket.damageSkinSaveResult(DamageSkinType.DamageSkinSaveReq_Reg, error, null));
        } else {
            if (!delete) {
                player.getDamageSkinByItemID(damageSkin.getItemID()).setNotSave(false);
                player.announce(UserPacket.damageSkinSaveResult(DamageSkinType.DamageSkinSaveReq_Active,
                        DamageSkinType.DamageSkinSave_Success, player));
            } else {
                int skinId = in.readInt(); // 1
                player.getDamageSkins().removeIf(dk -> dk.getDamageSkinID() == skinId);
                player.announce(UserPacket.damageSkinSaveResult(DamageSkinType.DamageSkinSaveReq_Remove,
                        DamageSkinType.DamageSkinSave_Success, player));
            }

            // val =  2
            // in.readShort();
        }
    }

    public static void handleUserActivateNickItem(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        int itemId = in.readInt();
        short pos = in.readShort();
        Item item = player.getInstallInventory().getItem(pos);
        if ((item == null || item.getItemId() != itemId) && itemId != 0) {
            return;
        }
        String date;
        String expired;
        if (itemId == 0) {
            date = "0";
            expired = "1";
        } else {
            date = "2079/01/01 00:00:00:000";
            expired = "0";
        }
        HashMap<String, String> value = new HashMap<>();
        value.put("id", String.valueOf(itemId));
        value.put("date", date);
        value.put("expired", expired);
        player.addQuestExAndSendPacket(QUEST_EX_NICK_ITEM, value);
    }

    public static void handleUserActivateDamageSkin(InPacket in, MapleClient c) {
        int damageSkinId = in.readInt();
        MapleCharacter chr = c.getPlayer();
        chr.setDamageSkin(chr.getDamageSkinBySkinId(damageSkinId));
        chr.getMap().broadcastMessage(chr, UserRemote.setDamageSkin(chr), true);
    }

    /*
        切换地图
     */
    public static void handleChangeMapRequest(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        if (in.available() == 0) {
            c.setLoginStatus(LoginStatus.SERVER_TRANSITION);
            player.changeChannel((byte) player.getChannelId());
            return;
        }
        if (in.available() != 0) {
            byte type = in.readByte();
            int targetId = in.readInt();
            String portalName = in.readMapleAsciiString();
            if (portalName != null && !"".equals(portalName)) {
                Portal portal = player.getMap().getPortal(portalName);
                if (portal == null) {
                    //Hack
                    player.changeMap(100000000);
                    return;
                }
                portal.enterPortal(c);
            } else if (player.getHp() <= 0) {
                int returnMap = player.getMap().getReturnMap();
                player.changeMap(returnMap);
                player.heal(50);
            }
        }
    }

    public static void handleUserPortalScriptRequest(InPacket in, MapleClient c) {
        byte type = in.readByte();
        String portalName = in.readMapleAsciiString();
        Portal portal = c.getPlayer().getMap().getPortal(portalName);
        if (portal == null) {
            c.announce(UserPacket.enableActions());
            return;
        }
        if (c.getPlayer().isChangingChannel()) {
            c.announce(UserPacket.enableActions());
            return;
        }
        portal.enterPortal(c);
    }


    //给其他角色增加人气
    public static void handleUserAddFameRequest(InPacket in, MapleClient c) {
        int charId = in.readInt();
        MapleCharacter player = c.getPlayer();
        MapleCharacter other = player.getMap().getCharById(charId);
        if (other == null) {
            player.chatMessage("找不到角色");
            return;
        }
        byte mode = in.readByte();
        int fameChange = mode == 0 ? -1 : 1;
        other.addStatAndSendPacket(Stat.FAME, fameChange);
        player.announce(UserPacket.fameResponse(FameResult.addFame(other.getName(), mode, other.getFame())));
        other.announce(UserPacket.fameResponse(FameResult.receiveFame(player.getName(), mode)));
    }

    //角色表情
    public static void handleCharEmotion(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        int emotion = in.readInt();
        int duration = in.readInt();
        boolean byItemOption = in.readByte() != 0;
        if (GameConstants.isValidEmotion(emotion)) {
            player.getMap().broadcastMessage(player, UserRemote.emotion(player.getId(), emotion, duration, byItemOption), false);
        }
    }

    /*
        超级技能/属性
     */
    public static void handleUserHyperUpRequest(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        player.setTick(in.readInt());
        int skillId = in.readInt();
        SkillInfo si = SkillData.getSkillInfo(skillId);
        if (si == null) {
            player.chatMessage("attempted assigning hyper SP to a skill with null");
            return;
        }
        if (si.getHyper() == 0 && si.getHyperStat() == 0) {
            log.error(String.format("Character %d attempted assigning hyper SP to a wrong skill (skill id %d, player job %d)", player.getId(), skillId, player.getJob()));
            return;
        }
        Skill skill = player.getSkill(skillId, true);
        if (si.getHyper() != 0) { //超级技能
            if (si.getHyper() == 1) {
                int totalSp = SkillConstants.getTotalHyperPassiveSkillSp(player.getLevel());
                int spentSp = player.getSpentHyperPassiveSkillSp();
                int availableSp = totalSp - spentSp;
                if (availableSp <= 0 || skill.getCurrentLevel() != 0) {
                    return;
                }
            } else if (si.getHyper() == 2) {
                int totalSp = SkillConstants.getTotalHyperActiveSkillSp(player.getLevel());
                int spentSp = player.getSpentHyperActiveSkillSp();
                int availableSp = totalSp - spentSp;
                if (availableSp <= 0 || skill.getCurrentLevel() != 0) {
                    return;
                }
            }
        } else if (si.getHyperStat() != 0) { //超级属性
            int totalHyperSp = SkillConstants.getHyperStatSpByLv((short) player.getLevel());
            int spentSp = player.getSpentHyperStatSp();
            int availableSp = totalHyperSp - spentSp;
            int neededSp = SkillConstants.getNeededSpForHyperStatSkill(skill.getCurrentLevel() + 1);
            if (skill.getCurrentLevel() >= skill.getMaxLevel() || availableSp < neededSp) {
                return;
            }
        } else {
            log.error(String.format("Character %d attempted assigning hyper stat to an improper skill. (%d, job %d)", player.getId(), skillId, player.getJob()));
            return;
        }
        player.removeFromBaseStatCache(skill);
        skill.setCurrentLevel(skill.getCurrentLevel() + 1);
        player.addToBaseStatCache(skill);
        List<Skill> skills = new ArrayList<>();
        skills.add(skill);
        player.addSkill(skill);
        player.announce(UserPacket.changeSkillRecordResult(skills, true, false, false, false));
    }

    public static void handleUserHyperSkillResetRequest(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        player.setTick(in.readInt());
        if (player.getMeso() < GameConstants.HYPER_SKILL_RESET_COST) {
            player.chatMessage("Not enough money for this operation.");
        } else {
            player.deductMoney(GameConstants.HYPER_SKILL_RESET_COST);
            List<Skill> skills = new ArrayList<>();
            for (int skillId = 80000400; skillId <= 80000418; skillId++) {
                Skill skill = player.getSkill(skillId);
                if (skill != null) {
                    skill.setCurrentLevel(0);
                    skills.add(skill);
                    player.addSkill(skill);
                }
            }
            player.announce(UserPacket.changeSkillRecordResult(skills, true, false, false, false));
        }
    }

    public static void handleUserHyperStatResetRequest(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        player.setTick(in.readInt());
        if (player.getMeso() < GameConstants.HYPER_STAT_RESET_COST) {
            player.chatMessage("Not enough money for this operation.");
        } else {
            player.deductMoney(GameConstants.HYPER_STAT_RESET_COST);
            List<Skill> skills = new ArrayList<>();
            int skillBaseId = player.getJob() * 10000 + 31;
            for (int skillId = skillBaseId; skillId <= skillBaseId + 100; skillId++) {
                Skill skill = player.getSkill(skillId);
                if (skill != null) {
                    skill.setCurrentLevel(0);
                    skills.add(skill);
                    player.addSkill(skill);
                }
            }
            player.announce(UserPacket.changeSkillRecordResult(skills, true, false, false, false));
        }
    }

    // 商城操作
    public static void handleCashShopCashItemRequest(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        Account account = player.getAccount();
        Locker locker = account.getLocker();
        byte type = in.readByte();
        CashItemType cit = CashItemType.getRequestTypeByVal(type);
        CashShopServer cashShop = Server.getInstance().getCashShop(player.getWorldId());
        if (cit == null) {
            log.error("Unhandled cash shop cash item request " + type);
            player.enableAction();
            return;
        }
        switch (cit) {
            case Req_SetCart: {
                in.readByte();
                List<WishedItem> wishedItems = new ArrayList<>();
                while (in.available() >= 4) {
                    int itemId = in.readInt();
                    WishedItem item = new WishedItem(in.readInt());
                    if (itemId != 0) {
                        wishedItems.add(item);
                    }
                }
                player.getWishedItems().clear();
                player.getWishedItems().addAll(wishedItems);
                break;
            }
            case Req_Buy: {
                CashShopCurrencyType currencyType = CashShopCurrencyType.getByVal(in.readByte());
                if (currencyType == null) {
                    player.chatMessage(ChatType.Notice, "暫不支持的貨幣類型");
                    return;
                }
                in.readShort(); // 00 00
                int sn = in.readInt();
                int quantity = in.readInt();
                CashItemInfo cashItemInfo = ItemData.getCashItemInfo(sn);
                if (cashItemInfo == null) {
                    player.announce(CashShopPacket.buyFailed(CashItemType.FailReason_OnWorld));
                    return;
                }
                int currency;
                switch (currencyType) {
                    case Cash:
                        currency = account.getCash();
                        break;
                    case MaplePoint:
                        currency = account.getPoint();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + currencyType);
                }
                int price = cashItemInfo.getPrice();
                int cost = price * quantity;
                if (cost > currency) {
                    player.announce(CashShopPacket.buyFailed(CashItemType.FailReason_NoRemainCash));
                    return;
                }
                player.addCurrency(currencyType, -cost);
                Item itemCopy = ItemData.getItemCopy(cashItemInfo.getItemId(), false);
                itemCopy.setCashItemSerialNumber(cashShop.getNextSerialNumber());
                locker.putItem(itemCopy, 1);
                player.announce(CashShopPacket.buyDone(account, itemCopy));
                if (itemCopy.getItemId() == ItemConstants.POTION_POT) {
                    ((PotionPot) itemCopy).setCharId(player.getId());
                    player.announce(CashShopPacket.createPotionPotDone(((PotionPot) itemCopy)));
                }
                player.announce(CashShopPacket.queryCashResult(account));
                break;
            }
            case Req_EnableEquipSlotExt: {
                in.readByte();
                int sn = in.readInt();
                CashItemInfo cashItemInfo = ItemData.getCashItemInfo(sn);
                int extendDay = 0;
                if (cashItemInfo.getItemId() == 5550001) {
                    extendDay = 7;
                } else if (cashItemInfo.getItemId() == 5550000) {
                    extendDay = 30;
                }
                long maxTime = LocalDateTime.now().plusDays(364).toInstant(ZoneOffset.of("+8")).toEpochMilli();
                if (player.getExtendedPendant() < DateUtil.getFileTime(System.currentTimeMillis())) { //已过期或还未购买过
                    long expiredTime = LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("+8")).toEpochMilli();
                    player.setExtendedPendant(DateUtil.getFileTime(expiredTime));
                } else if (player.getExtendedPendant() > DateUtil.getFileTime(maxTime)) {
                    player.announce(CashShopPacket.buyFailed(CashItemType.FailReason_Max_Time_Limit));
                    return;
                } else {
                    player.setExtendedPendant(player.getExtendedPendant() + ONE_DAY_TIMES * extendDay * 10000);
                }
                player.announce(CashShopPacket.enableEquipSlotExtDone(extendDay));
                break;
            }
            case Req_MoveLtoS: { // 保管箱-》背包
                long serialNumber = in.readLong();
                int itemId = in.readInt();
                byte val = in.readByte(); //invType
                short pos = in.readShort(); //toPos
                InventoryType inventoryType = InventoryType.getTypeById(val);
                Item item = locker.getItemBySerialNumber(serialNumber);
                if (item.getItemId() != itemId || inventoryType == null) {
                    return;
                }
                item.setPos(pos);
                locker.removeItemBySerialNumber(serialNumber);
                player.getInventory(inventoryType).addItem(item);
                player.announce(CashShopPacket.moveLtoSDone(item));
                break;
            }
            case Req_MoveStoL: { //背包-》保管箱
                long serialNumber = in.readLong();
                int itemId = in.readInt();
                byte val = in.readByte();
                short pos = in.readShort();
                InventoryType inventoryType = InventoryType.getTypeById(val);
                if (inventoryType == null) {
                    return;
                }
                Item item = player.getInventory(inventoryType).getItem(pos);
                if (item.getItemId() != itemId || item.getCashItemSerialNumber() != serialNumber) {
                    return;
                }
                item.setInvType(null);
                locker.putItem(item, 1);
                player.announce(CashShopPacket.moveStoLDone(account, item));
                break;
            }
            case Req_Destroy: {
                long serialNumber = in.readLong();
                locker.removeItemBySerialNumber(serialNumber);
                player.announce(CashShopPacket.rebateDone(serialNumber));
                break;
            }
            case Req_BuyPackage: {
                in.readByte();
                long sn = in.readInt();
                int itemCount = in.readInt();
                player.announce(CashShopPacket.buyPackageDone(new ArrayList<>(), account));
                break;
            }
            case Req_BuyNormal: {
                int sn = in.readInt();
                CashItemInfo cashItemInfo = ItemData.getCashItemInfo(sn);
                if (cashItemInfo != null) {
                    int itemId = cashItemInfo.getItemId();
                    int count = cashItemInfo.getCount();
                    int price = cashItemInfo.getPrice();
                    int cost = count * price;
                    if (player.getMeso() < cost) {
                        return; //crack
                    }
                    Item item = ItemData.getItemCopy(itemId);
                    item.setQuantity(count);
                    if (!player.canHold(Collections.singletonList(item))) {
                        player.announce(MessagePacket.broadcastMsg("can't hold", BroadcastMsgType.ALERT));
                        return;
                    }
                    player.addItemToInv(item);
                    player.deductMoney(cost, true);
                    player.announce(CashShopPacket.buyNormalDone(item, price));
                }
                //0x6f add
                //0x7c0 updatePlayerStat 00 00 04 00 00 00 00 00 B1 8A 2D 00 00 00 00 00
                //Res_BuyNormal_Done 4B 01 00 00 00 01 00 06 00 D7 82 3D 00
                break;
            }
            case Req_Rebate: {
                short i = in.readShort();
                in.readByte();
                long serialNumber = in.readLong();
                Item item = locker.getItemBySerialNumber(serialNumber);
                int sn = ItemData.getSn(item.getItemId());
                CashItemInfo cashItemInfo = ItemData.getCashItemInfo(sn);
                int price = cashItemInfo.getPrice();
                account.addPoint((int) (price * 0.3));
                locker.removeItemBySerialNumber(serialNumber);
                player.announce(CashShopPacket.destroyDone(serialNumber));
                player.announce(CashShopPacket.queryCashResult(account));
                break;
            }
            default:
                break;
        }
    }

    // todo 点击机器人打开商店
    public static void handleAndroidShopRequest(InPacket in, MapleClient c) {
        in.readInt(); //charId
        int type = in.readInt();
        Position position = in.readPositionInt();
        MapleCharacter player = c.getPlayer();
        Android android = player.getAndroid();
        AndroidInfo androidInfo = ItemData.getAndroidInfoByType(type);
        if (androidInfo == null || android == null) {
            player.enableAction();
            return;
        }
        if (androidInfo.isShopUsable()) {
            player.dropMessage("机器人商店");
            player.enableAction();
        }
    }

    public static void handleUserSoulEffectRequest(InPacket in, MapleClient c) {
        MapleCharacter chr = c.getPlayer();
        boolean set = in.readByte() != 0;
        HashMap<String, String> options = new HashMap<>();
        options.put("effect", set ? "1" : "0");
        chr.addQuestExAndSendPacket(QUEST_EX_SOUL_EFFECT, options);
        chr.getMap().broadcastMessage(UserRemote.setSoulEffect(chr.getId(), set));
    }

    public static void handleUserMacroSysDataModified(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        List<Macro> macros = new ArrayList<>();
        byte size = in.readByte();
        for (byte i = 0; i < size; i++) {
            Macro macro = new Macro();
            macro.setName(in.readMapleAsciiString());
            macro.setMuted(in.readByte() != 0);
            for (int j = 0; j < 3; j++) {
                macro.setSkillAtPos(j, in.readInt());
            }
            macros.add(macro);
        }
        player.getMacros().clear();
        player.getMacros().addAll(macros); // don't set macros directly, as a new row will be made in the DB
    }

    public static void handleUserActivateEffectItem(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        int i = in.readInt();
        //todo
    }

    public static void handleUserMemorialCubeOptionRequest(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        player.setTick(in.readInt());
        boolean chooseBefore = in.readByte() == 7;
        long id = in.readLong();
        Map<String, String> values = player.getQuestEx().getOrDefault(QuestConstants.QUEST_EX_MEMORIAL_CUBE, null);
        if (values == null) {
            return;
        }
        if (!chooseBefore) {
            int ePos = Integer.parseInt(values.getOrDefault("dst", "-1"));
            int pot0 = Integer.parseInt(values.getOrDefault("pot0", "-1"));
            int pot1 = Integer.parseInt(values.getOrDefault("pot1", "-1"));
            int pot2 = Integer.parseInt(values.getOrDefault("pot2", "-1"));
            boolean add = Boolean.parseBoolean(values.getOrDefault("add", "0"));
            boolean lvup = Boolean.parseBoolean(values.getOrDefault("lvup", "0"));
            Equip equip = (Equip) player.getEquipInventory().getItem((short) ePos);
            if (equip.getId() != id) {
                player.dropMessage("????");
                return;
            }
            equip.setOption(0, pot0 == -1 ? 0 : pot0, add);
            equip.setOption(1, pot1 == -1 ? 0 : pot1, add);
            equip.setOption(2, pot2 == -1 ? 0 : pot2, add);
            equip.updateToChar(player);
            player.removeQuestEx(QuestConstants.QUEST_EX_MEMORIAL_CUBE);
        }
        player.announce(UserPacket.memorialCubeModified());
    }

    public static void handleUserSystemOptionRequest(InPacket in, MapleClient c) {
        String key = in.readMapleAsciiString(); //TrembleOption
        boolean value = in.readByte() != 0;
    }

    public static void handleUserBeastTamerHideItemRequest(InPacket in, MapleClient c) {
        int itemId = in.readInt();
        MapleCharacter player = c.getPlayer();
        Item item = player.getCashInventory().getItemByItemID(itemId);
        if (item != null) {
            Map<String, String> values;
            if (player.getQuestEx().containsKey(QuestConstants.QUEST_EX_BEAST_TAMER_LOOK)) {
                values = player.getQuestEx().get(QuestConstants.QUEST_EX_BEAST_TAMER_LOOK);
            } else {
                values = new HashMap<>();
                values.put("bTail", "1");
                values.put("bEar", "0");
                values.put("TailID", "5010119");
                values.put("EarID", "5010116");
            }
            if (itemId == 5012000) {
                values.put("bEar", values.get("bEar").equals("1") ? "0" : "1");
            } else if (itemId == 5012001) {
                values.put("bTail", values.get("bTail").equals("1") ? "0" : "1");
            }

            player.addQuestExAndSendPacket(QuestConstants.QUEST_EX_BEAST_TAMER_LOOK, values);
        }
    }


    /*
        LINK技能
     */
    public static void handleSetSonOfLinkedSkillRequest(InPacket in, MapleClient c) {
        int skillId = in.readInt();
        int originId = in.readInt();
        int linkedId = in.readInt();
        Account account = c.getAccount();
        MapleCharacter player = c.getPlayer();
        MapleCharacter origin = account.getCharacter(originId);

    }

    public static void handleRemoveSonOfLinkedSkillRequest(InPacket in, MapleClient c) {
        int skillId = in.readInt();

    }

    public static void handleUserThrowGrenade(InPacket in, MapleClient c) {
        MapleCharacter chr = c.getPlayer();
        Position des = in.readPositionInt();
        Position src = in.readPositionInt();
        int keyDown = in.readInt();
        int skillId = in.readInt();
        int bySummonId = in.readInt();
        boolean left = in.readByte() != 0;
        int attackSpeed = in.readInt();
        int grenadeId = in.readInt();
        Skill skill = chr.getSkill(skillId);
        int slv = skill == null ? 0 : skill.getCurrentLevel();
        if (slv == 0) {
            chr.chatMessage(ChatType.GameDesc, "无法使用技能");
        } else {
            boolean success = true;
            if (SkillData.getSkillInfo(skillId).hasCooltime()) {
                if (chr.isSkillInCd(skillId)) {
                    success = false;
                } else {
                    chr.setSkillCooltime(skillId, (byte) slv);
                }
            }
            if (success) {
//                chr.getField().broadcastPacket(UserRemote.throwGrenade(chr.getId(), grenadeID, pos, keyDown, skillID,
//                        bySummonedID, slv, left, attackSpeed), chr);
            }
        }
    }

    public static void handleUserStatChangeItemCancelRequest(InPacket in, MapleClient c) {
        int itemId = in.readInt();
        MapleCharacter chr = c.getPlayer();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        tsm.removeStatsBySkill(itemId);
    }

    //todo
    public static void handleUserSkillPrepareRequest(InPacket in, MapleClient c) {
        //7C 9F 2F 00 1E 00 00 00 87 6F 2E A3 00 16 80 04 00 01 8A 3D 0D
        //7C 9F 2F 00 1E 00 00 00 87 6F 2E A3 00 16 00 04 00 7D 69 3C 0D
        MapleCharacter player = c.getPlayer();
        int skillId = in.readInt();
        int slv = in.readInt();
        in.readByte();
        in.readInt();
        in.readByte();//attackAction
        in.readByte();//direction
        in.readShort();
        player.setTick(in.readInt());
        if (!player.hasSkill(skillId)) {
            return;
        }
        if (SkillConstants.isKeyDownMovingSkill(skillId)) {
            SkillInfo si = SkillData.getSkillInfo(skillId);
            Option option = new Option();
            option.nOption = si.getValue(SkillStat.x, slv);
            option.rOption = skillId;
            TemporaryStatManager tsm = player.getTemporaryStatManager();
            tsm.putCharacterStatValue(KeyDownMoving, option);
            tsm.sendSetStatPacket();
        }
        player.setPrepareSkill(skillId, skillId);
    }

    //移动技能 包括五转钩锁或者法师的瞬移？
    public static void handleUserEffectLocal(InPacket in, MapleClient c) {
        int skillId = in.readInt();
        short slv = in.readShort();
        Position from = in.readPositionInt();
        Position to = in.readPositionInt();
    }

    //有啥用呢
    public static void handleUserB2BodyRequest(InPacket in, MapleClient c) {
        short type = in.readShort();
        int ownerCID = in.readInt();
        int bodyIdCounter = in.readInt();
        Position pos1 = in.readPosition();
        Position pos2 = in.readPosition();
        int skillID = in.readInt();
        boolean isLeft = in.readByte() != 0;
        in.skip(10);
        in.readInt();
        in.readByte();
        in.readShort();
        in.readShort();
        in.readShort();
        Position forcedPos = in.readPositionInt();
        c.announce(UserPacket.userB2Body(type, bodyIdCounter));
    }

    public static void handleCheckProcess(InPacket in, MapleClient c) {
        int sum = in.readShort();
        for (int i = 0; i < sum; i++) {
            String process = in.readMapleAsciiString();
            int i1 = in.readInt();
            String hash = in.readMapleAsciiString();
        }
    }

    public static void handleUserSetGameResolution(InPacket in, MapleClient c) {
        byte resolution = in.readByte();
        boolean windowed = in.readByte() == 0;
    }

    public static void handleGoldHammerComplete(InPacket in, MapleClient c) {
        int i1 = in.readInt();
        int i2 = in.readInt();


        c.write(UserPacket.goldHammerItemUpgradeResult((byte) 2, i1));
    }


    public static void handleStackChairs(InPacket in, MapleClient c) {
        int unk = in.readInt();
        //7266
        HashMap<String, String> questEx = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            int chairId = in.readInt();
            questEx.put(String.valueOf(i), String.valueOf(chairId));
        }
        MapleCharacter chr = c.getPlayer();
        chr.addQuestExAndSendPacket(QUEST_EX_STACK_CHAIRS, questEx);
    }

    public static void handleReactorClick(InPacket in, MapleClient c) {
        MapleCharacter chr = c.getPlayer();
        int objID = in.readInt();
        int idk = in.readInt();
        byte type = in.readByte();
        MapleMapObj life = chr.getMap().getObj(objID);
        if (!(life instanceof Reactor)) {
            log.error("Could not find reactor with objID " + objID);
            return;
        }
        Reactor reactor = (Reactor) life;
        int templateID = reactor.getTemplateId();
//        ReactorInfo ri = ReactorData.getReactorInfoByID(templateID);
//        String action = ri.getAction();
//        if (chr.getScriptManager().isActive(ScriptType.Reactor)
//                && chr.getScriptManager().getParentIDByScriptType(ScriptType.Reactor) == templateID) {
//            try {
//                chr.getScriptManager().getInvocableByType(ScriptType.Reactor).invokeFunction("action", reactor, type);
//            } catch (ScriptException | NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//        } else {
//            chr.getScriptManager().startScript(templateID, objID, action, ScriptType.Reactor);
//        }

    }

    public static void handleUserMiracleCirculatorSelect(InPacket in, MapleClient c) {
        MapleCharacter chr = c.getPlayer();
        chr.setTick(in.readInt());
        boolean chooseAfter = in.readByte() != 0;
        if (chooseAfter) {
            CharacterPotentialMan potentialMan = chr.getPotentialMan();
            Set<CharacterPotential> temp = potentialMan.getTemp();
            int i = 0;
            for (CharacterPotential characterPotential : temp) {
                i++;
                potentialMan.addPotential(characterPotential, i == temp.size());
            }
        } else {
            in.readByte();
        }
    }

    public static void handleMatrixRequest(InPacket in, MapleClient c) {
        MapleCharacter chr = c.getPlayer();
        int type = in.readInt();
        MatrixUpdateType updateType = MatrixUpdateType.getUpdateTypeByVal(type);
        if (updateType == null) {
            chr.chatMessage(String.format("[VMatrix Update] Packet Data %s", in));
            chr.chatMessage(String.format("[VMatrix Update] Unknown update type [%d]", type));
            return;
        }

        switch (updateType) {
            case ENABLE: {
                int slot = in.readInt();
                in.readInt();// -1
                in.readInt();// -1
                int toSlot = in.readInt();
                boolean replace = in.readByte() != 0;
                chr.write(UserPacket.updateVMatrix(chr, true, MatrixUpdateType.ENABLE, chr.getMatrixInventory().activateSkill(slot, toSlot)));
                MatrixInventory.reloadSkills(chr);
                break;
            }
            case DISABLE: {
                int slot = in.readInt();
                in.readInt();// -1
                chr.write(UserPacket.updateVMatrix(chr, true, MatrixUpdateType.DISABLE, chr.getMatrixInventory().deactivateSkill(slot)));
                MatrixInventory.reloadSkills(chr);
                break;
            }
            case MOVE: {
                int skillSlotID = in.readInt();
                int replaceSkill = in.readInt();
                int fromSlot = in.readInt();// 0
                int toSlot = in.readInt();
                chr.getMatrixInventory().moveSkill(skillSlotID, replaceSkill, fromSlot, toSlot);
                chr.write(UserPacket.updateVMatrix(chr, true, MatrixUpdateType.MOVE, 0));
                MatrixInventory.reloadSkills(chr);
                break;
            }
            case DISASSEMBLE_SINGLE: {
                int slot = in.readInt();
                in.readInt();// -1
                chr.getMatrixInventory().disassemble(chr, slot);
                MatrixInventory.reloadSkills(chr);
                break;
            }
            case DISASSEMBLE_MULTIPLE: {
                int count = in.readInt();

                List<MatrixSkill> skills = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    MatrixSkill skill = chr.getMatrixInventory().getSkill(in.readInt());
                    if (skill != null) {
                        skills.add(skill);
                    }
                }
                chr.getMatrixInventory().disassembleMultiple(chr, skills);
                MatrixInventory.reloadSkills(chr);
                break;
            }
            case ENHANCE: {
                int slot = in.readInt();
                MatrixSkill toEnhance = chr.getMatrixInventory().getSkill(slot);
                if (toEnhance != null && toEnhance.getLevel() < VCoreData.getMaxLevel(VCoreData.getCore(toEnhance.getCoreId()).getType())) {
                    int count = in.readInt();
                    List<MatrixSkill> skills = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        MatrixSkill skill = chr.getMatrixInventory().getSkill(in.readInt());
                        if (skill != null) {
                            skills.add(skill);
                        }
                    }
                    chr.getMatrixInventory().enhance(chr, toEnhance, skills);
                    MatrixInventory.reloadSkills(chr);
                }
                break;
            }
            case CRAFT_NODE: {
                int coreID = in.readInt();
                VCore core = VCoreData.getCore(coreID);
                int quantity = in.readInt();
                if (core != null) {
                    int price = 0;
                    if (VCoreData.isSkillNode(coreID)) {
                        price = MatrixConstants.CRAFT_SKILL_CORE_COST;
                    } else if (VCoreData.isBoostNode(coreID)) {
                        price = MatrixConstants.CRAFT_ENCHANT_CORE_COST;
                    } else if (VCoreData.isSpecialNode(coreID)) {
                        price = MatrixConstants.CRAFT_SPECIAL_CORE_COST;
                    } else if (VCoreData.isExpNode(coreID)) {
                        price = MatrixConstants.CRAFT_GEMSTONE_COST;
                    }
                    price *= quantity;
                    if (price > 0) {
                        int shardCount = chr.getShards();
                        if (shardCount >= price) {
                            chr.incShards(-price);

                            MatrixSkill skill = new MatrixSkill();
                            skill.setCoreId(coreID);
                            if (!VCoreData.isSpecialNode(coreID)) {
                                skill.setSkill1(core.getConnectSkills().get(0));
                                skill.setLevel(1);
                                skill.setMasterLevel(core.getMaxLevel());
                            } else {
                                skill.setSkill1(0);
                                skill.setLevel(1);
                                skill.setMasterLevel(1);
                                skill.setExpirationDate(DateUtil.getFileTime(System.currentTimeMillis() + (86400000L * core.getExpireAfter())));
                            }
                            if (VCoreData.isBoostNode(coreID)) {
                                List<VCore> boostNode = VCoreData.getBoostNodes();
                                boostNode.remove(core);

                                core = boostNode.get((int) (Math.random() % boostNode.size()));
                                while (!core.isJobSkill(chr.getJob())) {
                                    core = boostNode.get((int) (Math.random() % boostNode.size()));
                                }
                                boostNode.remove(core);
                                skill.setSkill2(core.getConnectSkills().get(0));

                                core = boostNode.get((int) (Math.random() % boostNode.size()));
                                while (!core.isJobSkill(chr.getJob())) {
                                    core = boostNode.get((int) (Math.random() % boostNode.size()));
                                }
                                skill.setSkill3(core.getConnectSkills().get(0));
                            }
                            chr.getMatrixInventory().addSkill(skill);
                            MatrixInventory.reloadSkills(chr);
                            chr.write(UserPacket.updateVMatrix(chr, false, MatrixUpdateType.CRAFT_NODE, 0));
                            chr.write(UserPacket.nodeCraftResult(coreID, quantity, skill.getSkill1(), skill.getSkill2(), skill.getSkill3()));
                        }
                    }
                }
                break;
            }
            case SLOT_ENHANCEMENT: {
                int slotId = in.readInt();
                in.readInt();//FF FF FF FF
                MatrixInventory matrixInventory = chr.getMatrixInventory();
                MatrixSlot matrixSlot = matrixInventory.getMatrixSlotBySlotId(slotId);
                if (matrixSlot.getEnhanceLevel() >= 5) {
                    return;
                }
                matrixSlot.setEnhanceLevel(matrixSlot.getEnhanceLevel() + 1);
                MatrixInventory.reloadSkills(chr);
                chr.announce(UserPacket.updateVMatrix(chr, false, MatrixUpdateType.SLOT_ENHANCEMENT, 0));
            }
            case RESET_SLOT_ENHANCEMENT: {
                chr.getMatrixInventory().resetSlotsEnhanceLevel(chr);
                break;
            }
            default: {
                break;
            }
        }

    }
}
