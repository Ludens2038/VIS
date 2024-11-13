package at.fhooe.sail.vis.general;

public class DataContainer {

    private String mData= null;

    public DataContainer(String _d) { mData = _d; }

    @Override
    public String toString(){
        return "DataContainer{" +
                "mData=" + mData + '\'' +
                '}';
    }

}
