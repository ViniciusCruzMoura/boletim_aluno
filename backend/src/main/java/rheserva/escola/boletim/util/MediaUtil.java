package rheserva.escola.boletim.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class MediaUtil {
    /**
     * Calcula média ponderada.
     * @param notasPorAvaliacao mapa avaliacaoId -> nota (nota pode ser null)
     * @param pesosPorAvaliacao mapa avaliacaoId -> peso (1..5)
     * @return média arredondada com 1 casa decimal (ex: 8.0), or null se nenhuma nota válida
     * @throws IllegalArgumentException se nota fora do intervalo [0,10] ou peso inválido
     */
    public static Double calcularMediaPonderada(Map<Long, Double> notasPorAvaliacao,
                                                Map<Long, Integer> pesosPorAvaliacao) {
        if (notasPorAvaliacao == null || pesosPorAvaliacao == null) return null;

        double somaPonderada = 0.0;
        int somaPesos = 0;

        for (Map.Entry<Long, Double> e : notasPorAvaliacao.entrySet()) {
            Long avId = e.getKey();
            Double nota = e.getValue();
            Integer peso = pesosPorAvaliacao.get(avId);
            if (nota == null) continue;
            if (nota < 0.0 || nota > 10.0) throw new IllegalArgumentException("Nota fora do intervalo 0-10: " + nota);
            if (peso == null || peso < 1 || peso > 5) throw new IllegalArgumentException("Peso inválido para avaliação " + avId);
            somaPonderada += nota * peso;
            somaPesos += peso;
        }

        if (somaPesos == 0) return null;
        double media = somaPonderada / somaPesos;
        BigDecimal bd = BigDecimal.valueOf(media).setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

