package io.github.zeculesu.itmo.prog5.error;

public class ElementNotFound extends RuntimeException{
    public ElementNotFound(String message){
        super(message);
    }
}