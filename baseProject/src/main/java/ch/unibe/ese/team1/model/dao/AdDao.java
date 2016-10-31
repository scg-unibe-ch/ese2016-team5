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
	
	/** this will be used if either rent or sale is selected */
	//public Iterable<Ad> findBySaleAndPrizeLessThan(boolean forSale,int i);
	
	/** this will be used if either rent or sale and either room or studio is selected*/
	//public Iterable<Ad> findBySaleAndStudioAndPrizeLessThan(boolean forSale, boolean studio, int i);
	
	public Iterable<Ad> findByUser(User user);
        
        public Iterable<Ad> findByStatus(int status);

	
	
}