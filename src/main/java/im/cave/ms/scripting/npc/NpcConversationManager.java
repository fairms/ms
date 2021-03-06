package im.cave.ms.scripting.npc;

import im.cave.ms.client.MapleClient;
import im.cave.ms.connection.packet.NpcPacket;
import im.cave.ms.connection.packet.WorldPacket;
import im.cave.ms.connection.packet.result.ExpressResult;
import im.cave.ms.connection.packet.result.GuildResult;
import im.cave.ms.enums.NpcMessageType;
import im.cave.ms.scripting.AbstractPlayerInteraction;

import java.util.Map;

import static im.cave.ms.enums.NpcMessageType.AskAccept;
import static im.cave.ms.enums.NpcMessageType.AskAvatar;
import static im.cave.ms.enums.NpcMessageType.AskMenu;
import static im.cave.ms.enums.NpcMessageType.AskText;
import static im.cave.ms.enums.NpcMessageType.AskYesNo;
import static im.cave.ms.enums.NpcMessageType.SayNext;
import static im.cave.ms.enums.NpcMessageType.SayOk;

/**
 * @author fair
 * @version V1.0
 * @Package im.cave.ms.scripting.npc
 * @date 11/30 17:29
 */
public class NpcConversationManager extends AbstractPlayerInteraction {

    private final int npcId;
    private final String script;
    private final NpcScriptManager nsm;
    private final NpcScriptInfo npcScriptInfo;


    public NpcConversationManager(MapleClient c, int npcId, NpcScriptManager nsm, String script) {
        super(c);
        this.npcId = npcId;
        this.nsm = nsm;
        this.npcScriptInfo = new NpcScriptInfo(npcId);
        this.script = script;
    }


    public int sendNext(String text) {
        return sendGeneralSay(text, SayNext);
    }

    public int sendAskYesNo(String text) {
        return sendGeneralSay(text, AskYesNo);
    }

    public int sendAskAccept(String text) {
        return sendGeneralSay(text, AskAccept);
    }

    public int sendSayOkay(String text) {
        return sendGeneralSay(text, SayOk);
    }

    public int sendAskMenu(Map<Integer, String> options) {
        StringBuilder sb = new StringBuilder();
        options.forEach((option, text) -> {
            if (option == null) {
                sb.append(text);
            } else {
                sb.append("#L").append(option).append("#")
                        .append(text).append("#l");
            }
            sb.append("\\n");
        });
        return sendGeneralSay(sb.toString(), AskMenu);
    }

    public String sendAskText(String text, String defaultText, short minLength, short maxLength) {
        getNpcScriptInfo().setMin(minLength);
        getNpcScriptInfo().setMax(maxLength);
        getNpcScriptInfo().setDefaultText(defaultText);
        getNpcScriptInfo().setText(text);
        getNpcScriptInfo().setMessageType(AskText);
        c.announce(NpcPacket.npcTalk(AskText, npcScriptInfo));
        Object response;
        response = npcScriptInfo.awaitResponse();
        if (response == null) {
            return "";
        }
        return ((String) response);
    }

    public int sendAskAvatar(String text, int requireCard, int[] options, boolean isAngelicBuster, boolean isZeroBeta) {
        npcScriptInfo.setOptions(options);
        npcScriptInfo.setColor(0);
        npcScriptInfo.setRequireCard(requireCard);
        return sendGeneralSay(text, AskAvatar);
    }

    public int sendAskAvatar(String text, int requireCard, int[] options) {
        return sendAskAvatar(text, requireCard, options, false, false);
    }


    public int sendGeneralSay(String text, NpcMessageType type) {
        text = text.replaceAll("<<", "<");
        npcScriptInfo.setText(text);
        npcScriptInfo.setMessageType(type);
        if (text.contains("#L")) {
            type = AskMenu;
        }
        c.announce(NpcPacket.npcTalk(type, npcScriptInfo));
        Object response;
        response = npcScriptInfo.awaitResponse();
        if (response == null) {
            return 0;
        }
        return ((int) response);
    }

    public void dispose() {
        nsm.dispose(getClient());
    }

    public void resetParam() {
        getNpcScriptInfo().resetParam();
    }

    public void removeEscapeButton() {
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.NotCancellable);
    }

    public void addEscapeButton() {
        if (getNpcScriptInfo().hasParam(NpcScriptInfo.Param.NotCancellable)) {
            getNpcScriptInfo().removeParam(NpcScriptInfo.Param.NotCancellable);
        }
    }

    public void flipSpeaker() {
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.FlipSpeaker);
    }

    public void flipDialogue() {
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.OverrideSpeakerID);
    }

    public void flipDialoguePlayerAsSpeaker() {
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.PlayerAsSpeakerFlip);
    }

    public void setPlayerAsSpeaker() {
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.PlayerAsSpeaker);
    }

    public void setColor(byte color) {
        getNpcScriptInfo().setColor(color);
    }

    public void setBoxChat() {
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.BoxChat);
    }

    public void setBoxChat(boolean color) { // true = Standard BoxChat  |  false = Zero BoxChat
        getNpcScriptInfo().setColor((byte) (color ? 1 : 0));
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.BoxChat);
    }

    public void setBoxOverrideSpeaker() {
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.BoxChatOverrideSpeaker);
    }

    public void setIntroBoxChat(int npcID) {
        setSpeakerID(npcID);
        getNpcScriptInfo().setColor((byte) 1);
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.BoxChatOverrideSpeakerNoEndChat);
    }

    public void setNpcOverrideBoxChat(int npcID) {
        setSpeakerID(npcID);
        getNpcScriptInfo().setColor((byte) 1);
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.BoxChatOverrideSpeakerNoEndChat);
    }

    public void flipBoxChat() {
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.FlipBoxChat);
    }

    public void boxChatPlayerAsSpeaker() {
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.BoxChatAsPlayer);
    }

    public void flipBoxChatPlayerAsSpeaker() {
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.FlipBoxChatAsPlayer);
    }

    public void flipBoxChatPlayerNoEscape() {
        getNpcScriptInfo().addParam(NpcScriptInfo.Param.FlipBoxChatAsPlayerNoEscape);
    }

    public NpcScriptInfo getNpcScriptInfo() {
        return npcScriptInfo;
    }

    public void setSpeakerID(int templateID) {
        NpcScriptInfo nsi = getNpcScriptInfo();
        boolean isNotCancellable = nsi.hasParam(NpcScriptInfo.Param.NotCancellable);
        nsi.resetParam();
        nsi.setOverrideSpeakerTemplateID(templateID);
        if (isNotCancellable) {
            nsi.addParam(NpcScriptInfo.Param.NotCancellable);
        }
    }

    public int getNpcId() {
        return npcId;
    }

    public String getScript() {
        return script;
    }

    public void openExpressDialog() {
        c.announce(WorldPacket.expressResult(ExpressResult.open()));
    }

    public void inputGuildName() {
        c.announce(WorldPacket.guildResult(GuildResult.inputGuildName()));
    }

}
