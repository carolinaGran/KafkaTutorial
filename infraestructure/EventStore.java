package com.banking.cqrs.core.infraestructure;

import com.banking.cqrs.core.events.BaseEvent;

import java.util.List;

//interface para crear in mostrar eventos
public interface EventStore {
    //guardar evento(id ,conjunto eventos, version)
    void saveEvents(String aggregateId, Iterable<BaseEvent> events,int expectedVersion);
    //devuelve la lista de eventos
    List<BaseEvent> getEvent(String aggregateId);


}
