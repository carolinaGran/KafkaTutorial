package com.banking.account.cmd;

import com.banking.account.cmd.api.command.*;
import com.banking.cqrs.core.infraestructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommandApplication {
//inyectar las clases commandHandler y commandDispatcher
	@Autowired
	private CommandDispatcher commandDispatcher;
	@Autowired
	private CommandHandler commandHandler;
	public static void main(String[] args) {

		SpringApplication.run(CommandApplication.class, args);
	}
	//metodo que se llamara despues de ejecutar el constructr de esta clase
	@PostConstruct
	public void registerHandlers(){
		commandDispatcher.registerHandler(OpenAccountCommand.class,commandHandler::handle);
		commandDispatcher.registerHandler(DepositFundsCommand.class,commandHandler::handle);
		commandDispatcher.registerHandler(WithdrawFundsCommand.class,commandHandler::handle);
		commandDispatcher.registerHandler(CloseAccountCommand.class,commandHandler::handle);
	}

}
