package im.cave.ms.connection.netty;

import im.cave.ms.connection.server.AbstractServer;
import im.cave.ms.connection.server.auction.AuctionHandler;
import im.cave.ms.connection.server.cashshop.CashShopHandler;
import im.cave.ms.connection.server.channel.ChannelHandler;
import im.cave.ms.connection.server.login.LoginServerHandler;
import im.cave.ms.enums.ServerType;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author fair
 * @version V1.0
 * @Package im.cave.ms.connection.netty
 * @date 11/19 19:09
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private final int channelId;
    private final int worldId;
    private final ServerType type;

    public ServerInitializer(AbstractServer abstractServer) {
        this.type = abstractServer.getType();
        this.channelId = abstractServer.getChannelId();
        this.worldId = abstractServer.getWorldId();
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("idleStateHandler", new IdleStateHandler(25, 25, 0));
        pipeline.addLast("decoder", new MaplePacketDecoder());
        pipeline.addLast("encoder", new MaplePacketEncoder());
        switch (type) {
            case LOGIN:
                pipeline.addLast(new LoginServerHandler());
                break;
            case CHANNEL:
                pipeline.addLast(new ChannelHandler(channelId, worldId));
                break;
            case CASHSHOP:
                pipeline.addLast(new CashShopHandler(worldId));
                break;
            case AUCTION:
                pipeline.addLast(new AuctionHandler(worldId));
                break;
        }
    }
}
