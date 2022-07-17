package mp4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

class BadIndentationException extends RuntimeException {
 

BadIndentationException(String error) {
       super(error);
   }
}

public class IndentChecker {
   Stack<Integer> indentStack = new Stack<Integer>();

   private int findFirstNonBlank(String line)
   {
       for (int i = 0; i < line.length(); i++)
       {
           if (line.charAt(i) != ' ')
           {
               return i;
           }
       }
       return -1;
   }

   private void processLine(String line, int lineNumber)
   {
       int index = findFirstNonBlank(line);
       if(index == -1)
           return;
       if (indentStack.isEmpty())
           indentStack.push(index);
       else
       {
           if(index > indentStack.peek()){  
               indentStack.push(index);
               return;
           }
           while(indentStack.peek() > index){
               indentStack.pop();}
           if(indentStack.peek() != index)
               throw new BadIndentationException("error at line: " + lineNumber);
          
       }
   }

   public void checkIndentation(String fileName)
   {
       indentStack.clear();

       Scanner input = null;
       try {
           input = new Scanner (new File(fileName));
           int lineNumber = 1;
           while (input.hasNextLine())
           {
               String line = input.nextLine();
               System.out.println(lineNumber + ": " + line);
               processLine(line, lineNumber);

               lineNumber += 1;
              
           }
           if (indentStack.peek()==0)
                   System.out.printf("*********" + fileName + " is properly indented."+ "%n%n");
              
       }
       catch (BadIndentationException error)
       {
           System.out.println(error);
       }
       catch (FileNotFoundException e)
       {
           System.out.println("Can't open file: "+fileName);
       }
       finally
       {
           if (input != null)
               input.close();
       }
   }

   public static void main(String[] args) {

       IndentChecker indentChecker = new IndentChecker();

       for (int i=0; i < args.length; i++)
       {
           System.out.println("Processing file: " + args[i]);
           indentChecker.checkIndentation(args[i]);
       }
   }
}