package com.lucas.appgestaopessoal.util;

import java.text.Normalizer;

public class NormalizeTexto {

    public static String normalizeText(String text) {
        if (text == null) {
            return "";
        }
        // Remove acentos e caracteres diacríticos, depois converte para minúsculas
        // [\\p{InCombiningDiacriticalMarks}] é uma regex para caracteres diacríticos combinantes
        // .replaceAll("\\p{M}", "") remove esses caracteres
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();

        // Adicionar esta linha para remover múltiplos espaços e trim
        return normalized.replaceAll("\\s+", " ").trim();
    }

}
