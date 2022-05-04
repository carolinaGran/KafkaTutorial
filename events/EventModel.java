package com.banking.cqrs.core.events;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "eventStore")//le indica que va a ser un documento que va a ser almacenado en una bbdd mongodb
//("nombre del documento en el que se guardara los datos")
public class EventModel {
    @Id//indica que es la primary key
    private String id;
    private Date timeStamp;
    private String aggregateIdentifier;//se utiliza cuando quieres volver a ejecutar el eventStore
    private String aggregateType;//el tipo de aggregate
    private int version ;
    private String eventType;//tipo de evento
    //captura el evento relacionado a este bigModel,almacena la descripcion de la operacion que quieras realizar
    private BaseEvent eventData;



}
