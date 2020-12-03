package im.cave.ms.net.server.world;

import im.cave.ms.config.WorldConfig;
import im.cave.ms.net.server.channel.MapleChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fair
 * @version V1.0
 * @Package im.cave.ms.abstractServer.world
 * @date 11/19 16:22
 */
public class World {
    private int id;
    private List<MapleChannel> channels = new ArrayList<>();
    private String eventMessage;

    public World(int id, String eventMessage) {
        this.id = id;
        this.eventMessage = eventMessage;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MapleChannel> getChannels() {
        return channels;
    }

    public MapleChannel getChannel(int id) {
        return channels.stream().filter(channel -> channel.getChannelId() == id).findAny().orElse(channels.get(0));
    }

    public void setChannels(List<MapleChannel> channels) {
        this.channels = channels;
    }

    public int getChannelsSize() {
        return channels.size();
    }


    public void init() {
        WorldConfig.WorldInfo info = WorldConfig.config.getWorldInfo(id);
        for (int i = 0; i < info.channels; i++) {
            MapleChannel channel = new MapleChannel(id, i);
            channels.add(channel);
        }
    }

    public String getEventMessage() {
        return eventMessage;
    }
}
