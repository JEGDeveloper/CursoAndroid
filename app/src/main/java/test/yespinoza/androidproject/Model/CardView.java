package test.yespinoza.androidproject.Model;

public class CardView {
    //private int imagen; //guarda el id de la imagen almacenada en la carpeta drawable
    private String name;
    private String descritpion;
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
