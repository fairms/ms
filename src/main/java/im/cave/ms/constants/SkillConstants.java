package im.cave.ms.constants;

import im.cave.ms.provider.data.SkillData;
import im.cave.ms.provider.info.SkillInfo;

/**
 * @author fair
 * @version V1.0
 * @Package im.cave.ms.constant
 * @date 11/19 23:37
 */
public class SkillConstants {

    public static int getBaseSpByLevel(short level) {
        return level > 140 ? 0
                : level > 130 ? 6
                : level > 120 ? 5
                : level > 110 ? 4
                : 3;
    }


    public static boolean isPassiveSkill(int skillId) {
        SkillInfo si = SkillData.getSkillInfo(skillId);
        return si != null && si.isPsd() && si.getPsdSkills().size() == 0;
    }

    public static boolean isMatching(int rootId, int job) {
        boolean matchingStart = job / 100 == rootId / 100;
        boolean matching = matchingStart;
        if (matchingStart && rootId % 100 != 0) {
            // job path must match
            matching = (rootId % 100) / 10 == (job % 100) / 10;
        }
        return matching;
    }

    public static boolean isSkillFromItem(int skillID) {
        switch (skillID) {
            case 80011123: // New Destiny
            case 80011247: // Dawn Shield
            case 80011248: // Dawn Shield
            case 80011249: // Divine Guardian
            case 80011250: // Divine Shield
            case 80011251: // Divine Brilliance
            case 80011261: // Monolith
            case 80011295: // Scouter
            case 80011346: // Ribbit Ring
            case 80011347: // Krrr Ring
            case 80011348: // Rawr Ring
            case 80011349: // Pew Pew Ring
            case 80011475: // Elunarium Power (ATT & M. ATT)
            case 80011476: // Elunarium Power (Skill EXP)
            case 80011477: // Elunarium Power (Boss Damage)
            case 80011478: // Elunarium Power (Ignore Enemy DEF)
            case 80011479: // Elunarium Power (Crit Rate)
            case 80011480: // Elunarium Power (Crit Damage)
            case 80011481: // Elunarium Power (Status Resistance)
            case 80011482: // Elunarium Power (All Stats)
            case 80011492: // Firestarter Ring
            case 80001768: // Rope Lift
            case 80001705: // Rope Lift
            case 80001941: // Scouter
            case 80010040: // Altered Fate
                return true;
        }
        // Tower of Oz skill rings
        return (skillID >= 80001455 && skillID <= 80001479);
    }

    public static boolean isSkillNeedMasterLevel(int skillId) {
        if (isIgnoreMasterLevel(skillId)
                || (skillId / 1000000 == 92 && (skillId % 10000 == 0))
                || isMakingSkillRecipe(skillId)
                || isCommonSkill(skillId)
                || isNoviceSkill(skillId)
                || isFieldAttackObjSkill(skillId)) {
            return false;
        }
        int job = getSkillRootFromSkill(skillId);
        return skillId != 42120024 && !JobConstants.isBeastTamer((short) job)
                && (isAddedSpDualAndZeroSkill(skillId) || (JobConstants.getJobLevel((short) job) == 4 && !JobConstants.isZero((short) job)));
    }

    public static boolean isIgnoreMasterLevel(int skillId) {
        switch (skillId) {
            case 1120012:
            case 1320011:
            case 2121009:
            case 2221009:
            case 2321010:
            case 3210015:
            case 4110012:
            case 4210012:
            case 4340009:
            case 5120011:
            case 5120012:
            case 5220012:
            case 5220014:
            case 5320007:
            case 5321004:
            case 5321006:
            case 21120011:
            case 21120014:
            case 21120020:
            case 21120021:
            case 22171069:
            case 23120011:
            case 23120012:
            case 23120013:
            case 23121008:
            case 33120010:
            case 35120014:
            case 80001913:
                return true;
            default:
                return false;
        }
    }

    public static boolean isMakingSkillRecipe(int recipeId) {
        boolean result = false;
        if (recipeId / 1000000 != 92 || recipeId % 10000 == 1) {
            int v1 = 10000 * (recipeId / 10000);
            if (v1 / 1000000 == 92 && (v1 % 10000 == 0))
                result = true;
        }
        return result;
    }

    private static boolean isCommonSkill(int skillId) {
        int prefix = skillId / 10000;
        if (skillId / 10000 == 8000) {
            prefix = skillId / 100;
        }
        return (prefix >= 800000 && prefix <= 800099) || prefix == 8001;
    }

    private static boolean isNoviceSkill(int skillId) {
        int prefix = skillId / 10000;
        if (skillId / 10000 == 8000) {
            prefix = skillId / 100;
        }
        return JobConstants.isBeginnerJob((short) prefix);
    }

    public static boolean isFieldAttackObjSkill(int skillId) {
        if (skillId <= 0) {
            return false;
        }
        int prefix = skillId / 10000;
        if (skillId / 10000 == 8000) {
            prefix = skillId / 100;
        }
        return prefix == 9500;
    }

    public static int getSkillRootFromSkill(int skillId) {
        int prefix = skillId / 10000;
        if (prefix == 8000) {
            prefix = skillId / 100;
        }
        return prefix;
    }


    public static boolean isAddedSpDualAndZeroSkill(int skillId) {
        if (skillId > 101100101) {
            if (skillId > 101110203) {
                if (skillId == 101120104)
                    return true;
                return skillId == 101120204;
            } else {
                if (skillId == 101110203 || skillId == 101100201 || skillId == 101110102)
                    return true;
                return skillId == 101110200;
            }
        } else {
            if (skillId == 101100101)
                return true;
            if (skillId > 4331002) {
                if (skillId == 4340007 || skillId == 4341004)
                    return true;
                return skillId == 101000101;
            } else {
                if (skillId == 4331002 || skillId == 4311003 || skillId == 4321006)
                    return true;
                return skillId == 4330009;
            }
        }
    }


