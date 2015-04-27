package logic;

import java.util.HashMap;
import java.util.Map;

// Object from EAV database model
public class Object {

    private int object_id;
    private int object_type_id;
    private int parent_id;
    private String name;
    private String type_name;
    private String description;

    private Map<String, String> params;

    public Object(int o_i, int o_t_i, String _name, String _t_name ) {
        object_id = o_i;
        object_type_id = o_t_i;
        name = _name;
        type_name = _t_name;
        params = new HashMap<String, String>();
    }

    public void addParams(String param_name, String param_value) {

        params.put(param_name, param_value);
    }


    public int getObject_id() {
        return object_id;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
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
}

