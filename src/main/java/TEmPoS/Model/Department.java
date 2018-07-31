package TEmPoS.Model;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class Department {

    private String id;
    private String department;

    public Department() {
        this.id = "";
        this.department = "";
    }

    public Department(String id, String department) {
            this.id =id;
        this.department =department;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public void setDepartment(String department){
        this.department = department;
    }

    public String getDepartment(){
        return this.department;
    }



    @Override
    public String toString(){
        String EOLN = "\n";
        return this.id + EOLN +
                this.department + EOLN;
    }

    public JSONObject toJson(){
        JSONObject json;
        Map<String, String> department = new LinkedHashMap<>();

        department.put("id", this.getId());
        department.put("department", this.getDepartment());
        json = new JSONObject(department);
        return json;
    }

}
