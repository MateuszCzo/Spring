package mc.project.online_store.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @OneToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String quantity;

    @Column(nullable = false)
    private float price;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "date_add", nullable = false)
    private LocalDate dateAdd;

    @Column(name = "date_update", nullable = false)
    private LocalDate dateUpdate;

    @ManyToMany
    @JoinTable(
            name = "product_image",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private Set<Image> images;

    @ManyToMany
    @JoinTable(
            name = "product_attachment",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private Set<Attachment> attachments;

    @ManyToMany
    @JoinTable(
            name = "product_attribute",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    private Set<Attribute> attributes;
}
