package com.banking.account.cmd.infrastructure;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.account.cmd.domain.EventStoreRepository;
import com.banking.cqrs.core.events.BaseEvent;
import com.banking.cqrs.core.events.EventModel;
import com.banking.cqrs.core.exceptions.AggregateNotFoundException;
import com.banking.cqrs.core.exceptions.ConcurrencyException;
import com.banking.cqrs.core.infraestructure.EventStore;
import com.banking.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.events.Event;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountEventStore implements EventStore {
    //para poder hacer las operaciones en la bbdd Mongodb
    @Autowired
    private EventStoreRepository eventStoreRepository;
    @Autowired
    private EventProducer eventProducer;
    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
    //buscar eventos ya existentes
        var eventStream=eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion != -1 && eventStream.get(eventStream.size() -1).getVersion() !=expectedVersion){
            throw new ConcurrencyException();
        }
        var version =expectedVersion;
        for (var event: events){
            version++;
            event.setVersion(version);
            //vamos aserializar el evento que quiero insertar en la bbdd
            var eventModel= EventModel.builder()
                    .timeStamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)//almacenar el dato
                    .build();//para construir el objeto
        //y ahora dicho objeto serializado lo vamos a instanciar,es decir lo estamos guardando en la bbdd
            var persistedEvent=eventStoreRepository.save(eventModel);
            if (!persistedEvent.getId().isEmpty()){
                //produce un evento para kafka
                //(nombre topic (nombre de clase),data)
                eventProducer.produce(event.getClass().getSimpleName(),event);
            }
        }


    }

    @Override
    public List<BaseEvent> getEvent(String aggregateId) {
//permite realizar las modificaciones en la base de datos mongodb
        var eventStream=eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (eventStream==null|| eventStream.isEmpty()){
            throw new AggregateNotFoundException("La cuenta del banco es incorrecta");
        }
       // pasamos la lista de objetos casteada, es decir en un formato que permite leerse lo que nos interesa
        return eventStream.stream().map(x-> x.getEventData()).collect(Collectors.toList());
    }
}
