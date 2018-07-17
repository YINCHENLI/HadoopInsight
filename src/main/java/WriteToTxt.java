import java.io.*;

public class WriteToTxt {

    public WriteToTxt() throws IOException {
        this.writeToFile();
    };

    public static void writeToFile() throws IOException{

        BufferedReader input = null;
        BufferedWriter output = null;

        try {
            String qLine = null;

            input = new BufferedReader(new FileReader("../output/part-r-00000"));
            output = new BufferedWriter(new FileWriter("../output/top_cost_drug.txt"));
            output.write("drug_name,num_prescriber,total_cost"+"\n");

            /* Each pass of the loop processes one line. */
            while ((qLine = input.readLine()) != null) {
                output.write(qLine+"\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            input.close();
            output.close();
        }

    }

}
