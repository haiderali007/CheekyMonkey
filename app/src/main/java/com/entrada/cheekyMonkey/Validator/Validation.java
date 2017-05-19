package com.entrada.cheekyMonkey.Validator;

public interface Validation {

    String getErrorMessage();

    boolean isValid(String text);

    boolean isValid(String text, String text2);

}