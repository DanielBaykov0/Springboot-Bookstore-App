package baykov.daniel.springbootbookstoreapp.config;

public class RequestData {

    private static final ThreadLocal<String> correlationIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> userIdHolder = new ThreadLocal<>();

    public static String getCorrelationId() {
        return correlationIdHolder.get();
    }

    public static void setCorrelationId(String correlationId) {
        correlationIdHolder.set(correlationId);
    }

    public static String getUserId() {
        return userIdHolder.get();
    }

    public static void setUserId(String userId) {
        userIdHolder.set(userId);
    }

    public static void clear() {
        correlationIdHolder.remove();
        userIdHolder.remove();
    }
}
