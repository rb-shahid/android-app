
package co.dtechsystem.carefer.Models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovedShopPriceModel {

    @SerializedName("price")
    @Expose
    private List<Price> price ;

    public List<Price> getPrice() {
        return price;
    }

    public void setPrice(List<Price> price) {
        this.price = price;
    }

}
