package com.proint.walletly.dto.dashboard;

import java.math.BigDecimal;

public record ResumoMensalDTO(
    String mes,
    BigDecimal ganhos,
    BigDecimal gastos
) {
}
