package com.lucas.appgestaopessoal.util;

public class IdGenerator {

    private static int nextId= 1;

    public static int generateNewId(){
        return nextId++;
    }

    public static void resetId() {
        nextId = 1;
    }
}
