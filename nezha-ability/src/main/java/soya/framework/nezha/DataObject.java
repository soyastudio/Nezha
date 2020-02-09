package soya.framework.nezha;

public final class DataObject {

    private final Object data;
    private DataSerializer serializer;

    private DataObject(Object data) {
        this.data = data;
        this.serializer = new DefaultDataSerializer();
    }

    public Object getData() {
        return data;
    }

    public <T> T getAsType(Class<T> type) {
        return (T) serializer.getAsType(data, type);
    }

    public String getAsString() {
        return serializer.getAsString(data);
    }

    public String getAsJson() {
        return serializer.getAsJson(data);
    }

    public String getAsXml() {
        return serializer.getAsXml(data);
    }

    private DataObject copy() {
        DataObject dataObject = new DataObject(deepCopy(data));
        dataObject.serializer = serializer;
        return dataObject;
    }

    private Object deepCopy(Object o) {
        return o;
    }

    public static DataObject newInstance(Object data) {
        return new DataObject(data);
    }

    public static <T> DataObject newInstance(T data, DataSerializer<T> serializer) {
        return new DataObject(data);
    }

    static class DefaultDataSerializer implements DataSerializer<Object> {

        @Override
        public <DT> DT getAsType(Object data, Class<DT> type) throws DataSerializerException {
            return null;
        }

        @Override
        public String getAsString(Object data) throws DataSerializerException {
            return null;
        }

        @Override
        public String getAsJson(Object data) throws DataSerializerException {
            return null;
        }

        @Override
        public String getAsXml(Object data) throws DataSerializerException {
            return null;
        }
    }

}
