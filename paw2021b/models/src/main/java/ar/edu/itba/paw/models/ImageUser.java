package ar.edu.itba.paw.models;

public class ImageUser {
    private long idUser;
    private byte[] data;
    private String mimeType;

    public ImageUser(long idUser, byte[] data, String mimeType) {
        this.idUser = idUser;
        this.data = data;
        this.mimeType = mimeType;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
