package guru.springframework.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamExample {

    public static void main(String []args){

        List<Product> productList = new ArrayList<>();

        productList.add(new Product("Chromebook", 1L, 23.0, "Google"));
        productList.add(new Product("Inspire", 2L, 19.0, "Dell"));
        productList.add(new Product("Celeron", 3L, 18.0, "Dell"));
        productList.add(new Product("Notepad", 4L, 21.0, "HP"));
        productList.add(new Product("Notepad1", 5L, 31.0, "HP"));


        List<String> pNames = productList.stream()
               .filter(product -> product.productPrice > 20)
                .map(product -> product.productName)
                .collect(Collectors.toList());

        pNames.forEach(System.out::println);




    }
}

class Product{
    String productName;
    Long productId;
    Double productPrice;
    String productVendor;

    public Product(String productName, Long productId, Double productPrice, String productVendor) {
        this.productName = productName;
        this.productId = productId;
        this.productPrice = productPrice;
        this.productVendor = productVendor;
    }
}
