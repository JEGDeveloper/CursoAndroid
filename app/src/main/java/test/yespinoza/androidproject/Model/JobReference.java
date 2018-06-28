package test.yespinoza.androidproject.Model;

public class JobReference extends Person {
    private int code;
    private Job job;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
