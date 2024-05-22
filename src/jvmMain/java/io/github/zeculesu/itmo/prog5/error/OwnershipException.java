package io.github.zeculesu.itmo.prog5.error;

public class OwnershipException extends RuntimeException{
    public OwnershipException(){
        super("Нельзя модифицировать не свой объект");
    }
}