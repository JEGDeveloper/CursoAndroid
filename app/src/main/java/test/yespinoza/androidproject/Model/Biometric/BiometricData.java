package test.yespinoza.androidproject.Model.Biometric;

public class BiometricData {
    private String IdCandidate;
    private int Score;
    private String Decision;

    public String getIdCandidate() {
        return IdCandidate;
    }

    public void setIdCandidate(String idCandidate) {
        IdCandidate = idCandidate;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public String getDecision() {
        return Decision;
    }

    public void setDecision(String decision) {
        Decision = decision;
    }
}
