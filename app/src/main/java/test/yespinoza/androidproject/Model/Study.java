package test.yespinoza.androidproject.Model;

import java.util.Date;

public class Study {
    private int code;
    private String name;
    private String description;
    private StudyType type;
    private Date startDate;
    private Date finishDate;
    private StudyState state;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StudyType getType() {
        return type;
    }

    public void setType(StudyType type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public StudyState getState() {
        return state;
    }

    public void setState(StudyState state) {
        this.state = state;
    }
}
