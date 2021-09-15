package com.codesoom.assignment.errors;

import com.codesoom.assignment.dto.UserLoginData;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException(UserLoginData loginData){
        super("잘못된 비밀번호입니다. : " +loginData.getEmail());
    }

}
