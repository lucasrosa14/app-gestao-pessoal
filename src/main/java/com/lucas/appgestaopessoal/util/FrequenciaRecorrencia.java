// src/main/java/com/lucas/appgestaopessoal/util/FrequenciaRecorrencia.java
package com.lucas.appgestaopessoal.util;

import java.time.LocalDate;

public enum FrequenciaRecorrencia {
    DIARIA,
    SEMANAL,
    QUINZENAL,
    MENSAL,
    ANUAL;


    public LocalDate proximaData(LocalDate dataBase) {
        if (dataBase == null) {
            throw new IllegalArgumentException("Data base não pode ser nula.");
        }
        return switch (this) {
            case DIARIA -> dataBase.plusDays(1);
            case SEMANAL -> dataBase.plusWeeks(1);
            case QUINZENAL -> dataBase.plusDays(15);
            case MENSAL -> dataBase.plusMonths(1);
            case ANUAL -> dataBase.plusYears(1);
        };
    }
}

