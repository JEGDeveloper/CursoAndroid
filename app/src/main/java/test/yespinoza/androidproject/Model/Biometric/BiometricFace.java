package test.yespinoza.androidproject.Model.Biometric;

public class BiometricFace {

    private int Width;
    private int Height;
    private int HorizontalResolution;
    private int VerticalResolution;
    private String Format;
    private String Buffer;
    private float RightEyePositionX;
    private float RightEyePositionY;
    private float LeftEyePositionX;
    private float LeftEyePositionY;
    private boolean IsFaceDetected;

    public int getWidth() {
        return Width;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public int getHorizontalResolution() {
        return HorizontalResolution;
    }

    public void setHorizontalResolution(int horizontalResolution) {
        HorizontalResolution = horizontalResolution;
    }

    public int getVerticalResolution() {
        return VerticalResolution;
    }

    public void setVerticalResolution(int verticalResolution) {
        VerticalResolution = verticalResolution;
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }

    public String getBuffer() {
        return Buffer;
    }

    public void setBuffer(String buffer) {
        Buffer = buffer;
    }

    public float getRightEyePositionX() {
        return RightEyePositionX;
    }

    public void setRightEyePositionX(float rightEyePositionX) {
        RightEyePositionX = rightEyePositionX;
    }

    public float getRightEyePositionY() {
        return RightEyePositionY;
    }

    public void setRightEyePositionY(float rightEyePositionY) {
        RightEyePositionY = rightEyePositionY;
    }

    public float getLeftEyePositionX() {
        return LeftEyePositionX;
    }

    public void setLeftEyePositionX(float leftEyePositionX) {
        LeftEyePositionX = leftEyePositionX;
    }

    public float getLeftEyePositionY() {
        return LeftEyePositionY;
    }

    public void setLeftEyePositionY(float leftEyePositionY) {
        LeftEyePositionY = leftEyePositionY;
    }

    public boolean isFaceDetected() {
        return IsFaceDetected;
    }

    public void setFaceDetected(boolean faceDetected) {
        IsFaceDetected = faceDetected;
    }
}
