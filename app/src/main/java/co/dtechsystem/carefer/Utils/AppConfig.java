package co.dtechsystem.carefer.Utils;


public class AppConfig {
    public static int socketTimeout = 30000;

    //live base
    private static String BaseUrl = "https://carefer.co/public/api/";

    //    dtech test base
    //public static String BaseUrl = "http://carefer.dtechsystems.co/public/api/";

    // carefer tes base

    //public static String BaseUrl = "https://test.carefer.co/";

    //live image base
    public static String BaseUrlImages = "https://carefer.co/public/uploads/";

    public static String APiCareferPolicy = BaseUrl + "policy-data";
    public static String APiBrandData = BaseUrl + "get-brands-data";
    public static String APiServiceTypeData = BaseUrl + "get-service-types";
    public static String APiShopsListData = BaseUrl + "shops-data";
    public static String APiShopsDetailsData = BaseUrl + "shop-details/id/";
    public static String APiShopsDetailsOrderModel = BaseUrl + "get-models";
    public static String APiMyOrdersList = BaseUrl + "get-customer-orders/id/";
    public static String APiMyFavouriteShopsList = BaseUrl + "user-favourite-shops/id/";
    public static String APiRatingShop = BaseUrl + "save-api-comments";
    public static String APiSaveOrder = BaseUrl + "save-api-order";
    public static String APiGetCustomerDetails = BaseUrl + "get-customer-detail/id/";
    public static String APisetCustomerDetails = BaseUrl + "edit-customer-detail/id/";
    public static String APiRegisterCustomer = BaseUrl + "save-customer-detail";
    public static String APiShopFavourite = BaseUrl + "shop-favourite";
    public static String APiCreateUserPhone = BaseUrl + "receive-mobile-number";
    public static String APiChangeUserPhone = BaseUrl + "change-mobile-number";
    public static String APiVarifyCustomer = BaseUrl + "verify-customer";
    public static String APiVarifyCustomerNumberChange = BaseUrl + "verify-customer-number-change";
    public static String APiVarifyPolicy = BaseUrl + "verify-policy";
    public static String APiGetBrandModels = BaseUrl + "get-brand-models";
    public static String APiGetPlaceTypes = BaseUrl + "get-place-type";
    public static String APiGetFilterTypes = BaseUrl + "filter-types";
    public static String APiGetShopReviews = BaseUrl + "get-shop-reviews";
    public static String APiGetCitiesList = BaseUrl + "get-city-list";
    public static String APiPostShopsListDataByCity = BaseUrl + "city-shops-data";
    public static String APiGetPriceShop = BaseUrl +"get-price";
    public static String APiGetMovedShopDesc= BaseUrl +"get-service-description/id/1";
    public static String APiGetReceiveCarDesc= BaseUrl +"get-service-description/id/2";
    public static String APIGetOrderDetails = BaseUrl + "get-order-details/id/";


    //test image base
    //    public static String BaseUrlImages = "http://carefer.dtechsystems.co/public/uploads/";
}



