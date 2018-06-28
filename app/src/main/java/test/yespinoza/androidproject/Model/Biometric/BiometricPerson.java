package test.yespinoza.androidproject.Model.Biometric;

public class BiometricPerson {

    private int IdRequest;
    private String IdPerson;
    private int AccuracyLevel;
    private BiometricFace PersonFace;

    public BiometricPerson(){
        IdRequest = 200;
        IdPerson = "0";
        AccuracyLevel = 1000000;
        PersonFace = new BiometricFace();
    }

    public int getIdRequest() {
        return IdRequest;
    }

    public void setIdRequest(int idRequest) {
        this.IdRequest = idRequest;
    }

    public String getIdPerson() {
        return IdPerson;
    }

    public void setIdPerson(String idPerson) {
        this.IdPerson = idPerson;
    }

    public int getAccuracyLevel() {
        return AccuracyLevel;
    }

    public void setAccuracyLevel(int accuracyLevel) {
        this.AccuracyLevel = accuracyLevel;
    }

    public BiometricFace getPersonFace() {
        return PersonFace;
    }

    public void setPersonFace(BiometricFace personFace) {
        PersonFace = personFace;
    }
}
