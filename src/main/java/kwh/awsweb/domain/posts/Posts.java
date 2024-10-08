package kwh.awsweb.domain.posts;

import jakarta.persistence.*;
import kwh.awsweb.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 580, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Builder
    public Posts(String title,
                 String content,
                 String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title,
                       String content) {
        this.title = title;
        this.content = content;
    }
}
