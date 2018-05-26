package co.dtechsystem.carefer.Models;

import java.util.ArrayList;
import java.util.List;


public class ShopsDetailsModel {
    private List<ShopsDetailsRecord> shopsDetail;
    private List<ShopsImagessRecord> shopImages;

    public ShopsDetailsModel() {
        shopsDetail = new ArrayList<>();
        shopImages = new ArrayList<>();
    }

    public List<ShopsDetailsRecord> getShopsDetail() {
        return shopsDetail;
    }

    public void setShopsDetail(List<ShopsDetailsRecord> shopsDetail) {
        this.shopsDetail = shopsDetail;
    }

    public List<ShopsImagessRecord> getShopImages() {
        return shopImages;
    }

    public void setShopImages(List<ShopsImagessRecord> shopImages) {
        this.shopImages = shopImages;
    }

    public class ShopsDetailsRecord {

        private String ID = "";
        private String shopName = "";
        private String shopDescription = "";
        private String serviceType = "";
        private String shopRating = "";
        private String shopType = "";
        private String brands = "";
        private String latitude = "";
        private String longitude = "";
        private String favourite = "";
        private String shopImage = "";
        private String contactNumber = "";
        private String city = "";
        private String provideWarranty = "";
        private String provideReplaceParts = "";
        private String specialisedBrand = "";
        private String nationality="";
        private String reviewCount="";
        private String isDiscounted="";
        private String discountPercent="";

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

        public String getShopType() {
            return shopType;
        }

        public void setShopType(String shopType) {
            this.shopType = shopType;
        }

        public String getBrands() {
            return brands;
        }

        public void setBrands(String brands) {
            this.brands = brands;
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

        public String getFavourite() {
            return favourite;
        }

        public void setFavourite(String favourite) {
            this.favourite = favourite;
        }

        public String getShopImage() {
            return shopImage;
        }

        public void setShopImage(String shopImage) {
            this.shopImage = shopImage;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }
        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
        public String getProvideWarranty() {
            return provideWarranty;
        }

        public void setProvideWarranty(String provideWarranty) {
            this.provideWarranty = provideWarranty;
        }


        public String getProvideReplaceParts() {
            return provideReplaceParts;
        }

        public void setProvideReplaceParts(String provideReplaceParts) {
            this.provideReplaceParts = provideReplaceParts;
        }
        public String getSpecialisedBrand() {
            return specialisedBrand;
        }

        public void setSpecialisedBrand(String specialisedBrand) {
            this.specialisedBrand = specialisedBrand;
        }
        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }
        public String getReviewCount() {
            return reviewCount;
        }

        public void setReviewCount(String reviewCount) {
            this.reviewCount = reviewCount;
        }

        public String getIsDiscounted() {
            return isDiscounted;
        }

        public void setIsDiscounted(String isDiscounted) {
            this.isDiscounted = isDiscounted;
        }

        public String getDiscountPercent() {
            return discountPercent;
        }

        public void setDiscountPercent(String discountPercent) {
            this.discountPercent = discountPercent;
        }
    }

    public class ShopsImagessRecord {

        private String ID = "";
        private String imageName = "";


        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }


    }
}
