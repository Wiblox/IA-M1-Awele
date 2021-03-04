package awele.bot.NegaScoutV2;

public class ttEntry {
    private double depth;
    private double value; //evaluation
    private Flag flag;

    public ttEntry(double depth, double value, Flag flag) {
        this.depth = depth;
        this.value = value;
        this.flag = flag;
    }

    public double getDepth() {
        return depth;
    }

    public double getValue() {
        return value;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }
}