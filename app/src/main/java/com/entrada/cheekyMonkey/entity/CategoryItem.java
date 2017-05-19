package com.entrada.cheekyMonkey.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${Tanuj.Sareen} on 28/02/2015.
 */
public class CategoryItem implements Parcelable {

    public String Category_Code = "";
    public String Category_Color = "";
    public String Category_Name = "";
    public String Category_Group_Code = "";
    public String Category_Image_Url = "";


    public static final Creator<CategoryItem> CREATOR = new Creator<CategoryItem>() {
        public CategoryItem createFromParcel(Parcel source) {
            CategoryItem categoryItem = new CategoryItem();
            categoryItem.Category_Code = source.readString();
            categoryItem.Category_Color = source.readString();
            categoryItem.Category_Name = source.readString();
            categoryItem.Category_Group_Code = source.readString();
            categoryItem.Category_Image_Url = source.readString();

            return categoryItem;
        }

        public CategoryItem[] newArray(int size) {
            return new CategoryItem[size];
        }
    };

    public CategoryItem() {
    }


    public CategoryItem(String category_Code, String category_Color, String category_Name,
                        String category_Group_Code, String Category_Image_Url) {
        this.setCategory_Code(category_Code);
        this.setCategory_Color(category_Color);
        this.setCategory_Name(category_Name);
        this.setCategory_Group_Code(category_Name);
        this.setCategory_Image_Url(Category_Image_Url);
    }

    public String getCategory_Code() {
        return Category_Code;
    }

    public void setCategory_Code(String category_Code) {
        Category_Code = category_Code;
    }

    public String getCategory_Color() {
        return Category_Color;
    }

    public void setCategory_Color(String category_Color) {
        Category_Color = category_Color;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public void setCategory_Name(String category_Name) {
        Category_Name = category_Name;
    }

    public String getCategory_Group_Code() {
        return Category_Group_Code;
    }

    public void setCategory_Group_Code(String category_Group_Code) {
        Category_Group_Code = category_Group_Code;
    }

    public String getCategory_Image_Url() {
        return Category_Image_Url;
    }

    public void setCategory_Image_Url(String category_Image_Url) {
        Category_Image_Url = category_Image_Url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Category_Code);
        dest.writeString(Category_Color);
        dest.writeString(Category_Name);
        dest.writeString(Category_Group_Code);
        dest.writeSerializable(Category_Image_Url);

    }

}
