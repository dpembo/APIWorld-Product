public class ProductItem {

    private Integer id;
    private String productName;
    private String productDescription;

    public ProductItem(){

    }

    public ProductItem(Integer id, String productName, String productDescription)
    {
        super();
        this.id = id;
        this.productName = productName;
        this.productDescription = productDescription;

    }

    @Override
    public String toString(){
        return "ProductItem [id=" + id + ", productName=" + productName + ", productDescription=" + productDescription + "]";
    }
}