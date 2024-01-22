package pl.bpd.ddd.interfaces.gateway;

public record PostDto(int id, int userId, String title, String body) {
}
