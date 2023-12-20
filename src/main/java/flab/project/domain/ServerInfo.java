package flab.project.domain;

import java.io.Serializable;
import java.net.InetAddress;
import lombok.Getter;

@Getter
public class ServerInfo extends BaseEntity implements Serializable {

    private String address;
    private int port;

    private ServerInfo(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public static ServerInfo of(String address, int port) {
        return new ServerInfo(address, port);
    }
}
