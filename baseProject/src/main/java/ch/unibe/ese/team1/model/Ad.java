package ch.unibe.ese.team1.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Describes an advertisement that users can place and search for.
 */
@Entity
public class Ad {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private int zipcode;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date moveInDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    private Date moveOutDate;

    @Column(nullable = true)
    private int pricePerMonth;

    @Column(nullable = false)
    private int squareFootage;

    @Column(nullable = false)
    @Lob
    private String roomDescription;

    @Column(nullable = false)
    @Lob
    private String preferences;

    @Column(nullable = false)
    private boolean smokers;

    @Column(nullable = false)
    private boolean animals;

    @Column(nullable = false)
    private boolean garden;

    @Column(nullable = false)
    private boolean balcony;

    @Column(nullable = false)
    private boolean cellar;

    @Column(nullable = false)
    private boolean furnished;

    @Column(nullable = false)
    private boolean cable;

    @Column(nullable = false)
    private boolean garage;

    @Column(nullable = false)
    private boolean internet;

    @Column(nullable = false)
    private boolean dishwasher;

    @Column(nullable = false)
    private int views;

    @Column(nullable = true)
    private int bidAmount;

    @Column(nullable = false)
    private String type;

    @Fetch(FetchMode.SELECT)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AdPicture> pictures;

    @ManyToOne(optional = false)
    private User user;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Visit> visits;

    // Auctions & Direct sell
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date auctionEndingDate;

    @Column(nullable = true)
    private int directBuyPrice;

    @Column(nullable = true)
    private int auctionStartingPrice;

    @Column(nullable = false)
    private int offerType;

    @ManyToOne(optional = true)
    private User lastBidder;

    @Column(nullable = true)
    private int lastBid;

    @Column(nullable = true)
    private Date lastBidDate;

    // Status
    @Column(nullable = false)
    private int status = 1;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public Date getAuctionEndingDate() {
        return auctionEndingDate;
    }

    public void setAuctionEndingDate(Date auctionEndingDate) {
        this.auctionEndingDate = auctionEndingDate;
    }

    public int getDirectBuyPrice() {
        return directBuyPrice;
    }

    public void setDirectBuyPrice(int price) {
        this.directBuyPrice = price;
    }

    public int getAuctionStartingPrice() {
        return auctionStartingPrice;
    }

    public void setAuctionStartingPrice(int price) {
        this.auctionStartingPrice = price;
    }

    public int getOfferType() {
        return offerType;
    }

    public void setOfferType(int offerType) {
        this.offerType = offerType;
    }

    public User getLastBidder() {
        return this.lastBidder;
    }

    public void setLastBidder(User user) {
        this.lastBidder = user;
    }

    public int getLastBid() {
        return this.lastBid;
    }

    public void setLastBid(int lastBid) {
        this.lastBid = lastBid;
    }

    public Date getLastBidDate() {
        return this.lastBidDate;
    }

    public void setLastBidDate(Date lastBidDate) {
        this.lastBidDate = lastBidDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean getSmokers() {
        return smokers;
    }

    public void setSmokers(boolean allowsSmokers) {
        this.smokers = allowsSmokers;
    }

    public boolean getAnimals() {
        return animals;
    }

    public void setAnimals(boolean allowsAnimals) {
        this.animals = allowsAnimals;
    }

    public boolean getGarden() {
        return garden;
    }

    public void setGarden(boolean hasGarden) {
        this.garden = hasGarden;
    }

    public boolean getBalcony() {
        return balcony;
    }

    public void setBalcony(boolean hasBalcony) {
        this.balcony = hasBalcony;
    }

    public boolean getCellar() {
        return cellar;
    }

    public void setCellar(boolean hasCellar) {
        this.cellar = hasCellar;
    }

    public boolean getFurnished() {
        return furnished;
    }

    public void setFurnished(boolean furnished) {
        this.furnished = furnished;
    }

    public boolean getCable() {
        return cable;
    }

    public void setCable(boolean hasCable) {
        this.cable = hasCable;
    }

    public boolean getGarage() {
        return garage;
    }

    public void setGarage(boolean garage) {
        this.garage = garage;
    }

    public boolean getInternet() {
        return internet;
    }

    public void setInternet(boolean internet) {
        this.internet = internet;
    }

    public boolean getDishwasher() {
        return dishwasher;
    }

    public void setDishwasher(boolean dishwasher) {
        this.dishwasher = dishwasher;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public Date getMoveInDate() {
        return moveInDate;
    }

    public void setMoveInDate(Date moveInDate) {
        this.moveInDate = moveInDate;
    }

    public void setMoveOutDate(Date moveOutDate) {
        this.moveOutDate = moveOutDate;
    }

    public int getPricePerMonth() {
        return pricePerMonth;
    }

    public void setPricePerMonth(int pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public int getSquareFootage() {
        return squareFootage;
    }

    public void setSquareFootage(int squareFootage) {
        this.squareFootage = squareFootage;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public List<AdPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<AdPicture> pictures) {
        this.pictures = pictures;
    }

    public Date getMoveOutDate() {
        return moveOutDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getDate(boolean date) {
        if (date) {
            return moveInDate;
        } else {
            return moveOutDate;
        }
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    public Type getType() {
        return Type.valueOf(this.type);
    }

    public void setType(Type type) {
        this.type = type.name();
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    // equals method is defined to check for id only
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Ad other = (Ad) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

}
