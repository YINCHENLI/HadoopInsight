import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CostSort {

    public static class CostSortMapper extends Mapper<LongWritable, Text, DoubleWritable, Text> {

        private static final Log LOG = LogFactory.getLog(CostSort.class);

        // map method
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();//
            String[] drugs = line.split("\t");//use ',' to seperate
            String[] prescriberNumAndCosts = drugs[1].split(",");

            double costSum = Double.parseDouble(prescriberNumAndCosts[1]);

            StringBuilder sb = new StringBuilder();
            sb.append(drugs[0]);//drug name
            sb.append(":");
            sb.append(prescriberNumAndCosts[0]);//num of prescribers

            context.write(new DoubleWritable(costSum), new Text(sb.toString().trim()));

        }
    }

    public static class CostSortReducer extends Reducer<DoubleWritable, Text, Text, Text> {
        // reduce method
        @Override
        public void reduce(DoubleWritable key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            //key = cost
            //value = drug_name:num_of_prescribers
            while(values.iterator().hasNext()) {
                String[] drugNameAndNum= values.iterator().next().toString().split(":");
                String drug_name = drugNameAndNum[0];
                String num = drugNameAndNum[1];

                BigDecimal costSum = new BigDecimal(key.toString());
                context.write(new Text(drug_name+","+num), new Text(costSum.toString()));

            }

        }
    }
}
