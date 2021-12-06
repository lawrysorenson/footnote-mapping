import java.util.*;
import java.io.*;

public class Comb
{
    public static void main(String[] args) throws IOException
    {
        if (args.length != 2) System.exit(-1);

        String l1 = args[0];
        String l2 = args[1];

        //combFile(l1, l2);
        //pred(l1, l2, false);
        trainTestSplit(l1, l2);
    }

    public static void trainTestSplit(String l1, String l2) throws IOException
    {
        Scanner in = new Scanner(new File("input/" + l1 + "-" + l2 + ".src-tgt"), "UTF8");

        int totalLines = 7139;

        String[] lines = new String[7139];
        for (int i=0;i<totalLines;++i) lines[i] = in.nextLine();
        in.close();

        int test = totalLines / 5;

        PrintStream out = new PrintStream("models/training/" + l1 + "-" + l2 + ".test", "UTF8");
        for (int i=0;i<test;++i)
        {
            int si = (int)(Math.random() * (totalLines - i)) + i;
            out.println(lines[si]);
            lines[si] = lines[i];
        }
        out.close();

        out = new PrintStream("models/training/" + l1 + "-" + l2 + ".train", "UTF8");
        for (int i=test;i<totalLines;++i) out.println(lines[i]);
        out.close();
    }

    public static void pred(String l1, String l2, boolean fine) throws IOException
    {
        Scanner in1 = new Scanner(new File("../scrapping/files/" + l1 + ".txt"), "UTF8");
        Scanner in2 = new Scanner(new File("../scrapping/files/" + l2 + ".txt"), "UTF8");
        Scanner align = new Scanner(new File("raw/" + l1 + "-" + l2 + "-align.out"));

        PrintStream out = new PrintStream("output/" + l1 + "-" + l2 + (fine ? "-fine" : "") + ".txt", "UTF8");

        while (in1.hasNextLine() && in2.hasNextLine() && align.hasNextLine())
        {
            String line1 = in1.nextLine();
            String line2 = in2.nextLine();
            if (line1.length()==0 || line1.equals("----------") || line1.startsWith("/study/scriptures"))
            {
                out.println(line1);
                continue;
            }

            ArrayList<Integer> tag1 = getTaggedWords(line1);

            int[] list = new int[300];
            for (int word : tag1) list[word] = -1;

            Scanner ap = new Scanner(align.nextLine());
            ap.useDelimiter("[\\s-]");

            while (ap.hasNextInt())
            {
                int a = ap.nextInt();
                int b = ap.nextInt();
                if (list[a]==-1 || b<list[a]) list[a] = b;
            }

            ArrayList<Integer> tag2 = new ArrayList<>();
            for (int word : tag1) if (list[word] >= 0) tag2.add(list[word]);
            
            {
                HashSet<Integer> temp = new HashSet<>(tag2);
                tag2.clear();
                tag2.addAll(temp);
                Collections.sort(tag2);
            }

            out.println(tagWords(line2, tag2));
        }

        in1.close();
        in2.close();
        align.close();
    }

    public static String tagWords(String line, ArrayList<Integer> toTag)
    {
        StringBuilder answer = new StringBuilder();

        int state = 0; //0 - out of word, 1 - in word, 2 - in puct
        int wordCount = 0;
        int tagi = 0;

        for (char c : line.toCharArray())
        {
            if (c == ' ' || c == '~')
            {
                state = 0;
            }
            else if (Character.isAlphabetic(c) || c==0x2019)
            {
                if (state!=1)
                {
                    if (tagi < toTag.size() && wordCount==toTag.get(tagi))
                    {
                        answer.append('~');
                        ++tagi;
                    }
                    ++wordCount;
                }
                state = 1;
            }
            else
            {
                if (state!=2)
                {
                    if (tagi < toTag.size() && wordCount==toTag.get(tagi))
                    {
                        answer.append('~');
                        ++tagi;
                    }
                    ++wordCount;
                }
                state = 2;
            }

            if (c != '~') answer.append(c);
        }

        return answer.toString().strip();
    }

    public static ArrayList<Integer> getTaggedWords(String line)
    {
        ArrayList<Integer> answer = new ArrayList<>();

        int state = 0; //0 - out of word, 1 - in word, 2 - in puct
        int wordCount = 0;

        for (char c : line.toCharArray())
        {
            if (c == ' ')
            {
                state = 0;
            }
            else if (c == '~')
            {
                answer.add(wordCount);
            }
            else if (Character.isAlphabetic(c) || c==0x2019)
            {
                if (state!=1)
                {
                    ++wordCount;
                }
                state = 1;
            }
            else
            {
                if (state!=2)
                {
                    ++wordCount;
                }
                state = 2;
            }
        }

        return answer;
    }

    public static void combFile(String l1, String l2) throws IOException
    {
        Scanner in1 = new Scanner(new File("../scrapping/files/" + l1 + ".txt"), "UTF8");
        Scanner in2 = new Scanner(new File("../scrapping/files/" + l2 + ".txt"), "UTF8");

        PrintStream out = new PrintStream("input/" + l1 + "-" + l2 + ".src-tgt", "UTF8");

        while (in1.hasNextLine() && in2.hasNextLine())
        {
            String line1 = in1.nextLine().strip();
            String line2 = in2.nextLine().strip();

            if (line1.length()==0 || line1.equals("----------") || line1.startsWith("/study/scriptures")) continue;
            //if (line2.length()==0 || line2.equals("----------") || line2.startsWith("/study/scriptures")) continue;

            out.println(tokenize(line1) + " ||| " + tokenize(line2));
        }

        in1.close();
        in2.close();
        out.close();
    }

    public static String tokenize(String s)
    {
        StringBuilder answer = new StringBuilder();

        int state = 0; //0 - out of word, 1 - in word, 2 - in puct

        for (char c : s.toCharArray())
        {
            if (c == ' ' || c == '~')
            {
                state = 0;
            }
            else if (Character.isAlphabetic(c) || c==0x2019)
            {
                if (state!=1)
                {
                    answer.append(' ');
                }
                answer.append(c);
                state = 1;
            }
            else
            {
                if (state!=2)
                {
                    answer.append(' ');
                }
                answer.append(c);
                state = 2;
            }
        }

        return answer.toString().strip();
    }
}