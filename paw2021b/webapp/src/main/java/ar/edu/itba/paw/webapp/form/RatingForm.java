package ar.edu.itba.paw.webapp.form;

public class RatingForm {
    private boolean like;
    private boolean dislike;

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean isDislike() {
        return dislike;
    }

    public void setDislike(boolean dislike) {
        this.dislike = dislike;
    }
}
