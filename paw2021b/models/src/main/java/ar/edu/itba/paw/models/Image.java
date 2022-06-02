package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_id_image_seq")
    @SequenceGenerator(sequenceName = "images_id_image_seq", name="images_id_image_seq", allocationSize = 1)
    @Column(name="id_image")
    private long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recipe", nullable = false)
    private Recipe recipe;

    @Column(name = "image_data", nullable = false, length = 30000000)
    @Basic(fetch = FetchType.LAZY, optional = false)
    private byte[] data;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    public Image(Recipe recipe, byte[] data, String mimeType) {
        this.recipe = recipe;
        this.data = data;
        this.mimeType = mimeType;
    }

    /* default */
    protected Image() {
        // Just for Hibernate
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
}
