package mc.project.customer.amqp;

public interface AmqpNotificationProducer {
    void publish(Object payload, String exchange, String routingKey);
}
