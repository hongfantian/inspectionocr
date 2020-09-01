package com.inspect.ocr.models;
import java.io.Serializable;
import java.util.HashMap;

public class PatientImage implements Serializable {

    int imageId;
    String userId;
    String hospitalId;
    String patientId;
    String imagePath;
    String thumbImagePath;
    double receiptDate;
    double createdDate;
    boolean isChecked;

    public PatientImage() {
        this.isChecked = false;
    }

    public PatientImage(HashMap map) {
        this.imageId = (int) map.get("image_id");
        this.userId = (String) map.get("user_id");
        this.hospitalId = (String) map.get("hospital_id");
        this.patientId = (String) map.get("patient_id");
        this.imagePath = (String) map.get("image_path");
        this.thumbImagePath = (String) map.get("thumb_image_path");
        this.receiptDate = (double) map.get("receipt_date");
        this.createdDate = (double) map.get("created_date");
        this.isChecked = false;
    }

    public void setUserId(String val) {
        this.userId = val;
    }
    public String getUserId() {
        return userId;
    }

    public void setImageId(int val) {
        this.imageId = val;
    }
    public int getImageId() {
        return imageId;
    }

    public void setPatientId(String val) {
        this.patientId = val;
    }
    public String getPatientId() {
        return patientId;
    }

    public void setHospitalId(String val) {
        this.hospitalId = val;
    }
    public String getHospitalId() {
        return hospitalId;
    }

    public void setImagePath(String val) {
        this.imagePath = val;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setThumbImagePath( String val ){ this.thumbImagePath = val; }
    public String getThumbImagePath(){
        return thumbImagePath;
    }

    public void setReceiptDate(double val) {
        this.receiptDate = val;
    }
    public double getReceiptDate() {
        return receiptDate;
    }

    public void setCreatedDate(double val) {
        this.createdDate = val;
    }
    public double getCreatedDate() {
        return createdDate;
    }

    public void setChecked(boolean val) {
        this.isChecked = val;
    }
    public boolean getChecked() {
        return isChecked;
    }
}
