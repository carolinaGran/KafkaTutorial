package com.banking.account.cmd.api.controllers;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.cmd.api.dto.OpenAccountResponse;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.infraestructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
//poder agregar URL
@RequestMapping(path = "/api/v1/openBankAccount")
public class OpenAccountController {
    //Almacenar logs
    private final Logger logger= Logger.getLogger(OpenAccountController.class.getName());
    //objeto command dispatcher para que nos perita enviar el mensaje al siguiente nivel
    @Autowired
    private CommandDispatcher commonDispatcher;
    //va a devolver el metodo mediante Post
    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand command){
        var id= UUID.randomUUID().toString();
        //settear comando con esa id
        command.setId(id);
        try {
            //llamar objeto que hace el envio commonDispather
            commonDispatcher.send(command);
            //(mensaje a mandar ,id generada),enviamos estatus como reada , pues hemos creado un nuevo record
            return new ResponseEntity<>(new OpenAccountResponse("La cuenta ha sido creada correctamente",id), HttpStatus.CREATED);
        }catch (IllegalStateException e){
logger.log(Level.WARNING, MessageFormat.format("No se pudo generar la cuenta de banco - {0}",e.toString()));
return new ResponseEntity<>(new BaseResponse(e.toString()),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            //{id que queria generar pero no se pudo genrar }
var safeErrorMessage=MessageFormat.format("Errores mientras procesaba el request - {0}",id);
        logger.log(Level.SEVERE,safeErrorMessage,e);
        return new ResponseEntity<>(new OpenAccountResponse(safeErrorMessage,id),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
