package test.crbm.transaction.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author carlos
 */
@Repository
public interface InputMessageRepository
        extends JpaRepository<InputMessage, Integer> {

}
