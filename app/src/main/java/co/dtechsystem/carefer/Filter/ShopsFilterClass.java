package co.dtechsystem.carefer.Filter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;

/**
 * Created by DELL on 6/12/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public abstract class ShopsFilterClass {
    private static List<ShopsListModel.ShopslistRecord> _ShopslistRecordList;
    private static List<ShopsListModel.ShopslistRecord> _ShopslistRecordListFilter;
    private static Activity activity;

    public static List<ShopsListModel.ShopslistRecord>
    filterShopsWithProviders(Activity activity,
                             List<ShopsListModel.ShopslistRecord> _ShopslistRecordList,
                             List<ShopsListModel.ShopslistRecord> _ShopslistOriginal, final String ProvideWarranty, final String ProvideReplacementParts,
                             final String topRated, final String placeType, String Brands, final String Service) {

        ShopsFilterClass._ShopslistRecordList = _ShopslistRecordList;
        _ShopslistRecordListFilter = new ArrayList<>();

        _ShopslistRecordListFilter.addAll(_ShopslistRecordList);
        ShopsFilterClass.activity = activity;
//        if (_ShopslistRecordListFilter.size() == 0) {
//            _ShopslistRecordListFilter.addAll(_ShopslistOriginal);
//        }
        if (_ShopslistRecordList != null) {
            Locale locale = new Locale("ar");
            Locale localeEn = new Locale("en");
            ShopsFilterClass._ShopslistRecordList.clear();
            for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
                if (!placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .equals(ProvideWarranty)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && !topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    float shopRating = -1;
                    try{
                       shopRating = Float.valueOf(_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(localeEn));
                    }catch (Exception e){
                        Log.v("floatNOTConverted",_ShopslistRecordListFilter.get(i).getShopRating().toString());
                        e.printStackTrace();
                    }
                    if(shopRating<0) {

                        if (_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(localeEn)
                                .equals(topRated)) {
                            ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                        }
                    }
                    else{
                        if (shopRating>=4.0f) {
                            ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                        }
                    }

                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .equals(ProvideWarranty)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (!placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && !topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(localeEn)
                            .equals(topRated)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (!placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && !topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(localeEn)
                            .equals(topRated)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && !topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(localeEn)
                            .equals(topRated)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                }
                else if (!placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && !topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(localeEn)
                            .equals(topRated)&& _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .equals( Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                }

                else if (placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && !topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(localeEn)
                            .equals(topRated)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && !topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(localeEn)
                            .equals(topRated) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && !topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(localeEn)
                            .equals(topRated) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (!placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && !topRated.equals("") && Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(localeEn)
                            .equals(topRated) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .contains(placeType) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .contains(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .contains(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && !topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(localeEn)
                            .contains(topRated) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .contains(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .contains(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .contains(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .contains(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .contains(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(localeEn)
                            .contains(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && !topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(localeEn)
                            .contains(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(localeEn)
                            .contains(topRated) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));

                }
            }

            if (ShopsFilterClass._ShopslistRecordList.size() == 0) {
                Toast.makeText(activity, activity.getResources().getString(R.string.no_record_found), Toast.LENGTH_SHORT).show();
            }
        }

        return ShopsFilterClass._ShopslistRecordList;
    }


}
