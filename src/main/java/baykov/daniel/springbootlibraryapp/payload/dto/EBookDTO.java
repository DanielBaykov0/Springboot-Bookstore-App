package baykov.daniel.springbootlibraryapp.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EBookDTO {

    private Long id;

    private String bookType;

    private String bookTitle;

    private String bookAuthor;

    private String bookGenre;
    private String bookDescription;

    private com.github.ladutsko.isbn.ISBN ISBN;


    private String eBookReadOnlineLink;
    private String eBookDownloadLink;
}
