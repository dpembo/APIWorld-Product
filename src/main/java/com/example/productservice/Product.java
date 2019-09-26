public class Product {

    private Integer id;
    private String productName;
    private String productDescription;

    public Product(){

    }

    public Product(Integer id, String productName, String productDescription)
    {
        super();
        this.id = id;
        this.productName = productName;
        this.productDescription = productDescription;

    }

    @Override
    public String toString(){
        return "Product [id=" + id + ", productName=" + productName + ", productDescription=" + productDescription + "]";
    }
}