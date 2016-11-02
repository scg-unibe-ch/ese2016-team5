package ch.unibe.ese.team1.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.User;

public interface AdDao extends CrudRepository<Ad, Long> {
	
	/** this will be used if both rooms AND studios are searched */
	public Iterable<Ad> findByPrizePerMonthLessThan (int prize);

	/** this will be used if only rooms or studios are searched */
	public Iterable<Ad> findByStudioAndPrizePerMonthLessThan(boolean studio,
			int i);
	
	public Iterable<Ad> findByForSale(boolean forSale); 
	public Iterable<Ad> findByForRent(boolean forRent); 
	public Iterable<Ad> findByForAuction(boolean forAuction); 
	public Iterable<Ad> findByForSaleAndForAuction(boolean forSale, boolean forAuction);
	public Iterable<Ad> findByForSaleAndForRent(boolean forSale, boolean forRent);
	public Iterable<Ad> findByForRentAndForAuction(boolean forRent, boolean forAuction); 
	public Iterable<Ad> findByForSaleAndForRentAndForAuction(boolean forSale, boolean forRent, boolean forAuction);
	
	public Iterable<Ad> findByUser(User user);
        
    public Iterable<Ad> findByStatus(int status);

	
	
}