package ewha.backend.domain.category.entity;

import ewha.backend.domain.feed.entity.Feed;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private CategoryType categoryType;

	@JsonManagedReference
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Feed> feeds = new ArrayList<>();

	public void addFeed(Feed feed) {
		this.feeds.add(feed);
		if (feed.getCategory() != this) {
			feed.addCategory(this);
		}
	}

}
