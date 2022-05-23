package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.entity.FlatDetail;
import org.example.entity.Review;

import java.util.Date;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class FlatDetailDTO {

    private UUID id;
    private Boolean kitchen;
    private Boolean bathroom;
    private Boolean towels;
    private Boolean wifi;

    public static FlatDetailDTO fromEntity(FlatDetail flatDetail) {
        return new FlatDetailDTO(flatDetail.getId(),
                flatDetail.getKitchen(),
                flatDetail.getBathroom(),
                flatDetail.getTowels(),
                flatDetail.getWifi());
    }
}