    public static int getHyperPassiveSkillSpByLv(int level) {
        return level >= 140 && level <= 220 && level % 10 == 0 ? 1 : 0;
    }

    public static int getHyperActiveSkillSpByLv(int level) {
        return level == 150 || level == 170 || level == 200 ? 1 : 0;
    }

    public static int getTotalHyperPassiveSkillSp(int level) {
        if (level >= 140) {
            return 1 + ((Math.min(level, 220) - 140) / 10);
        } else {
            return 0;
        }
    }

    public static int getTotalHyperActiveSkillSp(int level) {
        if (level >= 200) {
            return 3;
        } else if (level >= 170) {
            return 2;
        } else if (level >= 150) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int getHyperStatSpByLv(short level) {
        return 3 + ((level - 140) / 10);
    }

    public static int getNeededSpForHyperStatSkill(int lv) {
        switch (lv) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 8;
            case 5:
                return 10;
            case 6:
                return 15;
            case 7:
                return 20;
            case 8:
                return 25;
            case 9:
                return 30;
            case 10:
                return 35;
            default:
                return 0;
        }
    }

    public static int getTotalNeededSpForHyperStatSkill(int lv) {
        switch (lv) {
            case 1:
                return 1;
            case 2:
                return 3;
            case 3:
                return 7;
            case 4:
                return 15;
            case 5:
                return 25;
            case 6:
                return 40;
            case 7:
                return 60;
            case 8:
                return 85;
            case 9:
                return 115;
            case 10:
                return 150;
            default:
                return 0;
        }
    }

    public static boolean isPassiveSkill_NoPsdSkillsCheck(int skillId) {
        SkillInfo si = SkillData.getSkillInfo(skillId);
        return si != null && si.isPsd();
    }

    public static boolean isZeroSkill(int skillId) {
        int prefix = skillId / 10000;
        if (prefix == 8000) {
            prefix = skillId / 100;
        }
        return prefix == 10000 || prefix == 10100 || prefix == 10110 || prefix == 10111 || prefix == 10112;

    }

    public static int getLinkSkillLevelByMapleCharacterLevel(short level) {
        if (level >= 120) {
            return 2;
        } else if (level >= 70) {
            return 1;
        }
        return 0;
    }

    public static boolean isGuildContentSkill(int skillId) {
        return (skillId >= 91000007 && skillId <= 91000015) || (skillId >= 91001016 && skillId <= 91001021);
    }

    public static boolean isGuildNoblesseSkill(int skillId) {
        return skillId >= 91001022 && skillId <= 91001024;
    }

    public static boolean isStealableSkill(int skillID) {
        return false;
    }


    public static boolean isKeyDownSkill(int skillID) {
        return skillID == 1311011 || skillID == 2221011 || skillID == 2221052 || skillID == 2321001 || skillID == 3101008 || skillID == 3111013 || skillID == 3121020 || skillID == 4341002 ||
                skillID == 5081002 || skillID == 5081010 || skillID == 5221004 || skillID == 5311002 || skillID == 5700010 || skillID == 5711021 || skillID == 5721001 || skillID == 5721061 ||
                skillID == 11121052 || skillID == 11121055 || skillID == 12121054 || skillID == 13111020 || skillID == 13121001 || skillID == 14111006 || skillID == 14121004 || skillID == 20041226 ||
                skillID == 21120018 || skillID == 22171083 || skillID == 23121000 || skillID == 24121000 || skillID == 24121005 || skillID == 25111005 || skillID == 25121030 || skillID == 27101202 ||
                skillID == 27111100 || skillID == 30021238 || skillID == 31001000 || skillID == 31101000 || skillID == 31111005 || skillID == 31211001 || skillID == 33121009 || skillID == 33121114 ||
                skillID == 33121214 || skillID == 35121015 || skillID == 36101001 || skillID == 36121000 || skillID == 37121003 || skillID == 37121052 || skillID == 41121001 || skillID == 42121000 ||
                skillID == 60011216 || skillID == 64001000 || skillID == 64001007 || skillID == 64001008 || skillID == 64121002 || skillID == 65121003 || skillID == 80001392 || skillID == 80001587 ||
                skillID == 80001629 || skillID == 80001836 || skillID == 80001887 || skillID == 80002458 || skillID == 80011051 || skillID == 80011362 || skillID == 80011366 || skillID == 80011371 ||
                skillID == 80011381 || skillID == 80011382 || skillID == 80011387 || skillID == 95001001 || skillID == 101110101 || skillID == 101110102 || skillID == 112001008 || skillID == 112110003 ||
                skillID == 112111016 || skillID == 131001004 || skillID == 131001008 || skillID == 131001020 || skillID == 142111010 || skillID == 155121341;
    }

    //todo 更多的
    public static boolean isKeyDownMovingSkill(int skillId) {
        return skillId == 3121020;
    }

    public static boolean isFlipAffectAreaSkill(int skillID) {
        return skillID == 33111013 || skillID == 33121016 || skillID == 33121012 || skillID == 131001207 ||
                skillID == 131001107 || skillID == 4121015 || skillID == 51120057 || skillID == 400001017 ||
                skillID == 400021039 || skillID == 400041041 || skillID == 152121041 || skillID == 400020046 ||
                skillID == 400020051 || skillID == 35121052;
    }
}
