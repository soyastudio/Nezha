package soya.framework.settler;

public interface DataSerializer<T> {

    <DT> DT getAsType(T data, Class<DT> type) throws DataSerializerException;

    String getAsString(T data) throws DataSerializerException;

    String getAsJson(T data) throws DataSerializerException;

    String getAsXml(T data) throws DataSerializerException;
}
