package com.proint.walletly.dto;

import java.time.OffsetDateTime;

public record ExtratoHistoryDTO(
    Long id,
    String filename,
    OffsetDateTime uploadDate,
    String status,
    String logMessage
) {
}
