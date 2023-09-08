package com.example.chatcheck.controller.dto;



import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {

    private int code;
    private String msg;
    private Object data;

    public static R ok() {
      return   new R(200,"success",null);
    }

    public static R ok(Object data) {
        return   new R(200,"success",data);
    }


    public static R er(Object data) {
        return   new R(400,"error",data);
    }

}


