package flab.project.util;

public class AddressUtils {

    private final String externalIP;
    public AddressUtils(String externalIP) {
        this.externalIP = externalIP;
    }

    public String getMyExternalIP() {
        return externalIP;
    }
}
