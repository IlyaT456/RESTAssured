package threadQA.endpoints;

public enum ThreadQAEndpoints {
    USERS("/users"), GET_USERS_BY_ID("/users/{id}"), AUTH_LOGIN("/auth/login");

    public String endpoint;

    ThreadQAEndpoints(String endpoints) {
        this.endpoint = endpoints;
    }
}
