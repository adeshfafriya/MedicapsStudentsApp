package in.ac.medicaps.medicapsstudentsapp;

public class UserData {

    private String Uid,Email, Name, EnrollmentNumber, Faculty, Department, YearOfGrad, Section, Gender, BusFacility, BusStop, profile_image_url;

    public UserData(){}


    public UserData(String uid, String email, String name, String enrollmentNumber, String faculty, String department, String yearOfGrad, String section, String gender, String busFacility, String busStop, String profile_image_url) {
        Email = email;
        Uid = uid;
        Name = name;
        EnrollmentNumber = enrollmentNumber;
        Faculty = faculty;
        Department = department;
        YearOfGrad = yearOfGrad;
        Section = section;
        Gender = gender;
        BusFacility = busFacility;
        BusStop = busStop;
        this.profile_image_url = profile_image_url;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEnrollmentNumber() {
        return EnrollmentNumber;
    }

    public void setEnrollmentNumber(String enrollmentNumber) {
        EnrollmentNumber = enrollmentNumber;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getYearOfGrad() {
        return YearOfGrad;
    }

    public void setYearOfGrad(String yearOfGrad) {
        YearOfGrad = yearOfGrad;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBusFacility() {
        return BusFacility;
    }

    public void setBusFacility(String busFacility) {
        BusFacility = busFacility;
    }

    public String getBusStop() {
        return BusStop;
    }

    public void setBusStop(String busStop) {
        BusStop = busStop;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }
}
