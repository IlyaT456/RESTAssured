package threadQA.endpoints;

public enum ReqresInEndpoints {
    USERS("/api/users"), GET_USERS_BY_ID("/api/users/{id}"), REGISTER_USER("/api/register"), GET_COLORS("/api/unknown");

    public String endpoint;

    ReqresInEndpoints(String endpoint) {
        this.endpoint = endpoint;
    }
}
