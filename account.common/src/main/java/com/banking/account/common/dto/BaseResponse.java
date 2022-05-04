package com.banking.account.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*Ahora se creara las APIS para recibir y enviar los comandos del cliente ,para eso necesitamos objetos
* tipo Request y Response*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
    private String message;
}
