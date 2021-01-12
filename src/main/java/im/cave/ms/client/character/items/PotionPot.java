package im.cave.ms.client.character.items;

import im.cave.ms.connection.netty.OutPacket;
import im.cave.ms.connection.packet.opcode.SendOpcode;
import im.cave.ms.constants.GameConstants;
import im.cave.ms.tools.DateUtil;
import lombok.Getter;
import lombok.Setter;

import static im.cave.ms.constants.ServerConstants.MAX_TIME;


/**
 * @author fair
 * @version V1.0
 * @Package im.cave.ms.client.items
 * @date 1/2 17:39
 */
@Setter
@Getter
public class PotionPot {
    private int itemId;
    private int charId;
    private int maxValue;
    private int hp;
    private int mp;
    private long startTime;
    private long endTime;

    public PotionPot(int itemId, int charId) {
        this.itemId = itemId;
        this.charId = charId;
        this.maxValue = 1000000;
        this.startTime = DateUtil.getFileTime(System.currentTimeMillis());
        this.endTime = MAX_TIME;
    }

    public OutPacket updatePotionPot() {
        OutPacket out = new OutPacket();
        out.writeShort(SendOpcode.POTION_POT_UPDATE.getValue());
        encode(out);
        return out;
    }


    public OutPacket showPotionPotMsg(byte type, byte reason) {
        OutPacket out = new OutPacket();
        out.writeShort(SendOpcode.POTION_POT_MESSAGE.getValue());
        out.write(type);
        if (type == 0) {
            /*
             * 0x00 没有提示
             * 0x01 没有物品
             * 0x02 这个药剂罐已经满了。
             * 0x03 你的药剂罐容量已达最大值。
             * 0x04 药剂魔瓶不能用在生锈的药剂罐上。请用除锈剂为你的药剂罐除锈。
             * 0x05 你的药剂罐还没有生锈。
             * 0x06 这个药剂罐是空的，请再次填充。
             * 0x08 被奇怪的气息所围绕，暂时无法使用道具。
             */
            out.write(reason);
        }
        return out;
    }

    public boolean addMaxValue() {
        if (maxValue + 1000000 > GameConstants.POTION_POT_MAX_LIMIT) {
            return false;
        }
        maxValue += 1000000;
        return true;
    }

    public void encode(OutPacket out) {
        out.writeInt(getItemId());
        out.writeInt(getCharId());
        out.writeInt(getMaxValue());
        out.writeInt(getHp());
        out.writeInt(getMp());
        out.writeLong(getStartTime());
        out.writeLong(getEndTime());
    }
}
