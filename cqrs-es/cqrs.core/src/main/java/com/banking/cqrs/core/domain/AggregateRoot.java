package com.banking.cqrs.core.domain;

import com.banking.cqrs.core.commands.BaseCommand;
import com.banking.cqrs.core.events.BaseEvent;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//mantenimiento de lista de eventos,no se indica de forma especifica el tipo de evento,eso lo hace
//los hijos de aggrate root
public abstract class AggregateRoot {
    protected String id;
    private int version;
    //los cambios del agregate se guardaran en una lista de eventos
    private final List<BaseEvent> changes =new ArrayList<>();
    //La clase Logger de Java permite crear mensajes para el seguimiento o registro de la ejecución de una aplicación.
    //en este caso de la clase en la que nos encontramos
    private final Logger logger=Logger.getLogger(AggregateRoot.class.getName());

    //funcion para obtener id
    public String getId(){
        return this.id;
    }
    //getter y sette version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    //funcion para registrar eventos que aun no han sido guardados aqui ,es decir no estan confirmados
    public List<BaseEvent> getUncommitedChanges(){
        return this.changes;
    }
    //confirmacion de los evntos guardados
    public  void markChangesAsCommited(){
        this.changes.clear();
    }
    //funcion aclaratoria de cambios de los eventos
    protected void applyChange(BaseEvent event,Boolean isNewEvent){
        try {
            //obtencion de metodo y declaracion de este
            var method=getClass().getDeclaredMethod("apply",event.getClass());
            //hacer accesible el metodo
            method.setAccessible(true);
            //ejecutar evento
            method.invoke(this,event);
        }
        //no encuentra el metodo
        catch (NoSuchMethodException e){
            logger.log(Level.WARNING, MessageFormat.format("El metodo apply no fue encontrado para{0}",
                    event.getClass().getName()));
        }catch (Exception e){
            logger.log(Level.SEVERE,"Errores aplicando el evento al aggregate",e);
        }finally{//finally siempre pasa aunque no salte excepcion
           if (isNewEvent){
               changes.add(event);
           }
        }
    }
    //ejecutar y crear un nuevo evento
    public void raiseEvent(BaseEvent event){
        applyChange(event,true);
    }
    //reprocesar eventos dentro del list
    public void replayEvents(Iterable<BaseEvent> events){
        events.forEach(event ->applyChange(event,false));
    }

}
