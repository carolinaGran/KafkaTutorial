package com.banking.account.cmd.api.dto;

import com.banking.account.common.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//el mensaje que voy a mandar al cliente
public class OpenAccountResponse extends BaseResponse {
    private String id;

    public OpenAccountResponse(String message,String id){
        super(message);
        this.id=id;
    }
}
