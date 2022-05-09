package com.banking.cqrs.core.handlers;

import com.banking.cqrs.core.domain.AggregateRoot;

//interfaz generica previa al guardado de eventos ,para guardar las ultimas actualizaciones
//atravesando el del CommandHandler y haciendo de intermediario del Aggregate
public interface EventSourcingHandler <T>{
    //2 parametros
    //alamacenar el estado
    void save (AggregateRoot aggregate);
    //obtener el estado,devuelve un generico por eso la T
    T getById(String id);

}
