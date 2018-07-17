import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class CostComparator extends WritableComparator{

    public CostComparator() {
        super(DoubleWritable.class, true);
    }

    @Override
    public int compare(WritableComparable w1, WritableComparable w2) {
        DoubleWritable d1 = (DoubleWritable)w1;
        DoubleWritable d2 = (DoubleWritable)w2;
        //decending order
        return d2.compareTo(d1);

    }
}
