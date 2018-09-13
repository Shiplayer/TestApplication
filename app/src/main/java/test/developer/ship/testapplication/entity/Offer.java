package test.developer.ship.testapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shiplayer on 12.09.18.
 */
public class Offer implements Parcelable{
    private int id;
    private String name;
    private String des;
    private String logo;
    private String url;
    private String btn;
    private String btn2;
    private boolean browser;
    private boolean enabled;
    private String desc_full;

    public Offer(int id, String name, String des, String logo, String url, String btn, String btn2, boolean browser, boolean enabled, String desc_full) {
        this.id = id;
        this.name = name;
        this.des = des;
        this.logo = logo;
        this.url = url;
        this.btn = btn;
        this.btn2 = btn2;
        this.browser = browser;
        this.enabled = enabled;
        this.desc_full = desc_full;
    }

    protected Offer(Parcel in) {
        id = in.readInt();
        name = in.readString();
        des = in.readString();
        logo = in.readString();
        url = in.readString();
        btn = in.readString();
        btn2 = in.readString();
        browser = in.readByte() != 0;
        enabled = in.readByte() != 0;
        desc_full = in.readString();
    }

    public static final Creator<Offer> CREATOR = new Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBtn() {
        return btn;
    }

    public void setBtn(String btn) {
        this.btn = btn;
    }

    public String getBtn2() {
        return btn2;
    }

    public void setBtn2(String btn2) {
        this.btn2 = btn2;
    }

    public boolean isBrowser() {
        return browser;
    }

    public void setBrowser(boolean browser) {
        this.browser = browser;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDesc_full() {
        return desc_full;
    }

    public void setDesc_full(String desc_full) {
        this.desc_full = desc_full;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(des);
        dest.writeString(logo);
        dest.writeString(url);
        dest.writeString(btn);
        dest.writeString(btn2);
        dest.writeByte((byte) (browser ? 1 : 0));
        dest.writeByte((byte) (enabled ? 1 : 0));
        dest.writeString(desc_full);
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", des='" + des + '\'' +
                ", logo='" + logo + '\'' +
                ", url='" + url + '\'' +
                ", btn='" + btn + '\'' +
                ", btn2='" + btn2 + '\'' +
                ", browser=" + browser +
                ", enabled=" + enabled +
                ", desc_full='" + desc_full + '\'' +
                '}';
    }
}
