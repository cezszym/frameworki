package org.example.request.wrappers;

import lombok.Data;
import org.example.model.FlatDTO;
import org.example.model.FlatDetailDTO;

@Data
public class FlatWrapper {
    FlatDTO flatDTO;
    FlatDetailDTO flatDetailDTO;
}
