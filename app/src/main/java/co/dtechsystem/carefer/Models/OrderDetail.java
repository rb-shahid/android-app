
package co.dtechsystem.carefer.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {

    @SerializedName("orderNo")
    @Expose
    private String orderNo;
    @SerializedName("orderType")
    @Expose
    private String orderType;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;
    @SerializedName("receiveCarComments")
    @Expose
    private String receiveCarComments;
    @SerializedName("movedShopPrice")
    @Expose
    private String movedShopPrice;
    @SerializedName("modelName")
    @Expose
    private String modelName;
    @SerializedName("brandName")
    @Expose
    private String brandName;
    @SerializedName("serviceTypeName")
    @Expose
    private String serviceTypeName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("shopName")
    @Expose
    private String shopName;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerMobile")
    @Expose
    private String customerMobile;
    @SerializedName("customerLocation")
    @Expose
    private String customerLocation;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getReceiveCarComments() {
        return receiveCarComments;
    }

    public void setReceiveCarComments(String receiveCarComments) {
        this.receiveCarComments = receiveCarComments;
    }

    public String getMovedShopPrice() {
        return movedShopPrice;
    }

    public void setMovedShopPrice(String movedShopPrice) {
        this.movedShopPrice = movedShopPrice;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

}
