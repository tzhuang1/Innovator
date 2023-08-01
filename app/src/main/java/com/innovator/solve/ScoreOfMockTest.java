package com.innovator.solve;

public class ScoreOfMockTest extends SOLScoreTable {
    static int numSections = 3;
    static int total = 25;
    static int section1 = 15;
    static int section1total = 20;
    static int section2 = 5;
    static int section2total = 10;
    static int section3 = 5;
    static int section3total = 10;
    static int totalamount = 40;

    public static void refitNumSections() {
        if (numSections < 3) {
            section3 = 0;
            section3total = 0;
        }
        if (numSections < 2) {
            section2 = 0;
            section2total = 0;
        }
        total = section1 + section2 + section3;
        totalamount = section1total+section2total+section3total;
    }
    public String SectionScores1(){
        String score = Integer.toString(section1);
        String total = Integer.toString(section1total);
        return (score + "/" + total);
    }

    public String SectionScores2(){
        String score = Integer.toString(section2);
        String total = Integer.toString(section2total);
        return (score + "/" + total);
    }

    public String SectionScores3(){
        String score = Integer.toString(section3);
        String total = Integer.toString(section3total);
        return (score + "/" + total);
    }

    public String Interesting() {
        int SOLScore = 0;
        int RAW = 0;
        SOLScoreTable x = new SOLScoreTable();
        int[] stuff = x.RawScore;
        int[] SOLScores = x.WeightedScore;
        if (total == totalamount || total == totalamount - 1) {
            RAW = 600;
        }
        else {
            double b = (double)(total)/totalamount;
            double min_diff = 100000.0;

            for (int i = 0; i < SOLScores.length; i++) {
                double a = SOLScores[i] / 600.0;
                double diff = Math.abs(a - b);
                if (diff < min_diff) {
                    min_diff = diff;
                    RAW = SOLScores[i];
                }
            }
        }
        String Weighted = Integer.toString(RAW);
        String Original = total + "/" + totalamount ;

        return ("Total: " + Original + " " +  "SOL Score" + " " + Weighted);
    }
}