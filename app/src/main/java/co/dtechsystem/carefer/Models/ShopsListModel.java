package co.dtechsystem.carefer.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class ShopsListModel {
    private List<ShopslistRecord> shopsList;
    private LatLng mLatlngCurrent;

    public ShopsListModel(LatLng mLatlngCurrent) {
        shopsList = new ArrayList<>();
        this.mLatlngCurrent = mLatlngCurrent;
    }

    public List<ShopslistRecord> getShopsList() {
        return shopsList;
    }

    public void setShopsList(List<ShopslistRecord> shopsList) {
        this.shopsList = shopsList;
    }


    public class ShopslistRecord {

        private String ID = "";
        private String shopName = "";
        private String shopDescription = "";
        private String serviceType = "";
        private String shopRating = "";
        private String latitude = "";
        private String longitude = "";
        private String brands = "";
        private String provideWarranty = "";
        private String shopType = "";
        private String provideReplaceParts = "";
        private String shopImage = "";
        private String city = "";
        private String isDiscounted= "";
        private String isTrusted= "";


        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }


        public String getShopDescription() {
            return shopDescription;
        }

        public void setShopDescription(String shopDescription) {
            this.shopDescription = shopDescription;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }


        public String getShopRating() {
            return shopRating;
        }

        public void setShopRating(String shopRating) {
            this.shopRating = shopRating;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getBrands() {
            return brands;
        }

        public void setBrands(String brands) {
            this.brands = brands;
        }

        public String getProvideWarranty() {
            return provideWarranty;
        }

        public void setProvideWarranty(String provideWarranty) {
            this.provideWarranty = provideWarranty;
        }

        public String getShopType() {
            return shopType;
        }

        public void setShopType(String shopType) {
            this.shopType = shopType;
        }

        public String getProvideReplaceParts() {
            return provideReplaceParts;
        }

        public void setProvideReplaceParts(String provideReplaceParts) {
            this.provideReplaceParts = provideReplaceParts;
        }

        public String getShopImage() {
            return shopImage;
        }

        public void setShopImage(String shopImage) {
            this.shopImage = shopImage;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getIsDiscounted() {
            return isDiscounted;
        }

        public void setIsDiscounted(String isDiscounted) {
            this.isDiscounted = isDiscounted;
        }

        public String getIsTrusted() {
            return isTrusted;
        }

        public void setIsTrusted(String isTrusted) {
            this.isTrusted = isTrusted;
        }
    }
}
