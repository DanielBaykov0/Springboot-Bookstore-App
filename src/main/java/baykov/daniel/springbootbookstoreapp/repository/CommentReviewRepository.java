package baykov.daniel.springbootbookstoreapp.repository;

import baykov.daniel.springbootbookstoreapp.entity.CommentReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentReviewRepository extends JpaRepository<CommentReview, Long> {

    boolean existsByUserIdAndProductId(Long userId, Long productId);
    @Query("SELECT COALESCE(ROUND(AVG(cr.rating), 1), 0) FROM CommentReview cr WHERE cr.product.id = :productId")
    Double findAverageRatingByProductId(Long productId);

    @Query("SELECT cr FROM CommentReview cr WHERE cr.user.id = :userId ORDER BY CASE WHEN cr.updatedAt IS NOT NULL THEN cr.updatedAt ELSE cr.postedAt END DESC")
    CommentReview findFirstReviewByUser(Long userId);

    Page<CommentReview> findAllByProductId(Long productId, Pageable pageable);
}
