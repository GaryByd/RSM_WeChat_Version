package com.rc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private String msg;
    private Object data;
    private Long total;

    //简单的成功 操作成功返回
    public static Result ok(){
        return new Result(200, "操作成功", null, null);
    }
    public static Result ok(Object data){
        return new Result(200, null, data, null);
    }
    public static Result ok(String msg, Object data){
        return new Result(200, msg, data, null);
    }

    //带数据的简单成功
    //带数据带信息的成功
    public static Result ok(Object data, String msg){
        return new Result(200, msg, data, null);
    }

    //登入错误
    public static Result fail(String errorMsg){
        return new Result(500, errorMsg, null, null);
    }

    // 其他错误
    public static Result fail(Integer code,String errorMsg){
        return new Result(code, errorMsg, null, null);
    }


}