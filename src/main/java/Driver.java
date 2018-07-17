import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;



public class Driver {

	private static final String OUTPUT_PATH = "/intermediate";


	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {

		Path drugNameOutput = new Path(OUTPUT_PATH);

		Configuration conf1 = new Configuration();
		conf1.set("textinputformat.record.delimiter", "\n");

		
		Job job1 = Job.getInstance(conf1);
		job1.setJobName("pharmarcyCost");
		job1.setJarByClass(Driver.class);
		
		job1.setMapperClass(DrugNameBuilder.DrugNameMapper.class);
		job1.setReducerClass(DrugNameBuilder.DrugNameReducer.class);
		
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(Text.class);
		
		job1.setInputFormatClass(TextInputFormat.class);
		job1.setOutputFormatClass(TextOutputFormat.class);
		
		TextInputFormat.setInputPaths(job1, new Path(args[0]));
		TextOutputFormat.setOutputPath(job1, drugNameOutput);

		job1.waitForCompletion(true);

		//2nd job
		//2nd job
		Configuration conf2 = new Configuration();
		conf2.set("mapreduce.output.textoutputformat.separator",",");

		Job job2 = Job.getInstance(conf2);
		job2.setJobName("CostSort");
		job2.setJarByClass(Driver.class);

		job2.setMapOutputKeyClass(DoubleWritable.class);
		job2.setMapOutputValueClass(Text.class);

		job2.setMapperClass(CostSort.CostSortMapper.class);
		job2.setSortComparatorClass(CostComparator.class);

		job2.setReducerClass(CostSort.CostSortReducer.class);

		job2.setInputFormatClass(TextInputFormat.class);
		job2.setOutputFormatClass(TextOutputFormat.class);

		TextInputFormat.setInputPaths(job2, drugNameOutput);
		TextOutputFormat.setOutputPath(job2, new Path(args[1]));

		job2.waitForCompletion(true);
		//WriteToTxt writeToTxt = new WriteToTxt();

	}

}
