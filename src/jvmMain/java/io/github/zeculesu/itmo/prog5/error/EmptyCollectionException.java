package io.github.zeculesu.itmo.prog5.error;

public class EmptyCollectionException extends RuntimeException{
    public EmptyCollectionException(){
        super("Коллекция пуста");
    }
}