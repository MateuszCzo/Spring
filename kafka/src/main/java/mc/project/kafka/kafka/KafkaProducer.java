package mc.project.kafka.kafka;

public interface KafkaProducer {
    void sendMessage(String message);
}
