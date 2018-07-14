package TEmPoS.Model;

public class Product {

    private String id;
    private String SKU;
    private String name;
    private Integer RRP;
    private Integer cost;
    private String department;
    private String brand;
    private String description;
    private Integer branch01Stock;
    private Integer branch02Stock;
    private Integer warehousestock;


    public Product(String id, String sku, String name, Integer rrp, Integer cost, String department, String brand, String description, Integer branch01Stock, Integer branch02Stock, Integer warehousestock) {
        this.id = id;
        this.SKU = sku;
        this.name = name;
        this.RRP = rrp;
        this.cost = cost;
        this.department = department;
        this.brand = brand;
        this.description = description;
        this.branch01Stock = branch01Stock;
        this.branch02Stock = branch02Stock;
        this.warehousestock = warehousestock;
    }
    public Product() {
        this.id = "";
        this.SKU = null;
        this.name = "";
        this.RRP = null;
        this.cost = null;
        this.department = "";
        this.brand = "";
        this.description = "";
        this.branch01Stock = null;
        this.branch02Stock = null;
        this.warehousestock = null;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRRP() {
        return RRP;
    }

    public void setRRP(Integer RRP) {
        this.RRP = RRP;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBranch01Stock() {
        return branch01Stock;
    }

    public void setBranch01Stock(Integer branch01Stock) {
        this.branch01Stock = branch01Stock;
    }

    public Integer getBranch02Stock() {
        return branch02Stock;
    }

    public void setBranch02Stock(Integer branch02Stock) {
        this.branch02Stock = branch02Stock;
    }

    public Integer getWarehousestock() {
        return warehousestock;
    }

    public void setWarehousestock(Integer warehousestock) {
        this.warehousestock = warehousestock;
    }
}
