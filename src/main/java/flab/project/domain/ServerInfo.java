package flab.project.domain;

import java.io.Serializable;
import java.net.InetAddress;
import lombok.Getter;

@Getter
public class ServerInfo implements Serializable {

    private InetAddress address;
    private int port;

    private ServerInfo(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public static ServerInfo of(InetAddress address, int port) {
        return new ServerInfo(address, port);
    }
}
