package at.fhooe.sail.vis.general;

public interface IMyProtocolAPI {

    public DataContainer myMethod(String _param) throws Exception;

    //verbindung zur server überprüfung
    public boolean connect() throws Exception;
    public boolean isAlive() throws Exception;
}
