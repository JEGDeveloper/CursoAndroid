package test.yespinoza.androidproject.Model.Entity;

public class CardView {
    private String image;
    private String name;
    private String descritpion;

    public CardView(String name, String descritpion, String image) {
        this.image = image;
        this.name = name;
        this.descritpion = descritpion;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescritpion() {
        return descritpion;
    }

    public void setDescritpion(String descritpion) {
        this.descritpion = descritpion;
    }
}
