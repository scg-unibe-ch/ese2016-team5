package ch.unibe.ese.team1.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.User;
import java.util.Date;

public interface AdDao extends CrudRepository<Ad, Long> {
	
    /** this will be used if both rooms AND studios are searched */
    public Iterable<Ad> findByPricePerMonthLessThan (int price);

    /** this will be used if only rooms or studios are searched */
    public Iterable<Ad> findByUser(User user);
    public Iterable<Ad> findByStatus(int status);
    public Iterable<Ad> findByStatusAndAuctionEndingDateBefore(int i, Date date);
    public Iterable<Ad> findByOfferType(int type); 

}
