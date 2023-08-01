package com.innovator.solve;


public class SOLScoreTable {
    public int[] RawScore;
    public int[] WeightedScore = {0, 186, 222, 243, 259, 271, 281, 290, 298, 305, 312, 318, 324, 329, 334, 339, 344, 348, 353, 357, 361, 365, 369, 373, 377, 381, 384, 388, 392, 395, 399, 403, 407, 410, 414, 418, 422, 425, 429, 433, 437, 441, 446, 450, 455, 459, 464, 469, 475, 480, 487, 493, 500, 508, 517, 528, 540, 555, 577, 600, 600};

    public SOLScoreTable() {
        RawScore = new int[61];
        int i;
        for (i = 0; i < 61; i++) {
            RawScore[i] = i;
        }
    }
}