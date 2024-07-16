package gift.model.gift;

import gift.model.category.Category;
import gift.model.category.CategoryRequest;
import gift.model.wish.Wish;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gift")
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private int price;
    @NotNull
    @Column(name = "imageurl")
    private String imageUrl;

    @OneToMany(mappedBy = "gift", cascade = CascadeType.REMOVE)
    protected List<Wish> wishes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;

    public Gift() {
    }

    public Gift(String name, int price, String imageUrl) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("카카오 문구는 MD와 협의 후 사용가능합니다.");
        }
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Gift(String name, int price, String imageUrl, Category category) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("카카오 문구는 MD와 협의 후 사용가능합니다.");
        }
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public int getPrice() {
        return price;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    private boolean isValidName(String name) {
        return name != null && !name.contains("카카오");
    }

    public void modify(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}