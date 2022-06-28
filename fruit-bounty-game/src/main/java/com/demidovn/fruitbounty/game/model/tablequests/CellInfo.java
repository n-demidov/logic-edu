package com.demidovn.fruitbounty.game.model.tablequests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CellInfo {
    public static final int SUBJECT_ID = -1;

    private final int row;
    private final int col;
    private final int rowRef;
    private final int colRef;
    private final boolean positive;

    public CellInfo(int row, int col, int rowRef, int colRef) {
        this(row, col, rowRef, colRef, false);
    }

    public boolean isXSubject() {
        return rowRef == SUBJECT_ID;
    }
}
