package com.banking.account.query.infrastructure.consumers;

import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FundsDepositedEvent;
import com.banking.account.common.events.FundsWithdrawnEvent;
import com.banking.account.query.infrastructure.handlers.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class AccountEventConsumer implements EventConsumer{
    @Autowired
    private EventHandler eventHandler;
    //activar un listener para escuchar todos los topics que vengan
    @KafkaListener(topics = "AccountOpenedEvent",groupId = "${spring.kafka.consumer.group-id}")
    //en cuanto escuche el mensaje se activara la clase
    @Override
    public void consume(AccountOpenedEvent event, Acknowledgment ack) {
        //para procesar el mensaje recibido por kafka
        eventHandler.on(event);
        //confirmar que el mensaje ya ha sido leido
        ack.acknowledge();
    }
    @KafkaListener(topics = "FundsDepositedEvent",groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsDepositedEvent event, Acknowledgment ack) {
//para procesar el mensaje recibido por kafka
        eventHandler.on(event);
        //confirmar que el mensaje ya ha sido leido
        ack.acknowledge();
    }
    @KafkaListener(topics = "FundsWithdrawnEvent",groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsWithdrawnEvent event, Acknowledgment ack) {
//para procesar el mensaje recibido por kafka
        eventHandler.on(event);
        //confirmar que el mensaje ya ha sido leido
        ack.acknowledge();
    }
    @KafkaListener(topics = "AccountClosedEvent",groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountClosedEvent event, Acknowledgment ack) {
//para procesar el mensaje recibido por kafka
        eventHandler.on(event);
        //confirmar que el mensaje ya ha sido leido
        ack.acknowledge();
    }
}
