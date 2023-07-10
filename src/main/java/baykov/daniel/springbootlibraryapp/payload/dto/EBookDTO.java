package baykov.daniel.springbootlibraryapp.payload.dto;

import baykov.daniel.springbootlibraryapp.entities.Author;
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

    private String title;

    private Author author;

    private String genre;
    private String description;

    private com.github.ladutsko.isbn.ISBN ISBN;

    private String readingLink;
    private String downloadLink;


}
