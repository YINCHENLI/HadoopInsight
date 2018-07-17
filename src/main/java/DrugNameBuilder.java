
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class DrugNameBuilder {
    private static final Log LOG = LogFactory.getLog(DrugNameBuilder.class);

    private static DecimalFormat df = new DecimalFormat("#.##");

	public static class DrugNameMapper extends Mapper<LongWritable, Text, Text, Text> {


		// map method
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();//
			String[] words = line.split(",");//use ',' to seperate
			//edge case, there are less values than 5
            if (words[0].equals("id")) {
				LOG.info("skipped the header");
                return;
            }
			if (words.length < 5) {
				LOG.info("this is a exception that only has less than 5 words");
				return;
			}

			StringBuilder sb = new StringBuilder();
			sb.append(words[1].trim().toLowerCase());
			sb.append(words[2].trim().toLowerCase());
			sb.append(":");//use ':' to separate the name and the cost
			sb.append(words[4].trim());

			context.write(new Text(words[3].trim().toUpperCase()), new Text(sb.toString().trim()));

		}
	}

	public static class DrugNameReducer extends Reducer<Text, Text, Text, Text> {
		// reduce method
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {//merge data for one user
			//input key = drug_name
			//input value = <prescriberFNLN:cost,prescriberFNLN:cost...>
			HashSet<String> nameSet = new HashSet<String>();
			BigDecimal costSum = new BigDecimal(0);
			while(values.iterator().hasNext()) {
				String[] currPair = values.iterator().next().toString().split(":");
				if (currPair.length < 2){
					return;
				}
				nameSet.add(currPair[0]);
				try {
                    costSum = new BigDecimal(currPair[1]).add(costSum);
                }catch (NumberFormatException e){
				    LOG.info("this is not a double format");
				    e.printStackTrace();
                }
			}
			String sumStr = String.valueOf(costSum);
			context.write(key, new Text(nameSet.size() + "," + sumStr));
		}
	}

}