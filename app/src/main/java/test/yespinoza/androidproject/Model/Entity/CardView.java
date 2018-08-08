package test.yespinoza.androidproject.Model.Entity;

public class CardView {
    private String image;
    private String name;
    private String descritpion;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CardView(String name, String descritpion) {
        //this.imagen = imagen;
        this.name = name;
        this.descritpion = descritpion;

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
