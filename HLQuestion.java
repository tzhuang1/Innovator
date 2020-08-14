package com.example.solve;

public class HLQuestion extends MCQuestion implements Question  {
    private int id;
    private String passage;
    private String question;
    private String answer;
    private String optA=null, optB=null, optC=null, optD=null, optE=null, optF=null;
    private int optAPicNumber = -1, optBPicNumber = -1, optCPicNumber = -1, optDPicNumber = -1, optEPicNumber =-1, optFPicNumber=-1;
    private String explanation;
    private String category;
    private int explanationPicNumber;
    private int questionPicNumber=-1;
    public HLQuestion(int i, String p, String q, String a, String e, String c, int exp)
    {
        id=i;
        passage=p;
        question=q;
        answer=a;
        explanation=e;
        category=c;
        explanationPicNumber=exp;
        String arr[]=passage.split("\\.");
        optA=arr[0];
        optB=arr[1];
        optC=arr[2];
        optD=arr[3];
        for(int k=0; k<arr.length; k++) {
            switch(k) {
                case 0:
                    optA=arr[k];
                    break;
                case 1:
                    optB=arr[k];
                    break;
                case 2:
                    optC=arr[k];
                    break;
                case 3:
                    optD=arr[k];
                    break;
                case 4:
                    optE=arr[k];
                    break;
                case 5:
                    optF=arr[k];
                    break;
            }
        }

    }
    public HLQuestion() {
        id = 0;
        question = "";
        optA = "";
        optB = "";
        optC = "";
        optD = "";
        optE = "";
        optF = "";
        answer = "";
        explanation = "";
        category = "";
        //questionPicNumber = -1;
        explanationPicNumber = -1;
        passage="";
    }
    public int getPicNumber() {
        return questionPicNumber;
    }
    public int getExPicNumber() {return explanationPicNumber;}

    public int getOptAPicNumber() {
        return optAPicNumber;
    }

    public int getOptBPicNumber() {
        return optBPicNumber;
    }

    public int getOptCPicNumber() {
        return optCPicNumber;
    }

    public int getOptDPicNumber() {
        return optDPicNumber;
    }

    public int getOptEPicNumber() {
        return optEPicNumber;
    }

    public int getOptFPicNumber() {
        return optFPicNumber;
    }

    public String getOptA() {
        return optA;
    }

    public String getOptB() {
        return optB;
    }

    public String getOptC() {
        return optC;
    }

    public String getOptD() {
        return optD;
    }

    public String getOptE() {
        return optE;
    }

    public String getOptF() {
        return optF;
    }
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getQuestion() {
        return question;
    }

    @Override
    public String getAnswer() {
        return answer;
    }

    @Override
    public String getExplanation() {
        return explanation;
    }

    @Override
    public String getCategory() {
        return category;
    }
    public String getPassage(){
        return passage;
    }
    public int getExplanationPicNumber(){
        return explanationPicNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setPassage(String passage) {
        this.passage = passage;
    }

    public void setExplanationPicNumber(int explanationPicNumber) {
        this.explanationPicNumber = explanationPicNumber;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
