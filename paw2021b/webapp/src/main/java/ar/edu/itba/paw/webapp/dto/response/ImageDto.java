package ar.edu.itba.paw.webapp.dto.response;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Recipe;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ImageDto {

    private long id;
    private Recipe recipe;
    private byte[] data;
    private String mimeType;
    private URI url;

    public static ImageDto fromImage(UriInfo uri, Image image){
        ImageDto imageDto = new ImageDto();
        imageDto.id = image.getImageId();
        imageDto.recipe = image.getRecipe();
        imageDto.data = image.getData();
        imageDto.mimeType = image.getMimeType();
        imageDto.url = uri.getAbsolutePathBuilder().path(String.valueOf(image.getImageId())).build();
        return imageDto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
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

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }
}
