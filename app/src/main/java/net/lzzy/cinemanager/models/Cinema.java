package net.lzzy.cinemanager.models;

import net.lzzy.sqllib.Ignored;
import net.lzzy.sqllib.Sqlitable;

/**
 * Created by lzzy_gxy on 2019/3/11.
 * Description:
 */
public class Cinema extends BaseEntity implements Sqlitable {
    @Ignored
    static final String COL_LOCATION = "location";
    private String name;
    private String location;
    private String province;
    private String city;
    private String area;

    public Cinema(){}
    public Cinema(String name,String location,String province,String city,String area){
        super();
        this.name=name;
        this.location=location;
        this.province=province;
        this.city=city;
        this.area=area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public boolean needUpdate() {
        return false;
    }

    @Override
    public String toString() {
        return location+name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(obj instanceof Cinema){
            return this.toString().equals(obj.toString());
        }else {
            return false;
        }
    }
}
