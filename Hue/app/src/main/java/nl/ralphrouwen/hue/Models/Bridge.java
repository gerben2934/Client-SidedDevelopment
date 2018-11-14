package nl.ralphrouwen.hue.Models;

public class Bridge {

    String name;
    String ip;
    String token;

    public Bridge(String name, String ip, String token) {
        this.name = name;
        this.ip = ip;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public String getToken() {
        return token;
    }
}
