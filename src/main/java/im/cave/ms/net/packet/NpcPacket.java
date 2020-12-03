package im.cave.ms.net.packet;

import im.cave.ms.enums.NpcMessageType;
import im.cave.ms.net.packet.opcode.SendOpcode;
import im.cave.ms.scripting.npc.NpcScriptInfo;
import im.cave.ms.tools.data.output.MaplePacketLittleEndianWriter;

/**
 * @author fair
 * @version V1.0
 * @Package im.cave.ms.net.packet
 * @date 11/30 21:20
 */
public class NpcPacket {

    public static MaplePacketLittleEndianWriter npcTalk(NpcMessageType type, NpcScriptInfo nsi) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        int overrideTemplate = nsi.getOverrideSpeakerTemplateID();
        mplew.writeShort(SendOpcode.NPC_TALK.getValue());
        mplew.write(nsi.getSpeakerType());
        mplew.writeInt(nsi.getTemplateID());
        mplew.write(1);
        mplew.writeInt(0);
        mplew.write(type.getVal());
        mplew.writeShort(nsi.getParam());
        mplew.write(nsi.getColor());
        switch (type) {
            case Say:
            case SayOk:
            case SayNext:
            case SayPrev:
                if ((nsi.getParam() & 4) != 0) {
                    mplew.writeInt(nsi.getOverrideSpeakerTemplateID());
                }
                mplew.writeMapleAsciiString(nsi.getText());
                mplew.writeBool(type.isPrevPossible());
                mplew.writeBool(type.isNextPossible());
                mplew.writeInt(type.getDelay());
                mplew.write(1);
                break;
            case Say_2:
            case SayOk_2:
            case SayNext_2:
            case SayPrev_2:
                mplew.writeMapleAsciiString(nsi.getText());
                mplew.writeBool(type.isPrevPossible());
                mplew.writeBool(type.isNextPossible());
                mplew.writeInt(type.getDelay());
                break;
            case SayImage:
                String[] images = nsi.getImages();
                mplew.write(images.length);
                for (String image : images) {
                    mplew.writeMapleAsciiString(image);
                }
                break;
            case AskMenu:
            case AskAccept:
            case AskYesNo:
                if ((nsi.getParam() & 4) != 0) {
                    mplew.writeInt(nsi.getOverrideSpeakerTemplateID());
                }
                mplew.writeMapleAsciiString(nsi.getText());
                break;
            case AskText:
            case AskBoxtext:
                if ((nsi.getParam() & 4) != 0) {
                    mplew.writeInt(nsi.getOverrideSpeakerTemplateID());
                }
                mplew.writeMapleAsciiString(nsi.getText());
                mplew.writeMapleAsciiString(nsi.getDefaultText());
                mplew.writeShort(nsi.getMin());
                mplew.writeShort(nsi.getMax());
                break;
            case AskNumber:
                mplew.writeMapleAsciiString(nsi.getText());
                mplew.writeInt(nsi.getDefaultNumber());
                mplew.writeInt(nsi.getMin());
                mplew.writeInt(nsi.getMax());
                break;
            case InitialQuiz:
                mplew.write(nsi.getType());
                if (nsi.getType() != 1) {
                    mplew.writeMapleAsciiString(nsi.getTitle());
                    mplew.writeMapleAsciiString(nsi.getProblemText());
                    mplew.writeMapleAsciiString(nsi.getHintText());
                    mplew.writeInt(nsi.getMin());
                    mplew.writeInt(nsi.getMax());
                    mplew.writeInt(nsi.getTime()); // in seconds
                }
                break;
            case InitialSpeedQuiz:
                mplew.write(nsi.getType());
                if (nsi.getType() != 1) {
                    mplew.writeInt(nsi.getQuizType());
                    mplew.writeInt(nsi.getAnswer());
                    mplew.writeInt(nsi.getCorrectAnswers());
                    mplew.writeInt(nsi.getRemaining());
                    mplew.writeInt(nsi.getTime()); // in seconds
                }
                break;
            case ICQuiz:
                mplew.write(nsi.getType());
                if (nsi.getType() != 1) {
                    mplew.writeMapleAsciiString(nsi.getText());
                    mplew.writeMapleAsciiString(nsi.getHintText());
                    mplew.writeInt(nsi.getTime()); // in seconds
                }
                break;
            case AskAvatar:
                int[] options = nsi.getOptions();
                mplew.writeBool(nsi.isAngelicBuster());
                mplew.writeBool(nsi.isZeroBeta());
                mplew.writeMapleAsciiString(nsi.getText());
                mplew.writeInt(0);// unk
                mplew.write(options.length);
                for (int option : options) {
                    mplew.writeInt(option);
                }
                break;
            case AskSlideMenu:
//                mplew.writeInt(nsi.getDlgType());
//                // start CSlideMenuDlg::SetSlideMenuDlg
//                mplew.writeInt(0); // last selected
//                StringBuilder sb = new StringBuilder();
//                for (DimensionalPortalType dpt : DimensionalPortalType.values()) {
//                    if (dpt.getMapID() != 0) {
//                        sb.append("#").append(dpt.getVal()).append("#").append(dpt.getDesc());
//                    }
//                }
//                mplew.writeMapleAsciiString(sb.toString());
//                mplew.writeInt(0);
                break;
            case AskSelectMenu:
                mplew.writeInt(nsi.getDlgType());
                if (nsi.getDlgType() <= 0 || nsi.getDlgType() == 1) {
                    mplew.writeInt(nsi.getDefaultSelect());
                    mplew.writeInt(nsi.getSelectText().length);
                    for (String selectText : nsi.getSelectText()) {
                        mplew.writeMapleAsciiString(selectText);
                    }
                }
                break;
            case SayIllustration:
            case SayIllustrationOk:
            case SayIllustrationNext:
            case SayIllustrationPrev:
                if ((nsi.getParam() & 4) != 0) {
                    mplew.writeInt(nsi.getOverrideSpeakerTemplateID());
                }
                mplew.writeMapleAsciiString(nsi.getText());
                mplew.writeBool(type.isPrevPossible());
                mplew.writeBool(type.isNextPossible());
                mplew.writeInt((nsi.getParam() & 4) != 0 ? nsi.getOverrideSpeakerTemplateID() : overrideTemplate != 0 ? overrideTemplate : nsi.getTemplateID());
                mplew.writeInt(nsi.getFaceIndex());
                mplew.writeBool(nsi.isLeft());
                break;
        }
        if ((nsi.getParam() & 4) != 0) {
            nsi.setParam((byte) (nsi.getParam() ^ 4));
        }

        return mplew;
    }


    public static MaplePacketLittleEndianWriter removeNpc(int objId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcode.REMOVE_NPC.getValue());
        mplew.writeInt(objId);
        return mplew;
    }
}
