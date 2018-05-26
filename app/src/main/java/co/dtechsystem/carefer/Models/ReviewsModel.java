package co.dtechsystem.carefer.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsModel {

    @SerializedName("shopReviews")
    @Expose
    private List<ShopReview> shopReviews ;

    public List<ShopReview> getShopReviews() {
        return shopReviews;
    }

    public void setShopReviews(List<ShopReview> shopReviews) {
        this.shopReviews = shopReviews;
    }


    public class ShopReview {

        @SerializedName("ID")
        @Expose
        private String iD;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("priceRating")
        @Expose
        private String priceRating;
        @SerializedName("qualityRating")
        @Expose
        private String qualityRating;
        @SerializedName("timeRating")
        @Expose
        private String timeRating;
        @SerializedName("dateAdded")
        @Expose
        private String dateAdded;
        @SerializedName("hidAction")
        @Expose
        private String hidAction;
        @SerializedName("customerName")
        @Expose
        private String customerName;

        public String getID() {
            return iD;
        }

        public void setID(String iD) {
            this.iD = iD;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getPriceRating() {
            return priceRating;
        }

        public void setPriceRating(String priceRating) {
            this.priceRating = priceRating;
        }

        public String getQualityRating() {
            return qualityRating;
        }

        public void setQualityRating(String qualityRating) {
            this.qualityRating = qualityRating;
        }

        public String getTimeRating() {
            return timeRating;
        }

        public void setTimeRating(String timeRating) {
            this.timeRating = timeRating;
        }

        public String getDateAdded() {
            return dateAdded;
        }

        public void setDateAdded(String dateAdded) {
            this.dateAdded = dateAdded;
        }

        public String getHidAction() {
            return hidAction;
        }

        public void setHidAction(String hidAction) {
            this.hidAction = hidAction;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }
    }
}
