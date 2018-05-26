package co.dtechsystem.carefer.Models;

import java.util.ArrayList;
import java.util.List;


public class FavouriteShopsModel {
    private List<FavouriteShopsRecord> favouriteShops;

    public FavouriteShopsModel() {
        favouriteShops = new ArrayList<>();
    }

    public List<FavouriteShopsRecord> getFavouriteShops() {
        return favouriteShops;
    }

    public void setFavouriteShops(List<FavouriteShopsRecord> favouriteShops) {
        this.favouriteShops = favouriteShops;
    }

    public class FavouriteShopsRecord {
        private String shopImage = "";
        private String shopName = "";
        private String shopServiceType = "";
        private String ID = "";

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

        public String getShopImage() {
            return shopImage;
        }

        public void setShopImage(String shopImage) {
            this.shopImage = shopImage;
        }

        public void setShopServiceType(String shopServiceType){
            this.shopServiceType = shopServiceType;
        }

        public String getShopServiceType() {
            return shopServiceType;
        }
    }
}
