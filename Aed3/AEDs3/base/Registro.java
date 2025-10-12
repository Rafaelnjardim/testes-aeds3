package base;

public interface Registro {
    public byte[] toByteArray() throws Exception;
    public void fromByteArray(byte[] ba) throws Exception;
    public int getId();
    public void setId(int id);
}
