import java.io.*;
import java.util.*;

//MOCK TEST PARSER FROM GOOGLE DOC FORMAT - Andrew Ji

class test {
  
   public static void write() throws FileNotFoundException, IOException {
      String A = "a. ";
      String B = "b. ";
      String C = "c. ";
      String D = "d. ";
      String G = "g. ";
      String question;
      String ansA;
      String ansB;
      String ansC;
      String ansD;
      String answer;
      String explanation;
      String category;
      String nextLine;
      String checkA;
      String checkG;
      BufferedReader br = new BufferedReader(
          new FileReader("//Users//andrewji6//Documents//AJ//Innovator//googleDocFileHistory.txt"));
      PrintWriter bw
          = new PrintWriter(new BufferedWriter(new FileWriter("//Users//andrewji6//Documents//AJ//Innovator/outputFileHistory.txt")));   
      
      for(int runs = 0; runs < 39; runs++) {
         question = br.readLine();
         nextLine = br.readLine();
         try {
            checkA = nextLine.substring(0,3);
            while (!(checkA.equals(A))) {
               question += " " + nextLine;
               nextLine = br.readLine();
               checkA = nextLine.substring(0,3);
            }
            question = question.substring(13, question.length());
            for (int i = 0; i < question.length(); i++) {
               question = question.replace("\u201C", "\\\"");
               question = question.replace("\u201D", "\\\"");
            }
            ansA = nextLine;
            ansA = substringAnswer(ansA, A);
            ansB = br.readLine();
            ansB = substringAnswer(ansB, B);
            ansC = br.readLine();
            ansC = substringAnswer(ansC, C);
            ansD = br.readLine();
            ansD = substringAnswer(ansD, D);
            answer = br.readLine();
            answer = answer.substring(11, answer.length());
            explanation = br.readLine();
            nextLine = br.readLine();
            checkG = nextLine.substring(0,3);
            while (!(checkG.equals(G))) {
               explanation += " " + nextLine;
               nextLine = br.readLine();
               checkG = nextLine.substring(0,3);
            }
            explanation = explanation.substring(16, explanation.length());
            for (int i = 0; i < explanation.length(); i++) {
               explanation = explanation.replace("“", "\\\"");
               explanation = explanation.replace("”", "\\\"");
            }
            category = nextLine.substring(13,nextLine.length());
            bw.write("{\n\t");
            bw.write("\"ANSWER\": \"" + answer + "\",\n");
            bw.write("\t\"Category\": \"" + category + "\",\n" );
            bw.write("\t\"Choices\": {\n"  );
            bw.write("\t\t\"A\": " + "\""+ansA+"\"" + ",\n");
            bw.write("\t\t\"B\": " + "\""+ansB+"\"" + ",\n");
            bw.write("\t\t\"C\": " + "\""+ansC+"\"" + ",\n");
            bw.write("\t\t\"D\": " + "\""+ansD+"\"" + "\n");      
            bw.write("\t},\n");
            bw.write("\t\"MediaID\": -1,\n");
            bw.write("\t\"ExMediaID\": -1,\n");
            bw.write("\t\"Question\": " + "\"" + question + "\",\n");
            bw.write("\t\"Explanation\": " + "\"" + explanation + "\",\n");
            bw.write("\t\"Type\": " + "\"Multiple-Choice-4\",\n");
            bw.write("\t\"QuestionPicNumber\": -1\n\n");
            bw.write("},\n");
            bw.flush();
         }
         catch (Exception ex){
         
         }
      }
      br.close();
      bw.close();
   } 
   public static void main(String[] args) throws FileNotFoundException, IOException{
      write();
   }
  
   public static String substringAnswer (String answer, String letter) {
      answer = answer.substring(answer.indexOf(letter) + 3, answer.length());
      return answer;
   }
}
