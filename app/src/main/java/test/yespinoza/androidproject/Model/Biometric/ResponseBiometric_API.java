package test.yespinoza.androidproject.Model.Biometric;

import test.yespinoza.androidproject.Model.Utils.HttpApiResponse;

public class ResponseBiometric_API extends HttpApiResponse {
    public final static String HIT = "HIT";
    public final static String NO_HIT = "NO_HIT";
    public final static String PERSON_MATCH_REQUEST = "Biometric/PersonMatchFaceRequest";

    private BiometricData Data;

    public BiometricData getData() {
        return Data;
    }

    public void setData(BiometricData pData) {
        Data = pData;
    }
}
