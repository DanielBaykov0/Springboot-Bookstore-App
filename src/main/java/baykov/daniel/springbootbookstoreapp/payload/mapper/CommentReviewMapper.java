package baykov.daniel.springbootbookstoreapp.payload.mapper;

import baykov.daniel.springbootbookstoreapp.entity.CommentReview;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.CommentReviewRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.CommentReviewResponseDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentReviewMapper {

    CommentReviewMapper INSTANCE = Mappers.getMapper(CommentReviewMapper.class);

    @Mapping(target = "userName", expression = "java(userName(commentReview))")
    @Mapping(target = "averageRating", expression = "java(commentReview.getProduct().getAverageRating())")
    @Mapping(target = "postedAt", expression = "java(formatDate(commentReview))")
    @Mapping(target = "updatedAt", expression = "java(formatDate(commentReview))")
    CommentReviewResponseDTO entityToDTO(CommentReview commentReview);

    List<CommentReviewResponseDTO> entityToDTO(Iterable<CommentReview> commentReviews);

    CommentReview dtoToEntity(CommentReviewRequestDTO commentReviewRequestDTO);

    List<CommentReview> dtoToEntity(Iterable<CommentReviewRequestDTO> commentReviewRequestDTOS);

    default String userName(CommentReview commentReview) {
        return commentReview.getUser().getFirstName() + " " + commentReview.getUser().getLastName();
    }

    default String formatDate(CommentReview commentReview) {
        LocalDateTime displayedDate = commentReview.getUpdatedAt() != null
                ? commentReview.getUpdatedAt()
                : commentReview.getPostedAt();

        if (displayedDate == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return displayedDate.format(formatter);
    }
}
